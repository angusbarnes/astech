package net.astr0.astech.Fluid;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public abstract class MachineFluidHandler implements IFluidHandler {

    private FluidTank[] tanks;

    public void setTankCount(int count, int capacity) {
        tanks = new FluidTank[count];

        for (int i = 0; i < count; i++) {
            tanks[i] = new FluidTank(capacity);
        }
    }

    public MachineFluidHandler(int tank_count, int capacity) {
        setTankCount(tank_count, capacity);
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    public FluidTank getTank(int i) {
        return tanks[i];
    }

    public void setTank(int i, FluidTank tank) {
        tanks[i] = tank;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        LogUtils.getLogger().warn("Called getFluidInTank with: %d".formatted(i));
        return tanks[i].getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        LogUtils.getLogger().warn("Called getTankCapacity with: %d".formatted(i));
        return tanks[i].getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        LogUtils.getLogger().warn("Called isFluidValid with: %d".formatted(i));
        return tanks[i].isFluidValid(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {

        for (int i = 0; i < tanks.length; i++) {
            LogUtils.getLogger().warn("In fill loop: %d".formatted(i));
            if (fluidStack.isFluidEqual(tanks[i].getFluid()) || tanks[i].getFluid() == FluidStack.EMPTY) {
                LogUtils.getLogger().warn("Attempting to fill %d".formatted(i));
                onContentsChanged();
                return tanks[i].fill(fluidStack, fluidAction);
            }
        }

        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {

        for (FluidTank tank : tanks) {
            if (tank.getFluid().isFluidEqual(fluidStack)) {
                onContentsChanged();
                return tank.drain(fluidStack, fluidAction);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction fluidAction) {
        LogUtils.getLogger().warn("Called drain with");

        for(FluidTank tank : tanks) {
            if(tank.getFluidAmount() > 0) {

                onContentsChanged();
                return tank.drain(maxDrain, fluidAction);
            }
        }

        return FluidStack.EMPTY;
    }


    protected abstract void onContentsChanged();


    // ChatGPT wrote these. There is a few potential bug conditions,
    // but I don't really expect these to ever occur
    public CompoundTag writeToNBT(CompoundTag compoundTag) {
        for(int i = 0; i < tanks.length; i++) {
            compoundTag.put("tank_%d".formatted(i),tanks[i].writeToNBT(new CompoundTag()));
        }
        return compoundTag;
    }

    public void readFromNBT(CompoundTag compoundTag) {
        for(int i = 0; i < tanks.length; i++) {
            tanks[i].readFromNBT(compoundTag.getCompound("tank_%d".formatted(i)));
        }
    }
}
