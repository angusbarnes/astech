package net.astr0.astech.Fluid;

import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.Fluid.helpers.TintColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class ChemicalLiquidType extends AsTechChemicalFluidType {
    public ChemicalLiquidType(TintColor color) {
        super(color, AsTechFluidType.LIQUID, FluidType.Properties.create().density(10).viscosity(5));
    }
}
