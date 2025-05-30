package net.astr0.astech.Fluid;

import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.item.HazardBehavior;
import net.minecraftforge.fluids.FluidType;

public class ChemicalGasType extends AsTechChemicalFluidType {
    public ChemicalGasType(TintColor color, HazardBehavior.BehaviorType hazardType) {
        super(color, AsTechFluidType.GAS, FluidType.Properties.create().density(-5).viscosity(0), hazardType);
    }
}
