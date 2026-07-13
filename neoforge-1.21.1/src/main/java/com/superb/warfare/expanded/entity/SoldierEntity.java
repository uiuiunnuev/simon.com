package com.superb.warfare.expanded.entity;

import com.superb.warfare.expanded.registry.SWEEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.level.Level;

public class SoldierEntity extends AbstractSoldierEntity {
    private int grenadeCooldown = 0;

    public SoldierEntity(EntityType<? extends SoldierEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSoldierEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 25, 18.0F));
    }

    @Override
    protected String getWeaponItemId() {
        return "tacz:ak47";
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.grenadeCooldown > 0) {
            this.grenadeCooldown--;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        double dist = this.distanceToSqr(target);

        if (this.grenadeCooldown == 0 && dist > 16.0D && dist < 144.0D && this.random.nextFloat() < 0.25F) {
            this.grenadeCooldown = 150;
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.HOSTILES, 1.0F, 1.0F);
            ThrownGrenade grenade = new ThrownGrenade(this.level(), this);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - grenade.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            grenade.shoot(d0, d1 + d3 * 0.2D, d2, 0.9F, 8.0F);
            this.level().addFreshEntity(grenade);
            return;
        }

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILES, 1.0F, 2.0F);
        target.hurt(this.damageSources().mobAttack(this), 5.0F);

        double dx = target.getX() - this.getX();
        double dy = target.getY() - this.getY();
        double dz = target.getZ() - this.getZ();
        int steps = 10;
        for (int i = 0; i < steps; i++) {
            double progress = (double) i / steps;
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.CRIT,
                    this.getX() + dx * progress, this.getEyeY() - 0.2D + dy * progress, this.getZ() + dz * progress,
                    0, 0, 0);
        }
    }
}
