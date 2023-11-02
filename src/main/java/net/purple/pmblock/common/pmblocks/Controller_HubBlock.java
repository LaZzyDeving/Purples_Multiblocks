package net.purple.pmblock.common.pmblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Controller_HubBlock extends Block {
    public Controller_HubBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        // Check if Hand is Controller and if so reduce its number
        if(pPlayer.getItemInHand(pHand).getItem() instanceof Core){
            if(pPlayer.getItemInHand(pHand).getCount()==1){
                pPlayer.setItemInHand(pHand,new ItemStack(Items.AIR));
            } else {
                pPlayer.getItemInHand(pHand).setCount(pPlayer.getItemInHand(pHand).getCount()-1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
