package net.astr0.astrocraft.Fluid;

import net.astr0.astrocraft.Fluid.helpers.AsTechFluidType;
import net.astr0.astrocraft.gui.TintColor;
import net.astr0.astrocraft.item.HazardBehavior;
import net.minecraftforge.fluids.FluidType;

public class ChemicalLiquidType extends AsTechChemicalFluidType {
    public ChemicalLiquidType(TintColor color, HazardBehavior.BehaviorType hazardType) {
        super(color, AsTechFluidType.LIQUID, FluidType.Properties.create().density(10).viscosity(5), hazardType);
    }
}
