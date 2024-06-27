package net.astr0.astech.item;

import net.astr0.astech.AsTech;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    // This is the global instance of the items registry
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AsTech.MODID);
    public static RegistryObject<Item> registerBucketItem(String fluidName, RegistryObject<FlowingFluid> source) {
        return ITEMS.register(String.format("%s_bucket", fluidName),
                () -> new AsTechBucketItem(source,
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1), String.format("tooltip.%s.fluid", fluidName)));
    }

    // Call this function from the entry point to allow the items register to link itself to the eventBus
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Create a simple crafting ingredient
    private static RegistryObject<Item> SimpleIngredientItem(String name, int stack_size) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(stack_size)));
    }

    public static final RegistryObject<Item> DEEZ_NUTS_ITEM = SimpleIngredientItem("deez_nuts", 16);
    public static final RegistryObject<Item> DEEZ_BUTTS_ITEM = SimpleIngredientItem("deez_butts", 32);
    public static final RegistryObject<Item> GOD_FORGED_INGOT_ITEM = SimpleIngredientItem("god_forged_ingot", 64);

    
    //#anchor MATERIAL_REGION
public static final RegistryObject<AsTechMaterialItem> POLYTETRAFLUOROETHYLENE_INGOT = ITEMS.register("polytetrafluoroethylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "polytetrafluoroethylene"));
public static final RegistryObject<AsTechMaterialItem> POLYTETRAFLUOROETHYLENE_DUST = ITEMS.register("polytetrafluoroethylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "polytetrafluoroethylene"));
public static final RegistryObject<AsTechMaterialItem> DIMETHYL_ETHER_INGOT = ITEMS.register("dimethyl_ether_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dimethyl_ether"));
public static final RegistryObject<AsTechMaterialItem> DIMETHYL_ETHER_DUST = ITEMS.register("dimethyl_ether_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dimethyl_ether"));
public static final RegistryObject<AsTechMaterialItem> HYDROCARBONIC_BROTH_INGOT = ITEMS.register("hydrocarbonic_broth_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrocarbonic_broth"));
public static final RegistryObject<AsTechMaterialItem> HYDROCARBONIC_BROTH_DUST = ITEMS.register("hydrocarbonic_broth_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrocarbonic_broth"));
public static final RegistryObject<AsTechMaterialItem> ETHANE_INGOT = ITEMS.register("ethane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethane"));
public static final RegistryObject<AsTechMaterialItem> ETHANE_DUST = ITEMS.register("ethane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethane"));
public static final RegistryObject<AsTechMaterialItem> PISS_WATER_INGOT = ITEMS.register("piss_water_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "piss_water"));
public static final RegistryObject<AsTechMaterialItem> PISS_WATER_DUST = ITEMS.register("piss_water_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "piss_water"));
public static final RegistryObject<AsTechMaterialItem> ALUMINIUM_HYDROXIDE_INGOT = ITEMS.register("aluminium_hydroxide_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aluminium_hydroxide"));
public static final RegistryObject<AsTechMaterialItem> ALUMINIUM_HYDROXIDE_DUST = ITEMS.register("aluminium_hydroxide_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aluminium_hydroxide"));
public static final RegistryObject<AsTechMaterialItem> SULFURIC_ACID_INGOT = ITEMS.register("sulfuric_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sulfuric_acid"));
public static final RegistryObject<AsTechMaterialItem> SULFURIC_ACID_DUST = ITEMS.register("sulfuric_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sulfuric_acid"));
public static final RegistryObject<AsTechMaterialItem> AMMONIA_INGOT = ITEMS.register("ammonia_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ammonia"));
public static final RegistryObject<AsTechMaterialItem> AMMONIA_DUST = ITEMS.register("ammonia_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ammonia"));
public static final RegistryObject<AsTechMaterialItem> BENZENE_INGOT = ITEMS.register("benzene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "benzene"));
public static final RegistryObject<AsTechMaterialItem> BENZENE_DUST = ITEMS.register("benzene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "benzene"));
public static final RegistryObject<AsTechMaterialItem> CHLORINE_INGOT = ITEMS.register("chlorine_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "chlorine"));
public static final RegistryObject<AsTechMaterialItem> CHLORINE_DUST = ITEMS.register("chlorine_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "chlorine"));
public static final RegistryObject<AsTechMaterialItem> ACETONE_INGOT = ITEMS.register("acetone_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acetone"));
public static final RegistryObject<AsTechMaterialItem> ACETONE_DUST = ITEMS.register("acetone_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acetone"));
public static final RegistryObject<AsTechMaterialItem> METHANOL_INGOT = ITEMS.register("methanol_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methanol"));
public static final RegistryObject<AsTechMaterialItem> METHANOL_DUST = ITEMS.register("methanol_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methanol"));
public static final RegistryObject<AsTechMaterialItem> HYDROGEN_INGOT = ITEMS.register("hydrogen_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrogen"));
public static final RegistryObject<AsTechMaterialItem> HYDROGEN_DUST = ITEMS.register("hydrogen_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrogen"));
public static final RegistryObject<AsTechMaterialItem> NITROGEN_INGOT = ITEMS.register("nitrogen_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "nitrogen"));
public static final RegistryObject<AsTechMaterialItem> NITROGEN_DUST = ITEMS.register("nitrogen_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "nitrogen"));
public static final RegistryObject<AsTechMaterialItem> TOLUENE_INGOT = ITEMS.register("toluene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "toluene"));
public static final RegistryObject<AsTechMaterialItem> TOLUENE_DUST = ITEMS.register("toluene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "toluene"));
public static final RegistryObject<AsTechMaterialItem> PROPANE_INGOT = ITEMS.register("propane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "propane"));
public static final RegistryObject<AsTechMaterialItem> PROPANE_DUST = ITEMS.register("propane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "propane"));
public static final RegistryObject<AsTechMaterialItem> ETHANOL_INGOT = ITEMS.register("ethanol_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethanol"));
public static final RegistryObject<AsTechMaterialItem> ETHANOL_DUST = ITEMS.register("ethanol_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethanol"));
public static final RegistryObject<AsTechMaterialItem> FORMALDEHYDE_INGOT = ITEMS.register("formaldehyde_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "formaldehyde"));
public static final RegistryObject<AsTechMaterialItem> FORMALDEHYDE_DUST = ITEMS.register("formaldehyde_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "formaldehyde"));
public static final RegistryObject<AsTechMaterialItem> HEXANE_INGOT = ITEMS.register("hexane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hexane"));
public static final RegistryObject<AsTechMaterialItem> HEXANE_DUST = ITEMS.register("hexane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hexane"));
public static final RegistryObject<AsTechMaterialItem> BUTANE_INGOT = ITEMS.register("butane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "butane"));
public static final RegistryObject<AsTechMaterialItem> BUTANE_DUST = ITEMS.register("butane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "butane"));
public static final RegistryObject<AsTechMaterialItem> CARBON_TETRACHLORIDE_INGOT = ITEMS.register("carbon_tetrachloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "carbon_tetrachloride"));
public static final RegistryObject<AsTechMaterialItem> CARBON_TETRACHLORIDE_DUST = ITEMS.register("carbon_tetrachloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "carbon_tetrachloride"));
public static final RegistryObject<AsTechMaterialItem> ETHYLENE_GLYCOL_INGOT = ITEMS.register("ethylene_glycol_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethylene_glycol"));
public static final RegistryObject<AsTechMaterialItem> ETHYLENE_GLYCOL_DUST = ITEMS.register("ethylene_glycol_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "ethylene_glycol"));
public static final RegistryObject<AsTechMaterialItem> ACETIC_ACID_INGOT = ITEMS.register("acetic_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acetic_acid"));
public static final RegistryObject<AsTechMaterialItem> ACETIC_ACID_DUST = ITEMS.register("acetic_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acetic_acid"));
public static final RegistryObject<AsTechMaterialItem> METHYL_CHLORIDE_INGOT = ITEMS.register("methyl_chloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> METHYL_CHLORIDE_DUST = ITEMS.register("methyl_chloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> PHOSGENE_INGOT = ITEMS.register("phosgene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "phosgene"));
public static final RegistryObject<AsTechMaterialItem> PHOSGENE_DUST = ITEMS.register("phosgene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "phosgene"));
public static final RegistryObject<AsTechMaterialItem> ISOPROPANOL_INGOT = ITEMS.register("isopropanol_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "isopropanol"));
public static final RegistryObject<AsTechMaterialItem> ISOPROPANOL_DUST = ITEMS.register("isopropanol_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "isopropanol"));
public static final RegistryObject<AsTechMaterialItem> ANILINE_INGOT = ITEMS.register("aniline_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aniline"));
public static final RegistryObject<AsTechMaterialItem> ANILINE_DUST = ITEMS.register("aniline_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aniline"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_HYPOCHLORITE_INGOT = ITEMS.register("sodium_hypochlorite_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_hypochlorite"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_HYPOCHLORITE_DUST = ITEMS.register("sodium_hypochlorite_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_hypochlorite"));
public static final RegistryObject<AsTechMaterialItem> HYDROGEN_SULFIDE_INGOT = ITEMS.register("hydrogen_sulfide_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrogen_sulfide"));
public static final RegistryObject<AsTechMaterialItem> HYDROGEN_SULFIDE_DUST = ITEMS.register("hydrogen_sulfide_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrogen_sulfide"));
public static final RegistryObject<AsTechMaterialItem> VINYL_CHLORIDE_INGOT = ITEMS.register("vinyl_chloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "vinyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> VINYL_CHLORIDE_DUST = ITEMS.register("vinyl_chloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "vinyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> XYLENE_INGOT = ITEMS.register("xylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "xylene"));
public static final RegistryObject<AsTechMaterialItem> XYLENE_DUST = ITEMS.register("xylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "xylene"));
public static final RegistryObject<AsTechMaterialItem> HYDROCHLORIC_ACID_INGOT = ITEMS.register("hydrochloric_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrochloric_acid"));
public static final RegistryObject<AsTechMaterialItem> HYDROCHLORIC_ACID_DUST = ITEMS.register("hydrochloric_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrochloric_acid"));
public static final RegistryObject<AsTechMaterialItem> NITRIC_ACID_INGOT = ITEMS.register("nitric_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "nitric_acid"));
public static final RegistryObject<AsTechMaterialItem> NITRIC_ACID_DUST = ITEMS.register("nitric_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "nitric_acid"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_HYDROXIDE_INGOT = ITEMS.register("sodium_hydroxide_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_hydroxide"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_HYDROXIDE_DUST = ITEMS.register("sodium_hydroxide_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_hydroxide"));
public static final RegistryObject<AsTechMaterialItem> DICHLOROMETHANE_INGOT = ITEMS.register("dichloromethane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dichloromethane"));
public static final RegistryObject<AsTechMaterialItem> DICHLOROMETHANE_DUST = ITEMS.register("dichloromethane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dichloromethane"));
public static final RegistryObject<AsTechMaterialItem> TRICHLOROETHYLENE_INGOT = ITEMS.register("trichloroethylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "trichloroethylene"));
public static final RegistryObject<AsTechMaterialItem> TRICHLOROETHYLENE_DUST = ITEMS.register("trichloroethylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "trichloroethylene"));
public static final RegistryObject<AsTechMaterialItem> PERCHLOROETHYLENE_INGOT = ITEMS.register("perchloroethylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "perchloroethylene"));
public static final RegistryObject<AsTechMaterialItem> PERCHLOROETHYLENE_DUST = ITEMS.register("perchloroethylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "perchloroethylene"));
public static final RegistryObject<AsTechMaterialItem> BROMINE_INGOT = ITEMS.register("bromine_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "bromine"));
public static final RegistryObject<AsTechMaterialItem> BROMINE_DUST = ITEMS.register("bromine_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "bromine"));
public static final RegistryObject<AsTechMaterialItem> PHOSPHORIC_ACID_INGOT = ITEMS.register("phosphoric_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "phosphoric_acid"));
public static final RegistryObject<AsTechMaterialItem> PHOSPHORIC_ACID_DUST = ITEMS.register("phosphoric_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "phosphoric_acid"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_BICARBONATE_INGOT = ITEMS.register("sodium_bicarbonate_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_bicarbonate"));
public static final RegistryObject<AsTechMaterialItem> SODIUM_BICARBONATE_DUST = ITEMS.register("sodium_bicarbonate_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "sodium_bicarbonate"));
public static final RegistryObject<AsTechMaterialItem> DIMETHYL_SULFOXIDE_INGOT = ITEMS.register("dimethyl_sulfoxide_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dimethyl_sulfoxide"));
public static final RegistryObject<AsTechMaterialItem> DIMETHYL_SULFOXIDE_DUST = ITEMS.register("dimethyl_sulfoxide_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dimethyl_sulfoxide"));
public static final RegistryObject<AsTechMaterialItem> HYDRAZINE_INGOT = ITEMS.register("hydrazine_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrazine"));
public static final RegistryObject<AsTechMaterialItem> HYDRAZINE_DUST = ITEMS.register("hydrazine_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrazine"));
public static final RegistryObject<AsTechMaterialItem> HEXAFLUOROPROPYLENE_INGOT = ITEMS.register("hexafluoropropylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hexafluoropropylene"));
public static final RegistryObject<AsTechMaterialItem> HEXAFLUOROPROPYLENE_DUST = ITEMS.register("hexafluoropropylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hexafluoropropylene"));
public static final RegistryObject<AsTechMaterialItem> TETRAHYDROFURAN_INGOT = ITEMS.register("tetrahydrofuran_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "tetrahydrofuran"));
public static final RegistryObject<AsTechMaterialItem> TETRAHYDROFURAN_DUST = ITEMS.register("tetrahydrofuran_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "tetrahydrofuran"));
public static final RegistryObject<AsTechMaterialItem> STYRENE_INGOT = ITEMS.register("styrene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "styrene"));
public static final RegistryObject<AsTechMaterialItem> STYRENE_DUST = ITEMS.register("styrene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "styrene"));
public static final RegistryObject<AsTechMaterialItem> PROPYLENE_INGOT = ITEMS.register("propylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "propylene"));
public static final RegistryObject<AsTechMaterialItem> PROPYLENE_DUST = ITEMS.register("propylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "propylene"));
public static final RegistryObject<AsTechMaterialItem> ACROLEIN_INGOT = ITEMS.register("acrolein_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acrolein"));
public static final RegistryObject<AsTechMaterialItem> ACROLEIN_DUST = ITEMS.register("acrolein_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "acrolein"));
public static final RegistryObject<AsTechMaterialItem> TETRACHLOROETHYLENE_INGOT = ITEMS.register("tetrachloroethylene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "tetrachloroethylene"));
public static final RegistryObject<AsTechMaterialItem> TETRACHLOROETHYLENE_DUST = ITEMS.register("tetrachloroethylene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "tetrachloroethylene"));
public static final RegistryObject<AsTechMaterialItem> AQUA_REGIA_INGOT = ITEMS.register("aqua_regia_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aqua_regia"));
public static final RegistryObject<AsTechMaterialItem> AQUA_REGIA_DUST = ITEMS.register("aqua_regia_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "aqua_regia"));
public static final RegistryObject<AsTechMaterialItem> CYANOGEN_INGOT = ITEMS.register("cyanogen_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "cyanogen"));
public static final RegistryObject<AsTechMaterialItem> CYANOGEN_DUST = ITEMS.register("cyanogen_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "cyanogen"));
public static final RegistryObject<AsTechMaterialItem> FLUOROSILICIC_ACID_INGOT = ITEMS.register("fluorosilicic_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "fluorosilicic_acid"));
public static final RegistryObject<AsTechMaterialItem> FLUOROSILICIC_ACID_DUST = ITEMS.register("fluorosilicic_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "fluorosilicic_acid"));
public static final RegistryObject<AsTechMaterialItem> TITANIUM_TETRACHLORIDE_INGOT = ITEMS.register("titanium_tetrachloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "titanium_tetrachloride"));
public static final RegistryObject<AsTechMaterialItem> TITANIUM_TETRACHLORIDE_DUST = ITEMS.register("titanium_tetrachloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "titanium_tetrachloride"));
public static final RegistryObject<AsTechMaterialItem> METHYL_ETHYL_KETONE_INGOT = ITEMS.register("methyl_ethyl_ketone_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methyl_ethyl_ketone"));
public static final RegistryObject<AsTechMaterialItem> METHYL_ETHYL_KETONE_DUST = ITEMS.register("methyl_ethyl_ketone_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "methyl_ethyl_ketone"));
public static final RegistryObject<AsTechMaterialItem> THIONYL_CHLORIDE_INGOT = ITEMS.register("thionyl_chloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "thionyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> THIONYL_CHLORIDE_DUST = ITEMS.register("thionyl_chloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "thionyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> AZOTH_INGOT = ITEMS.register("azoth_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "azoth"));
public static final RegistryObject<AsTechMaterialItem> AZOTH_DUST = ITEMS.register("azoth_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "azoth"));
public static final RegistryObject<AsTechMaterialItem> UNOBTANIUM_INGOT = ITEMS.register("unobtanium_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "unobtanium"));
public static final RegistryObject<AsTechMaterialItem> UNOBTANIUM_DUST = ITEMS.register("unobtanium_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "unobtanium"));
public static final RegistryObject<AsTechMaterialItem> DILITHIUM_INGOT = ITEMS.register("dilithium_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dilithium"));
public static final RegistryObject<AsTechMaterialItem> DILITHIUM_DUST = ITEMS.register("dilithium_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "dilithium"));
public static final RegistryObject<AsTechMaterialItem> ADAMANTIUM_INGOT = ITEMS.register("adamantium_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "adamantium"));
public static final RegistryObject<AsTechMaterialItem> ADAMANTIUM_DUST = ITEMS.register("adamantium_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "adamantium"));
public static final RegistryObject<AsTechMaterialItem> CARBONADIUM_INGOT = ITEMS.register("carbonadium_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "carbonadium"));
public static final RegistryObject<AsTechMaterialItem> CARBONADIUM_DUST = ITEMS.register("carbonadium_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "carbonadium"));
public static final RegistryObject<AsTechMaterialItem> RADON_INGOT = ITEMS.register("radon_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "radon"));
public static final RegistryObject<AsTechMaterialItem> RADON_DUST = ITEMS.register("radon_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "radon"));
public static final RegistryObject<AsTechMaterialItem> NEON_INGOT = ITEMS.register("neon_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "neon"));
public static final RegistryObject<AsTechMaterialItem> NEON_DUST = ITEMS.register("neon_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "neon"));
public static final RegistryObject<AsTechMaterialItem> ARGON_INGOT = ITEMS.register("argon_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "argon"));
public static final RegistryObject<AsTechMaterialItem> ARGON_DUST = ITEMS.register("argon_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "argon"));
public static final RegistryObject<AsTechMaterialItem> XENON_INGOT = ITEMS.register("xenon_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "xenon"));
public static final RegistryObject<AsTechMaterialItem> XENON_DUST = ITEMS.register("xenon_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "xenon"));
public static final RegistryObject<AsTechMaterialItem> CRYPTIC_ACID_INGOT = ITEMS.register("cryptic_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "cryptic_acid"));
public static final RegistryObject<AsTechMaterialItem> CRYPTIC_ACID_DUST = ITEMS.register("cryptic_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "cryptic_acid"));
public static final RegistryObject<AsTechMaterialItem> POLYVINYL_CHLORIDE_INGOT = ITEMS.register("polyvinyl_chloride_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "polyvinyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> POLYVINYL_CHLORIDE_DUST = ITEMS.register("polyvinyl_chloride_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "polyvinyl_chloride"));
public static final RegistryObject<AsTechMaterialItem> HYDROFLUORIC_ACID_INGOT = ITEMS.register("hydrofluoric_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrofluoric_acid"));
public static final RegistryObject<AsTechMaterialItem> HYDROFLUORIC_ACID_DUST = ITEMS.register("hydrofluoric_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "hydrofluoric_acid"));
public static final RegistryObject<AsTechMaterialItem> TRICHLOROMETHANE_INGOT = ITEMS.register("trichloromethane_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "trichloromethane"));
public static final RegistryObject<AsTechMaterialItem> TRICHLOROMETHANE_DUST = ITEMS.register("trichloromethane_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "trichloromethane"));
public static final RegistryObject<AsTechMaterialItem> PIRANHA_INGOT = ITEMS.register("piranha_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "piranha"));
public static final RegistryObject<AsTechMaterialItem> PIRANHA_DUST = ITEMS.register("piranha_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "piranha"));
public static final RegistryObject<AsTechMaterialItem> FLUOROANTIMONIC_ACID_INGOT = ITEMS.register("fluoroantimonic_acid_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "fluoroantimonic_acid"));
public static final RegistryObject<AsTechMaterialItem> FLUOROANTIMONIC_ACID_DUST = ITEMS.register("fluoroantimonic_acid_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "fluoroantimonic_acid"));
public static final RegistryObject<AsTechMaterialItem> STYRENE_BUTADIENE_INGOT = ITEMS.register("styrene_butadiene_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "styrene_butadiene"));
public static final RegistryObject<AsTechMaterialItem> STYRENE_BUTADIENE_DUST = ITEMS.register("styrene_butadiene_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "styrene_butadiene"));

    //#end MATERIAL_REGION
}
