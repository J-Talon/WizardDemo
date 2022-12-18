package me.camm.productions.wizardrydemo.Spells.Util;

import org.bukkit.entity.Player;

public abstract class AbstractSpellHolding extends AbstractSpell {

    public AbstractSpellHolding(Player caster) {
        super(caster);
    }

    public abstract void castHolding(SpellData<?> data);
}
