package me.camm.productions.wizardrydemo.Spells.Util;
import me.camm.productions.wizardrydemo.Spells.*;

//enum for all types of spells
/*
You could improve this by overriding name() to return a beautified name instead of
the raw enum name
 */
public enum SpellType {
    FIREBALL(SpellFireball.class),
    WEATHER(SpellInvokeWeather.class),
    LIGHTNING(SpellLightning.class),

    ARROW_STORM(SpellArrowStorm.class),
    SNOWBALL(SpellSnowball .class);

   public final Class<? extends AbstractSpell> clazz;

   SpellType(Class<? extends AbstractSpell> clazz){
       this.clazz = clazz;
   }

}
