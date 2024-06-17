package net.astr0.astech;

import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.gui.FluidTankSlot;

public class FilteredFluidTankSlot extends FluidTankSlot {

    private MachineFluidHandler handler;

    public FilteredFluidTankSlot(MachineFluidHandler filteredHandler, int index, int x, int y) {
        super(filteredHandler.getTank(index), index, x, y);

        handler = filteredHandler;
    }

    @Override
}
