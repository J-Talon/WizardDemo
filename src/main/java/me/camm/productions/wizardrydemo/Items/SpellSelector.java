package me.camm.productions.wizardrydemo.Items;

import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpellSelector {

    static Inventory selector = null;
    static Map<String, SpellType> spells = new HashMap<>();
    static Map<UUID, Integer> chStore = new HashMap<>();


    static {
        //put the spells onto a map for easy access when we need to get them via a string
        for (SpellType type: SpellType.values())
          spells.put(ChatColor.BLUE+""+type.name(), type);
    }

    public static Inventory getSelector(){
        //first, if the inventory is not null, then we return it
        if (selector != null)
            return selector;


        //otherwise we make it
        selector = Bukkit.createInventory(null, getFitSize(SpellType.values().length),
                "Spell Select");

        populate();
        return selector;
    }

    //this method rounds a number to the nearest 9.
    //Inventory sizes must be a multiple of 9, otherwise an exception is thrown
    public static int getFitSize(int itemNumber){
        return itemNumber <=0 ? 9:(((itemNumber/9))+1)*9;
    }

    /*
    Populate the inventory with choices of spells.
     */
    private static void populate(){
        int current = 0;
        SpellType[] values = SpellType.values();
        for (int slot = 0; slot < selector.getSize(); slot ++ ) {
          ItemStack stack = createRepresentative(values[current]);
         selector.setItem(slot, stack);

            current ++;
         if (current == values.length)
             break;

        }
    }

    //We create an item to fill the inventory with.
    //We use the item meta to set the display name.
    private static ItemStack createRepresentative(SpellType type){

        //first create the itemstack
        ItemStack stack = new ItemStack(Material.DIAMOND);

        //next we get the meta. It's basically the item data, so tags, durability for tools, name, lore, etc
        ItemMeta meta = stack.getItemMeta();

        //generally, meta is not null, but it can be null if the item is broken or corrupted.
        if (meta == null) throw new IllegalStateException("Meta is null for spell item creation");

        //when we get the item meta, it is a copy of the meta on the item, so we need to set our
        //modified meta back onto the item when we are done
        meta.setDisplayName(ChatColor.BLUE+""+type.name());
        stack.setItemMeta(meta);
        return stack;
    }

    //here, we are using a string to get a spell type, or null if there isn't one
    @Nullable
    public static SpellType getType(String type) {
        return spells.getOrDefault(type, null);
    }

    //these are used to store and get the slot the player will change their spells into
    public static void store(UUID id, int slot) {
        chStore.put(id, slot);
    }

    public static int get(UUID id) {
        return chStore.getOrDefault(id, -1);
    }





}
