package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.util.SpawnerReplacementProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SWERegistries {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, "superb_warfare_expanded");

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<SpawnerReplacementProcessor>> SPAWNER_REPLACEMENT = PROCESSORS.register("spawner_replacement",
            () -> () -> SpawnerReplacementProcessor.CODEC);
}
