package com.superb.warfare.expanded.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SyncedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractSoldierEntity extends Monster implements RangedAttackMob {
    public static final EntityDataAccessor<Byte> STATE = SyncedEntityData.defineId(AbstractSoldierEntity.class, EntityDataSerializers.BYTE);

    public static final byte STATE_PATROL = 0;
    public static final byte STATE_ALERT = 1;

    private int alertTicks = 0;

    protected AbstractSoldierEntity(EntityType<? extends AbstractSoldierEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D) {
            @Override
            public boolean canUse() {
                return super.canUse() && getSoldierState() == STATE_PATROL;
            }
        });
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).toAndWithOtherMobs(AbstractSoldierEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SyncedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, STATE_PATROL);
    }

    public byte getSoldierState() {
        return this.entityData.get(STATE);
    }

    public void setSoldierState(byte state) {
        this.entityData.set(STATE, state);
        if (state == STATE_ALERT) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.32D);
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(40.0D);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(24.0D);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null && getSoldierState() == STATE_PATROL) {
            triggerAlert(target);
        }
    }

    public void triggerAlert(LivingEntity enemy) {
        if (!this.level().isClientSide) {
            this.setSoldierState(STATE_ALERT);
            this.alertTicks = 300; // Alert for 15 seconds

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.NOTE_BLOCK_BELL.value(), this.getSoundSource(), 1.5F, 1.2F);

            AABB bounds = this.getBoundingBox().inflate(24.0D, 12.0D, 24.0D);
            List<AbstractSoldierEntity> nearbyAllies = this.level().getEntitiesOfClass(AbstractSoldierEntity.class, bounds);
            for (AbstractSoldierEntity ally : nearbyAllies) {
                if (ally != this && ally.getSoldierState() == STATE_PATROL) {
                    ally.setSoldierState(STATE_ALERT);
                    ally.setTarget(enemy);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (getSoldierState() == STATE_ALERT) {
                if (this.getTarget() == null || !this.getTarget().isAlive()) {
                    this.alertTicks--;
                    if (this.alertTicks <= 0) {
                        this.setSoldierState(STATE_PATROL);
                    }
                } else {
                    this.alertTicks = 300;
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("SoldierState", getSoldierState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("SoldierState")) {
            setSoldierState(compound.getByte("SoldierState"));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        this.populateDefaultEquipmentSlots(difficulty);
        return data;
    }

    protected abstract String getWeaponItemId();

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        String gunId = getWeaponItemId();
        ItemStack weaponStack = ItemStack.EMPTY;
        try {
            ResourceLocation weaponLoc = ResourceLocation.parse(gunId);
            if (BuiltInRegistries.ITEM.containsKey(weaponLoc)) {
                weaponStack = new ItemStack(BuiltInRegistries.ITEM.get(weaponLoc));
            }
        } catch (Exception ignored) {}

        if (weaponStack.isEmpty()) {
            weaponStack = new ItemStack(net.minecraft.world.item.Items.BOW);
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, weaponStack);
    }
}
