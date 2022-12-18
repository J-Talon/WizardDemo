package me.camm.productions.wizardrydemo.Spells;

import me.camm.productions.wizardrydemo.Spells.Entities.Snowball;
import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellSnowball extends AbstractSpell
{

    public SpellSnowball(Player caster) {
        super(caster);
    }

    @Override
    public void cast() {

        //canCast() takes care of the cooldown on the spells.

        if (!canCast(caster,getType())) {
            caster.sendMessage(ChatColor.RED+"You cannot cast this yet!");
            return;
        }

        Location loc = caster.getLocation();
        World nms = ((CraftWorld)caster.getWorld()).getHandle();
        Vector dir = caster.getEyeLocation().getDirection().multiply(2);

        /*
        This is an alternative to the player.launchProjectile() method.
         I'm using this since this is a custom snowball class.

         this is considered a nms (net.mc.server) implementation, so it is not gonna work for all versions of spigot.
         */
        Snowball ball = new Snowball(nms, loc.getX(), caster.getEyeHeight()+loc.getY(), loc.getZ());
        ball.setMot(dir.getX(), dir.getY(), dir.getZ());
        ball.setShooter(((CraftPlayer)caster).getHandle());
        nms.addEntity(ball);


    }


    @Override
    public SpellType getType() {
        return SpellType.SNOWBALL;
    }

    @Override
    public long getCooldown() {
        return 200;
    }


    // probably better to do getType().getName() or something like that.
    //you'd need to modify the enum first.
    @Override
    public String getName() {
        return "Snowball";
    }
}
