package net.astr0.astech.compat.mek;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.astr0.astech.AsTech;
import net.minecraft.resources.ResourceLocation;

public class AsTechSlurries {

    public static final SlurryRegistry SLURRIES = new SlurryRegistry(AsTech.MODID);


    public static SlurryRegistryObject<Slurry, Slurry> TEST_SLURRY = SLURRIES.register("stipnicium", "#FF0011", new ResourceLocation("forge","tags/items/ores/stipnicium"));
}
