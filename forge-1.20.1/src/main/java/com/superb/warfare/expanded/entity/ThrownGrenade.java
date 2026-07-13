package com.superb.warfare.expanded.entity;

import com.superb.warfare.expanded.registry.SWEItems;
import com.superb.warfare.expanded.registry.SWEEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public class ThrownGrenade extends ThrowableItemProjectile {
    private int fuse = 60; // 3 seconds

    public ThrownGrenade(EntityType<? extends ThrownGrenade> type, Level level) {
        super(type, level);
    }

    public ThrownGrenade(Level level, LivingEntity shooter) {
        super(SWEEntities.THROWN_GRENADE.get(), shooter, level);
    }

    public ThrownGrenade(Level level, double x, double y, double z) {
        super(SWEEntities.THROWN_GRENADE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return SWEItems.GRENADE.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.fuse--;
            if (this.fuse <= 0) {
                explode();
            }
        }
    }

    private void explode() {
        this.discard();
        // Non-destructive explosion
        this.level().explode(this, this.getX(), this.getY() + 0.1D, this.getZ(), 2.5F, false, Level.ExplosionInteraction.NONE);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            Direction direction = hitResult.getDirection();

            // Bounce physics
            double x = motion.x;
            double y = motion.y;
            double z = motion.z;

            if (direction == Direction.UP || direction == Direction.DOWN) {
                y = -y * 0.4D;
            } else if (direction == Direction.EAST || direction == Direction.WEST) {
                x = -x * 0.4D;
            } else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                z = -z * 0.4D;
            }

            this.setDeltaMovement(x, y, z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide) {
            // Explode on direct contact with living entities
            if (hitResult.getEntity() instanceof LivingEntity) {
                explode();
            }
        }
    }
}
