package me.camm.productions.wizardrydemo.Spells;

import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpellInvokeWeather extends AbstractSpell
{

    public SpellInvokeWeather(Player caster) {
        super(caster);
    }

    @Override
    public void cast() {
        if (!canCast(caster,getType())) {
            caster.sendMessage(ChatColor.RED+"You cannot cast this yet!");
            return;
        }

        World world = caster.getWorld();
        world.setStorm(!world.hasStorm());
    }

    @Override
    public SpellType getType() {
        return SpellType.WEATHER;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public String getName() {
        return "Invoke Weather";
    }
}
