package net.astr0.astech.Fluid;

import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.item.HazardBehavior;
import net.minecraftforge.fluids.FluidType;

public class ChemicalLiquidType extends AsTechChemicalFluidType {
    public ChemicalLiquidType(TintColor color, HazardBehavior.BehaviorType hazardType) {
        super(color, AsTechFluidType.LIQUID, FluidType.Properties.create().density(10).viscosity(5), hazardType);
    }
}
