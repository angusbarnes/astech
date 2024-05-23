package net.astr0.astech.Fluid;

import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.Fluid.helpers.TintColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class ChemicalGasType extends AsTechChemicalFluidType {
    public ChemicalGasType(TintColor color) {
        super(color, AsTechFluidType.GAS, FluidType.Properties.create().density(-5).viscosity(0));
    }
}
