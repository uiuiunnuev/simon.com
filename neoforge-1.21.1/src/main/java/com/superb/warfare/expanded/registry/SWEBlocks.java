package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.block.FlagBlock;
import com.superb.warfare.expanded.block.LandmineBlock;
import com.superb.warfare.expanded.block.WaypointBlock;
import com.superb.warfare.expanded.blockentity.FlagBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SWEBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, "superb_warfare_expanded");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "superb_warfare_expanded");

    public static final DeferredHolder<Block, WaypointBlock> WAYPOINT = BLOCKS.register("waypoint",
            () -> new WaypointBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.5F).sound(SoundType.STONE)));

    public static final DeferredHolder<Block, LandmineBlock> LANDMINE = BLOCKS.register("landmine",
            () -> new LandmineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F).sound(SoundType.METAL)));

    public static final DeferredHolder<Block, FlagBlock> FLAG = BLOCKS.register("flag",
            () -> new FlagBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FlagBlockEntity>> FLAG_ENTITY_TYPE = BLOCK_ENTITIES.register("flag",
            () -> BlockEntityType.Builder.of(FlagBlockEntity::new, FLAG.get()).build(null));

    public static void triggerConquestAdvancement(ServerPlayer player) {
        try {
            ResourceLocation advLoc = ResourceLocation.parse("superb_warfare_expanded:conquest");
            net.minecraft.advancements.AdvancementHolder adv = player.server.getAdvancements().get(advLoc);
            if (adv != null) {
                player.getAdvancements().award(adv, "conquest_accomplished");
            }
        } catch (Exception ignored) {}
    }

    public static void triggerCommanderDefeatAdvancement(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            try {
                ResourceLocation advLoc = ResourceLocation.parse("superb_warfare_expanded:commander_defeat");
                net.minecraft.advancements.AdvancementHolder adv = serverPlayer.server.getAdvancements().get(advLoc);
                if (adv != null) {
                    serverPlayer.getAdvancements().award(adv, "defeat_accomplished");
                }
            } catch (Exception ignored) {}
        }
    }
}
