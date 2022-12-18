package me.camm.productions.wizardrydemo.Spells.Util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractSpell {

   static protected Map<UUID, Map<SpellType, Long>> time;

   static {
       time = new HashMap<>();
   }

    protected Player caster;
    public AbstractSpell(final Player caster) {
        this.caster = caster;

    }

    public boolean canCast(Player player, SpellType type) {


        if (!time.containsKey(player.getUniqueId())) {
            Map<SpellType, Long> entries = new HashMap<>();
            entries.put(type, System.currentTimeMillis());
            time.put(player.getUniqueId(), entries);
            return true;
        }

        Map<SpellType, Long> entryMap = time.getOrDefault(player.getUniqueId(), null);
        if (!entryMap.containsKey(type)) {
            entryMap.put(type, System.currentTimeMillis());
            return true;
        }

        Long cd = entryMap.getOrDefault(type,null);


        if (cd == null) return false;

        if (System.currentTimeMillis() - cd >= getCooldown()) {
            time.get(player.getUniqueId()).replace(type, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public abstract void cast();
    public abstract SpellType getType();
    public abstract long getCooldown();

    public abstract String getName();


}
