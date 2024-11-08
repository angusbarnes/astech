package net.astr0.astech.Fluid;

import net.astr0.astech.FluidFilter;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineFluidHandler implements IFluidHandler {

    private FluidTank[] tanks;
    final List<FluidFilter> filters;
    final boolean allowDrain;

    public void setTankCount(int count, int capacity) {
        tanks = new FluidTank[count];

        for (int i = 0; i < count; i++) {
            tanks[i] = new FluidTank(capacity) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    MachineFluidHandler.this.onContentsChanged();
                }
            };

            tanks[i].setFluid(FluidStack.EMPTY);
        }
    }

    public MachineFluidHandler(int tank_count, int capacity) {
        this(tank_count, capacity, false);
    }

    public MachineFluidHandler(int tank_count, int capacity, boolean allowDrain) {
        setTankCount(tank_count, capacity);

        filters = new ArrayList<>(tank_count);

        for(int i = 0; i < tank_count; i++) {
            filters.add(new FluidFilter(false, FluidStack.EMPTY));
        }

        this.allowDrain = allowDrain;
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
        return tanks[i].getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return tanks[i].getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return tanks[i].isFluidValid(fluidStack) && filters.get(i).TestFilterForMatch(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {

        for (int i = 0; i < tanks.length; i++) {
            if ((fluidStack.isFluidEqual(tanks[i].getFluid())
                    || tanks[i].getFluid() == FluidStack.EMPTY
                    || tanks[i].getFluidAmount() <= 0)
                    && filters.get(i).TestFilterForMatch(fluidStack)
            ) {
                if(fluidAction == FluidAction.EXECUTE) onContentsChanged();
                return tanks[i].fill(fluidStack, fluidAction);
            }
        }

        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {

        if(!allowDrain) return FluidStack.EMPTY;

        for (FluidTank tank : tanks) {
            if (tank.getFluid().isFluidEqual(fluidStack)) {
                if(fluidAction == FluidAction.EXECUTE) onContentsChanged();
                return tank.drain(fluidStack, fluidAction);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction fluidAction) {

        if(!allowDrain) return FluidStack.EMPTY;

        for(FluidTank tank : tanks) {
            if(tank.getFluidAmount() > 0) {

                // Only trigger a change call back if a drain action is acutally executed
                // no need to tell anyone about simulations
                if(fluidAction == FluidAction.EXECUTE) onContentsChanged();

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

        ListTag filterTagList = new ListTag();
        for (int i = 0; i < filters.size(); i++)
        {
            if (filters.get(i).isLocked())
            {
                CompoundTag filterTag = new CompoundTag();
                filterTag.putInt("Slot", i);
                filters.get(i).GetFilter().writeToNBT(filterTag);
                filterTagList.add(filterTag);
            }
        }

        compoundTag.put("Filters", filterTagList);


        return compoundTag;
    }

    public void readFromNBT(CompoundTag compoundTag) {
        for(int i = 0; i < tanks.length; i++) {
            tanks[i].readFromNBT(compoundTag.getCompound("tank_%d".formatted(i)));
        }

        ListTag filterTagList = compoundTag.getList("Filters", Tag.TAG_COMPOUND);
        for (int i = 0; i < filterTagList.size(); i++)
        {
            CompoundTag itemTags = filterTagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < filters.size())
            {
                FluidStack stack = FluidStack.loadFluidStackFromNBT(itemTags);
                filters.get(slot).Lock(stack);
            }
        }
    }

    public void WriteToFlexiPacket(FlexiPacket packet) {
        for(int i = 0; i < filters.size(); i++) {

            boolean lock = filters.get(i).isLocked();
            packet.writeBool(lock);
            if(lock) {
                packet.writeFluidStack(filters.get(i).GetFilter());
            }
        }
    }

    public void ReadFromFlexiPacket(FlexiPacket packet) {
        for(int i = 0; i < filters.size(); i++) {

            boolean lock = packet.readBool();
            filters.get(i).setLocked(lock);
            if(lock) {
                filters.get(i).Lock(packet.readFluidStack());
            }
        }
    }

    public void LockSot(int i) {
        if(i > this.getTanks()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters.get(i).Lock(getFluidInTank(i));
    }

    public void UnlockSot(int i) {
        if(i > this.getTanks()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters.get(i).Unlock();
    }

    public void ToggleSlotLock(int i) {
        if(i > this.getTanks()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        if (filters.get(i).isLocked()) {
            UnlockSot(i);
        } else {
            LockSot(i);

        }
    }

    public boolean canFluidFit(int index, FluidStack fluid) {
        FluidStack stack = tanks[index].getFluid();

        boolean result = (fluid.isEmpty() || stack.isEmpty() || stack.containsFluid(fluid)) && stack.getAmount() + fluid.getAmount() <= tanks[index].getTankCapacity(0);

        return result;
    }

    public boolean checkSlot(int i) {
        return filters.get(i).isLocked();
    }

    public FluidStack getFilterForSlot(int slot) {
        return filters.get(slot).GetFilter();
    }
}
