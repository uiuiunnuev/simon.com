package com.superb.warfare.expanded.entity;

import com.superb.warfare.expanded.block.WaypointBlock;
import com.superb.warfare.expanded.registry.SWEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CommanderEntity extends AbstractSoldierEntity {
    private int grenadeCooldown = 0;

    public CommanderEntity(EntityType<? extends CommanderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSoldierEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 28.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 18, 15.0F));
    }

    @Override
    protected String getWeaponItemId() {
        return "tacz:deagle";
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

        if (this.grenadeCooldown == 0 && dist > 16.0D && dist < 144.0D && this.random.nextFloat() < 0.35F) {
            this.grenadeCooldown = 120;
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

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILES, 0.9F, 1.5F);
        target.hurt(this.damageSources().mobAttack(this), 4.5F);

        double dx = target.getX() - this.getX();
        double dy = target.getY() - this.getY();
        double dz = target.getZ() - this.getZ();
        int steps = 8;
        for (int i = 0; i < steps; i++) {
            double progress = (double) i / steps;
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.CRIT,
                    this.getX() + dx * progress, this.getEyeY() - 0.2D + dy * progress, this.getZ() + dz * progress,
                    0, 0, 0);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide) {
            Player conqueror = null;
            if (source.getEntity() instanceof Player p) {
                conqueror = p;
            } else {
                conqueror = this.level().getNearestPlayer(this, 35.0D);
            }

            if (conqueror != null) {
                SWEBlocks.triggerCommanderDefeatAdvancement(conqueror);

                BlockPos pos = this.blockPosition();
                for (BlockPos bPos : BlockPos.betweenClosed(pos.offset(-30, -15, -30), pos.offset(30, 15, 30))) {
                    if (this.level().getBlockState(bPos).is(SWEBlocks.WAYPOINT.get())) {
                        WaypointBlock.activateWaypoint(this.level(), bPos, conqueror);
                        break;
                    }
                }
            }
        }
    }
}
