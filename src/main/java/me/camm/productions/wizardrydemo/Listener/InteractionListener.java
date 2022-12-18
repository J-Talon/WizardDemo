package me.camm.productions.wizardrydemo.Listener;

import me.camm.productions.wizardrydemo.Items.SpellSelector;
import me.camm.productions.wizardrydemo.Items.WandGenerator;

import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractionListener implements Listener {


    //a better way to do this would probably be to use a player wrapper class which holds data
    Map<UUID, SpellType[]> spells;
    Map<UUID, Integer> casterSelection;
    Map<UUID, Long> prevHoldingTime;

    final int LENGTH = 4;
    final long HOLDING_TIME = 3000;
    final int TICK_TIME = (int)((HOLDING_TIME / 1000) * 20);


    //constructor
    public InteractionListener() {
        spells = new HashMap<>();
        casterSelection = new HashMap<>();
        prevHoldingTime = new HashMap<>();
    }


    //adds a player to the tracking system
    private void addPlayer(Player player){
        spells.put(player.getUniqueId(), new SpellType[LENGTH]);
        casterSelection.put(player.getUniqueId(), 0);
        prevHoldingTime.put(player.getUniqueId(), 0L);
    }


   //this is sending a title to the action bar of the player.
    //kinda  like a chat message, but in the centre of the screen
    private void sendActionbar(Player player, int in, int stay, int out, String title){
        PacketPlayOutTitle times = new PacketPlayOutTitle(in, stay, out);
        PacketPlayOutTitle actionBar =
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, CraftChatMessage.fromStringOrNull(title));

        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(times);
        connection.sendPacket(actionBar);

    }

    //sends a title to the player about what spell they have selected
    private void notifySelection(Player player, UUID id) {
        sendActionbar(player, 0, TICK_TIME, 0,
                ChatColor.GOLD+""+spells.get(id)[casterSelection.get(id)]+"");
    }


    private synchronized void castSpell(Action act, Player player){
        String action = act.toString();
        UUID id = player.getUniqueId();

        /*
        When you implement spells which can be held, you'd probably check here to see if they have a spell they're holding
        first
         */


        //check if the action is a left click or a right click.
        /*
        If you look in the Action class, you will see there's also a PHYSICAL
        action, which is why we use an else-if
         */
        if (action.contains("LEFT")) {

            //checking how long it has been since the player has selected a spell.
            //if it is greater than a holding time, then reset to 0
            //
            if (System.currentTimeMillis() - prevHoldingTime.get(id) > HOLDING_TIME) {
                casterSelection.replace(id,0);
            }

            //Otherwise, we refresh the holding time
            prevHoldingTime.replace(id, System.currentTimeMillis());

            //get their spell selection
            int selection = casterSelection.get(id);

            try {
                //try to create a new instance of the spell, notify the player about which spell they
                //have selected, and try to cast.
                //then reset the selection back to 0.
                Class<? extends AbstractSpell> spellClass = spells.get(id)[selection].clazz;

               AbstractSpell spell = spellClass.getConstructor(Player.class).newInstance(player);
               sendActionbar(player, 0,TICK_TIME,0,ChatColor.RED+"++"+ChatColor.BOLD+spell.getName()+"++");
               spell.cast();
               casterSelection.replace(id,0);

               /*
               for held spells, this is where you would set them most likely
                */
            }
            catch (Exception e) {
                sendActionbar(player, 0,TICK_TIME,0,ChatColor.GREEN+""+ChatColor.BOLD+"None");
            }

            return;

        } else if (action.contains("RIGHT")) {

            //play a sound to the player to let them know they are switching spells
            player.playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON,
                    SoundCategory.BLOCKS,1,1);

            //if the last time they switched was some time ago, reset their selection back to 0
            if (System.currentTimeMillis() - prevHoldingTime.get(id) > HOLDING_TIME) {
                casterSelection.replace(id,0);
            }
            //refresh their holding time
           prevHoldingTime.replace(id, System.currentTimeMillis());


            //replace their selection
            int slot = casterSelection.get(id);

            if (slot >= LENGTH-1)
                casterSelection.replace(id,0);
            else
                casterSelection.replace(id,slot+1);
        }
        notifySelection(player, id);

    }



    //listens for an interaction event
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        //get the wand item
        ItemStack wand = WandGenerator.getWand();

       Player player = event.getPlayer();
       ItemStack stack = event.getItem();

       //check if they are holding the wand.
       if (!wand.isSimilar(stack))
           return;

       //if the player is not registered, then add them
       if (!spells.containsKey(player.getUniqueId()))
           addPlayer(player);


       //we cancel the event and try to cast a spell
        //cancelling is important, since this ensures that the original action they were doing is not actually
        //performed.
        //we're trying to cast a spell here, not open a door.
       event.setCancelled(true);
       castSpell(event.getAction(), player);

    }


    //listen for an inventory click
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        Inventory select = SpellSelector.getSelector();

        //see if the clicked inventory is the spell selector inventory
        if (!select.equals(inv)) {
            return;
        }

        HumanEntity clicked = event.getWhoClicked();

        //we get who clicked, and if they are a player, we add them to the tracker if they are not
        //in there
        if (!spells.containsKey(clicked.getUniqueId())) {

            if (clicked instanceof Player) {
                addPlayer((Player)clicked);
            }
            else return;
        }


        ItemStack stack = event.getCurrentItem();
        if (stack == null || stack.getItemMeta() == null) {
            return;
        }

        //we get the name of the item if it exists
        String display = stack.getItemMeta().getDisplayName();
        SpellType type = SpellSelector.getType(display);

        //we try to get the spell type from the item name
        if (type == null) {
            return;
        }

        //we also see what slot the player wanted to switch
        int slot = SpellSelector.get(clicked.getUniqueId());
        if (slot == -1) {
            return;
        }

        //if all goes well, we cancel the event, close the inventory, and notify the player
        event.setCancelled(true);
        spells.get(clicked.getUniqueId())[slot] = type;
        clicked.sendMessage(ChatColor.GOLD+"Changed slot "+slot+" to "+type.name());
        clicked.closeInventory();
    }


    /*
    Players actually have 2 interactions: dragging and clicking.
     I've simplified things here and said: okay, we don't allow dragging at all when the player is choosing the spells
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
      Inventory inv = event.getView().getTopInventory();
       if (inv == SpellSelector.getSelector()) {
           event.setCancelled(true);
       }
    }
}
