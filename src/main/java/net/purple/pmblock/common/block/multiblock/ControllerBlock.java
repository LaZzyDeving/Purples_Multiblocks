package net.purple.pmblock.common.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class ControllerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected ControllerBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING,Direction.NORTH).setValue(PartBlock.ASSEMBLED,false));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit){
        ControllerBlockEntity controller = (ControllerBlockEntity) pLevel.getBlockEntity(pPos);
        if (pLevel.isClientSide){
            return InteractionResult.SUCCESS;
        } else {
            if(controller.isAssembled()) {
                openContainer(pLevel, pPos, pPlayer);
            } else {
                controller.validateMultiBlock((ServerPlayer) pPlayer);
            }
            return InteractionResult.CONSUME;
        }
    }

    protected abstract void openContainer(Level pLevel, BlockPos pPos, Player player);




    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext){
        return this.defaultBlockState().setValue(FACING,pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState pState, Rotation pRotation){
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror){
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }


    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving){
        if(!pState.is(pNewState.getBlock())){
            if(pNewState.isAir()){
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (blockEntity instanceof ControllerBlockEntity controller){
                    controller.disassemble();
                    // controller.drop(); TODO - Add back in
                }
                pLevel.setBlockAndUpdate(pPos,pNewState);
            }
            super.onRemove(pState,pLevel,pPos,pNewState,pIsMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
        pBuilder.add(PartBlock.ASSEMBLED);
    }

}
