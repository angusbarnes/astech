package net.astr0.astech.Fluid;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.item.HazardBehavior;
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

    public static RegistryObject<FluidType> registerType(String name, String type, String colorCode, HazardBehavior.BehaviorType hazardType) {
        if (type.equals("gas")) {
            return FLUID_TYPES.register(name, () -> new ChemicalGasType(TintColor.fromHex(colorCode, 179), hazardType));
        } else {
            return FLUID_TYPES.register(name, () -> new ChemicalLiquidType(TintColor.fromHex(colorCode, 205), hazardType));
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
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACETIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACETIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACROLEIN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACROLEIN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ADAMANTIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ADAMANTIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AEROGEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AEROGEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ALUMINIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ALUMINIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AMMONIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AMMONIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AMMONIUM_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AMMONIUM_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ANTIMONY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ANTIMONY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_AQUA_REGIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_AQUA_REGIA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BIOLUMINESCENT_COON_JUICE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BIOLUMINESCENT_COON_JUICE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BROMINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BROMINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CARBONADIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CARBONADIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CHLORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CHLORINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CHLOROBENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CHLOROBENZENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CHLORODIFLUOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CHLORODIFLUOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_COBALT.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_COBALT.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CRYPTIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CRYPTIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CYANOGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CYANOGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DILITHIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DILITHIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_DIMETHYL_SULFOXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_DIMETHYL_SULFOXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ETHYLENE_GLYCOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ETHYLENE_GLYCOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FENTANYL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FENTANYL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FLUOROANTIMONIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FLUOROANTIMONIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FLUOROSILICIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FLUOROSILICIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FORMALDEHYDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FORMALDEHYDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GELID_CRYOTHEUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GELID_CRYOTHEUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GHASTLY_LIQUID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GHASTLY_LIQUID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GRAPHENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GRAPHENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HELIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HELIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HEXAFLUOROPROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HEXAFLUOROPROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HEXANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HEXANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HIGH_ENTROPY_ALLOY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HIGH_ENTROPY_ALLOY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDRAZINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDRAZINE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROCHLORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROCHLORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN_PEROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN_PEROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN_FLUORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN_FLUORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HYDROGEN_SULFIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HYDROGEN_SULFIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ISOPROPANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ISOPROPANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_IUMIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_IUMIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHANOL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_METHYL_ETHYL_KETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_METHYL_ETHYL_KETONE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NEON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NEON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NETHERITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NETHERITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NITROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NITROGEN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSGENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSGENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSPHORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSPHORIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PIRANHA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PIRANHA.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PISS_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PISS_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POLYTETRAFLUOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POLYTETRAFLUOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POLYVINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POLYVINYL_CHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PROPYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_RADON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_RADON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ROCKET_FUEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ROCKET_FUEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SILICOALUMINOPHOSPHATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SILICOALUMINOPHOSPHATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_HYPOCHLORITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_HYPOCHLORITE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SODIUM_SULFATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SODIUM_SULFATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOLDERING_FLUX.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOLDERING_FLUX.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STIPNICIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STIPNICIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STYRENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STYRENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_STYRENE_BUTADIENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_STYRENE_BUTADIENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SULFURIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SULFURIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TETRACHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TETRACHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TETRAETHYL_ORTHOSILICATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TETRAETHYL_ORTHOSILICATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TETRAHYDROFURAN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TETRAHYDROFURAN.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TRICHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TRICHLOROETHYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TRICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TRICHLOROMETHANE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_UNOBTANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_UNOBTANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CATALYSED_URANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CATALYSED_URANIUM.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_XENON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_XENON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_XYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_XYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_RP_1.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_RP_1.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ANTIMONY_PENTAFLUORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ANTIMONY_PENTAFLUORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POLYMETHYL_METHACRYLATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POLYMETHYL_METHACRYLATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GASEOUS_HYRDOCARBONS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GASEOUS_HYRDOCARBONS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ACETYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ACETYLENE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSPHORUS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSPHORUS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NATURAL_GAS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NATURAL_GAS.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SILICON_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SILICON_TETRACHLORIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ENERGISED_NAQADAH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ENERGISED_NAQADAH.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MONOCRYSTALINE_SILICON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MONOCRYSTALINE_SILICON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_REFINED_SILICON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_REFINED_SILICON.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_TREATED_BIODIESEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_TREATED_BIODIESEL.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POTASSIUM_DICHROMATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POTASSIUM_DICHROMATE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_HIGH_CARBON_STEEL_52100.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_HIGH_CARBON_STEEL_52100.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ENGINEERED_ALLOY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ENGINEERED_ALLOY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_GOOD_BEER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_GOOD_BEER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_FOSTERS_BEER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_FOSTERS_BEER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PH_STABLISED_BESKAR.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PH_STABLISED_BESKAR.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MODIFIED_PHOSPHINIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MODIFIED_PHOSPHINIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PHOSPHINIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PHOSPHINIC_ACID.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BESKAR_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BESKAR_HYDROXIDE.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SUPERHEATED_BESKAR_SLURRY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SUPERHEATED_BESKAR_SLURRY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_BESKAR.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_BESKAR.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SEA_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SEA_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_ICE_SLURRY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_ICE_SLURRY.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PH_BALANCED_PURIFIED_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PH_BALANCED_PURIFIED_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PURIFIED_WATER.get(), RenderType.translucent());
ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PURIFIED_WATER.get(), RenderType.translucent());
        //#end RENDER_REGION
    }

    public static final RegistryObject<FluidType> SOAP_WATER_FLUID_TYPE = registerType("soap_water_fluid", "liquid", "#3486eb", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_SOAP_WATER = FLUIDS.register("soap_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SOAP_WATER = FLUIDS.register("flowing_soap_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SOAP_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("soap_water", SOURCE_SOAP_WATER))
            .bucket(ModItems.registerBucketItem("soap_water", SOURCE_SOAP_WATER));

    //#anchor FLUID_REGION

    public static final RegistryObject<FluidType> ACETIC_ACID_FLUID_TYPE = registerType("acetic_acid", "liquid", "#ff6666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ACETIC_ACID = FLUIDS.register("acetic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACETIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACETIC_ACID = FLUIDS.register("flowing_acetic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACETIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACETIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACETIC_ACID_FLUID_TYPE, SOURCE_ACETIC_ACID, FLOWING_ACETIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acetic_acid", SOURCE_ACETIC_ACID))
            .bucket(ModItems.registerBucketItem("acetic_acid", SOURCE_ACETIC_ACID));
    

    public static final RegistryObject<FluidType> ACETONE_FLUID_TYPE = registerType("acetone", "liquid", "#e7e4e4", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_ACETONE = FLUIDS.register("acetone",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACETONE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACETONE = FLUIDS.register("flowing_acetone",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACETONE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACETONE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACETONE_FLUID_TYPE, SOURCE_ACETONE, FLOWING_ACETONE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acetone", SOURCE_ACETONE))
            .bucket(ModItems.registerBucketItem("acetone", SOURCE_ACETONE));
    

    public static final RegistryObject<FluidType> ACROLEIN_FLUID_TYPE = registerType("acrolein", "liquid", "#cc9966", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ACROLEIN = FLUIDS.register("acrolein",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACROLEIN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACROLEIN = FLUIDS.register("flowing_acrolein",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACROLEIN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACROLEIN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACROLEIN_FLUID_TYPE, SOURCE_ACROLEIN, FLOWING_ACROLEIN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acrolein", SOURCE_ACROLEIN))
            .bucket(ModItems.registerBucketItem("acrolein", SOURCE_ACROLEIN));
    

    public static final RegistryObject<FluidType> ADAMANTIUM_FLUID_TYPE = registerType("adamantium", "liquid", "#cccccc", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ADAMANTIUM = FLUIDS.register("adamantium",
            () -> new ForgeFlowingFluid.Source(ModFluids.ADAMANTIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ADAMANTIUM = FLUIDS.register("flowing_adamantium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ADAMANTIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ADAMANTIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ADAMANTIUM_FLUID_TYPE, SOURCE_ADAMANTIUM, FLOWING_ADAMANTIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("adamantium", SOURCE_ADAMANTIUM))
            .bucket(ModItems.registerBucketItem("adamantium", SOURCE_ADAMANTIUM));
    

    public static final RegistryObject<FluidType> AEROGEL_FLUID_TYPE = registerType("aerogel", "gas", "#d7ebb7", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_AEROGEL = FLUIDS.register("aerogel",
            () -> new ForgeFlowingFluid.Source(ModFluids.AEROGEL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AEROGEL = FLUIDS.register("flowing_aerogel",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AEROGEL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AEROGEL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AEROGEL_FLUID_TYPE, SOURCE_AEROGEL, FLOWING_AEROGEL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aerogel", SOURCE_AEROGEL))
            .bucket(ModItems.registerBucketItem("aerogel", SOURCE_AEROGEL));
    

    public static final RegistryObject<FluidType> ALUMINIUM_HYDROXIDE_FLUID_TYPE = registerType("aluminium_hydroxide", "gas", "#a8e5eb", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ALUMINIUM_HYDROXIDE = FLUIDS.register("aluminium_hydroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ALUMINIUM_HYDROXIDE = FLUIDS.register("flowing_aluminium_hydroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ALUMINIUM_HYDROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ALUMINIUM_HYDROXIDE_FLUID_TYPE, SOURCE_ALUMINIUM_HYDROXIDE, FLOWING_ALUMINIUM_HYDROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aluminium_hydroxide", SOURCE_ALUMINIUM_HYDROXIDE))
            .bucket(ModItems.registerBucketItem("aluminium_hydroxide", SOURCE_ALUMINIUM_HYDROXIDE));
    

    public static final RegistryObject<FluidType> AMMONIA_FLUID_TYPE = registerType("ammonia", "gas", "#b2dfdb", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_AMMONIA = FLUIDS.register("ammonia",
            () -> new ForgeFlowingFluid.Source(ModFluids.AMMONIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AMMONIA = FLUIDS.register("flowing_ammonia",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AMMONIA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AMMONIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AMMONIA_FLUID_TYPE, SOURCE_AMMONIA, FLOWING_AMMONIA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ammonia", SOURCE_AMMONIA))
            .bucket(ModItems.registerBucketItem("ammonia", SOURCE_AMMONIA));
    

    public static final RegistryObject<FluidType> AMMONIUM_CHLORIDE_FLUID_TYPE = registerType("ammonium_chloride", "gas", "#dcfae4", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_AMMONIUM_CHLORIDE = FLUIDS.register("ammonium_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.AMMONIUM_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AMMONIUM_CHLORIDE = FLUIDS.register("flowing_ammonium_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AMMONIUM_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AMMONIUM_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AMMONIUM_CHLORIDE_FLUID_TYPE, SOURCE_AMMONIUM_CHLORIDE, FLOWING_AMMONIUM_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ammonium_chloride", SOURCE_AMMONIUM_CHLORIDE))
            .bucket(ModItems.registerBucketItem("ammonium_chloride", SOURCE_AMMONIUM_CHLORIDE));
    

    public static final RegistryObject<FluidType> ANTIMONY_FLUID_TYPE = registerType("antimony", "liquid", "#a095ad", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ANTIMONY = FLUIDS.register("antimony",
            () -> new ForgeFlowingFluid.Source(ModFluids.ANTIMONY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ANTIMONY = FLUIDS.register("flowing_antimony",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ANTIMONY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ANTIMONY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ANTIMONY_FLUID_TYPE, SOURCE_ANTIMONY, FLOWING_ANTIMONY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("antimony", SOURCE_ANTIMONY))
            .bucket(ModItems.registerBucketItem("antimony", SOURCE_ANTIMONY));
    

    public static final RegistryObject<FluidType> AQUA_REGIA_FLUID_TYPE = registerType("aqua_regia", "liquid", "#ffcc00", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_AQUA_REGIA = FLUIDS.register("aqua_regia",
            () -> new ForgeFlowingFluid.Source(ModFluids.AQUA_REGIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_AQUA_REGIA = FLUIDS.register("flowing_aqua_regia",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.AQUA_REGIA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties AQUA_REGIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            AQUA_REGIA_FLUID_TYPE, SOURCE_AQUA_REGIA, FLOWING_AQUA_REGIA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("aqua_regia", SOURCE_AQUA_REGIA))
            .bucket(ModItems.registerBucketItem("aqua_regia", SOURCE_AQUA_REGIA));
    

    public static final RegistryObject<FluidType> BENZENE_FLUID_TYPE = registerType("benzene", "liquid", "#f28e1c", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_BENZENE = FLUIDS.register("benzene",
            () -> new ForgeFlowingFluid.Source(ModFluids.BENZENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BENZENE = FLUIDS.register("flowing_benzene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BENZENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BENZENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BENZENE_FLUID_TYPE, SOURCE_BENZENE, FLOWING_BENZENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("benzene", SOURCE_BENZENE))
            .bucket(ModItems.registerBucketItem("benzene", SOURCE_BENZENE));
    

    public static final RegistryObject<FluidType> BIOLUMINESCENT_COON_JUICE_FLUID_TYPE = registerType("bioluminescent_coon_juice", "liquid", "#00ff55", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_BIOLUMINESCENT_COON_JUICE = FLUIDS.register("bioluminescent_coon_juice",
            () -> new ForgeFlowingFluid.Source(ModFluids.BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BIOLUMINESCENT_COON_JUICE = FLUIDS.register("flowing_bioluminescent_coon_juice",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BIOLUMINESCENT_COON_JUICE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BIOLUMINESCENT_COON_JUICE_FLUID_TYPE, SOURCE_BIOLUMINESCENT_COON_JUICE, FLOWING_BIOLUMINESCENT_COON_JUICE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("bioluminescent_coon_juice", SOURCE_BIOLUMINESCENT_COON_JUICE))
            .bucket(ModItems.registerBucketItem("bioluminescent_coon_juice", SOURCE_BIOLUMINESCENT_COON_JUICE));
    

    public static final RegistryObject<FluidType> BROMINE_FLUID_TYPE = registerType("bromine", "liquid", "#ff3300", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_BROMINE = FLUIDS.register("bromine",
            () -> new ForgeFlowingFluid.Source(ModFluids.BROMINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BROMINE = FLUIDS.register("flowing_bromine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BROMINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BROMINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BROMINE_FLUID_TYPE, SOURCE_BROMINE, FLOWING_BROMINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("bromine", SOURCE_BROMINE))
            .bucket(ModItems.registerBucketItem("bromine", SOURCE_BROMINE));
    

    public static final RegistryObject<FluidType> CARBONADIUM_FLUID_TYPE = registerType("carbonadium", "liquid", "#666666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_CARBONADIUM = FLUIDS.register("carbonadium",
            () -> new ForgeFlowingFluid.Source(ModFluids.CARBONADIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CARBONADIUM = FLUIDS.register("flowing_carbonadium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CARBONADIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CARBONADIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CARBONADIUM_FLUID_TYPE, SOURCE_CARBONADIUM, FLOWING_CARBONADIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("carbonadium", SOURCE_CARBONADIUM))
            .bucket(ModItems.registerBucketItem("carbonadium", SOURCE_CARBONADIUM));
    

    public static final RegistryObject<FluidType> CHLORINE_FLUID_TYPE = registerType("chlorine", "gas", "#d4ff00", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_CHLORINE = FLUIDS.register("chlorine",
            () -> new ForgeFlowingFluid.Source(ModFluids.CHLORINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHLORINE = FLUIDS.register("flowing_chlorine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CHLORINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CHLORINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CHLORINE_FLUID_TYPE, SOURCE_CHLORINE, FLOWING_CHLORINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("chlorine", SOURCE_CHLORINE))
            .bucket(ModItems.registerBucketItem("chlorine", SOURCE_CHLORINE));
    

    public static final RegistryObject<FluidType> CHLOROBENZENE_FLUID_TYPE = registerType("chlorobenzene", "liquid", "#57186e", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_CHLOROBENZENE = FLUIDS.register("chlorobenzene",
            () -> new ForgeFlowingFluid.Source(ModFluids.CHLOROBENZENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHLOROBENZENE = FLUIDS.register("flowing_chlorobenzene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CHLOROBENZENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CHLOROBENZENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CHLOROBENZENE_FLUID_TYPE, SOURCE_CHLOROBENZENE, FLOWING_CHLOROBENZENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("chlorobenzene", SOURCE_CHLOROBENZENE))
            .bucket(ModItems.registerBucketItem("chlorobenzene", SOURCE_CHLOROBENZENE));
    

    public static final RegistryObject<FluidType> CHLORODIFLUOROMETHANE_FLUID_TYPE = registerType("chlorodifluoromethane", "liquid", "#d4ffe8", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_CHLORODIFLUOROMETHANE = FLUIDS.register("chlorodifluoromethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.CHLORODIFLUOROMETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CHLORODIFLUOROMETHANE = FLUIDS.register("flowing_chlorodifluoromethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CHLORODIFLUOROMETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CHLORODIFLUOROMETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CHLORODIFLUOROMETHANE_FLUID_TYPE, SOURCE_CHLORODIFLUOROMETHANE, FLOWING_CHLORODIFLUOROMETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("chlorodifluoromethane", SOURCE_CHLORODIFLUOROMETHANE))
            .bucket(ModItems.registerBucketItem("chlorodifluoromethane", SOURCE_CHLORODIFLUOROMETHANE));
    

    public static final RegistryObject<FluidType> COBALT_FLUID_TYPE = registerType("cobalt", "liquid", "#0e389c", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_COBALT = FLUIDS.register("cobalt",
            () -> new ForgeFlowingFluid.Source(ModFluids.COBALT_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_COBALT = FLUIDS.register("flowing_cobalt",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.COBALT_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties COBALT_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            COBALT_FLUID_TYPE, SOURCE_COBALT, FLOWING_COBALT)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("cobalt", SOURCE_COBALT))
            .bucket(ModItems.registerBucketItem("cobalt", SOURCE_COBALT));
    

    public static final RegistryObject<FluidType> CRYPTIC_ACID_FLUID_TYPE = registerType("cryptic_acid", "liquid", "#DD33DD", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_CRYPTIC_ACID = FLUIDS.register("cryptic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CRYPTIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CRYPTIC_ACID = FLUIDS.register("flowing_cryptic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CRYPTIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CRYPTIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CRYPTIC_ACID_FLUID_TYPE, SOURCE_CRYPTIC_ACID, FLOWING_CRYPTIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("cryptic_acid", SOURCE_CRYPTIC_ACID))
            .bucket(ModItems.registerBucketItem("cryptic_acid", SOURCE_CRYPTIC_ACID));
    

    public static final RegistryObject<FluidType> CYANOGEN_FLUID_TYPE = registerType("cyanogen", "gas", "#ccffff", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_CYANOGEN = FLUIDS.register("cyanogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.CYANOGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CYANOGEN = FLUIDS.register("flowing_cyanogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CYANOGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CYANOGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CYANOGEN_FLUID_TYPE, SOURCE_CYANOGEN, FLOWING_CYANOGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("cyanogen", SOURCE_CYANOGEN))
            .bucket(ModItems.registerBucketItem("cyanogen", SOURCE_CYANOGEN));
    

    public static final RegistryObject<FluidType> DICHLOROMETHANE_FLUID_TYPE = registerType("dichloromethane", "gas", "#c6c6a7", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_DICHLOROMETHANE = FLUIDS.register("dichloromethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.DICHLOROMETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DICHLOROMETHANE = FLUIDS.register("flowing_dichloromethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DICHLOROMETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DICHLOROMETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DICHLOROMETHANE_FLUID_TYPE, SOURCE_DICHLOROMETHANE, FLOWING_DICHLOROMETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dichloromethane", SOURCE_DICHLOROMETHANE))
            .bucket(ModItems.registerBucketItem("dichloromethane", SOURCE_DICHLOROMETHANE));
    

    public static final RegistryObject<FluidType> DILITHIUM_FLUID_TYPE = registerType("dilithium", "liquid", "#e60045", HazardBehavior.BehaviorType.FREEZE);
    public static final RegistryObject<FlowingFluid> SOURCE_DILITHIUM = FLUIDS.register("dilithium",
            () -> new ForgeFlowingFluid.Source(ModFluids.DILITHIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DILITHIUM = FLUIDS.register("flowing_dilithium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DILITHIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DILITHIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DILITHIUM_FLUID_TYPE, SOURCE_DILITHIUM, FLOWING_DILITHIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dilithium", SOURCE_DILITHIUM))
            .bucket(ModItems.registerBucketItem("dilithium", SOURCE_DILITHIUM));
    

    public static final RegistryObject<FluidType> DIMETHYL_SULFOXIDE_FLUID_TYPE = registerType("dimethyl_sulfoxide", "liquid", "#99ccff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_DIMETHYL_SULFOXIDE = FLUIDS.register("dimethyl_sulfoxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.DIMETHYL_SULFOXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DIMETHYL_SULFOXIDE = FLUIDS.register("flowing_dimethyl_sulfoxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DIMETHYL_SULFOXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DIMETHYL_SULFOXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            DIMETHYL_SULFOXIDE_FLUID_TYPE, SOURCE_DIMETHYL_SULFOXIDE, FLOWING_DIMETHYL_SULFOXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("dimethyl_sulfoxide", SOURCE_DIMETHYL_SULFOXIDE))
            .bucket(ModItems.registerBucketItem("dimethyl_sulfoxide", SOURCE_DIMETHYL_SULFOXIDE));
    

    public static final RegistryObject<FluidType> ETHANE_FLUID_TYPE = registerType("ethane", "gas", "#ebba34", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_ETHANE = FLUIDS.register("ethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.ETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ETHANE = FLUIDS.register("flowing_ethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ETHANE_FLUID_TYPE, SOURCE_ETHANE, FLOWING_ETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethane", SOURCE_ETHANE))
            .bucket(ModItems.registerBucketItem("ethane", SOURCE_ETHANE));
    

    public static final RegistryObject<FluidType> ETHYLENE_GLYCOL_FLUID_TYPE = registerType("ethylene_glycol", "liquid", "#99ccff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ETHYLENE_GLYCOL = FLUIDS.register("ethylene_glycol",
            () -> new ForgeFlowingFluid.Source(ModFluids.ETHYLENE_GLYCOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ETHYLENE_GLYCOL = FLUIDS.register("flowing_ethylene_glycol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ETHYLENE_GLYCOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ETHYLENE_GLYCOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ETHYLENE_GLYCOL_FLUID_TYPE, SOURCE_ETHYLENE_GLYCOL, FLOWING_ETHYLENE_GLYCOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethylene_glycol", SOURCE_ETHYLENE_GLYCOL))
            .bucket(ModItems.registerBucketItem("ethylene_glycol", SOURCE_ETHYLENE_GLYCOL));
    

    public static final RegistryObject<FluidType> FENTANYL_FLUID_TYPE = registerType("fentanyl", "liquid", "#19ffdd", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_FENTANYL = FLUIDS.register("fentanyl",
            () -> new ForgeFlowingFluid.Source(ModFluids.FENTANYL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FENTANYL = FLUIDS.register("flowing_fentanyl",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FENTANYL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FENTANYL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FENTANYL_FLUID_TYPE, SOURCE_FENTANYL, FLOWING_FENTANYL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fentanyl", SOURCE_FENTANYL))
            .bucket(ModItems.registerBucketItem("fentanyl", SOURCE_FENTANYL));
    

    public static final RegistryObject<FluidType> FLUOROANTIMONIC_ACID_FLUID_TYPE = registerType("fluoroantimonic_acid", "liquid", "#ffffdd", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_FLUOROANTIMONIC_ACID = FLUIDS.register("fluoroantimonic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUOROANTIMONIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLUOROANTIMONIC_ACID = FLUIDS.register("flowing_fluoroantimonic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUOROANTIMONIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLUOROANTIMONIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUOROANTIMONIC_ACID_FLUID_TYPE, SOURCE_FLUOROANTIMONIC_ACID, FLOWING_FLUOROANTIMONIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fluoroantimonic_acid", SOURCE_FLUOROANTIMONIC_ACID))
            .bucket(ModItems.registerBucketItem("fluoroantimonic_acid", SOURCE_FLUOROANTIMONIC_ACID));
    

    public static final RegistryObject<FluidType> FLUOROSILICIC_ACID_FLUID_TYPE = registerType("fluorosilicic_acid", "liquid", "#99cc99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_FLUOROSILICIC_ACID = FLUIDS.register("fluorosilicic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUOROSILICIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLUOROSILICIC_ACID = FLUIDS.register("flowing_fluorosilicic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUOROSILICIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLUOROSILICIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUOROSILICIC_ACID_FLUID_TYPE, SOURCE_FLUOROSILICIC_ACID, FLOWING_FLUOROSILICIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fluorosilicic_acid", SOURCE_FLUOROSILICIC_ACID))
            .bucket(ModItems.registerBucketItem("fluorosilicic_acid", SOURCE_FLUOROSILICIC_ACID));
    

    public static final RegistryObject<FluidType> FORMALDEHYDE_FLUID_TYPE = registerType("formaldehyde", "gas", "#ccffcc", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_FORMALDEHYDE = FLUIDS.register("formaldehyde",
            () -> new ForgeFlowingFluid.Source(ModFluids.FORMALDEHYDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FORMALDEHYDE = FLUIDS.register("flowing_formaldehyde",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FORMALDEHYDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FORMALDEHYDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FORMALDEHYDE_FLUID_TYPE, SOURCE_FORMALDEHYDE, FLOWING_FORMALDEHYDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("formaldehyde", SOURCE_FORMALDEHYDE))
            .bucket(ModItems.registerBucketItem("formaldehyde", SOURCE_FORMALDEHYDE));
    

    public static final RegistryObject<FluidType> GELID_CRYOTHEUM_FLUID_TYPE = registerType("gelid_cryotheum", "liquid", "#549eff", HazardBehavior.BehaviorType.FREEZE);
    public static final RegistryObject<FlowingFluid> SOURCE_GELID_CRYOTHEUM = FLUIDS.register("gelid_cryotheum",
            () -> new ForgeFlowingFluid.Source(ModFluids.GELID_CRYOTHEUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GELID_CRYOTHEUM = FLUIDS.register("flowing_gelid_cryotheum",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GELID_CRYOTHEUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GELID_CRYOTHEUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GELID_CRYOTHEUM_FLUID_TYPE, SOURCE_GELID_CRYOTHEUM, FLOWING_GELID_CRYOTHEUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("gelid_cryotheum", SOURCE_GELID_CRYOTHEUM))
            .bucket(ModItems.registerBucketItem("gelid_cryotheum", SOURCE_GELID_CRYOTHEUM));
    

    public static final RegistryObject<FluidType> GHASTLY_LIQUID_FLUID_TYPE = registerType("ghastly_liquid", "liquid", "#f4e6ff", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_GHASTLY_LIQUID = FLUIDS.register("ghastly_liquid",
            () -> new ForgeFlowingFluid.Source(ModFluids.GHASTLY_LIQUID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GHASTLY_LIQUID = FLUIDS.register("flowing_ghastly_liquid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GHASTLY_LIQUID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GHASTLY_LIQUID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GHASTLY_LIQUID_FLUID_TYPE, SOURCE_GHASTLY_LIQUID, FLOWING_GHASTLY_LIQUID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ghastly_liquid", SOURCE_GHASTLY_LIQUID))
            .bucket(ModItems.registerBucketItem("ghastly_liquid", SOURCE_GHASTLY_LIQUID));
    

    public static final RegistryObject<FluidType> GRAPHENE_FLUID_TYPE = registerType("graphene", "liquid", "#191a19", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_GRAPHENE = FLUIDS.register("graphene",
            () -> new ForgeFlowingFluid.Source(ModFluids.GRAPHENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GRAPHENE = FLUIDS.register("flowing_graphene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GRAPHENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GRAPHENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GRAPHENE_FLUID_TYPE, SOURCE_GRAPHENE, FLOWING_GRAPHENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("graphene", SOURCE_GRAPHENE))
            .bucket(ModItems.registerBucketItem("graphene", SOURCE_GRAPHENE));
    

    public static final RegistryObject<FluidType> HELIUM_FLUID_TYPE = registerType("helium", "gas", "#f7ecad", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HELIUM = FLUIDS.register("helium",
            () -> new ForgeFlowingFluid.Source(ModFluids.HELIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HELIUM = FLUIDS.register("flowing_helium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HELIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HELIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HELIUM_FLUID_TYPE, SOURCE_HELIUM, FLOWING_HELIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("helium", SOURCE_HELIUM))
            .bucket(ModItems.registerBucketItem("helium", SOURCE_HELIUM));
    

    public static final RegistryObject<FluidType> HEXAFLUOROPROPYLENE_FLUID_TYPE = registerType("hexafluoropropylene", "gas", "#99ffff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HEXAFLUOROPROPYLENE = FLUIDS.register("hexafluoropropylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.HEXAFLUOROPROPYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HEXAFLUOROPROPYLENE = FLUIDS.register("flowing_hexafluoropropylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HEXAFLUOROPROPYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HEXAFLUOROPROPYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HEXAFLUOROPROPYLENE_FLUID_TYPE, SOURCE_HEXAFLUOROPROPYLENE, FLOWING_HEXAFLUOROPROPYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hexafluoropropylene", SOURCE_HEXAFLUOROPROPYLENE))
            .bucket(ModItems.registerBucketItem("hexafluoropropylene", SOURCE_HEXAFLUOROPROPYLENE));
    

    public static final RegistryObject<FluidType> HEXANE_FLUID_TYPE = registerType("hexane", "liquid", "#ffd700", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HEXANE = FLUIDS.register("hexane",
            () -> new ForgeFlowingFluid.Source(ModFluids.HEXANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HEXANE = FLUIDS.register("flowing_hexane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HEXANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HEXANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HEXANE_FLUID_TYPE, SOURCE_HEXANE, FLOWING_HEXANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hexane", SOURCE_HEXANE))
            .bucket(ModItems.registerBucketItem("hexane", SOURCE_HEXANE));
    

    public static final RegistryObject<FluidType> HIGH_ENTROPY_ALLOY_FLUID_TYPE = registerType("high_entropy_alloy", "liquid", "#b6b5ff", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_HIGH_ENTROPY_ALLOY = FLUIDS.register("high_entropy_alloy",
            () -> new ForgeFlowingFluid.Source(ModFluids.HIGH_ENTROPY_ALLOY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HIGH_ENTROPY_ALLOY = FLUIDS.register("flowing_high_entropy_alloy",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HIGH_ENTROPY_ALLOY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HIGH_ENTROPY_ALLOY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HIGH_ENTROPY_ALLOY_FLUID_TYPE, SOURCE_HIGH_ENTROPY_ALLOY, FLOWING_HIGH_ENTROPY_ALLOY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("high_entropy_alloy", SOURCE_HIGH_ENTROPY_ALLOY))
            .bucket(ModItems.registerBucketItem("high_entropy_alloy", SOURCE_HIGH_ENTROPY_ALLOY));
    

    public static final RegistryObject<FluidType> HYDRAZINE_FLUID_TYPE = registerType("hydrazine", "liquid", "#ccffcc", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDRAZINE = FLUIDS.register("hydrazine",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDRAZINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDRAZINE = FLUIDS.register("flowing_hydrazine",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDRAZINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDRAZINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDRAZINE_FLUID_TYPE, SOURCE_HYDRAZINE, FLOWING_HYDRAZINE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrazine", SOURCE_HYDRAZINE))
            .bucket(ModItems.registerBucketItem("hydrazine", SOURCE_HYDRAZINE));
    

    public static final RegistryObject<FluidType> HYDROCHLORIC_ACID_FLUID_TYPE = registerType("hydrochloric_acid", "liquid", "#ff6666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROCHLORIC_ACID = FLUIDS.register("hydrochloric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROCHLORIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROCHLORIC_ACID = FLUIDS.register("flowing_hydrochloric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROCHLORIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROCHLORIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROCHLORIC_ACID_FLUID_TYPE, SOURCE_HYDROCHLORIC_ACID, FLOWING_HYDROCHLORIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrochloric_acid", SOURCE_HYDROCHLORIC_ACID))
            .bucket(ModItems.registerBucketItem("hydrochloric_acid", SOURCE_HYDROCHLORIC_ACID));
    

    public static final RegistryObject<FluidType> HYDROGEN_PEROXIDE_FLUID_TYPE = registerType("hydrogen_peroxide", "liquid", "#7e7ecf", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN_PEROXIDE = FLUIDS.register("hydrogen_peroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_PEROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN_PEROXIDE = FLUIDS.register("flowing_hydrogen_peroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_PEROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_PEROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_PEROXIDE_FLUID_TYPE, SOURCE_HYDROGEN_PEROXIDE, FLOWING_HYDROGEN_PEROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen_peroxide", SOURCE_HYDROGEN_PEROXIDE))
            .bucket(ModItems.registerBucketItem("hydrogen_peroxide", SOURCE_HYDROGEN_PEROXIDE));
    

    public static final RegistryObject<FluidType> HYDROGEN_FLUID_TYPE = registerType("hydrogen", "gas", "#fff4e6", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN = FLUIDS.register("hydrogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN = FLUIDS.register("flowing_hydrogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_FLUID_TYPE, SOURCE_HYDROGEN, FLOWING_HYDROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen", SOURCE_HYDROGEN))
            .bucket(ModItems.registerBucketItem("hydrogen", SOURCE_HYDROGEN));
    

    public static final RegistryObject<FluidType> HYDROGEN_FLUORIDE_FLUID_TYPE = registerType("hydrogen_fluoride", "", "#e2bdff", HazardBehavior.BehaviorType.HEAT);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN_FLUORIDE = FLUIDS.register("hydrogen_fluoride",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_FLUORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN_FLUORIDE = FLUIDS.register("flowing_hydrogen_fluoride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_FLUORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_FLUORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_FLUORIDE_FLUID_TYPE, SOURCE_HYDROGEN_FLUORIDE, FLOWING_HYDROGEN_FLUORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen_fluoride", SOURCE_HYDROGEN_FLUORIDE))
            .bucket(ModItems.registerBucketItem("hydrogen_fluoride", SOURCE_HYDROGEN_FLUORIDE));
    

    public static final RegistryObject<FluidType> HYDROGEN_SULFIDE_FLUID_TYPE = registerType("hydrogen_sulfide", "gas", "#ffff99", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_HYDROGEN_SULFIDE = FLUIDS.register("hydrogen_sulfide",
            () -> new ForgeFlowingFluid.Source(ModFluids.HYDROGEN_SULFIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HYDROGEN_SULFIDE = FLUIDS.register("flowing_hydrogen_sulfide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HYDROGEN_SULFIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HYDROGEN_SULFIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HYDROGEN_SULFIDE_FLUID_TYPE, SOURCE_HYDROGEN_SULFIDE, FLOWING_HYDROGEN_SULFIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("hydrogen_sulfide", SOURCE_HYDROGEN_SULFIDE))
            .bucket(ModItems.registerBucketItem("hydrogen_sulfide", SOURCE_HYDROGEN_SULFIDE));
    

    public static final RegistryObject<FluidType> ISOPROPANOL_FLUID_TYPE = registerType("isopropanol", "liquid", "#cc99ff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ISOPROPANOL = FLUIDS.register("isopropanol",
            () -> new ForgeFlowingFluid.Source(ModFluids.ISOPROPANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ISOPROPANOL = FLUIDS.register("flowing_isopropanol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ISOPROPANOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ISOPROPANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ISOPROPANOL_FLUID_TYPE, SOURCE_ISOPROPANOL, FLOWING_ISOPROPANOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("isopropanol", SOURCE_ISOPROPANOL))
            .bucket(ModItems.registerBucketItem("isopropanol", SOURCE_ISOPROPANOL));
    

    public static final RegistryObject<FluidType> IUMIUM_FLUID_TYPE = registerType("iumium", "liquid", "#ff903b", HazardBehavior.BehaviorType.RADIO);
    public static final RegistryObject<FlowingFluid> SOURCE_IUMIUM = FLUIDS.register("iumium",
            () -> new ForgeFlowingFluid.Source(ModFluids.IUMIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_IUMIUM = FLUIDS.register("flowing_iumium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.IUMIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties IUMIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            IUMIUM_FLUID_TYPE, SOURCE_IUMIUM, FLOWING_IUMIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("iumium", SOURCE_IUMIUM))
            .bucket(ModItems.registerBucketItem("iumium", SOURCE_IUMIUM));
    

    public static final RegistryObject<FluidType> METHANOL_FLUID_TYPE = registerType("methanol", "liquid", "#80d4ff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_METHANOL = FLUIDS.register("methanol",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHANOL = FLUIDS.register("flowing_methanol",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHANOL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHANOL_FLUID_TYPE, SOURCE_METHANOL, FLOWING_METHANOL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methanol", SOURCE_METHANOL))
            .bucket(ModItems.registerBucketItem("methanol", SOURCE_METHANOL));
    

    public static final RegistryObject<FluidType> METHYL_CHLORIDE_FLUID_TYPE = registerType("methyl_chloride", "gas", "#ffcc99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_METHYL_CHLORIDE = FLUIDS.register("methyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHYL_CHLORIDE = FLUIDS.register("flowing_methyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHYL_CHLORIDE_FLUID_TYPE, SOURCE_METHYL_CHLORIDE, FLOWING_METHYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methyl_chloride", SOURCE_METHYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("methyl_chloride", SOURCE_METHYL_CHLORIDE));
    

    public static final RegistryObject<FluidType> METHYL_ETHYL_KETONE_FLUID_TYPE = registerType("methyl_ethyl_ketone", "liquid", "#e6ccff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_METHYL_ETHYL_KETONE = FLUIDS.register("methyl_ethyl_ketone",
            () -> new ForgeFlowingFluid.Source(ModFluids.METHYL_ETHYL_KETONE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_METHYL_ETHYL_KETONE = FLUIDS.register("flowing_methyl_ethyl_ketone",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.METHYL_ETHYL_KETONE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties METHYL_ETHYL_KETONE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            METHYL_ETHYL_KETONE_FLUID_TYPE, SOURCE_METHYL_ETHYL_KETONE, FLOWING_METHYL_ETHYL_KETONE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("methyl_ethyl_ketone", SOURCE_METHYL_ETHYL_KETONE))
            .bucket(ModItems.registerBucketItem("methyl_ethyl_ketone", SOURCE_METHYL_ETHYL_KETONE));
    

    public static final RegistryObject<FluidType> NEON_FLUID_TYPE = registerType("neon", "gas", "#ff5ccd", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_NEON = FLUIDS.register("neon",
            () -> new ForgeFlowingFluid.Source(ModFluids.NEON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NEON = FLUIDS.register("flowing_neon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NEON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NEON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NEON_FLUID_TYPE, SOURCE_NEON, FLOWING_NEON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("neon", SOURCE_NEON))
            .bucket(ModItems.registerBucketItem("neon", SOURCE_NEON));
    

    public static final RegistryObject<FluidType> NETHERITE_FLUID_TYPE = registerType("netherite", "liquid", "#301e05", HazardBehavior.BehaviorType.HEAT);
    public static final RegistryObject<FlowingFluid> SOURCE_NETHERITE = FLUIDS.register("netherite",
            () -> new ForgeFlowingFluid.Source(ModFluids.NETHERITE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NETHERITE = FLUIDS.register("flowing_netherite",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NETHERITE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NETHERITE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NETHERITE_FLUID_TYPE, SOURCE_NETHERITE, FLOWING_NETHERITE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("netherite", SOURCE_NETHERITE))
            .bucket(ModItems.registerBucketItem("netherite", SOURCE_NETHERITE));
    

    public static final RegistryObject<FluidType> NITROGEN_FLUID_TYPE = registerType("nitrogen", "gas", "#8a8dff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_NITROGEN = FLUIDS.register("nitrogen",
            () -> new ForgeFlowingFluid.Source(ModFluids.NITROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NITROGEN = FLUIDS.register("flowing_nitrogen",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NITROGEN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NITROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NITROGEN_FLUID_TYPE, SOURCE_NITROGEN, FLOWING_NITROGEN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("nitrogen", SOURCE_NITROGEN))
            .bucket(ModItems.registerBucketItem("nitrogen", SOURCE_NITROGEN));
    

    public static final RegistryObject<FluidType> PHOSGENE_FLUID_TYPE = registerType("phosgene", "gas", "#999966", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSGENE = FLUIDS.register("phosgene",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSGENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSGENE = FLUIDS.register("flowing_phosgene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSGENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSGENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSGENE_FLUID_TYPE, SOURCE_PHOSGENE, FLOWING_PHOSGENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosgene", SOURCE_PHOSGENE))
            .bucket(ModItems.registerBucketItem("phosgene", SOURCE_PHOSGENE));
    

    public static final RegistryObject<FluidType> PHOSPHORIC_ACID_FLUID_TYPE = registerType("phosphoric_acid", "liquid", "#ccff99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSPHORIC_ACID = FLUIDS.register("phosphoric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSPHORIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSPHORIC_ACID = FLUIDS.register("flowing_phosphoric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSPHORIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSPHORIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSPHORIC_ACID_FLUID_TYPE, SOURCE_PHOSPHORIC_ACID, FLOWING_PHOSPHORIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosphoric_acid", SOURCE_PHOSPHORIC_ACID))
            .bucket(ModItems.registerBucketItem("phosphoric_acid", SOURCE_PHOSPHORIC_ACID));
    

    public static final RegistryObject<FluidType> PIRANHA_FLUID_TYPE = registerType("piranha", "liquid", "#f2f0d5", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_PIRANHA = FLUIDS.register("piranha",
            () -> new ForgeFlowingFluid.Source(ModFluids.PIRANHA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PIRANHA = FLUIDS.register("flowing_piranha",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PIRANHA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PIRANHA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PIRANHA_FLUID_TYPE, SOURCE_PIRANHA, FLOWING_PIRANHA)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("piranha", SOURCE_PIRANHA))
            .bucket(ModItems.registerBucketItem("piranha", SOURCE_PIRANHA));
    

    public static final RegistryObject<FluidType> PISS_WATER_FLUID_TYPE = registerType("piss_water", "liquid", "#ebba34", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PISS_WATER = FLUIDS.register("piss_water",
            () -> new ForgeFlowingFluid.Source(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PISS_WATER = FLUIDS.register("flowing_piss_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PISS_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PISS_WATER_FLUID_TYPE, SOURCE_PISS_WATER, FLOWING_PISS_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("piss_water", SOURCE_PISS_WATER))
            .bucket(ModItems.registerBucketItem("piss_water", SOURCE_PISS_WATER));
    

    public static final RegistryObject<FluidType> POLYTETRAFLUOROETHYLENE_FLUID_TYPE = registerType("polytetrafluoroethylene", "liquid", "#c6fff8", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_POLYTETRAFLUOROETHYLENE = FLUIDS.register("polytetrafluoroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POLYTETRAFLUOROETHYLENE = FLUIDS.register("flowing_polytetrafluoroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POLYTETRAFLUOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POLYTETRAFLUOROETHYLENE_FLUID_TYPE, SOURCE_POLYTETRAFLUOROETHYLENE, FLOWING_POLYTETRAFLUOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("polytetrafluoroethylene", SOURCE_POLYTETRAFLUOROETHYLENE))
            .bucket(ModItems.registerBucketItem("polytetrafluoroethylene", SOURCE_POLYTETRAFLUOROETHYLENE));
    

    public static final RegistryObject<FluidType> POLYVINYL_CHLORIDE_FLUID_TYPE = registerType("polyvinyl_chloride", "liquid", "#e8fffa", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_POLYVINYL_CHLORIDE = FLUIDS.register("polyvinyl_chloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.POLYVINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POLYVINYL_CHLORIDE = FLUIDS.register("flowing_polyvinyl_chloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POLYVINYL_CHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POLYVINYL_CHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POLYVINYL_CHLORIDE_FLUID_TYPE, SOURCE_POLYVINYL_CHLORIDE, FLOWING_POLYVINYL_CHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("polyvinyl_chloride", SOURCE_POLYVINYL_CHLORIDE))
            .bucket(ModItems.registerBucketItem("polyvinyl_chloride", SOURCE_POLYVINYL_CHLORIDE));
    

    public static final RegistryObject<FluidType> PROPYLENE_FLUID_TYPE = registerType("propylene", "gas", "#ffcc99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PROPYLENE = FLUIDS.register("propylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.PROPYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PROPYLENE = FLUIDS.register("flowing_propylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PROPYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PROPYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PROPYLENE_FLUID_TYPE, SOURCE_PROPYLENE, FLOWING_PROPYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("propylene", SOURCE_PROPYLENE))
            .bucket(ModItems.registerBucketItem("propylene", SOURCE_PROPYLENE));
    

    public static final RegistryObject<FluidType> RADON_FLUID_TYPE = registerType("radon", "gas", "#f542cb", HazardBehavior.BehaviorType.RADIO);
    public static final RegistryObject<FlowingFluid> SOURCE_RADON = FLUIDS.register("radon",
            () -> new ForgeFlowingFluid.Source(ModFluids.RADON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RADON = FLUIDS.register("flowing_radon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.RADON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RADON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            RADON_FLUID_TYPE, SOURCE_RADON, FLOWING_RADON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("radon", SOURCE_RADON))
            .bucket(ModItems.registerBucketItem("radon", SOURCE_RADON));
    

    public static final RegistryObject<FluidType> ROCKET_FUEL_FLUID_TYPE = registerType("rocket_fuel", "gas", "#c9d400", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ROCKET_FUEL = FLUIDS.register("rocket_fuel",
            () -> new ForgeFlowingFluid.Source(ModFluids.ROCKET_FUEL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ROCKET_FUEL = FLUIDS.register("flowing_rocket_fuel",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ROCKET_FUEL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ROCKET_FUEL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ROCKET_FUEL_FLUID_TYPE, SOURCE_ROCKET_FUEL, FLOWING_ROCKET_FUEL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("rocket_fuel", SOURCE_ROCKET_FUEL))
            .bucket(ModItems.registerBucketItem("rocket_fuel", SOURCE_ROCKET_FUEL));
    

    public static final RegistryObject<FluidType> SILICOALUMINOPHOSPHATE_FLUID_TYPE = registerType("silicoaluminophosphate", "", "#83decf", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SILICOALUMINOPHOSPHATE = FLUIDS.register("silicoaluminophosphate",
            () -> new ForgeFlowingFluid.Source(ModFluids.SILICOALUMINOPHOSPHATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SILICOALUMINOPHOSPHATE = FLUIDS.register("flowing_silicoaluminophosphate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SILICOALUMINOPHOSPHATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SILICOALUMINOPHOSPHATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SILICOALUMINOPHOSPHATE_FLUID_TYPE, SOURCE_SILICOALUMINOPHOSPHATE, FLOWING_SILICOALUMINOPHOSPHATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("silicoaluminophosphate", SOURCE_SILICOALUMINOPHOSPHATE))
            .bucket(ModItems.registerBucketItem("silicoaluminophosphate", SOURCE_SILICOALUMINOPHOSPHATE));
    

    public static final RegistryObject<FluidType> SODIUM_FLUID_TYPE = registerType("sodium", "gas", "#ffaf54", HazardBehavior.BehaviorType.EXPLOSION);
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM = FLUIDS.register("sodium",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM = FLUIDS.register("flowing_sodium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_FLUID_TYPE, SOURCE_SODIUM, FLOWING_SODIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium", SOURCE_SODIUM))
            .bucket(ModItems.registerBucketItem("sodium", SOURCE_SODIUM));
    

    public static final RegistryObject<FluidType> SODIUM_HYDROXIDE_FLUID_TYPE = registerType("sodium_hydroxide", "liquid", "#99ccff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_HYDROXIDE = FLUIDS.register("sodium_hydroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_HYDROXIDE = FLUIDS.register("flowing_sodium_hydroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_HYDROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_HYDROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_HYDROXIDE_FLUID_TYPE, SOURCE_SODIUM_HYDROXIDE, FLOWING_SODIUM_HYDROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_hydroxide", SOURCE_SODIUM_HYDROXIDE))
            .bucket(ModItems.registerBucketItem("sodium_hydroxide", SOURCE_SODIUM_HYDROXIDE));
    

    public static final RegistryObject<FluidType> SODIUM_HYPOCHLORITE_FLUID_TYPE = registerType("sodium_hypochlorite", "liquid", "#99ff99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_HYPOCHLORITE = FLUIDS.register("sodium_hypochlorite",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_HYPOCHLORITE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_HYPOCHLORITE = FLUIDS.register("flowing_sodium_hypochlorite",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_HYPOCHLORITE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_HYPOCHLORITE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_HYPOCHLORITE_FLUID_TYPE, SOURCE_SODIUM_HYPOCHLORITE, FLOWING_SODIUM_HYPOCHLORITE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_hypochlorite", SOURCE_SODIUM_HYPOCHLORITE))
            .bucket(ModItems.registerBucketItem("sodium_hypochlorite", SOURCE_SODIUM_HYPOCHLORITE));
    

    public static final RegistryObject<FluidType> SODIUM_SULFATE_FLUID_TYPE = registerType("sodium_sulfate", "liquid", "#590033", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SODIUM_SULFATE = FLUIDS.register("sodium_sulfate",
            () -> new ForgeFlowingFluid.Source(ModFluids.SODIUM_SULFATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SODIUM_SULFATE = FLUIDS.register("flowing_sodium_sulfate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SODIUM_SULFATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SODIUM_SULFATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SODIUM_SULFATE_FLUID_TYPE, SOURCE_SODIUM_SULFATE, FLOWING_SODIUM_SULFATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sodium_sulfate", SOURCE_SODIUM_SULFATE))
            .bucket(ModItems.registerBucketItem("sodium_sulfate", SOURCE_SODIUM_SULFATE));
    

    public static final RegistryObject<FluidType> SOLDERING_FLUX_FLUID_TYPE = registerType("soldering_flux", "", "#f7ecad", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SOLDERING_FLUX = FLUIDS.register("soldering_flux",
            () -> new ForgeFlowingFluid.Source(ModFluids.SOLDERING_FLUX_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SOLDERING_FLUX = FLUIDS.register("flowing_soldering_flux",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SOLDERING_FLUX_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SOLDERING_FLUX_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SOLDERING_FLUX_FLUID_TYPE, SOURCE_SOLDERING_FLUX, FLOWING_SOLDERING_FLUX)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("soldering_flux", SOURCE_SOLDERING_FLUX))
            .bucket(ModItems.registerBucketItem("soldering_flux", SOURCE_SOLDERING_FLUX));
    

    public static final RegistryObject<FluidType> STIPNICIUM_FLUID_TYPE = registerType("stipnicium", "liquid", "#ff4245", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_STIPNICIUM = FLUIDS.register("stipnicium",
            () -> new ForgeFlowingFluid.Source(ModFluids.STIPNICIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STIPNICIUM = FLUIDS.register("flowing_stipnicium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STIPNICIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STIPNICIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STIPNICIUM_FLUID_TYPE, SOURCE_STIPNICIUM, FLOWING_STIPNICIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("stipnicium", SOURCE_STIPNICIUM))
            .bucket(ModItems.registerBucketItem("stipnicium", SOURCE_STIPNICIUM));
    

    public static final RegistryObject<FluidType> STYRENE_FLUID_TYPE = registerType("styrene", "liquid", "#ff9999", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_STYRENE = FLUIDS.register("styrene",
            () -> new ForgeFlowingFluid.Source(ModFluids.STYRENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STYRENE = FLUIDS.register("flowing_styrene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STYRENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STYRENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STYRENE_FLUID_TYPE, SOURCE_STYRENE, FLOWING_STYRENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("styrene", SOURCE_STYRENE))
            .bucket(ModItems.registerBucketItem("styrene", SOURCE_STYRENE));
    

    public static final RegistryObject<FluidType> STYRENE_BUTADIENE_FLUID_TYPE = registerType("styrene_butadiene", "liquid", "#2b2b29", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_STYRENE_BUTADIENE = FLUIDS.register("styrene_butadiene",
            () -> new ForgeFlowingFluid.Source(ModFluids.STYRENE_BUTADIENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STYRENE_BUTADIENE = FLUIDS.register("flowing_styrene_butadiene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.STYRENE_BUTADIENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STYRENE_BUTADIENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            STYRENE_BUTADIENE_FLUID_TYPE, SOURCE_STYRENE_BUTADIENE, FLOWING_STYRENE_BUTADIENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("styrene_butadiene", SOURCE_STYRENE_BUTADIENE))
            .bucket(ModItems.registerBucketItem("styrene_butadiene", SOURCE_STYRENE_BUTADIENE));
    

    public static final RegistryObject<FluidType> SULFURIC_ACID_FLUID_TYPE = registerType("sulfuric_acid", "liquid", "#ffcc00", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SULFURIC_ACID = FLUIDS.register("sulfuric_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SULFURIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SULFURIC_ACID = FLUIDS.register("flowing_sulfuric_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SULFURIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SULFURIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SULFURIC_ACID_FLUID_TYPE, SOURCE_SULFURIC_ACID, FLOWING_SULFURIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sulfuric_acid", SOURCE_SULFURIC_ACID))
            .bucket(ModItems.registerBucketItem("sulfuric_acid", SOURCE_SULFURIC_ACID));
    

    public static final RegistryObject<FluidType> TETRACHLOROETHYLENE_FLUID_TYPE = registerType("tetrachloroethylene", "liquid", "#999999", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_TETRACHLOROETHYLENE = FLUIDS.register("tetrachloroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.TETRACHLOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TETRACHLOROETHYLENE = FLUIDS.register("flowing_tetrachloroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TETRACHLOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TETRACHLOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TETRACHLOROETHYLENE_FLUID_TYPE, SOURCE_TETRACHLOROETHYLENE, FLOWING_TETRACHLOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("tetrachloroethylene", SOURCE_TETRACHLOROETHYLENE))
            .bucket(ModItems.registerBucketItem("tetrachloroethylene", SOURCE_TETRACHLOROETHYLENE));
    

    public static final RegistryObject<FluidType> TETRAETHYL_ORTHOSILICATE_FLUID_TYPE = registerType("tetraethyl_orthosilicate", "", "#ffffff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_TETRAETHYL_ORTHOSILICATE = FLUIDS.register("tetraethyl_orthosilicate",
            () -> new ForgeFlowingFluid.Source(ModFluids.TETRAETHYL_ORTHOSILICATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TETRAETHYL_ORTHOSILICATE = FLUIDS.register("flowing_tetraethyl_orthosilicate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TETRAETHYL_ORTHOSILICATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TETRAETHYL_ORTHOSILICATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TETRAETHYL_ORTHOSILICATE_FLUID_TYPE, SOURCE_TETRAETHYL_ORTHOSILICATE, FLOWING_TETRAETHYL_ORTHOSILICATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("tetraethyl_orthosilicate", SOURCE_TETRAETHYL_ORTHOSILICATE))
            .bucket(ModItems.registerBucketItem("tetraethyl_orthosilicate", SOURCE_TETRAETHYL_ORTHOSILICATE));
    

    public static final RegistryObject<FluidType> TETRAHYDROFURAN_FLUID_TYPE = registerType("tetrahydrofuran", "liquid", "#e6e6e6", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_TETRAHYDROFURAN = FLUIDS.register("tetrahydrofuran",
            () -> new ForgeFlowingFluid.Source(ModFluids.TETRAHYDROFURAN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TETRAHYDROFURAN = FLUIDS.register("flowing_tetrahydrofuran",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TETRAHYDROFURAN_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TETRAHYDROFURAN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TETRAHYDROFURAN_FLUID_TYPE, SOURCE_TETRAHYDROFURAN, FLOWING_TETRAHYDROFURAN)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("tetrahydrofuran", SOURCE_TETRAHYDROFURAN))
            .bucket(ModItems.registerBucketItem("tetrahydrofuran", SOURCE_TETRAHYDROFURAN));
    

    public static final RegistryObject<FluidType> TRICHLOROETHYLENE_FLUID_TYPE = registerType("trichloroethylene", "liquid", "#cc9999", HazardBehavior.BehaviorType.EXTREME);
    public static final RegistryObject<FlowingFluid> SOURCE_TRICHLOROETHYLENE = FLUIDS.register("trichloroethylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.TRICHLOROETHYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TRICHLOROETHYLENE = FLUIDS.register("flowing_trichloroethylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TRICHLOROETHYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TRICHLOROETHYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TRICHLOROETHYLENE_FLUID_TYPE, SOURCE_TRICHLOROETHYLENE, FLOWING_TRICHLOROETHYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("trichloroethylene", SOURCE_TRICHLOROETHYLENE))
            .bucket(ModItems.registerBucketItem("trichloroethylene", SOURCE_TRICHLOROETHYLENE));
    

    public static final RegistryObject<FluidType> TRICHLOROMETHANE_FLUID_TYPE = registerType("trichloromethane", "liquid", "#fDfeed", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_TRICHLOROMETHANE = FLUIDS.register("trichloromethane",
            () -> new ForgeFlowingFluid.Source(ModFluids.TRICHLOROMETHANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TRICHLOROMETHANE = FLUIDS.register("flowing_trichloromethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TRICHLOROMETHANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TRICHLOROMETHANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TRICHLOROMETHANE_FLUID_TYPE, SOURCE_TRICHLOROMETHANE, FLOWING_TRICHLOROMETHANE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("trichloromethane", SOURCE_TRICHLOROMETHANE))
            .bucket(ModItems.registerBucketItem("trichloromethane", SOURCE_TRICHLOROMETHANE));
    

    public static final RegistryObject<FluidType> UNOBTANIUM_FLUID_TYPE = registerType("unobtanium", "liquid", "#ff66cc", HazardBehavior.BehaviorType.RADIO);
    public static final RegistryObject<FlowingFluid> SOURCE_UNOBTANIUM = FLUIDS.register("unobtanium",
            () -> new ForgeFlowingFluid.Source(ModFluids.UNOBTANIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_UNOBTANIUM = FLUIDS.register("flowing_unobtanium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.UNOBTANIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties UNOBTANIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            UNOBTANIUM_FLUID_TYPE, SOURCE_UNOBTANIUM, FLOWING_UNOBTANIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("unobtanium", SOURCE_UNOBTANIUM))
            .bucket(ModItems.registerBucketItem("unobtanium", SOURCE_UNOBTANIUM));
    

    public static final RegistryObject<FluidType> CATALYSED_URANIUM_FLUID_TYPE = registerType("catalysed_uranium", "liquid", "#488257", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_CATALYSED_URANIUM = FLUIDS.register("catalysed_uranium",
            () -> new ForgeFlowingFluid.Source(ModFluids.CATALYSED_URANIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CATALYSED_URANIUM = FLUIDS.register("flowing_catalysed_uranium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CATALYSED_URANIUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CATALYSED_URANIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            CATALYSED_URANIUM_FLUID_TYPE, SOURCE_CATALYSED_URANIUM, FLOWING_CATALYSED_URANIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("catalysed_uranium", SOURCE_CATALYSED_URANIUM))
            .bucket(ModItems.registerBucketItem("catalysed_uranium", SOURCE_CATALYSED_URANIUM));
    

    public static final RegistryObject<FluidType> XENON_FLUID_TYPE = registerType("xenon", "gas", "#3a79ff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_XENON = FLUIDS.register("xenon",
            () -> new ForgeFlowingFluid.Source(ModFluids.XENON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_XENON = FLUIDS.register("flowing_xenon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.XENON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties XENON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            XENON_FLUID_TYPE, SOURCE_XENON, FLOWING_XENON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("xenon", SOURCE_XENON))
            .bucket(ModItems.registerBucketItem("xenon", SOURCE_XENON));
    

    public static final RegistryObject<FluidType> XYLENE_FLUID_TYPE = registerType("xylene", "liquid", "#f8cfff", HazardBehavior.BehaviorType.FREEZE);
    public static final RegistryObject<FlowingFluid> SOURCE_XYLENE = FLUIDS.register("xylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.XYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_XYLENE = FLUIDS.register("flowing_xylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.XYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties XYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            XYLENE_FLUID_TYPE, SOURCE_XYLENE, FLOWING_XYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("xylene", SOURCE_XYLENE))
            .bucket(ModItems.registerBucketItem("xylene", SOURCE_XYLENE));
    

    public static final RegistryObject<FluidType> RP_1_FLUID_TYPE = registerType("rp_1", "liquid", "#f0243f", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_RP_1 = FLUIDS.register("rp_1",
            () -> new ForgeFlowingFluid.Source(ModFluids.RP_1_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RP_1 = FLUIDS.register("flowing_rp_1",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.RP_1_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RP_1_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            RP_1_FLUID_TYPE, SOURCE_RP_1, FLOWING_RP_1)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("rp_1", SOURCE_RP_1))
            .bucket(ModItems.registerBucketItem("rp_1", SOURCE_RP_1));
    

    public static final RegistryObject<FluidType> ANTIMONY_PENTAFLUORIDE_FLUID_TYPE = registerType("antimony_pentafluoride", "liquid", "#f980ff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ANTIMONY_PENTAFLUORIDE = FLUIDS.register("antimony_pentafluoride",
            () -> new ForgeFlowingFluid.Source(ModFluids.ANTIMONY_PENTAFLUORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ANTIMONY_PENTAFLUORIDE = FLUIDS.register("flowing_antimony_pentafluoride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ANTIMONY_PENTAFLUORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ANTIMONY_PENTAFLUORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ANTIMONY_PENTAFLUORIDE_FLUID_TYPE, SOURCE_ANTIMONY_PENTAFLUORIDE, FLOWING_ANTIMONY_PENTAFLUORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("antimony_pentafluoride", SOURCE_ANTIMONY_PENTAFLUORIDE))
            .bucket(ModItems.registerBucketItem("antimony_pentafluoride", SOURCE_ANTIMONY_PENTAFLUORIDE));
    

    public static final RegistryObject<FluidType> POLYMETHYL_METHACRYLATE_FLUID_TYPE = registerType("polymethyl_methacrylate", "liquid", "#979fc7", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_POLYMETHYL_METHACRYLATE = FLUIDS.register("polymethyl_methacrylate",
            () -> new ForgeFlowingFluid.Source(ModFluids.POLYMETHYL_METHACRYLATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POLYMETHYL_METHACRYLATE = FLUIDS.register("flowing_polymethyl_methacrylate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POLYMETHYL_METHACRYLATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POLYMETHYL_METHACRYLATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POLYMETHYL_METHACRYLATE_FLUID_TYPE, SOURCE_POLYMETHYL_METHACRYLATE, FLOWING_POLYMETHYL_METHACRYLATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("polymethyl_methacrylate", SOURCE_POLYMETHYL_METHACRYLATE))
            .bucket(ModItems.registerBucketItem("polymethyl_methacrylate", SOURCE_POLYMETHYL_METHACRYLATE));
    

    public static final RegistryObject<FluidType> GASEOUS_HYRDOCARBONS_FLUID_TYPE = registerType("gaseous_hyrdocarbons", "gas", "#666666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_GASEOUS_HYRDOCARBONS = FLUIDS.register("gaseous_hyrdocarbons",
            () -> new ForgeFlowingFluid.Source(ModFluids.GASEOUS_HYRDOCARBONS_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GASEOUS_HYRDOCARBONS = FLUIDS.register("flowing_gaseous_hyrdocarbons",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GASEOUS_HYRDOCARBONS_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GASEOUS_HYRDOCARBONS_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GASEOUS_HYRDOCARBONS_FLUID_TYPE, SOURCE_GASEOUS_HYRDOCARBONS, FLOWING_GASEOUS_HYRDOCARBONS)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("gaseous_hyrdocarbons", SOURCE_GASEOUS_HYRDOCARBONS))
            .bucket(ModItems.registerBucketItem("gaseous_hyrdocarbons", SOURCE_GASEOUS_HYRDOCARBONS));
    

    public static final RegistryObject<FluidType> ACETYLENE_FLUID_TYPE = registerType("acetylene", "liquid", "#ffffff", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ACETYLENE = FLUIDS.register("acetylene",
            () -> new ForgeFlowingFluid.Source(ModFluids.ACETYLENE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ACETYLENE = FLUIDS.register("flowing_acetylene",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ACETYLENE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ACETYLENE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ACETYLENE_FLUID_TYPE, SOURCE_ACETYLENE, FLOWING_ACETYLENE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("acetylene", SOURCE_ACETYLENE))
            .bucket(ModItems.registerBucketItem("acetylene", SOURCE_ACETYLENE));
    

    public static final RegistryObject<FluidType> PHOSPHORUS_FLUID_TYPE = registerType("phosphorus", "liquid", "#7d2718", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSPHORUS = FLUIDS.register("phosphorus",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSPHORUS_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSPHORUS = FLUIDS.register("flowing_phosphorus",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSPHORUS_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSPHORUS_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSPHORUS_FLUID_TYPE, SOURCE_PHOSPHORUS, FLOWING_PHOSPHORUS)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosphorus", SOURCE_PHOSPHORUS))
            .bucket(ModItems.registerBucketItem("phosphorus", SOURCE_PHOSPHORUS));
    

    public static final RegistryObject<FluidType> NATURAL_GAS_FLUID_TYPE = registerType("natural_gas", "gas", "#ffcc99", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_NATURAL_GAS = FLUIDS.register("natural_gas",
            () -> new ForgeFlowingFluid.Source(ModFluids.NATURAL_GAS_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NATURAL_GAS = FLUIDS.register("flowing_natural_gas",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NATURAL_GAS_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties NATURAL_GAS_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            NATURAL_GAS_FLUID_TYPE, SOURCE_NATURAL_GAS, FLOWING_NATURAL_GAS)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("natural_gas", SOURCE_NATURAL_GAS))
            .bucket(ModItems.registerBucketItem("natural_gas", SOURCE_NATURAL_GAS));
    

    public static final RegistryObject<FluidType> SILICON_TETRACHLORIDE_FLUID_TYPE = registerType("silicon_tetrachloride", "liquid", "#fc7303", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SILICON_TETRACHLORIDE = FLUIDS.register("silicon_tetrachloride",
            () -> new ForgeFlowingFluid.Source(ModFluids.SILICON_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SILICON_TETRACHLORIDE = FLUIDS.register("flowing_silicon_tetrachloride",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SILICON_TETRACHLORIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SILICON_TETRACHLORIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SILICON_TETRACHLORIDE_FLUID_TYPE, SOURCE_SILICON_TETRACHLORIDE, FLOWING_SILICON_TETRACHLORIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("silicon_tetrachloride", SOURCE_SILICON_TETRACHLORIDE))
            .bucket(ModItems.registerBucketItem("silicon_tetrachloride", SOURCE_SILICON_TETRACHLORIDE));
    

    public static final RegistryObject<FluidType> ENERGISED_NAQADAH_FLUID_TYPE = registerType("energised_naqadah", "liquid", "#c6fff8", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ENERGISED_NAQADAH = FLUIDS.register("energised_naqadah",
            () -> new ForgeFlowingFluid.Source(ModFluids.ENERGISED_NAQADAH_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ENERGISED_NAQADAH = FLUIDS.register("flowing_energised_naqadah",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ENERGISED_NAQADAH_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ENERGISED_NAQADAH_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ENERGISED_NAQADAH_FLUID_TYPE, SOURCE_ENERGISED_NAQADAH, FLOWING_ENERGISED_NAQADAH)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("energised_naqadah", SOURCE_ENERGISED_NAQADAH))
            .bucket(ModItems.registerBucketItem("energised_naqadah", SOURCE_ENERGISED_NAQADAH));
    

    public static final RegistryObject<FluidType> MONOCRYSTALINE_SILICON_FLUID_TYPE = registerType("monocrystaline_silicon", "liquid", "#2b2b29", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_MONOCRYSTALINE_SILICON = FLUIDS.register("monocrystaline_silicon",
            () -> new ForgeFlowingFluid.Source(ModFluids.MONOCRYSTALINE_SILICON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MONOCRYSTALINE_SILICON = FLUIDS.register("flowing_monocrystaline_silicon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.MONOCRYSTALINE_SILICON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties MONOCRYSTALINE_SILICON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            MONOCRYSTALINE_SILICON_FLUID_TYPE, SOURCE_MONOCRYSTALINE_SILICON, FLOWING_MONOCRYSTALINE_SILICON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("monocrystaline_silicon", SOURCE_MONOCRYSTALINE_SILICON))
            .bucket(ModItems.registerBucketItem("monocrystaline_silicon", SOURCE_MONOCRYSTALINE_SILICON));
    

    public static final RegistryObject<FluidType> REFINED_SILICON_FLUID_TYPE = registerType("refined_silicon", "liquid", "#666666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_REFINED_SILICON = FLUIDS.register("refined_silicon",
            () -> new ForgeFlowingFluid.Source(ModFluids.REFINED_SILICON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_REFINED_SILICON = FLUIDS.register("flowing_refined_silicon",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.REFINED_SILICON_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties REFINED_SILICON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            REFINED_SILICON_FLUID_TYPE, SOURCE_REFINED_SILICON, FLOWING_REFINED_SILICON)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("refined_silicon", SOURCE_REFINED_SILICON))
            .bucket(ModItems.registerBucketItem("refined_silicon", SOURCE_REFINED_SILICON));
    

    public static final RegistryObject<FluidType> TREATED_BIODIESEL_FLUID_TYPE = registerType("treated_biodiesel", "liquid", "#999966", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_TREATED_BIODIESEL = FLUIDS.register("treated_biodiesel",
            () -> new ForgeFlowingFluid.Source(ModFluids.TREATED_BIODIESEL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_TREATED_BIODIESEL = FLUIDS.register("flowing_treated_biodiesel",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.TREATED_BIODIESEL_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties TREATED_BIODIESEL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            TREATED_BIODIESEL_FLUID_TYPE, SOURCE_TREATED_BIODIESEL, FLOWING_TREATED_BIODIESEL)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("treated_biodiesel", SOURCE_TREATED_BIODIESEL))
            .bucket(ModItems.registerBucketItem("treated_biodiesel", SOURCE_TREATED_BIODIESEL));
    

    public static final RegistryObject<FluidType> POTASSIUM_DICHROMATE_FLUID_TYPE = registerType("potassium_dichromate", "liquid", "#d91455", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_POTASSIUM_DICHROMATE = FLUIDS.register("potassium_dichromate",
            () -> new ForgeFlowingFluid.Source(ModFluids.POTASSIUM_DICHROMATE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_POTASSIUM_DICHROMATE = FLUIDS.register("flowing_potassium_dichromate",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.POTASSIUM_DICHROMATE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties POTASSIUM_DICHROMATE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            POTASSIUM_DICHROMATE_FLUID_TYPE, SOURCE_POTASSIUM_DICHROMATE, FLOWING_POTASSIUM_DICHROMATE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("potassium_dichromate", SOURCE_POTASSIUM_DICHROMATE))
            .bucket(ModItems.registerBucketItem("potassium_dichromate", SOURCE_POTASSIUM_DICHROMATE));
    

    public static final RegistryObject<FluidType> HIGH_CARBON_STEEL_52100_FLUID_TYPE = registerType("high_carbon_steel_52100", "liquid", "#2b2b29", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_HIGH_CARBON_STEEL_52100 = FLUIDS.register("high_carbon_steel_52100",
            () -> new ForgeFlowingFluid.Source(ModFluids.HIGH_CARBON_STEEL_52100_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HIGH_CARBON_STEEL_52100 = FLUIDS.register("flowing_high_carbon_steel_52100",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HIGH_CARBON_STEEL_52100_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HIGH_CARBON_STEEL_52100_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            HIGH_CARBON_STEEL_52100_FLUID_TYPE, SOURCE_HIGH_CARBON_STEEL_52100, FLOWING_HIGH_CARBON_STEEL_52100)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("high_carbon_steel_52100", SOURCE_HIGH_CARBON_STEEL_52100))
            .bucket(ModItems.registerBucketItem("high_carbon_steel_52100", SOURCE_HIGH_CARBON_STEEL_52100));
    

    public static final RegistryObject<FluidType> ENGINEERED_ALLOY_FLUID_TYPE = registerType("engineered_alloy", "liquid", "#666666", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ENGINEERED_ALLOY = FLUIDS.register("engineered_alloy",
            () -> new ForgeFlowingFluid.Source(ModFluids.ENGINEERED_ALLOY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ENGINEERED_ALLOY = FLUIDS.register("flowing_engineered_alloy",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ENGINEERED_ALLOY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ENGINEERED_ALLOY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ENGINEERED_ALLOY_FLUID_TYPE, SOURCE_ENGINEERED_ALLOY, FLOWING_ENGINEERED_ALLOY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("engineered_alloy", SOURCE_ENGINEERED_ALLOY))
            .bucket(ModItems.registerBucketItem("engineered_alloy", SOURCE_ENGINEERED_ALLOY));
    

    public static final RegistryObject<FluidType> GOOD_BEER_FLUID_TYPE = registerType("good_beer", "liquid", "#ffbd4a", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_GOOD_BEER = FLUIDS.register("good_beer",
            () -> new ForgeFlowingFluid.Source(ModFluids.GOOD_BEER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GOOD_BEER = FLUIDS.register("flowing_good_beer",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GOOD_BEER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GOOD_BEER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            GOOD_BEER_FLUID_TYPE, SOURCE_GOOD_BEER, FLOWING_GOOD_BEER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("good_beer", SOURCE_GOOD_BEER))
            .bucket(ModItems.registerBucketItem("good_beer", SOURCE_GOOD_BEER));
    

    public static final RegistryObject<FluidType> FOSTERS_BEER_FLUID_TYPE = registerType("fosters_beer", "liquid", "#e3cda8", HazardBehavior.BehaviorType.SUFFOCATE);
    public static final RegistryObject<FlowingFluid> SOURCE_FOSTERS_BEER = FLUIDS.register("fosters_beer",
            () -> new ForgeFlowingFluid.Source(ModFluids.FOSTERS_BEER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FOSTERS_BEER = FLUIDS.register("flowing_fosters_beer",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FOSTERS_BEER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FOSTERS_BEER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FOSTERS_BEER_FLUID_TYPE, SOURCE_FOSTERS_BEER, FLOWING_FOSTERS_BEER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("fosters_beer", SOURCE_FOSTERS_BEER))
            .bucket(ModItems.registerBucketItem("fosters_beer", SOURCE_FOSTERS_BEER));
    

    public static final RegistryObject<FluidType> PH_STABLISED_BESKAR_FLUID_TYPE = registerType("ph_stablised_beskar", "liquid", "#413c6e", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PH_STABLISED_BESKAR = FLUIDS.register("ph_stablised_beskar",
            () -> new ForgeFlowingFluid.Source(ModFluids.PH_STABLISED_BESKAR_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PH_STABLISED_BESKAR = FLUIDS.register("flowing_ph_stablised_beskar",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PH_STABLISED_BESKAR_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PH_STABLISED_BESKAR_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PH_STABLISED_BESKAR_FLUID_TYPE, SOURCE_PH_STABLISED_BESKAR, FLOWING_PH_STABLISED_BESKAR)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ph_stablised_beskar", SOURCE_PH_STABLISED_BESKAR))
            .bucket(ModItems.registerBucketItem("ph_stablised_beskar", SOURCE_PH_STABLISED_BESKAR));
    

    public static final RegistryObject<FluidType> MODIFIED_PHOSPHINIC_ACID_FLUID_TYPE = registerType("modified_phosphinic_acid", "liquid", "#37a9b8", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_MODIFIED_PHOSPHINIC_ACID = FLUIDS.register("modified_phosphinic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.MODIFIED_PHOSPHINIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MODIFIED_PHOSPHINIC_ACID = FLUIDS.register("flowing_modified_phosphinic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.MODIFIED_PHOSPHINIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties MODIFIED_PHOSPHINIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            MODIFIED_PHOSPHINIC_ACID_FLUID_TYPE, SOURCE_MODIFIED_PHOSPHINIC_ACID, FLOWING_MODIFIED_PHOSPHINIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("modified_phosphinic_acid", SOURCE_MODIFIED_PHOSPHINIC_ACID))
            .bucket(ModItems.registerBucketItem("modified_phosphinic_acid", SOURCE_MODIFIED_PHOSPHINIC_ACID));
    

    public static final RegistryObject<FluidType> PHOSPHINIC_ACID_FLUID_TYPE = registerType("phosphinic_acid", "liquid", "#78ffe8", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PHOSPHINIC_ACID = FLUIDS.register("phosphinic_acid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PHOSPHINIC_ACID_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PHOSPHINIC_ACID = FLUIDS.register("flowing_phosphinic_acid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PHOSPHINIC_ACID_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PHOSPHINIC_ACID_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PHOSPHINIC_ACID_FLUID_TYPE, SOURCE_PHOSPHINIC_ACID, FLOWING_PHOSPHINIC_ACID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("phosphinic_acid", SOURCE_PHOSPHINIC_ACID))
            .bucket(ModItems.registerBucketItem("phosphinic_acid", SOURCE_PHOSPHINIC_ACID));
    

    public static final RegistryObject<FluidType> BESKAR_HYDROXIDE_FLUID_TYPE = registerType("beskar_hydroxide", "liquid", "#493c6e", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_BESKAR_HYDROXIDE = FLUIDS.register("beskar_hydroxide",
            () -> new ForgeFlowingFluid.Source(ModFluids.BESKAR_HYDROXIDE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BESKAR_HYDROXIDE = FLUIDS.register("flowing_beskar_hydroxide",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BESKAR_HYDROXIDE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BESKAR_HYDROXIDE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BESKAR_HYDROXIDE_FLUID_TYPE, SOURCE_BESKAR_HYDROXIDE, FLOWING_BESKAR_HYDROXIDE)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("beskar_hydroxide", SOURCE_BESKAR_HYDROXIDE))
            .bucket(ModItems.registerBucketItem("beskar_hydroxide", SOURCE_BESKAR_HYDROXIDE));
    

    public static final RegistryObject<FluidType> SUPERHEATED_BESKAR_SLURRY_FLUID_TYPE = registerType("superheated_beskar_slurry", "liquid", "#6e3c58", HazardBehavior.BehaviorType.HEAT);
    public static final RegistryObject<FlowingFluid> SOURCE_SUPERHEATED_BESKAR_SLURRY = FLUIDS.register("superheated_beskar_slurry",
            () -> new ForgeFlowingFluid.Source(ModFluids.SUPERHEATED_BESKAR_SLURRY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SUPERHEATED_BESKAR_SLURRY = FLUIDS.register("flowing_superheated_beskar_slurry",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SUPERHEATED_BESKAR_SLURRY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SUPERHEATED_BESKAR_SLURRY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SUPERHEATED_BESKAR_SLURRY_FLUID_TYPE, SOURCE_SUPERHEATED_BESKAR_SLURRY, FLOWING_SUPERHEATED_BESKAR_SLURRY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("superheated_beskar_slurry", SOURCE_SUPERHEATED_BESKAR_SLURRY))
            .bucket(ModItems.registerBucketItem("superheated_beskar_slurry", SOURCE_SUPERHEATED_BESKAR_SLURRY));
    

    public static final RegistryObject<FluidType> BESKAR_FLUID_TYPE = registerType("beskar", "liquid", "#14151c", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_BESKAR = FLUIDS.register("beskar",
            () -> new ForgeFlowingFluid.Source(ModFluids.BESKAR_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BESKAR = FLUIDS.register("flowing_beskar",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BESKAR_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BESKAR_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            BESKAR_FLUID_TYPE, SOURCE_BESKAR, FLOWING_BESKAR)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("beskar", SOURCE_BESKAR))
            .bucket(ModItems.registerBucketItem("beskar", SOURCE_BESKAR));
    

    public static final RegistryObject<FluidType> SEA_WATER_FLUID_TYPE = registerType("sea_water", "liquid", "#0018d1", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_SEA_WATER = FLUIDS.register("sea_water",
            () -> new ForgeFlowingFluid.Source(ModFluids.SEA_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SEA_WATER = FLUIDS.register("flowing_sea_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SEA_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SEA_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SEA_WATER_FLUID_TYPE, SOURCE_SEA_WATER, FLOWING_SEA_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("sea_water", SOURCE_SEA_WATER))
            .bucket(ModItems.registerBucketItem("sea_water", SOURCE_SEA_WATER));
    

    public static final RegistryObject<FluidType> ICE_SLURRY_FLUID_TYPE = registerType("ice_slurry", "liquid", "#1e99f7", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_ICE_SLURRY = FLUIDS.register("ice_slurry",
            () -> new ForgeFlowingFluid.Source(ModFluids.ICE_SLURRY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ICE_SLURRY = FLUIDS.register("flowing_ice_slurry",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ICE_SLURRY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ICE_SLURRY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ICE_SLURRY_FLUID_TYPE, SOURCE_ICE_SLURRY, FLOWING_ICE_SLURRY)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ice_slurry", SOURCE_ICE_SLURRY))
            .bucket(ModItems.registerBucketItem("ice_slurry", SOURCE_ICE_SLURRY));
    

    public static final RegistryObject<FluidType> PH_BALANCED_PURIFIED_WATER_FLUID_TYPE = registerType("ph_balanced_purified_water", "liquid", "#3061c2", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PH_BALANCED_PURIFIED_WATER = FLUIDS.register("ph_balanced_purified_water",
            () -> new ForgeFlowingFluid.Source(ModFluids.PH_BALANCED_PURIFIED_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PH_BALANCED_PURIFIED_WATER = FLUIDS.register("flowing_ph_balanced_purified_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PH_BALANCED_PURIFIED_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PH_BALANCED_PURIFIED_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PH_BALANCED_PURIFIED_WATER_FLUID_TYPE, SOURCE_PH_BALANCED_PURIFIED_WATER, FLOWING_PH_BALANCED_PURIFIED_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ph_balanced_purified_water", SOURCE_PH_BALANCED_PURIFIED_WATER))
            .bucket(ModItems.registerBucketItem("ph_balanced_purified_water", SOURCE_PH_BALANCED_PURIFIED_WATER));
    

    public static final RegistryObject<FluidType> PURIFIED_WATER_FLUID_TYPE = registerType("purified_water", "liquid", "#3c64b5", HazardBehavior.BehaviorType.NONE);
    public static final RegistryObject<FlowingFluid> SOURCE_PURIFIED_WATER = FLUIDS.register("purified_water",
            () -> new ForgeFlowingFluid.Source(ModFluids.PURIFIED_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PURIFIED_WATER = FLUIDS.register("flowing_purified_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PURIFIED_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PURIFIED_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PURIFIED_WATER_FLUID_TYPE, SOURCE_PURIFIED_WATER, FLOWING_PURIFIED_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("purified_water", SOURCE_PURIFIED_WATER))
            .bucket(ModItems.registerBucketItem("purified_water", SOURCE_PURIFIED_WATER));
    
    //#end FLUID_REGION
}
