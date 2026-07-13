package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.util.SpawnerReplacementProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SWERegistries {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, "superb_warfare_expanded");

    public static final RegistryObject<StructureProcessorType<SpawnerReplacementProcessor>> SPAWNER_REPLACEMENT = PROCESSORS.register("spawner_replacement",
            () -> () -> SpawnerReplacementProcessor.CODEC);
}
