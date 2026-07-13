package com.superb.warfare.expanded.block;

import com.superb.warfare.expanded.blockentity.FlagBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class FlagBlock extends Block implements EntityBlock {
    public static final BooleanProperty CONQUERED = BooleanProperty.create("conquered");

    public FlagBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONQUERED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FlagBlockEntity(pos, state);
    }

    public static void conquerFlag(Level level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        if (state.getValue(CONQUERED)) {
            return; // Already conquered
        }

        level.setBlock(pos, state.setValue(CONQUERED, true), 3);
        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FlagBlockEntity flagBE) {
            flagBE.setConqueror(player.getName().getString(), player.getUUID());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONQUERED);
    }
}
