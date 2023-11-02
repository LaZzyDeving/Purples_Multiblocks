package net.purple.pmblock.common.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.purple.pmblock.PMBlock;
import org.jetbrains.annotations.NotNull;


import java.util.Arrays;
import java.util.function.Predicate;

public class PMFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

    public static final String TAG_KEY = "FluidHandler";

    private final PMFluidTank[] tanks;

    public PMFluidHandler(PMFluidTank[] tanks) {
        this.tanks = tanks;
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    public PMFluidTank getTank(int tank){
        PMFluidTank fluidTank = null;
        try {
            fluidTank = tanks[tank];
        } catch (ArrayIndexOutOfBoundsException exception){
            PMBlock.LOGGER.warn("Attempted to access invalid tank");
        }
        return fluidTank;
    }



    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return getTank(tank).getFluid();
    }



    public void setFluidInTank(int tank, FluidStack stack) {
        getTank(tank).setFluid(stack);
    }

    @Override
    public int getTankCapacity(int tank) {
        return getTank(tank).getCapacity();
    }


    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        System.out.println("PM Test - isFluidValid");
        return getTank(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        FluidStack copy = resource.copy();
        for (PMFluidTank tank : tanks) {
            copy.shrink(tank.fill(copy, action));
        }
        return resource.getAmount() - copy.getAmount();
    }


    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack copy = resource.copy();
        for (int i = tanks.length - 1; i >= 0; i--) {
            copy.shrink(tanks[i].drain(copy, action).getAmount());
        }
        return new FluidStack(resource.getFluid(), resource.getAmount() - copy.getAmount());
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        int drained = 0;
        Fluid fluid = Arrays.stream(tanks).map(t -> t.fluid.getFluid()).toList().get(getTanks() - 1);
        for (int i = tanks.length - 1; i >= 0; i--) {
            if(tanks[i].getFluid().getFluid().isSame(fluid)) {
                FluidStack stack = tanks[i].drain(maxDrain - drained, action);
                drained += stack.getAmount();
            }
        }
        return new FluidStack(fluid, drained);
    }

    /*
        NBT and CompoundTag's
     */

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < getTanks(); i++) {
            CompoundTag compoundTag = new CompoundTag();
            getFluidInTank(i).writeToNBT(compoundTag);
            compoundTag.putInt("Capacity", getTankCapacity(i));

            listTag.add(compoundTag);
        }
        nbt.put("IFluidHandler", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getCompound(PMFluidHandler.TAG_KEY).getList("IFluidHandler", 10);
        PMFluidHandler.PMFluidTank[] fluidTanks = listTag.stream().map(compoundTag -> {
            FluidStack stack = FluidStack.loadFluidStackFromNBT((CompoundTag) compoundTag);
            int capacity = ((CompoundTag) compoundTag).getInt("Capacity");
            PMFluidHandler.PMFluidTank tank = new PMFluidHandler.PMFluidTank(capacity);
            tank.setFluid(stack);

            return tank;
        }).toArray(PMFluidHandler.PMFluidTank[]::new);
        for (int i = 0; i < getTanks(); i++) {
            if(getTankCapacity(i) != fluidTanks[i].getCapacity()) {
                PMBlock.LOGGER.warn("Tank capacity was saved incorrectly or changed, still attempting to fill tank");
            }
            setFluidInTank(i, fluidTanks[i].getFluid());
        }
    }


    // TODO - Builder to call PmFluidHandler easier








    public static class PMFluidTank implements IFluidTank {
        private final int capacity;

        private final Predicate<FluidStack> validator;

        private FluidStack fluid = FluidStack.EMPTY;

        public PMFluidTank(int capacity) {
            this(capacity, fluidStack -> true);
        }

        public PMFluidTank(int capacity, Predicate<FluidStack> validator) {
            this.capacity = capacity;
            this.validator = validator;
        }

        @Override
        public @NotNull FluidStack getFluid() {
            return this.fluid;
        }

        @Override
        public int getFluidAmount() {
            return this.fluid.getAmount();
        }

        @Override
        public int getCapacity() {
            return this.capacity;
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
            // FIXME return validator.test(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            // A non existing fluid or one that shouldnt fit in shouldnt work
            if (resource.isEmpty() || !isFluidValid(resource)) {
                return 0;
            }

            if (action.simulate()) {
                // If the tank is empty fill it but max to the capacity amount
                if (fluid.isEmpty()) {
                    return Math.min(capacity, resource.getAmount());
                }
                // No 2 different Fluids in 1 Tank
                if (!fluid.isFluidEqual(resource)) {
                    return 0;
                }
                // If the same fluid is added to existng beware to only fill to the capacity at max
                return Math.min(capacity - fluid.getAmount(), resource.getAmount());
            }



            // If the fluid is allowed and tank is empty
            if (fluid.isEmpty()) {
                fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
                // onContentsChanged(); TODO - Determine if needed
                return fluid.getAmount();
            }

            // No 2 different Fluids in 1 Tank
            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }

            // Amount that gets filled
            int filled = capacity - fluid.getAmount();

            // Check if more wants to get filled than its possible.
            // If every fits
            if (resource.getAmount() < filled) {
                fluid.grow(resource.getAmount());
                filled = resource.getAmount();
            }
            else { // Max fill to capacity
                fluid.setAmount(capacity);
            }
            // if (filled > 0) onContentsChanged(); TODO - See above
            return filled;
        }

        @Override // Drains the whole amount
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            // Cant drain what's empty  or if its the wrong fluid
            if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
                return FluidStack.EMPTY;
            }
            return drain(resource.getAmount(), action);
        }
        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            int drained = maxDrain;
            if (fluid.getAmount() < drained) {
                drained = fluid.getAmount();
            }
            FluidStack stack = new FluidStack(fluid, drained);
            if (action.execute() && drained > 0) {
                fluid.shrink(drained);
                // onContentsChanged(); TODO - See above
            }
            return stack;
        }

        // How is that used ? Isnt it dangerous without any stacks ? Or is that for machines that change the fluid ?
        private void setFluid(FluidStack stack) {
            this.fluid = stack;
        }


    }



}
