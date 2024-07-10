package net.astr0.astech.Fluid;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, AsTech.MODID);

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, AsTech.MODID);

    public static RegistryObject<FluidType> registerType(String name, String type, String colorCode) {
        if (type.equals("gas")) {
            return FLUID_TYPES.register(name, () -> new ChemicalGasType(TintColor.fromHex(colorCode, 179)));
        } else {
            return FLUID_TYPES.register(name, () -> new ChemicalLiquidType(TintColor.fromHex(colorCode, 205)));
        }
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);

        eventBus.addListener(ModFluids::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());
        //#anchor RENDER_REGION
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POLYTETRAFLUOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POLYTETRAFLUOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DIMETHYL_ETHER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DIMETHYL_ETHER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROCARBONIC_BROTH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROCARBONIC_BROTH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PISS_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PISS_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ALUMINIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ALUMINIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SULFURIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SULFURIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AMMONIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AMMONIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CHLORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CHLORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NITROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NITROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TOLUENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TOLUENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PROPANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PROPANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ETHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ETHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FORMALDEHYDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FORMALDEHYDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HEXANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HEXANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BUTANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BUTANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CARBON_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CARBON_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ETHYLENE_GLYCOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ETHYLENE_GLYCOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACETIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACETIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSGENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSGENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ISOPROPANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ISOPROPANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ANILINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ANILINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_HYPOCHLORITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_HYPOCHLORITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN_SULFIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN_SULFIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_VINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_VINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_XYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_XYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROCHLORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROCHLORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NITRIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NITRIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TRICHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TRICHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PERCHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PERCHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BROMINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BROMINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSPHORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSPHORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_BICARBONATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_BICARBONATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DIMETHYL_SULFOXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DIMETHYL_SULFOXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDRAZINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDRAZINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HEXAFLUOROPROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HEXAFLUOROPROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TETRAHYDROFURAN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TETRAHYDROFURAN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STYRENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STYRENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACROLEIN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACROLEIN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TETRACHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TETRACHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AQUA_REGIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AQUA_REGIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CYANOGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CYANOGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FLUOROSILICIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FLUOROSILICIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TITANIUM_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TITANIUM_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHYL_ETHYL_KETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHYL_ETHYL_KETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_THIONYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_THIONYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AZOTH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AZOTH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_UNOBTANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_UNOBTANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DILITHIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DILITHIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ADAMANTIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ADAMANTIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CARBONADIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CARBONADIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_RADON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_RADON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NEON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NEON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ARGON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ARGON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_XENON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_XENON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CRYPTIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CRYPTIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POLYVINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POLYVINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROFLUORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROFLUORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TRICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TRICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PIRANHA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PIRANHA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FLUOROANTIMONIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FLUOROANTIMONIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STYRENE_BUTADIENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STYRENE_BUTADIENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FLUORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FLUORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STIPNICIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STIPNICIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BIOLUMINESCENT_COON_JUICE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BIOLUMINESCENT_COON_JUICE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GELID_CRYOTHEUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GELID_CRYOTHEUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POTASSIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POTASSIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HELIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HELIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_KRYPTON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_KRYPTON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GHASTLY_LIQUID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GHASTLY_LIQUID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FENTANYL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FENTANYL.get(), RenderType.translucent());

        //#end RENDER_REGION
    }

    public static final RegistryObject<FluidType> SOAP_WATER_FLUID_TYPE = registerType("soap_water_fluid", "liquid", "#3486eb");
    public static final RegistryObject<FlowingFluid> SOURCE_SOAP_WATER = FLUIDS.register("soap_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SOAP_WATER = FLUIDS.register("flowing_soap_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SOAP_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("soap_water", SOURCE_SOAP_WATER))
            .bucket(ModItems.registerBucketItem("soap_water", SOURCE_SOAP_WATER));

    //#anchor FLUID_REGION

    public static final RegistryObject<FluidType> POLYTETRAFLUOROETHYLENE_FLUID_TYPE = registerType("polytetrafluoroethylene", "liquid", "#c6fff8");
    public static final RegistryObject<FlowingFluid> SOURCE_POLYTETRAFLUOROETHYLENE = FLUIDS.register("polytetrafluoroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POLYTETRAFLUOROETHYLENE = FLUIDS.register("flowing_polytetrafluoroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POLYTETRAFLUOROETHYLENE_FLUID_TYPE, SOURCE_POLYTETRAFLUOROETHYLENE, FLOWING_POLYTETRAFLUOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("polytetrafluoroethylene", SOURCE_POLYTETRAFLUOROETHYLENE))
            .bucket(ModItems.registerBucketItem("polytetrafluoroethylene", SOURCE_POLYTETRAFLUOROETHYLENE));

    
    public static final RegistryObject<FluidType> DIMETHYL_ETHER_FLUID_TYPE = registerType("dimethyl_ether", "gas", "#fcfffe");
    public static final RegistryObject<FlowingFluid> SOURCE_DIMETHYL_ETHER = FLUIDS.register("dimethyl_ether",
            () -> new ForgeFlowingFluid.Source(ModFluids.DIMETHYL_ETHER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DIMETHYL_ETHER = FLUIDS.register("flowing_dimethyl_ether",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DIMETHYL_ETHER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DIMETHYL_ETHER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DIMETHYL_ETHER_FLUID_TYPE, SOURCE_DIMETHYL_ETHER, FLOWING_DIMETHYL_ETHER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dimethyl_ether", SOURCE_DIMETHYL_ETHER))
            .bucket(ModItems.registerBucketItem("dimethyl_ether", SOURCE_DIMETHYL_ETHER));

    
    public static final RegistryObject<FluidType> HYDROCARBONIC_BROTH_FLUID_TYPE = registerType("hydrocarbonic_broth", "liquid", "#1e1e1e");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROCARBONIC_BROTH = FLUIDS.register("hydrocarbonic_broth",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROCARBONIC_BROTH_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROCARBONIC_BROTH = FLUIDS.register("flowing_hydrocarbonic_broth",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROCARBONIC_BROTH_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROCARBONIC_BROTH_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROCARBONIC_BROTH_FLUID_TYPE, SOURCE_HYDROCARBONIC_BROTH, FLOWING_HYDROCARBONIC_BROTH)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrocarbonic_broth", SOURCE_HYDROCARBONIC_BROTH))
            .bucket(ModItems.registerBucketItem("hydrocarbonic_broth", SOURCE_HYDROCARBONIC_BROTH));

    
    public static final RegistryObject<FluidType> ETHANE_FLUID_TYPE = registerType("ethane", "gas", "#ebba34");
    public static final RegistryObject<FlowingFluid> SOURCE_ETHANE = FLUIDS.register("ethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.ETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ETHANE = FLUIDS.register("flowing_ethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ETHANE_FLUID_TYPE, SOURCE_ETHANE, FLOWING_ETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethane", SOURCE_ETHANE))
            .bucket(ModItems.registerBucketItem("ethane", SOURCE_ETHANE));

    
    public static final RegistryObject<FluidType> PISS_WATER_FLUID_TYPE = registerType("piss_water", "liquid", "#ebba34");
    public static final RegistryObject<FlowingFluid> SOURCE_PISS_WATER = FLUIDS.register("piss_water",
            () -> new ForgeFlowingFluid.Source(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PISS_WATER = FLUIDS.register("flowing_piss_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PISS_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PISS_WATER_FLUID_TYPE, SOURCE_PISS_WATER, FLOWING_PISS_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("piss_water", SOURCE_PISS_WATER))
            .bucket(ModItems.registerBucketItem("piss_water", SOURCE_PISS_WATER));

    
    public static final RegistryObject<FluidType> ALUMINIUM_HYDROXIDE_FLUID_TYPE = registerType("aluminium_hydroxide", "gas", "#a8e5eb");
    public static final RegistryObject<FlowingFluid> SOURCE_ALUMINIUM_HYDROXIDE = FLUIDS.register("aluminium_hydroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ALUMINIUM_HYDROXIDE = FLUIDS.register("flowing_aluminium_hydroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ALUMINIUM_HYDROXIDE_FLUID_TYPE, SOURCE_ALUMINIUM_HYDROXIDE, FLOWING_ALUMINIUM_HYDROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aluminium_hydroxide", SOURCE_ALUMINIUM_HYDROXIDE))
            .bucket(ModItems.registerBucketItem("aluminium_hydroxide", SOURCE_ALUMINIUM_HYDROXIDE));

    
    public static final RegistryObject<FluidType> SULFURIC_ACID_FLUID_TYPE = registerType("sulfuric_acid", "liquid", "#ffcc00");
    public static final RegistryObject<FlowingFluid> SOURCE_SULFURIC_ACID = FLUIDS.register("sulfuric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SULFURIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SULFURIC_ACID = FLUIDS.register("flowing_sulfuric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SULFURIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SULFURIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SULFURIC_ACID_FLUID_TYPE, SOURCE_SULFURIC_ACID, FLOWING_SULFURIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sulfuric_acid", SOURCE_SULFURIC_ACID))
            .bucket(ModItems.registerBucketItem("sulfuric_acid", SOURCE_SULFURIC_ACID));

    
    public static final RegistryObject<FluidType> AMMONIA_FLUID_TYPE = registerType("ammonia", "gas", "#b2dfdb");
    public static final RegistryObject<FlowingFluid> SOURCE_AMMONIA = FLUIDS.register("ammonia",
            () -> new ForgeFlowingFluid.Source(ModFluids.AMMONIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AMMONIA = FLUIDS.register("flowing_ammonia",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AMMONIA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AMMONIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AMMONIA_FLUID_TYPE, SOURCE_AMMONIA, FLOWING_AMMONIA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ammonia", SOURCE_AMMONIA))
            .bucket(ModItems.registerBucketItem("ammonia", SOURCE_AMMONIA));

    
    public static final RegistryObject<FluidType> BENZENE_FLUID_TYPE = registerType("benzene", "liquid", "#f28e1c");
    public static final RegistryObject<FlowingFluid> SOURCE_BENZENE = FLUIDS.register("benzene",
            () -> new ForgeFlowingFluid.Source(ModFluids.BENZENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BENZENE = FLUIDS.register("flowing_benzene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BENZENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BENZENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BENZENE_FLUID_TYPE, SOURCE_BENZENE, FLOWING_BENZENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("benzene", SOURCE_BENZENE))
            .bucket(ModItems.registerBucketItem("benzene", SOURCE_BENZENE));

    
    public static final RegistryObject<FluidType> CHLORINE_FLUID_TYPE = registerType("chlorine", "gas", "#d4ff00");
    public static final RegistryObject<FlowingFluid> SOURCE_CHLORINE = FLUIDS.register("chlorine",
            () -> new ForgeFlowingFluid.Source(ModFluids.CHLORINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHLORINE = FLUIDS.register("flowing_chlorine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CHLORINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CHLORINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CHLORINE_FLUID_TYPE, SOURCE_CHLORINE, FLOWING_CHLORINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("chlorine", SOURCE_CHLORINE))
            .bucket(ModItems.registerBucketItem("chlorine", SOURCE_CHLORINE));

    
    public static final RegistryObject<FluidType> ACETONE_FLUID_TYPE = registerType("acetone", "liquid", "#e7e4e4");
    public static final RegistryObject<FlowingFluid> SOURCE_ACETONE = FLUIDS.register("acetone",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACETONE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACETONE = FLUIDS.register("flowing_acetone",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACETONE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACETONE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACETONE_FLUID_TYPE, SOURCE_ACETONE, FLOWING_ACETONE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acetone", SOURCE_ACETONE))
            .bucket(ModItems.registerBucketItem("acetone", SOURCE_ACETONE));

    
    public static final RegistryObject<FluidType> METHANOL_FLUID_TYPE = registerType("methanol", "liquid", "#80d4ff");
    public static final RegistryObject<FlowingFluid> SOURCE_METHANOL = FLUIDS.register("methanol",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHANOL = FLUIDS.register("flowing_methanol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHANOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHANOL_FLUID_TYPE, SOURCE_METHANOL, FLOWING_METHANOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methanol", SOURCE_METHANOL))
            .bucket(ModItems.registerBucketItem("methanol", SOURCE_METHANOL));

    
    public static final RegistryObject<FluidType> HYDROGEN_FLUID_TYPE = registerType("hydrogen", "gas", "#fff4e6");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN = FLUIDS.register("hydrogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN = FLUIDS.register("flowing_hydrogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_FLUID_TYPE, SOURCE_HYDROGEN, FLOWING_HYDROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen", SOURCE_HYDROGEN))
            .bucket(ModItems.registerBucketItem("hydrogen", SOURCE_HYDROGEN));

    
    public static final RegistryObject<FluidType> NITROGEN_FLUID_TYPE = registerType("nitrogen", "gas", "#8a8dff");
    public static final RegistryObject<FlowingFluid> SOURCE_NITROGEN = FLUIDS.register("nitrogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.NITROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NITROGEN = FLUIDS.register("flowing_nitrogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NITROGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NITROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NITROGEN_FLUID_TYPE, SOURCE_NITROGEN, FLOWING_NITROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("nitrogen", SOURCE_NITROGEN))
            .bucket(ModItems.registerBucketItem("nitrogen", SOURCE_NITROGEN));

    
    public static final RegistryObject<FluidType> TOLUENE_FLUID_TYPE = registerType("toluene", "liquid", "#ff6600");
    public static final RegistryObject<FlowingFluid> SOURCE_TOLUENE = FLUIDS.register("toluene",
            () -> new ForgeFlowingFluid.Source(ModFluids.TOLUENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TOLUENE = FLUIDS.register("flowing_toluene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TOLUENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TOLUENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TOLUENE_FLUID_TYPE, SOURCE_TOLUENE, FLOWING_TOLUENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("toluene", SOURCE_TOLUENE))
            .bucket(ModItems.registerBucketItem("toluene", SOURCE_TOLUENE));

    
    public static final RegistryObject<FluidType> PROPANE_FLUID_TYPE = registerType("propane", "gas", "#ffe6cc");
    public static final RegistryObject<FlowingFluid> SOURCE_PROPANE = FLUIDS.register("propane",
            () -> new ForgeFlowingFluid.Source(ModFluids.PROPANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PROPANE = FLUIDS.register("flowing_propane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PROPANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PROPANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PROPANE_FLUID_TYPE, SOURCE_PROPANE, FLOWING_PROPANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("propane", SOURCE_PROPANE))
            .bucket(ModItems.registerBucketItem("propane", SOURCE_PROPANE));

    
    public static final RegistryObject<FluidType> ETHANOL_FLUID_TYPE = registerType("ethanol", "liquid", "#ff9999");
    public static final RegistryObject<FlowingFluid> SOURCE_ETHANOL = FLUIDS.register("ethanol",
            () -> new ForgeFlowingFluid.Source(ModFluids.ETHANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ETHANOL = FLUIDS.register("flowing_ethanol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ETHANOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ETHANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ETHANOL_FLUID_TYPE, SOURCE_ETHANOL, FLOWING_ETHANOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethanol", SOURCE_ETHANOL))
            .bucket(ModItems.registerBucketItem("ethanol", SOURCE_ETHANOL));

    
    public static final RegistryObject<FluidType> FORMALDEHYDE_FLUID_TYPE = registerType("formaldehyde", "gas", "#ccffcc");
    public static final RegistryObject<FlowingFluid> SOURCE_FORMALDEHYDE = FLUIDS.register("formaldehyde",
            () -> new ForgeFlowingFluid.Source(ModFluids.FORMALDEHYDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FORMALDEHYDE = FLUIDS.register("flowing_formaldehyde",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FORMALDEHYDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FORMALDEHYDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FORMALDEHYDE_FLUID_TYPE, SOURCE_FORMALDEHYDE, FLOWING_FORMALDEHYDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("formaldehyde", SOURCE_FORMALDEHYDE))
            .bucket(ModItems.registerBucketItem("formaldehyde", SOURCE_FORMALDEHYDE));

    
    public static final RegistryObject<FluidType> HEXANE_FLUID_TYPE = registerType("hexane", "liquid", "#ffd700");
    public static final RegistryObject<FlowingFluid> SOURCE_HEXANE = FLUIDS.register("hexane",
            () -> new ForgeFlowingFluid.Source(ModFluids.HEXANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HEXANE = FLUIDS.register("flowing_hexane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HEXANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HEXANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HEXANE_FLUID_TYPE, SOURCE_HEXANE, FLOWING_HEXANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hexane", SOURCE_HEXANE))
            .bucket(ModItems.registerBucketItem("hexane", SOURCE_HEXANE));

    
    public static final RegistryObject<FluidType> BUTANE_FLUID_TYPE = registerType("butane", "gas", "#ccffff");
    public static final RegistryObject<FlowingFluid> SOURCE_BUTANE = FLUIDS.register("butane",
            () -> new ForgeFlowingFluid.Source(ModFluids.BUTANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BUTANE = FLUIDS.register("flowing_butane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BUTANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BUTANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BUTANE_FLUID_TYPE, SOURCE_BUTANE, FLOWING_BUTANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("butane", SOURCE_BUTANE))
            .bucket(ModItems.registerBucketItem("butane", SOURCE_BUTANE));

    
    public static final RegistryObject<FluidType> CARBON_TETRACHLORIDE_FLUID_TYPE = registerType("carbon_tetrachloride", "liquid", "#cccccc");
    public static final RegistryObject<FlowingFluid> SOURCE_CARBON_TETRACHLORIDE = FLUIDS.register("carbon_tetrachloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.CARBON_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CARBON_TETRACHLORIDE = FLUIDS.register("flowing_carbon_tetrachloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CARBON_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CARBON_TETRACHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CARBON_TETRACHLORIDE_FLUID_TYPE, SOURCE_CARBON_TETRACHLORIDE, FLOWING_CARBON_TETRACHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("carbon_tetrachloride", SOURCE_CARBON_TETRACHLORIDE))
            .bucket(ModItems.registerBucketItem("carbon_tetrachloride", SOURCE_CARBON_TETRACHLORIDE));

    
    public static final RegistryObject<FluidType> ETHYLENE_GLYCOL_FLUID_TYPE = registerType("ethylene_glycol", "liquid", "#99ccff");
    public static final RegistryObject<FlowingFluid> SOURCE_ETHYLENE_GLYCOL = FLUIDS.register("ethylene_glycol",
            () -> new ForgeFlowingFluid.Source(ModFluids.ETHYLENE_GLYCOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ETHYLENE_GLYCOL = FLUIDS.register("flowing_ethylene_glycol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ETHYLENE_GLYCOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ETHYLENE_GLYCOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ETHYLENE_GLYCOL_FLUID_TYPE, SOURCE_ETHYLENE_GLYCOL, FLOWING_ETHYLENE_GLYCOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethylene_glycol", SOURCE_ETHYLENE_GLYCOL))
            .bucket(ModItems.registerBucketItem("ethylene_glycol", SOURCE_ETHYLENE_GLYCOL));

    
    public static final RegistryObject<FluidType> ACETIC_ACID_FLUID_TYPE = registerType("acetic_acid", "liquid", "#ff6666");
    public static final RegistryObject<FlowingFluid> SOURCE_ACETIC_ACID = FLUIDS.register("acetic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACETIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACETIC_ACID = FLUIDS.register("flowing_acetic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACETIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACETIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACETIC_ACID_FLUID_TYPE, SOURCE_ACETIC_ACID, FLOWING_ACETIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acetic_acid", SOURCE_ACETIC_ACID))
            .bucket(ModItems.registerBucketItem("acetic_acid", SOURCE_ACETIC_ACID));

    
    public static final RegistryObject<FluidType> METHYL_CHLORIDE_FLUID_TYPE = registerType("methyl_chloride", "gas", "#ffcc99");
    public static final RegistryObject<FlowingFluid> SOURCE_METHYL_CHLORIDE = FLUIDS.register("methyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHYL_CHLORIDE = FLUIDS.register("flowing_methyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHYL_CHLORIDE_FLUID_TYPE, SOURCE_METHYL_CHLORIDE, FLOWING_METHYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methyl_chloride", SOURCE_METHYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("methyl_chloride", SOURCE_METHYL_CHLORIDE));

    
    public static final RegistryObject<FluidType> PHOSGENE_FLUID_TYPE = registerType("phosgene", "gas", "#999966");
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSGENE = FLUIDS.register("phosgene",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSGENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSGENE = FLUIDS.register("flowing_phosgene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSGENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSGENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSGENE_FLUID_TYPE, SOURCE_PHOSGENE, FLOWING_PHOSGENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosgene", SOURCE_PHOSGENE))
            .bucket(ModItems.registerBucketItem("phosgene", SOURCE_PHOSGENE));

    
    public static final RegistryObject<FluidType> ISOPROPANOL_FLUID_TYPE = registerType("isopropanol", "liquid", "#cc99ff");
    public static final RegistryObject<FlowingFluid> SOURCE_ISOPROPANOL = FLUIDS.register("isopropanol",
            () -> new ForgeFlowingFluid.Source(ModFluids.ISOPROPANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ISOPROPANOL = FLUIDS.register("flowing_isopropanol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ISOPROPANOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ISOPROPANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ISOPROPANOL_FLUID_TYPE, SOURCE_ISOPROPANOL, FLOWING_ISOPROPANOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("isopropanol", SOURCE_ISOPROPANOL))
            .bucket(ModItems.registerBucketItem("isopropanol", SOURCE_ISOPROPANOL));

    
    public static final RegistryObject<FluidType> ANILINE_FLUID_TYPE = registerType("aniline", "liquid", "#b2b2b2");
    public static final RegistryObject<FlowingFluid> SOURCE_ANILINE = FLUIDS.register("aniline",
            () -> new ForgeFlowingFluid.Source(ModFluids.ANILINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ANILINE = FLUIDS.register("flowing_aniline",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ANILINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ANILINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ANILINE_FLUID_TYPE, SOURCE_ANILINE, FLOWING_ANILINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aniline", SOURCE_ANILINE))
            .bucket(ModItems.registerBucketItem("aniline", SOURCE_ANILINE));

    
    public static final RegistryObject<FluidType> SODIUM_HYPOCHLORITE_FLUID_TYPE = registerType("sodium_hypochlorite", "liquid", "#99ff99");
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_HYPOCHLORITE = FLUIDS.register("sodium_hypochlorite",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_HYPOCHLORITE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_HYPOCHLORITE = FLUIDS.register("flowing_sodium_hypochlorite",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_HYPOCHLORITE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_HYPOCHLORITE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_HYPOCHLORITE_FLUID_TYPE, SOURCE_SODIUM_HYPOCHLORITE, FLOWING_SODIUM_HYPOCHLORITE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_hypochlorite", SOURCE_SODIUM_HYPOCHLORITE))
            .bucket(ModItems.registerBucketItem("sodium_hypochlorite", SOURCE_SODIUM_HYPOCHLORITE));

    
    public static final RegistryObject<FluidType> HYDROGEN_SULFIDE_FLUID_TYPE = registerType("hydrogen_sulfide", "gas", "#ffff99");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN_SULFIDE = FLUIDS.register("hydrogen_sulfide",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_SULFIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN_SULFIDE = FLUIDS.register("flowing_hydrogen_sulfide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_SULFIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_SULFIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_SULFIDE_FLUID_TYPE, SOURCE_HYDROGEN_SULFIDE, FLOWING_HYDROGEN_SULFIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen_sulfide", SOURCE_HYDROGEN_SULFIDE))
            .bucket(ModItems.registerBucketItem("hydrogen_sulfide", SOURCE_HYDROGEN_SULFIDE));

    
    public static final RegistryObject<FluidType> VINYL_CHLORIDE_FLUID_TYPE = registerType("vinyl_chloride", "gas", "#b3ffb3");
    public static final RegistryObject<FlowingFluid> SOURCE_VINYL_CHLORIDE = FLUIDS.register("vinyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.VINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_VINYL_CHLORIDE = FLUIDS.register("flowing_vinyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.VINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties VINYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            VINYL_CHLORIDE_FLUID_TYPE, SOURCE_VINYL_CHLORIDE, FLOWING_VINYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("vinyl_chloride", SOURCE_VINYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("vinyl_chloride", SOURCE_VINYL_CHLORIDE));

    
    public static final RegistryObject<FluidType> XYLENE_FLUID_TYPE = registerType("xylene", "liquid", "#f8cfff");
    public static final RegistryObject<FlowingFluid> SOURCE_XYLENE = FLUIDS.register("xylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.XYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_XYLENE = FLUIDS.register("flowing_xylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.XYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties XYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            XYLENE_FLUID_TYPE, SOURCE_XYLENE, FLOWING_XYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("xylene", SOURCE_XYLENE))
            .bucket(ModItems.registerBucketItem("xylene", SOURCE_XYLENE));

    
    public static final RegistryObject<FluidType> HYDROCHLORIC_ACID_FLUID_TYPE = registerType("hydrochloric_acid", "liquid", "#ff6666");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROCHLORIC_ACID = FLUIDS.register("hydrochloric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROCHLORIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROCHLORIC_ACID = FLUIDS.register("flowing_hydrochloric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROCHLORIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROCHLORIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROCHLORIC_ACID_FLUID_TYPE, SOURCE_HYDROCHLORIC_ACID, FLOWING_HYDROCHLORIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrochloric_acid", SOURCE_HYDROCHLORIC_ACID))
            .bucket(ModItems.registerBucketItem("hydrochloric_acid", SOURCE_HYDROCHLORIC_ACID));

    
    public static final RegistryObject<FluidType> NITRIC_ACID_FLUID_TYPE = registerType("nitric_acid", "liquid", "#ffcc00");
    public static final RegistryObject<FlowingFluid> SOURCE_NITRIC_ACID = FLUIDS.register("nitric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.NITRIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NITRIC_ACID = FLUIDS.register("flowing_nitric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NITRIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NITRIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NITRIC_ACID_FLUID_TYPE, SOURCE_NITRIC_ACID, FLOWING_NITRIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("nitric_acid", SOURCE_NITRIC_ACID))
            .bucket(ModItems.registerBucketItem("nitric_acid", SOURCE_NITRIC_ACID));

    
    public static final RegistryObject<FluidType> SODIUM_HYDROXIDE_FLUID_TYPE = registerType("sodium_hydroxide", "liquid", "#99ccff");
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_HYDROXIDE = FLUIDS.register("sodium_hydroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_HYDROXIDE = FLUIDS.register("flowing_sodium_hydroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_HYDROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_HYDROXIDE_FLUID_TYPE, SOURCE_SODIUM_HYDROXIDE, FLOWING_SODIUM_HYDROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_hydroxide", SOURCE_SODIUM_HYDROXIDE))
            .bucket(ModItems.registerBucketItem("sodium_hydroxide", SOURCE_SODIUM_HYDROXIDE));

    
    public static final RegistryObject<FluidType> DICHLOROMETHANE_FLUID_TYPE = registerType("dichloromethane", "gas", "#c6c6a7");
    public static final RegistryObject<FlowingFluid> SOURCE_DICHLOROMETHANE = FLUIDS.register("dichloromethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.DICHLOROMETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DICHLOROMETHANE = FLUIDS.register("flowing_dichloromethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DICHLOROMETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DICHLOROMETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DICHLOROMETHANE_FLUID_TYPE, SOURCE_DICHLOROMETHANE, FLOWING_DICHLOROMETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dichloromethane", SOURCE_DICHLOROMETHANE))
            .bucket(ModItems.registerBucketItem("dichloromethane", SOURCE_DICHLOROMETHANE));

    
    public static final RegistryObject<FluidType> TRICHLOROETHYLENE_FLUID_TYPE = registerType("trichloroethylene", "liquid", "#cc9999");
    public static final RegistryObject<FlowingFluid> SOURCE_TRICHLOROETHYLENE = FLUIDS.register("trichloroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.TRICHLOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TRICHLOROETHYLENE = FLUIDS.register("flowing_trichloroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TRICHLOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TRICHLOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TRICHLOROETHYLENE_FLUID_TYPE, SOURCE_TRICHLOROETHYLENE, FLOWING_TRICHLOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("trichloroethylene", SOURCE_TRICHLOROETHYLENE))
            .bucket(ModItems.registerBucketItem("trichloroethylene", SOURCE_TRICHLOROETHYLENE));

    
    public static final RegistryObject<FluidType> PERCHLOROETHYLENE_FLUID_TYPE = registerType("perchloroethylene", "liquid", "#9999cc");
    public static final RegistryObject<FlowingFluid> SOURCE_PERCHLOROETHYLENE = FLUIDS.register("perchloroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.PERCHLOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PERCHLOROETHYLENE = FLUIDS.register("flowing_perchloroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PERCHLOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PERCHLOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PERCHLOROETHYLENE_FLUID_TYPE, SOURCE_PERCHLOROETHYLENE, FLOWING_PERCHLOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("perchloroethylene", SOURCE_PERCHLOROETHYLENE))
            .bucket(ModItems.registerBucketItem("perchloroethylene", SOURCE_PERCHLOROETHYLENE));

    
    public static final RegistryObject<FluidType> BROMINE_FLUID_TYPE = registerType("bromine", "liquid", "#ff3300");
    public static final RegistryObject<FlowingFluid> SOURCE_BROMINE = FLUIDS.register("bromine",
            () -> new ForgeFlowingFluid.Source(ModFluids.BROMINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BROMINE = FLUIDS.register("flowing_bromine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BROMINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BROMINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BROMINE_FLUID_TYPE, SOURCE_BROMINE, FLOWING_BROMINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("bromine", SOURCE_BROMINE))
            .bucket(ModItems.registerBucketItem("bromine", SOURCE_BROMINE));

    
    public static final RegistryObject<FluidType> PHOSPHORIC_ACID_FLUID_TYPE = registerType("phosphoric_acid", "liquid", "#ccff99");
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSPHORIC_ACID = FLUIDS.register("phosphoric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSPHORIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSPHORIC_ACID = FLUIDS.register("flowing_phosphoric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSPHORIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSPHORIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSPHORIC_ACID_FLUID_TYPE, SOURCE_PHOSPHORIC_ACID, FLOWING_PHOSPHORIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosphoric_acid", SOURCE_PHOSPHORIC_ACID))
            .bucket(ModItems.registerBucketItem("phosphoric_acid", SOURCE_PHOSPHORIC_ACID));

    
    public static final RegistryObject<FluidType> SODIUM_BICARBONATE_FLUID_TYPE = registerType("sodium_bicarbonate", "liquid", "#ffffff");
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_BICARBONATE = FLUIDS.register("sodium_bicarbonate",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_BICARBONATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_BICARBONATE = FLUIDS.register("flowing_sodium_bicarbonate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_BICARBONATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_BICARBONATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_BICARBONATE_FLUID_TYPE, SOURCE_SODIUM_BICARBONATE, FLOWING_SODIUM_BICARBONATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_bicarbonate", SOURCE_SODIUM_BICARBONATE))
            .bucket(ModItems.registerBucketItem("sodium_bicarbonate", SOURCE_SODIUM_BICARBONATE));

    
    public static final RegistryObject<FluidType> DIMETHYL_SULFOXIDE_FLUID_TYPE = registerType("dimethyl_sulfoxide", "liquid", "#99ccff");
    public static final RegistryObject<FlowingFluid> SOURCE_DIMETHYL_SULFOXIDE = FLUIDS.register("dimethyl_sulfoxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.DIMETHYL_SULFOXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DIMETHYL_SULFOXIDE = FLUIDS.register("flowing_dimethyl_sulfoxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DIMETHYL_SULFOXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DIMETHYL_SULFOXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DIMETHYL_SULFOXIDE_FLUID_TYPE, SOURCE_DIMETHYL_SULFOXIDE, FLOWING_DIMETHYL_SULFOXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dimethyl_sulfoxide", SOURCE_DIMETHYL_SULFOXIDE))
            .bucket(ModItems.registerBucketItem("dimethyl_sulfoxide", SOURCE_DIMETHYL_SULFOXIDE));

    
    public static final RegistryObject<FluidType> HYDRAZINE_FLUID_TYPE = registerType("hydrazine", "liquid", "#ccffcc");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDRAZINE = FLUIDS.register("hydrazine",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDRAZINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDRAZINE = FLUIDS.register("flowing_hydrazine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDRAZINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDRAZINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDRAZINE_FLUID_TYPE, SOURCE_HYDRAZINE, FLOWING_HYDRAZINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrazine", SOURCE_HYDRAZINE))
            .bucket(ModItems.registerBucketItem("hydrazine", SOURCE_HYDRAZINE));

    
    public static final RegistryObject<FluidType> HEXAFLUOROPROPYLENE_FLUID_TYPE = registerType("hexafluoropropylene", "gas", "#99ffff");
    public static final RegistryObject<FlowingFluid> SOURCE_HEXAFLUOROPROPYLENE = FLUIDS.register("hexafluoropropylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.HEXAFLUOROPROPYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HEXAFLUOROPROPYLENE = FLUIDS.register("flowing_hexafluoropropylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HEXAFLUOROPROPYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HEXAFLUOROPROPYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HEXAFLUOROPROPYLENE_FLUID_TYPE, SOURCE_HEXAFLUOROPROPYLENE, FLOWING_HEXAFLUOROPROPYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hexafluoropropylene", SOURCE_HEXAFLUOROPROPYLENE))
            .bucket(ModItems.registerBucketItem("hexafluoropropylene", SOURCE_HEXAFLUOROPROPYLENE));

    
    public static final RegistryObject<FluidType> TETRAHYDROFURAN_FLUID_TYPE = registerType("tetrahydrofuran", "liquid", "#e6e6e6");
    public static final RegistryObject<FlowingFluid> SOURCE_TETRAHYDROFURAN = FLUIDS.register("tetrahydrofuran",
            () -> new ForgeFlowingFluid.Source(ModFluids.TETRAHYDROFURAN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TETRAHYDROFURAN = FLUIDS.register("flowing_tetrahydrofuran",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TETRAHYDROFURAN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TETRAHYDROFURAN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TETRAHYDROFURAN_FLUID_TYPE, SOURCE_TETRAHYDROFURAN, FLOWING_TETRAHYDROFURAN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("tetrahydrofuran", SOURCE_TETRAHYDROFURAN))
            .bucket(ModItems.registerBucketItem("tetrahydrofuran", SOURCE_TETRAHYDROFURAN));

    
    public static final RegistryObject<FluidType> STYRENE_FLUID_TYPE = registerType("styrene", "liquid", "#ff9999");
    public static final RegistryObject<FlowingFluid> SOURCE_STYRENE = FLUIDS.register("styrene",
            () -> new ForgeFlowingFluid.Source(ModFluids.STYRENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STYRENE = FLUIDS.register("flowing_styrene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STYRENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STYRENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STYRENE_FLUID_TYPE, SOURCE_STYRENE, FLOWING_STYRENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("styrene", SOURCE_STYRENE))
            .bucket(ModItems.registerBucketItem("styrene", SOURCE_STYRENE));

    
    public static final RegistryObject<FluidType> PROPYLENE_FLUID_TYPE = registerType("propylene", "gas", "#ffcc99");
    public static final RegistryObject<FlowingFluid> SOURCE_PROPYLENE = FLUIDS.register("propylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.PROPYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PROPYLENE = FLUIDS.register("flowing_propylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PROPYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PROPYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PROPYLENE_FLUID_TYPE, SOURCE_PROPYLENE, FLOWING_PROPYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("propylene", SOURCE_PROPYLENE))
            .bucket(ModItems.registerBucketItem("propylene", SOURCE_PROPYLENE));

    
    public static final RegistryObject<FluidType> ACROLEIN_FLUID_TYPE = registerType("acrolein", "liquid", "#cc9966");
    public static final RegistryObject<FlowingFluid> SOURCE_ACROLEIN = FLUIDS.register("acrolein",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACROLEIN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACROLEIN = FLUIDS.register("flowing_acrolein",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACROLEIN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACROLEIN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACROLEIN_FLUID_TYPE, SOURCE_ACROLEIN, FLOWING_ACROLEIN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acrolein", SOURCE_ACROLEIN))
            .bucket(ModItems.registerBucketItem("acrolein", SOURCE_ACROLEIN));

    
    public static final RegistryObject<FluidType> TETRACHLOROETHYLENE_FLUID_TYPE = registerType("tetrachloroethylene", "liquid", "#999999");
    public static final RegistryObject<FlowingFluid> SOURCE_TETRACHLOROETHYLENE = FLUIDS.register("tetrachloroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.TETRACHLOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TETRACHLOROETHYLENE = FLUIDS.register("flowing_tetrachloroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TETRACHLOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TETRACHLOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TETRACHLOROETHYLENE_FLUID_TYPE, SOURCE_TETRACHLOROETHYLENE, FLOWING_TETRACHLOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("tetrachloroethylene", SOURCE_TETRACHLOROETHYLENE))
            .bucket(ModItems.registerBucketItem("tetrachloroethylene", SOURCE_TETRACHLOROETHYLENE));

    
    public static final RegistryObject<FluidType> AQUA_REGIA_FLUID_TYPE = registerType("aqua_regia", "liquid", "#ffcc00");
    public static final RegistryObject<FlowingFluid> SOURCE_AQUA_REGIA = FLUIDS.register("aqua_regia",
            () -> new ForgeFlowingFluid.Source(ModFluids.AQUA_REGIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AQUA_REGIA = FLUIDS.register("flowing_aqua_regia",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AQUA_REGIA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AQUA_REGIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AQUA_REGIA_FLUID_TYPE, SOURCE_AQUA_REGIA, FLOWING_AQUA_REGIA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aqua_regia", SOURCE_AQUA_REGIA))
            .bucket(ModItems.registerBucketItem("aqua_regia", SOURCE_AQUA_REGIA));

    
    public static final RegistryObject<FluidType> CYANOGEN_FLUID_TYPE = registerType("cyanogen", "gas", "#ccffff");
    public static final RegistryObject<FlowingFluid> SOURCE_CYANOGEN = FLUIDS.register("cyanogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.CYANOGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CYANOGEN = FLUIDS.register("flowing_cyanogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CYANOGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CYANOGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CYANOGEN_FLUID_TYPE, SOURCE_CYANOGEN, FLOWING_CYANOGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("cyanogen", SOURCE_CYANOGEN))
            .bucket(ModItems.registerBucketItem("cyanogen", SOURCE_CYANOGEN));

    
    public static final RegistryObject<FluidType> FLUOROSILICIC_ACID_FLUID_TYPE = registerType("fluorosilicic_acid", "liquid", "#99cc99");
    public static final RegistryObject<FlowingFluid> SOURCE_FLUOROSILICIC_ACID = FLUIDS.register("fluorosilicic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUOROSILICIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLUOROSILICIC_ACID = FLUIDS.register("flowing_fluorosilicic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUOROSILICIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLUOROSILICIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUOROSILICIC_ACID_FLUID_TYPE, SOURCE_FLUOROSILICIC_ACID, FLOWING_FLUOROSILICIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fluorosilicic_acid", SOURCE_FLUOROSILICIC_ACID))
            .bucket(ModItems.registerBucketItem("fluorosilicic_acid", SOURCE_FLUOROSILICIC_ACID));

    
    public static final RegistryObject<FluidType> TITANIUM_TETRACHLORIDE_FLUID_TYPE = registerType("titanium_tetrachloride", "liquid", "#cccccc");
    public static final RegistryObject<FlowingFluid> SOURCE_TITANIUM_TETRACHLORIDE = FLUIDS.register("titanium_tetrachloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.TITANIUM_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TITANIUM_TETRACHLORIDE = FLUIDS.register("flowing_titanium_tetrachloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TITANIUM_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TITANIUM_TETRACHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TITANIUM_TETRACHLORIDE_FLUID_TYPE, SOURCE_TITANIUM_TETRACHLORIDE, FLOWING_TITANIUM_TETRACHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("titanium_tetrachloride", SOURCE_TITANIUM_TETRACHLORIDE))
            .bucket(ModItems.registerBucketItem("titanium_tetrachloride", SOURCE_TITANIUM_TETRACHLORIDE));

    
    public static final RegistryObject<FluidType> METHYL_ETHYL_KETONE_FLUID_TYPE = registerType("methyl_ethyl_ketone", "liquid", "#e6ccff");
    public static final RegistryObject<FlowingFluid> SOURCE_METHYL_ETHYL_KETONE = FLUIDS.register("methyl_ethyl_ketone",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHYL_ETHYL_KETONE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHYL_ETHYL_KETONE = FLUIDS.register("flowing_methyl_ethyl_ketone",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHYL_ETHYL_KETONE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHYL_ETHYL_KETONE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHYL_ETHYL_KETONE_FLUID_TYPE, SOURCE_METHYL_ETHYL_KETONE, FLOWING_METHYL_ETHYL_KETONE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methyl_ethyl_ketone", SOURCE_METHYL_ETHYL_KETONE))
            .bucket(ModItems.registerBucketItem("methyl_ethyl_ketone", SOURCE_METHYL_ETHYL_KETONE));

    
    public static final RegistryObject<FluidType> THIONYL_CHLORIDE_FLUID_TYPE = registerType("thionyl_chloride", "liquid", "#ccff99");
    public static final RegistryObject<FlowingFluid> SOURCE_THIONYL_CHLORIDE = FLUIDS.register("thionyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.THIONYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_THIONYL_CHLORIDE = FLUIDS.register("flowing_thionyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.THIONYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties THIONYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            THIONYL_CHLORIDE_FLUID_TYPE, SOURCE_THIONYL_CHLORIDE, FLOWING_THIONYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("thionyl_chloride", SOURCE_THIONYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("thionyl_chloride", SOURCE_THIONYL_CHLORIDE));

    
    public static final RegistryObject<FluidType> AZOTH_FLUID_TYPE = registerType("azoth", "gas", "#b3ffff");
    public static final RegistryObject<FlowingFluid> SOURCE_AZOTH = FLUIDS.register("azoth",
            () -> new ForgeFlowingFluid.Source(ModFluids.AZOTH_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AZOTH = FLUIDS.register("flowing_azoth",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AZOTH_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AZOTH_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AZOTH_FLUID_TYPE, SOURCE_AZOTH, FLOWING_AZOTH)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("azoth", SOURCE_AZOTH))
            .bucket(ModItems.registerBucketItem("azoth", SOURCE_AZOTH));

    
    public static final RegistryObject<FluidType> UNOBTANIUM_FLUID_TYPE = registerType("unobtanium", "liquid", "#ff66cc");
    public static final RegistryObject<FlowingFluid> SOURCE_UNOBTANIUM = FLUIDS.register("unobtanium",
            () -> new ForgeFlowingFluid.Source(ModFluids.UNOBTANIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_UNOBTANIUM = FLUIDS.register("flowing_unobtanium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.UNOBTANIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties UNOBTANIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            UNOBTANIUM_FLUID_TYPE, SOURCE_UNOBTANIUM, FLOWING_UNOBTANIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("unobtanium", SOURCE_UNOBTANIUM))
            .bucket(ModItems.registerBucketItem("unobtanium", SOURCE_UNOBTANIUM));

    
    public static final RegistryObject<FluidType> DILITHIUM_FLUID_TYPE = registerType("dilithium", "liquid", "#e60045");
    public static final RegistryObject<FlowingFluid> SOURCE_DILITHIUM = FLUIDS.register("dilithium",
            () -> new ForgeFlowingFluid.Source(ModFluids.DILITHIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DILITHIUM = FLUIDS.register("flowing_dilithium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DILITHIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DILITHIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DILITHIUM_FLUID_TYPE, SOURCE_DILITHIUM, FLOWING_DILITHIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dilithium", SOURCE_DILITHIUM))
            .bucket(ModItems.registerBucketItem("dilithium", SOURCE_DILITHIUM));

    
    public static final RegistryObject<FluidType> ADAMANTIUM_FLUID_TYPE = registerType("adamantium", "liquid", "#cccccc");
    public static final RegistryObject<FlowingFluid> SOURCE_ADAMANTIUM = FLUIDS.register("adamantium",
            () -> new ForgeFlowingFluid.Source(ModFluids.ADAMANTIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ADAMANTIUM = FLUIDS.register("flowing_adamantium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ADAMANTIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ADAMANTIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ADAMANTIUM_FLUID_TYPE, SOURCE_ADAMANTIUM, FLOWING_ADAMANTIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("adamantium", SOURCE_ADAMANTIUM))
            .bucket(ModItems.registerBucketItem("adamantium", SOURCE_ADAMANTIUM));

    
    public static final RegistryObject<FluidType> CARBONADIUM_FLUID_TYPE = registerType("carbonadium", "liquid", "#666666");
    public static final RegistryObject<FlowingFluid> SOURCE_CARBONADIUM = FLUIDS.register("carbonadium",
            () -> new ForgeFlowingFluid.Source(ModFluids.CARBONADIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CARBONADIUM = FLUIDS.register("flowing_carbonadium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CARBONADIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CARBONADIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CARBONADIUM_FLUID_TYPE, SOURCE_CARBONADIUM, FLOWING_CARBONADIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("carbonadium", SOURCE_CARBONADIUM))
            .bucket(ModItems.registerBucketItem("carbonadium", SOURCE_CARBONADIUM));

    
    public static final RegistryObject<FluidType> RADON_FLUID_TYPE = registerType("radon", "gas", "#ffd700");
    public static final RegistryObject<FlowingFluid> SOURCE_RADON = FLUIDS.register("radon",
            () -> new ForgeFlowingFluid.Source(ModFluids.RADON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RADON = FLUIDS.register("flowing_radon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.RADON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RADON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            RADON_FLUID_TYPE, SOURCE_RADON, FLOWING_RADON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("radon", SOURCE_RADON))
            .bucket(ModItems.registerBucketItem("radon", SOURCE_RADON));

    
    public static final RegistryObject<FluidType> NEON_FLUID_TYPE = registerType("neon", "gas", "#ff5ccd");
    public static final RegistryObject<FlowingFluid> SOURCE_NEON = FLUIDS.register("neon",
            () -> new ForgeFlowingFluid.Source(ModFluids.NEON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NEON = FLUIDS.register("flowing_neon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NEON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NEON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NEON_FLUID_TYPE, SOURCE_NEON, FLOWING_NEON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("neon", SOURCE_NEON))
            .bucket(ModItems.registerBucketItem("neon", SOURCE_NEON));

    
    public static final RegistryObject<FluidType> ARGON_FLUID_TYPE = registerType("argon", "gas", "#bc47ff");
    public static final RegistryObject<FlowingFluid> SOURCE_ARGON = FLUIDS.register("argon",
            () -> new ForgeFlowingFluid.Source(ModFluids.ARGON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ARGON = FLUIDS.register("flowing_argon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ARGON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ARGON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ARGON_FLUID_TYPE, SOURCE_ARGON, FLOWING_ARGON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("argon", SOURCE_ARGON))
            .bucket(ModItems.registerBucketItem("argon", SOURCE_ARGON));

    
    public static final RegistryObject<FluidType> XENON_FLUID_TYPE = registerType("xenon", "gas", "#3a79ff");
    public static final RegistryObject<FlowingFluid> SOURCE_XENON = FLUIDS.register("xenon",
            () -> new ForgeFlowingFluid.Source(ModFluids.XENON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_XENON = FLUIDS.register("flowing_xenon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.XENON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties XENON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            XENON_FLUID_TYPE, SOURCE_XENON, FLOWING_XENON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("xenon", SOURCE_XENON))
            .bucket(ModItems.registerBucketItem("xenon", SOURCE_XENON));

    
    public static final RegistryObject<FluidType> CRYPTIC_ACID_FLUID_TYPE = registerType("cryptic_acid", "liquid", "#DD33DD");
    public static final RegistryObject<FlowingFluid> SOURCE_CRYPTIC_ACID = FLUIDS.register("cryptic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CRYPTIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CRYPTIC_ACID = FLUIDS.register("flowing_cryptic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CRYPTIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CRYPTIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CRYPTIC_ACID_FLUID_TYPE, SOURCE_CRYPTIC_ACID, FLOWING_CRYPTIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("cryptic_acid", SOURCE_CRYPTIC_ACID))
            .bucket(ModItems.registerBucketItem("cryptic_acid", SOURCE_CRYPTIC_ACID));

    
    public static final RegistryObject<FluidType> POLYVINYL_CHLORIDE_FLUID_TYPE = registerType("polyvinyl_chloride", "liquid", "#e8fffa");
    public static final RegistryObject<FlowingFluid> SOURCE_POLYVINYL_CHLORIDE = FLUIDS.register("polyvinyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.POLYVINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POLYVINYL_CHLORIDE = FLUIDS.register("flowing_polyvinyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POLYVINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POLYVINYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POLYVINYL_CHLORIDE_FLUID_TYPE, SOURCE_POLYVINYL_CHLORIDE, FLOWING_POLYVINYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("polyvinyl_chloride", SOURCE_POLYVINYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("polyvinyl_chloride", SOURCE_POLYVINYL_CHLORIDE));

    
    public static final RegistryObject<FluidType> HYDROFLUORIC_ACID_FLUID_TYPE = registerType("hydrofluoric_acid", "liquid", "#7e7ecf");
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROFLUORIC_ACID = FLUIDS.register("hydrofluoric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROFLUORIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROFLUORIC_ACID = FLUIDS.register("flowing_hydrofluoric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROFLUORIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROFLUORIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROFLUORIC_ACID_FLUID_TYPE, SOURCE_HYDROFLUORIC_ACID, FLOWING_HYDROFLUORIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrofluoric_acid", SOURCE_HYDROFLUORIC_ACID))
            .bucket(ModItems.registerBucketItem("hydrofluoric_acid", SOURCE_HYDROFLUORIC_ACID));

    
    public static final RegistryObject<FluidType> TRICHLOROMETHANE_FLUID_TYPE = registerType("trichloromethane", "liquid", "#fDfeed");
    public static final RegistryObject<FlowingFluid> SOURCE_TRICHLOROMETHANE = FLUIDS.register("trichloromethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.TRICHLOROMETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TRICHLOROMETHANE = FLUIDS.register("flowing_trichloromethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TRICHLOROMETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TRICHLOROMETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TRICHLOROMETHANE_FLUID_TYPE, SOURCE_TRICHLOROMETHANE, FLOWING_TRICHLOROMETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("trichloromethane", SOURCE_TRICHLOROMETHANE))
            .bucket(ModItems.registerBucketItem("trichloromethane", SOURCE_TRICHLOROMETHANE));

    
    public static final RegistryObject<FluidType> PIRANHA_FLUID_TYPE = registerType("piranha", "liquid", "#f2f0d5");
    public static final RegistryObject<FlowingFluid> SOURCE_PIRANHA = FLUIDS.register("piranha",
            () -> new ForgeFlowingFluid.Source(ModFluids.PIRANHA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PIRANHA = FLUIDS.register("flowing_piranha",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PIRANHA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PIRANHA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PIRANHA_FLUID_TYPE, SOURCE_PIRANHA, FLOWING_PIRANHA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("piranha", SOURCE_PIRANHA))
            .bucket(ModItems.registerBucketItem("piranha", SOURCE_PIRANHA));

    
    public static final RegistryObject<FluidType> FLUOROANTIMONIC_ACID_FLUID_TYPE = registerType("fluoroantimonic_acid", "liquid", "#ffffdd");
    public static final RegistryObject<FlowingFluid> SOURCE_FLUOROANTIMONIC_ACID = FLUIDS.register("fluoroantimonic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUOROANTIMONIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLUOROANTIMONIC_ACID = FLUIDS.register("flowing_fluoroantimonic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUOROANTIMONIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLUOROANTIMONIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUOROANTIMONIC_ACID_FLUID_TYPE, SOURCE_FLUOROANTIMONIC_ACID, FLOWING_FLUOROANTIMONIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fluoroantimonic_acid", SOURCE_FLUOROANTIMONIC_ACID))
            .bucket(ModItems.registerBucketItem("fluoroantimonic_acid", SOURCE_FLUOROANTIMONIC_ACID));

    
    public static final RegistryObject<FluidType> STYRENE_BUTADIENE_FLUID_TYPE = registerType("styrene_butadiene", "liquid", "#2b2b29");
    public static final RegistryObject<FlowingFluid> SOURCE_STYRENE_BUTADIENE = FLUIDS.register("styrene_butadiene",
            () -> new ForgeFlowingFluid.Source(ModFluids.STYRENE_BUTADIENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STYRENE_BUTADIENE = FLUIDS.register("flowing_styrene_butadiene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STYRENE_BUTADIENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STYRENE_BUTADIENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STYRENE_BUTADIENE_FLUID_TYPE, SOURCE_STYRENE_BUTADIENE, FLOWING_STYRENE_BUTADIENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("styrene_butadiene", SOURCE_STYRENE_BUTADIENE))
            .bucket(ModItems.registerBucketItem("styrene_butadiene", SOURCE_STYRENE_BUTADIENE));

    
    public static final RegistryObject<FluidType> FLUORINE_FLUID_TYPE = registerType("fluorine", "liquid", "#ccffff");
    public static final RegistryObject<FlowingFluid> SOURCE_FLUORINE = FLUIDS.register("fluorine",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUORINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLUORINE = FLUIDS.register("flowing_fluorine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUORINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLUORINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUORINE_FLUID_TYPE, SOURCE_FLUORINE, FLOWING_FLUORINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fluorine", SOURCE_FLUORINE))
            .bucket(ModItems.registerBucketItem("fluorine", SOURCE_FLUORINE));

    
    public static final RegistryObject<FluidType> STIPNICIUM_FLUID_TYPE = registerType("stipnicium", "liquid", "#ff4245");
    public static final RegistryObject<FlowingFluid> SOURCE_STIPNICIUM = FLUIDS.register("stipnicium",
            () -> new ForgeFlowingFluid.Source(ModFluids.STIPNICIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STIPNICIUM = FLUIDS.register("flowing_stipnicium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STIPNICIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STIPNICIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STIPNICIUM_FLUID_TYPE, SOURCE_STIPNICIUM, FLOWING_STIPNICIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("stipnicium", SOURCE_STIPNICIUM))
            .bucket(ModItems.registerBucketItem("stipnicium", SOURCE_STIPNICIUM));

    
    public static final RegistryObject<FluidType> BIOLUMINESCENT_COON_JUICE_FLUID_TYPE = registerType("bioluminescent_coon_juice", "liquid", "#00ff55");
    public static final RegistryObject<FlowingFluid> SOURCE_BIOLUMINESCENT_COON_JUICE = FLUIDS.register("bioluminescent_coon_juice",
            () -> new ForgeFlowingFluid.Source(ModFluids.BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BIOLUMINESCENT_COON_JUICE = FLUIDS.register("flowing_bioluminescent_coon_juice",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BIOLUMINESCENT_COON_JUICE_FLUID_TYPE, SOURCE_BIOLUMINESCENT_COON_JUICE, FLOWING_BIOLUMINESCENT_COON_JUICE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("bioluminescent_coon_juice", SOURCE_BIOLUMINESCENT_COON_JUICE))
            .bucket(ModItems.registerBucketItem("bioluminescent_coon_juice", SOURCE_BIOLUMINESCENT_COON_JUICE));

    
    public static final RegistryObject<FluidType> GELID_CRYOTHEUM_FLUID_TYPE = registerType("gelid_cryotheum", "liquid", "#549eff");
    public static final RegistryObject<FlowingFluid> SOURCE_GELID_CRYOTHEUM = FLUIDS.register("gelid_cryotheum",
            () -> new ForgeFlowingFluid.Source(ModFluids.GELID_CRYOTHEUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GELID_CRYOTHEUM = FLUIDS.register("flowing_gelid_cryotheum",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GELID_CRYOTHEUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GELID_CRYOTHEUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GELID_CRYOTHEUM_FLUID_TYPE, SOURCE_GELID_CRYOTHEUM, FLOWING_GELID_CRYOTHEUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("gelid_cryotheum", SOURCE_GELID_CRYOTHEUM))
            .bucket(ModItems.registerBucketItem("gelid_cryotheum", SOURCE_GELID_CRYOTHEUM));

    
    public static final RegistryObject<FluidType> SODIUM_FLUID_TYPE = registerType("sodium", "gas", "#ffaf54");
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM = FLUIDS.register("sodium",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM = FLUIDS.register("flowing_sodium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_FLUID_TYPE, SOURCE_SODIUM, FLOWING_SODIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium", SOURCE_SODIUM))
            .bucket(ModItems.registerBucketItem("sodium", SOURCE_SODIUM));

    
    public static final RegistryObject<FluidType> POTASSIUM_FLUID_TYPE = registerType("potassium", "gas", "#d6adf7");
    public static final RegistryObject<FlowingFluid> SOURCE_POTASSIUM = FLUIDS.register("potassium",
            () -> new ForgeFlowingFluid.Source(ModFluids.POTASSIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POTASSIUM = FLUIDS.register("flowing_potassium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POTASSIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POTASSIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POTASSIUM_FLUID_TYPE, SOURCE_POTASSIUM, FLOWING_POTASSIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("potassium", SOURCE_POTASSIUM))
            .bucket(ModItems.registerBucketItem("potassium", SOURCE_POTASSIUM));

    
    public static final RegistryObject<FluidType> HELIUM_FLUID_TYPE = registerType("helium", "gas", "#f7ecad");
    public static final RegistryObject<FlowingFluid> SOURCE_HELIUM = FLUIDS.register("helium",
            () -> new ForgeFlowingFluid.Source(ModFluids.HELIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HELIUM = FLUIDS.register("flowing_helium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HELIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HELIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HELIUM_FLUID_TYPE, SOURCE_HELIUM, FLOWING_HELIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("helium", SOURCE_HELIUM))
            .bucket(ModItems.registerBucketItem("helium", SOURCE_HELIUM));

    
    public static final RegistryObject<FluidType> KRYPTON_FLUID_TYPE = registerType("krypton", "gas", "#96fff1");
    public static final RegistryObject<FlowingFluid> SOURCE_KRYPTON = FLUIDS.register("krypton",
            () -> new ForgeFlowingFluid.Source(ModFluids.KRYPTON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_KRYPTON = FLUIDS.register("flowing_krypton",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.KRYPTON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties KRYPTON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            KRYPTON_FLUID_TYPE, SOURCE_KRYPTON, FLOWING_KRYPTON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("krypton", SOURCE_KRYPTON))
            .bucket(ModItems.registerBucketItem("krypton", SOURCE_KRYPTON));

    
    public static final RegistryObject<FluidType> GHASTLY_LIQUID_FLUID_TYPE = registerType("ghastly_liquid", "liquid", "#f4e6ff");
    public static final RegistryObject<FlowingFluid> SOURCE_GHASTLY_LIQUID = FLUIDS.register("ghastly_liquid",
            () -> new ForgeFlowingFluid.Source(ModFluids.GHASTLY_LIQUID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GHASTLY_LIQUID = FLUIDS.register("flowing_ghastly_liquid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GHASTLY_LIQUID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GHASTLY_LIQUID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GHASTLY_LIQUID_FLUID_TYPE, SOURCE_GHASTLY_LIQUID, FLOWING_GHASTLY_LIQUID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ghastly_liquid", SOURCE_GHASTLY_LIQUID))
            .bucket(ModItems.registerBucketItem("ghastly_liquid", SOURCE_GHASTLY_LIQUID));

    
    public static final RegistryObject<FluidType> FENTANYL_FLUID_TYPE = registerType("fentanyl", "liquid", "#19ffdd");
    public static final RegistryObject<FlowingFluid> SOURCE_FENTANYL = FLUIDS.register("fentanyl",
            () -> new ForgeFlowingFluid.Source(ModFluids.FENTANYL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FENTANYL = FLUIDS.register("flowing_fentanyl",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FENTANYL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FENTANYL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FENTANYL_FLUID_TYPE, SOURCE_FENTANYL, FLOWING_FENTANYL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fentanyl", SOURCE_FENTANYL))
            .bucket(ModItems.registerBucketItem("fentanyl", SOURCE_FENTANYL));

    
    //#end FLUID_REGION
}
