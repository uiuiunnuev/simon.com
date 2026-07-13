package com.superb.warfare.expanded.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.level.Level;

public class SniperEntity extends AbstractSoldierEntity {

    public SniperEntity(EntityType<? extends SniperEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSoldierEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.FOLLOW_RANGE, 36.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Snipers fire slower (50 ticks) but have very long distance (30 blocks)
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 0.9D, 50, 30.0F));
    }

    @Override
    protected String getWeaponItemId() {
        return "tacz:m24";
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        // Fire sniper shot (M24) - low pitch explosive shot
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILES, 1.2F, 0.8F);

        // High sniper damage
        target.hurt(this.damageSources().mobAttack(this), 10.0F);

        // Visual bullet trace (very dense for sniper)
        double dx = target.getX() - this.getX();
        double dy = target.getY() - this.getY();
        double dz = target.getZ() - this.getZ();
        int steps = 15;
        for (int i = 0; i < steps; i++) {
            double progress = (double) i / steps;
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.CRIT,
                    this.getX() + dx * progress, this.getEyeY() - 0.2D + dy * progress, this.getZ() + dz * progress,
                    0, 0.1D, 0);
        }
    }
}
