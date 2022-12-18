package me.camm.productions.wizardrydemo.Spells;


import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;


public class SpellFireball extends AbstractSpell {

    public SpellFireball(Player caster){
        super(caster);
    }

    @Override
    public void cast() {

        if (!canCast(caster,getType())) {
            caster.sendMessage(ChatColor.RED+"You cannot cast this yet!");
            return;
        }
       Fireball ball = caster.launchProjectile(Fireball.class);
        ball.setYield(2);

    }

    @Override
    public SpellType getType() {
        return SpellType.FIREBALL;
    }


    //Generally you would get this from a config file
    /*
     So maybe have a member variable called coolDown,
      and set it to a value when the plugin loads.

      if static, then you can set it within the onEnable() method
     */
    @Override
    public long getCooldown() {

          // 1 sec
        return 1000;
    }

    @Override
    public String getName() {
        return "Fireball";
    }
}
