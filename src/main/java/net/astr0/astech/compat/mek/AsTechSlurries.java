package net.astr0.astech.compat.mek;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.astr0.astech.AsTech;
import net.minecraft.resources.ResourceLocation;

public class AsTechSlurries {

    public static final SlurryRegistry SLURRIES = new SlurryRegistry(AsTech.MODID);

    //#anchor SLURRY_REGION
public static SlurryRegistryObject<Slurry, Slurry> CHLORINE_SLURRY = SLURRIES.register("chlorine", "#d4ff00", new ResourceLocation("forge","tags/items/ores/chlorine"));
public static SlurryRegistryObject<Slurry, Slurry> NITROGEN_SLURRY = SLURRIES.register("nitrogen", "#8a8dff", new ResourceLocation("forge","tags/items/ores/nitrogen"));
public static SlurryRegistryObject<Slurry, Slurry> BROMINE_SLURRY = SLURRIES.register("bromine", "#ff3300", new ResourceLocation("forge","tags/items/ores/bromine"));
public static SlurryRegistryObject<Slurry, Slurry> ADAMANTIUM_SLURRY = SLURRIES.register("adamantium", "#cccccc", new ResourceLocation("forge","tags/items/ores/adamantium"));
public static SlurryRegistryObject<Slurry, Slurry> CARBONADIUM_SLURRY = SLURRIES.register("carbonadium", "#666666", new ResourceLocation("forge","tags/items/ores/carbonadium"));
public static SlurryRegistryObject<Slurry, Slurry> RADON_SLURRY = SLURRIES.register("radon", "#ffd700", new ResourceLocation("forge","tags/items/ores/radon"));
public static SlurryRegistryObject<Slurry, Slurry> NEON_SLURRY = SLURRIES.register("neon", "#ff5ccd", new ResourceLocation("forge","tags/items/ores/neon"));
public static SlurryRegistryObject<Slurry, Slurry> XENON_SLURRY = SLURRIES.register("xenon", "#3a79ff", new ResourceLocation("forge","tags/items/ores/xenon"));
public static SlurryRegistryObject<Slurry, Slurry> STIPNICIUM_SLURRY = SLURRIES.register("stipnicium", "#ff4245", new ResourceLocation("forge","tags/items/ores/stipnicium"));
public static SlurryRegistryObject<Slurry, Slurry> SODIUM_SLURRY = SLURRIES.register("sodium", "#ffaf54", new ResourceLocation("forge","tags/items/ores/sodium"));
public static SlurryRegistryObject<Slurry, Slurry> POTASSIUM_SLURRY = SLURRIES.register("potassium", "#d6adf7", new ResourceLocation("forge","tags/items/ores/potassium"));
public static SlurryRegistryObject<Slurry, Slurry> HELIUM_SLURRY = SLURRIES.register("helium", "#f7ecad", new ResourceLocation("forge","tags/items/ores/helium"));
public static SlurryRegistryObject<Slurry, Slurry> KRYPTON_SLURRY = SLURRIES.register("krypton", "#96fff1", new ResourceLocation("forge","tags/items/ores/krypton"));
public static SlurryRegistryObject<Slurry, Slurry> IUMIUM_SLURRY = SLURRIES.register("iumium", "#ff903b", new ResourceLocation("forge","tags/items/ores/iumium"));
    //#end SLURRY_REGION
}
