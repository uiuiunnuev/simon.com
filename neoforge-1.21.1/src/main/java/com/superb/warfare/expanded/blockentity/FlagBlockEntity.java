package com.superb.warfare.expanded.blockentity;

import com.superb.warfare.expanded.registry.SWEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class FlagBlockEntity extends BlockEntity {
    private String conquerorName = "";
    private UUID conquerorUUID = null;

    public FlagBlockEntity(BlockPos pos, BlockState state) {
        super(SWEBlocks.FLAG_ENTITY_TYPE.get(), pos, state);
    }

    public String getConquerorName() {
        return this.conquerorName;
    }

    public UUID getConquerorUUID() {
        return this.conquerorUUID;
    }

    public void setConqueror(String name, UUID uuid) {
        this.conquerorName = name;
        this.conquerorUUID = uuid;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        if (nbt.contains("ConquerorName")) {
            this.conquerorName = nbt.getString("ConquerorName");
        }
        if (nbt.hasUUID("ConquerorUUID")) {
            this.conquerorUUID = nbt.getUUID("ConquerorUUID");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        if (this.conquerorName != null && !this.conquerorName.isEmpty()) {
            nbt.putString("ConquerorName", this.conquerorName);
        }
        if (this.conquerorUUID != null) {
            nbt.putUUID("ConquerorUUID", this.conquerorUUID);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt, registries);
        return nbt;
    }
}
