package net.purple.pmblock.common.block.cobbleGen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.purple.pmblock.common.block.PMFluidHandler;
import net.purple.pmblock.common.block.PMItemHandler;
import net.purple.pmblock.common.block.multiblock.ControllerBlockEntity;
import net.purple.pmblock.common.registry.BlockEntityRegistry;
import net.purple.pmblock.common.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.state.BoneSnapshot;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CobbleGenBlockEntity extends ControllerBlockEntity {

    // TODO - Make own or not make own ?
    
    // GeckoLib necessity
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation DEPLOY = RawAnimation.begin();
    
    private final LazyOptional<PMItemHandler> itemHandlerOptional = LazyOptional.of(() -> new PMItemHandler(1));

    private final LazyOptional<PMFluidHandler> fluidHandlerOptional = LazyOptional.of(() -> new PMFluidHandler(
            new PMFluidHandler.PMFluidTank[]{
                    new PMFluidHandler.PMFluidTank(1000, CobbleGenBlockEntity::isInRecipeListBase),
                    new PMFluidHandler.PMFluidTank(3000, CobbleGenBlockEntity::isInRecipeListAccel)}));

    public static ArrayList<Fluid> RecipeListBase;
    public static ArrayList<Fluid> RecipleListAccelerator;

    private static boolean isInRecipeListBase(FluidStack fluidStack) {
        if(RecipeListBase == null){
            RecipeListBase = new ArrayList<>();
            // TODO - For loop through Recipes
        }
        //return (RecipeListBase.contains(fluidStack.getFluid()));
        return fluidStack.getFluid().isSame(Fluids.WATER);
    }

    private static boolean isInRecipeListAccel(FluidStack fluidStack) {
        if(RecipleListAccelerator == null){
            RecipleListAccelerator = new ArrayList<>();
            // TODO - For loop through Recipes
        }
        return (RecipleListAccelerator.contains(fluidStack.getFluid()));
    }



    // Cobble gen specifics


    // TODO - Add First in first out > Out and in with buckets

    // TODO - Change Recipe. Take Igneous Extruder as example

    // TODO - Add render for base and acceleration

    // TODO - Add render for outcome

    // TODO - Add overlay only visible when looking at for Output amount.


    // Next 3 = Centrifuge Specific > TODO - Remove once checked no problem
    public final Map<String, BoneSnapshot> boneSnapshots = new HashMap<>();

    public float getSpeedRatio() {
        return speedRatio;
    }

    float speedRatio = 0;

    public CobbleGenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.COBBLE_GEN.get(), pPos, pBlockState);
    }

    @Override
    public BlockState[][][] getBlockPattern() {
        BlockState a = BlockRegistry.COBBLE_GEN.get().defaultBlockState();
        BlockState b = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState c = null;

        //FIXME
        return new BlockState[][][]{
                {{b, b, c},{b, b, c},{c, c, c}},
                {{a, b, c},{b, b, c},{c, c, c}},
                {{null, null, null},{null, null, null},{null, null, c}}
        };
    }

    @Override
    public void assemble(ServerPlayer player){
        // TODO - > enable Again with animation
        triggerAnim("assemble_controller","assemble");
        super.assemble(player);
    }

    // TODO - Whats the DisplayName ? > Also no lang key for this yet
    @Override
    public Component getDisplayName() {
        return Component.literal("machine.cobble_gen");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    // TODO - Implement Drop
    @Override
    public void drop() {

    }





    // TODO > Also JEI ?
    public static void serverTick(Level level, BlockPos pos, BlockState blockState, CobbleGenBlockEntity cobbleGenBlockEntity) {
        cobbleGenBlockEntity.markUpdated();
    }

    protected void outputCobble() {

    }
    
    // GeckoLib necessity
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this,this::deployAnimController));
    }

    protected <E extends CobbleGenBlockEntity> PlayState deployAnimController(final AnimationState<E> state) {
        return state.setAndContinue(DEPLOY);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    @Override
    public void setItem(int pSlot, ItemStack pStack) {

    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    /*
        NBT Bullshit + Capabilities
     */
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandlerOptional.ifPresent(itemHandler -> itemHandler.deserializeNBT(pTag));
        fluidHandlerOptional.ifPresent(fluidHandler -> fluidHandler.deserializeNBT(pTag));
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        itemHandlerOptional.ifPresent(itemHandler -> tag.put(PMItemHandler.TAG_KEY, itemHandler.serializeNBT()));
        fluidHandlerOptional.ifPresent(fluidHandler -> tag.put(PMFluidHandler.TAG_KEY, fluidHandler.serializeNBT()));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return itemHandlerOptional.cast();
        }
        if(cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            return fluidHandlerOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerOptional.invalidate();
        fluidHandlerOptional.invalidate();
    }


    public void debugAmount() {

        fluidHandlerOptional.ifPresent(PMFluidHandler ->{
            for(int i = 0 ; i < PMFluidHandler.getTanks();i++){
                System.out.println("Tank:" + i + ": " + PMFluidHandler.getFluidInTank(i).getAmount());
            }


        });
    }
}
