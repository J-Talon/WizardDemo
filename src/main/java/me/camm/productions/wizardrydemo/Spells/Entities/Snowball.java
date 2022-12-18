package me.camm.productions.wizardrydemo.Spells.Entities;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Snowball extends EntitySnowball {

    protected double spinX, spinY, spinZ;
    protected int time;
    final double DISTANCE = 0.5;

    public Snowball(World world, double x, double y, double z) {
        super(world, x,y,z);
        spinX = 0;
        spinY = 0;
        spinZ = 0;
        time = 0;
    }

    @Override
    public void tick() {
        time ++;
        entityBaseTick();
        spinEffect();

        MovingObjectPosition movingobjectposition = ProjectileHelper.a(this, this::a);
        boolean teleported = false;
        if (movingobjectposition.getType() == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            BlockPosition blockposition = ((MovingObjectPositionBlock) movingobjectposition).getBlockPosition();
            IBlockData iblockdata = this.world.getType(blockposition);
            if (iblockdata.a(Blocks.NETHER_PORTAL)) {
                this.d(blockposition);
                teleported = true;
            } else if (iblockdata.a(Blocks.END_GATEWAY)) {
                TileEntity tileentity = this.world.getTileEntity(blockposition);
                if (tileentity instanceof TileEntityEndGateway && TileEntityEndGateway.a(this)) {
                    ((TileEntityEndGateway) tileentity).b(this);
                }

                teleported = true;
            }
        }

        if (movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.MISS && !teleported) {
            this.preOnHit(movingobjectposition);
        }

        this.checkBlockCollisions();
        Vec3D vec3d = this.getMot();
        double d0 = this.locX() + vec3d.x;
        double d1 = this.locY() + vec3d.y;
        double d2 = this.locZ() + vec3d.z;
        this.x();
        if (this.isInWater()) {
            world.setTypeAndData(new BlockPosition(locX(), locY(), locZ()), Blocks.ICE.getBlockData(), 3);
            hitEffect();
            this.die();
            return;
        }

            this.setMot(vec3d.a(0.99));

            if (!this.isNoGravity()) {
                Vec3D vec3d1 = this.getMot();
                this.setMot(vec3d1.x, vec3d1.y - (double) this.k(), vec3d1.z);
            }

            this.setPosition(d0, d1, d2);

    }

    //hit entity
    protected void a(MovingObjectPositionEntity var0) {
       // You would also get the damage from config ideally
        Entity var1 = var0.getEntity();
        var1.damageEntity(DamageSource.projectile(this, this.getShooter()), 4);
    }

    //
    protected void a(MovingObjectPosition var0) {

        MovingObjectPosition.EnumMovingObjectType movingobjectposition_enummovingobjecttype = var0.getType();
        if (movingobjectposition_enummovingobjecttype == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
            this.a((MovingObjectPositionEntity)var0);
        } else if (movingobjectposition_enummovingobjecttype == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            this.a((MovingObjectPositionBlock)var0);
        }

        hitEffect();
        this.die();


    }

    protected void hitEffect(){
        playSound(SoundEffects.ENTITY_ZOMBIE_VILLAGER_CURE,1,1);
        ((WorldServer)world).sendParticles(null,Particles.CLOUD,locX(),locY(),locZ(),10,0.1, 0.1, 0.1, 0.1, true );
    }

    protected void spinEffect(){
        spinX = Math.sin(time);
        spinY = Math.sin(time);
        spinZ = Math.sin(time);

        Vec3D loc = new Vec3D(locX() + spinX, locY() + spinY, locZ() + spinZ);
        ((WorldServer)world).sendParticles(null,Particles.SOUL_FIRE_FLAME,loc.x,loc.y,loc.z,3,0, 0, 0, 0, true );

    }






}
