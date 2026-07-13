package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.entity.CommanderEntity;
import com.superb.warfare.expanded.entity.SniperEntity;
import com.superb.warfare.expanded.entity.SoldierEntity;
import com.superb.warfare.expanded.entity.ThrownGrenade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SWEEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "superb_warfare_expanded");

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownGrenade>> THROWN_GRENADE = ENTITIES.register("thrown_grenade",
            () -> EntityType.Builder.<ThrownGrenade>of(ThrownGrenade::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_grenade"));

    public static final DeferredHolder<EntityType<?>, EntityType<SoldierEntity>> SOLDIER = ENTITIES.register("soldier",
            () -> EntityType.Builder.<SoldierEntity>of(SoldierEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("soldier"));

    public static final DeferredHolder<EntityType<?>, EntityType<SniperEntity>> SNIPER = ENTITIES.register("sniper",
            () -> EntityType.Builder.<SniperEntity>of(SniperEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("sniper"));

    public static final DeferredHolder<EntityType<?>, EntityType<CommanderEntity>> COMMANDER = ENTITIES.register("commander",
            () -> EntityType.Builder.<CommanderEntity>of(CommanderEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("commander"));
}
