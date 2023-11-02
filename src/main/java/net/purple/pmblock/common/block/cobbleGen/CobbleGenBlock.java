package net.purple.pmblock.common.block.cobbleGen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import net.purple.pmblock.PMBlock;
import net.purple.pmblock.common.block.PMFluidHandler;
import net.purple.pmblock.common.block.multiblock.ControllerBlock;
import net.purple.pmblock.common.registry.BlockEntityRegistry;
import org.jetbrains.annotations.Nullable;

import static net.minecraftforge.fluids.FluidUtil.interactWithFluidHandler;

public class CobbleGenBlock extends ControllerBlock {


    public CobbleGenBlock(BlockBehaviour.Properties Properties) {
        super(Properties);
    }


    // TODO ???? > Maybe shouldnt have container for now
    @Override
    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer){
        NetworkHooks.openScreen((ServerPlayer) pPlayer,(MenuProvider) pLevel.getBlockEntity(pPos), buf -> buf.writeBlockPos(pPos));
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel,BlockState pState, BlockEntityType<T> pBlockEntityType){
        return pLevel.isClientSide() ? null: createTickerHelper(pBlockEntityType, BlockEntityRegistry.COBBLE_GEN.get(),CobbleGenBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState){
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CobbleGenBlockEntity(pPos,pState);
    }


    // Cobblegen Specific
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit){
        ItemStack held = pPlayer.getItemInHand(pHand);
        CobbleGenBlockEntity cobbleGenBlockEntity = (CobbleGenBlockEntity) pLevel.getBlockEntity(pPos);

        System.out.println("Before Test - test");
// && held.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()

        if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHit.getDirection()) || held.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()) {
            System.out.println("PMTest - Use Water Bucket");

            cobbleGenBlockEntity.debug();
            return InteractionResult.SUCCESS;
        }


        if (cobbleGenBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() ){
            if(held.getItem() instanceof BucketItem){

            }
            return InteractionResult.SUCCESS;
        }



        cobbleGenBlockEntity.outputCobble();

        return super.use(pState,pLevel,pPos,pPlayer,pHand,pHit);
    }

}
