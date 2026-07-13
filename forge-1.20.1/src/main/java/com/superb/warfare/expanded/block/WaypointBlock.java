package com.superb.warfare.expanded.block;

import com.superb.warfare.expanded.registry.SWEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaypointBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);

    public WaypointBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public static void activateWaypoint(Level level, BlockPos waypointPos, Player player) {
        BlockState state = level.getBlockState(waypointPos);
        if (state.is(SWEBlocks.WAYPOINT.get()) && !state.getValue(ACTIVE)) {
            level.setBlock(waypointPos, state.setValue(ACTIVE, true), 3);
            level.playSound(null, waypointPos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);

            // Give advancement
            if (player instanceof ServerPlayer serverPlayer) {
                // We will trigger the custom advancement here or handle it
                SWEBlocks.triggerConquestAdvancement(serverPlayer);
            }

            // Find nearest FlagBlock to update it
            for (BlockPos flagPos : BlockPos.betweenClosed(waypointPos.offset(-15, -15, -15), waypointPos.offset(15, 15, 15))) {
                BlockState flagState = level.getBlockState(flagPos);
                if (flagState.is(SWEBlocks.FLAG.get())) {
                    FlagBlock.conquerFlag(level, flagPos, player);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
