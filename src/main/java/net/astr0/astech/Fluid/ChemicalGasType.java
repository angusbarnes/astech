package net.astr0.astech.Fluid;

import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.gui.TintColor;
import net.minecraftforge.fluids.FluidType;

public class ChemicalGasType extends AsTechChemicalFluidType {
    public ChemicalGasType(TintColor color) {
        super(color, AsTechFluidType.GAS, FluidType.Properties.create().density(-5).viscosity(0));
    }
}
