package me.camm.productions.wizardrydemo.Spells;

import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class SpellLightning extends AbstractSpell {
    public SpellLightning(Player caster) {
        super(caster);
    }

    @Override
    public void cast() {

        if (canCast(caster, getType())) {

            RayTraceResult result = caster.rayTraceBlocks(100);
            Block block;
            if (result == null || result.getHitBlock() == null) {
                Vector dir = caster.getEyeLocation().getDirection().normalize().multiply(100);
                Location loc = caster.getEyeLocation();
                dir.add(loc.toVector());
                block = caster.getWorld().getHighestBlockAt(dir.getBlockX(), dir.getBlockZ());
            } else
                block = result.getHitBlock();

            caster.getWorld().strikeLightning(block.getLocation().add(0, 1, 0));

        }
        else caster.sendMessage(ChatColor.RED+"You cannot cast this yet");

    }

    @Override
    public SpellType getType() {
        return SpellType.LIGHTNING;
    }

    @Override
    public long getCooldown() {
        return 5000;
    }

    @Override
    public String getName() {
        return "Lightning";
    }
}
