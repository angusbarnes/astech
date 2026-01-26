package net.astr0.astrocraft.compat.mek;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryBuilder;
import mekanism.common.registration.WrappedDeferredRegister;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.astr0.astrocraft.gui.TintColor;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class SlurryRegistry extends WrappedDeferredRegister<Slurry> {


    public SlurryRegistry(String modid) {
        super(modid, MekanismAPI.SLURRY_REGISTRY_NAME);
    }

    public SlurryRegistryObject<Slurry, Slurry> register(String resource, String tint, ResourceLocation OreTag) {
        return register(resource, builder -> builder.tint(TintColor.fromHex(tint).getTintColor()).ore(OreTag));
    }

    public SlurryRegistryObject<Slurry, Slurry> register(String baseName, UnaryOperator<SlurryBuilder> builderModifier) {
        return new SlurryRegistryObject<>(internal.register("dirty_" + baseName, () -> new Slurry(builderModifier.apply(SlurryBuilder.dirty()))),
                internal.register("clean_" + baseName, () -> new Slurry(builderModifier.apply(SlurryBuilder.clean()))));
    }


}
