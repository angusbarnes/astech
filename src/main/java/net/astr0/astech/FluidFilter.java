package net.astr0.astech;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fluids.FluidStack;

public class FluidFilter {
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    private boolean isLocked;
    private FluidStack filterFluidStack;

    public FluidFilter(boolean isLocked, FluidStack hash) {
        this.isLocked = isLocked;
        this.filterFluidStack = hash;
    }

    public void Lock(FluidStack stack) {
        isLocked = true;
        filterFluidStack = stack.copy();
        LogUtils.getLogger().info("Filter set as {}", filterFluidStack);
    }

    public void Unlock() {
        isLocked = false;
        filterFluidStack = FluidStack.EMPTY;
    }


    public FluidStack GetFilter() {
        return filterFluidStack;
    }

    public boolean TestFilterForMatch(FluidStack itemStack) {

        if(!this.isLocked) {
            return true;
        } else {
            return filterFluidStack.isFluidEqual(itemStack);
        }
    }
}
