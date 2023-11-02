package net.purple.pmblock.common.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MimicBlock extends PartBlock {

    public MimicBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState pState){
        return RenderShape.INVISIBLE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState){
        return new MimicBlockEntity(pPos,pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext){
        if(pLevel.getBlockEntity(pPos) instanceof MimicBlockEntity mimicBlockEntity){
            return mimicBlockEntity.getOriginalState().getShape(pLevel, pPos,pContext);
        }
        return Shapes.block();
    }


}
