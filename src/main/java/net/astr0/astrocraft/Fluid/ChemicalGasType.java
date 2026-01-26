package net.astr0.astrocraft.Fluid;

import net.astr0.astrocraft.Fluid.helpers.AsTechFluidType;
import net.astr0.astrocraft.gui.TintColor;
import net.astr0.astrocraft.item.HazardBehavior;
import net.minecraftforge.fluids.FluidType;

public class ChemicalGasType extends AsTechChemicalFluidType {
    public ChemicalGasType(TintColor color, HazardBehavior.BehaviorType hazardType) {
        super(color, AsTechFluidType.GAS, FluidType.Properties.create().density(-5).viscosity(0), hazardType);
    }
}
