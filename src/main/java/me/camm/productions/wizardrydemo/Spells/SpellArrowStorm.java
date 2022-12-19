package me.camm.productions.wizardrydemo.Spells;

import me.camm.productions.wizardrydemo.Spells.Util.AbstractSpell;
import me.camm.productions.wizardrydemo.Spells.Util.SpellType;
import me.camm.productions.wizardrydemo.WizardryDemo;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class SpellArrowStorm extends AbstractSpell {


    public SpellArrowStorm(Player caster) {
        super(caster);
    }

    @Override
    public void cast() {


        /*
        it is also valid (and probably better) to do
         if (!canCast(caster, getType())) {
         caster.sendMessage(...);
         return;
         }

         */
        if (canCast(caster, getType())) {
            RayTraceResult result = caster.rayTraceBlocks(50);
            Block block;
            Location cloud;
            World world = caster.getWorld();
            if (result == null || result.getHitBlock() == null) {

                //we're multiplying the vector to get the location 100 blocks away from the
                //starting location
                Vector dir = caster.getEyeLocation().getDirection().normalize().multiply(50);
                Location loc = caster.getEyeLocation();
                dir.add(loc.toVector());
                block = caster.getWorld().getHighestBlockAt(dir.getBlockX(), dir.getBlockZ());
            } else
                block = result.getHitBlock();

            cloud = block.getLocation().add(0,20,0);
            world.playSound(cloud, Sound.ENTITY_LIGHTNING_BOLT_THUNDER,SoundCategory.WEATHER,1,1);

       /*
       Bukkit runnables are a type of thread that run in sync to the ticks of the game.
       they're a way to make things happen later.
       always remember to cancel() them once their execution is finished.
        */
            new BukkitRunnable() {
                int iterations = 0;
                public void run() {
                    if (iterations > 100)
                    {
                        cancel();
                        return;
                    }
                    iterations ++;
                    world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE,cloud,10,1.5,0,1.5,0.05);

                    int arrows = 0;
                    while (arrows < 3) {

                        //we're spawning an arrow in a random location within the cloud.
                        //cloud.clone() is important since we do not want to modify the original vector.
                        //this is true generally for location and vector objects in spigot. There are exceptions
                        //depending on usage.
                       Arrow arrow = world
                               .spawn(cloud.clone().add(Math.random()*5 - Math.random()*5,0, Math.random()*5 - Math.random()*5),Arrow.class);
                       arrow.setShooter(caster);
                       arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                       arrow.setVelocity(new Vector(0,-1,0));
                        arrows ++;
                    }

                }
                //delay: ticks to wait before starting the runnable
                //period: ticks to wait before executing again
            }.runTaskTimer(WizardryDemo.getPlugin(),0,1);



        }
        else caster.sendMessage(ChatColor.RED+"You cannot cast this yet");
    }

    @Override
    public SpellType getType() {
        return SpellType.ARROW_STORM;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public String getName() {
        return "Arrow Storm";
    }
}
