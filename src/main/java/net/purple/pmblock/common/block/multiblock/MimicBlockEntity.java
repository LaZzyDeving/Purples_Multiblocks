package net.purple.pmblock.common.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.purple.pmblock.common.registry.BlockEntityRegistry;

public class MimicBlockEntity extends PartBlockEntity{

    private BlockState originalState;

    public MimicBlockEntity(BlockPos pPos, BlockState pState) {
        super(BlockEntityRegistry.MIMIC.get(), pPos,pState);
    }

    @Override
    public void load(CompoundTag tag){
        HolderGetter<Block> holderGetter = (this.level != null ?this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK.asLookup());
        if(tag.contains(ORIGINAL_STATE)) originalState = NbtUtils.readBlockState(holderGetter, tag.getCompound(ORIGINAL_STATE));
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag){
        if(!getOriginalState().isAir()) tag.put(ORIGINAL_STATE,NbtUtils.writeBlockState(getOriginalState()));
        super.saveAdditional(tag);
    }

    public void setOriginalState(BlockState previousState){
        this.originalState = previousState;
    }


    public BlockState getOriginalState(){
        if(originalState == null || originalState.getBlock() instanceof MimicBlock){
            return Blocks.AIR.defaultBlockState();
        }
        return originalState;
    }

    public void dissamble(){
        super.dissamble();
        level.setBlockAndUpdate(getBlockPos(),getOriginalState());
    }





}
