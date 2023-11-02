package net.purple.pmblock.common.block;

import com.mojang.blaze3d.Blaze3D;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public abstract class PmBlockEntity extends BlockEntity implements Container, MenuProvider, ICapabilityProvider {

    private final double tickSpawned = Blaze3D.getTime() * 20D;

    public PmBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    // Dont know yet for whats its good to know the age of the BlockEntity
    public double getAge() {
        return  (Blaze3D.getTime() * 20D)-tickSpawned;
    }

    // Container Size
    @Override
    public int getContainerSize() {
        return getItems().size();
    }
    protected abstract NonNullList<ItemStack> getItems();

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    // Single Itemstack at Slot X

    @Override
    public ItemStack getItem(int pSlot) {
        // Check if the Container is empty or the pSlot is out of index
        if(getItems() == null || pSlot >= getItems().size()){
            return ItemStack.EMPTY;
        };
        // Otherwise return the Itemstack at Slot X
        return getItems().get(pSlot);
    }

    // Remove x Amount of the ItemStack at Slot pSlot

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemStack = ContainerHelper.removeItem(this.getItems(),pSlot,pAmount);
        // When there is something to remove
        if (!itemStack.isEmpty()){
            this.setChanged();
        };
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.getItems(), pSlot);
    }

    // Check if its still a valid Container. > Here Max Distance = 8
    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    // Clears Container
    @Override
    public void clearContent(){
        getItems().clear();
    }

    // IDK - Assume Update between Client and Server ?
    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    protected void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public abstract void drop();












}
