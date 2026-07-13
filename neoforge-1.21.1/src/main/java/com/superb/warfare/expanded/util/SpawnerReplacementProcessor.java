package com.superb.warfare.expanded.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class SpawnerReplacementProcessor extends StructureProcessor {
    public static final MapCodec<SpawnerReplacementProcessor> CODEC = MapCodec.unit(() -> SpawnerReplacementProcessor.INSTANCE);
    public static final SpawnerReplacementProcessor INSTANCE = new SpawnerReplacementProcessor();

    private SpawnerReplacementProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        if (blockInfo.state().is(Blocks.SPAWNER)) {
            CompoundTag tag = blockInfo.nbt() != null ? blockInfo.nbt().copy() : new CompoundTag();

            double rand = settings.getRandom(pos).nextDouble();
            String entityId = "superb_warfare_expanded:soldier";
            if (rand < 0.25D) {
                entityId = "superb_warfare_expanded:commander";
            } else if (rand < 0.5D) {
                entityId = "superb_warfare_expanded:sniper";
            }

            CompoundTag spawnData = new CompoundTag();
            CompoundTag entity = new CompoundTag();
            entity.putString("id", entityId);
            spawnData.put("entity", entity);
            tag.put("SpawnData", spawnData);

            return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), blockInfo.state(), tag);
        }
        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLACKSTONE_REPLACE; // Fallback or registered
    }
}
