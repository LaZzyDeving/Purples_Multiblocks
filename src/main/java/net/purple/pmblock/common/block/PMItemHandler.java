package net.purple.pmblock.common.block;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class PMItemHandler extends ItemStackHandler {

    public static final String TAG_KEY = "ItemHandler";

    public PMItemHandler(int size) {
        super(size);
    }

    public PMItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public NonNullList<ItemStack> getItems(){
        return stacks;
    }

    // THE FUCK DO I NEED THIS ?
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt.getCompound(TAG_KEY));

    }
}
