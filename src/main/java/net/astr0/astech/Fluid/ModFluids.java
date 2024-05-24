package net.astr0.astech.Fluid;

import net.astr0.astech.AsTech;
import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.Fluid.helpers.TintColor;
import net.astr0.astech.ModBlocks;
import net.astr0.astech.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.SoundAction;
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

    private static RegistryObject<FluidType> registerType(String name, String type, String colorCode) {
        if (type.equals("gas")) {
            return FLUID_TYPES.register(name, () -> new ChemicalGasType(TintColor.fromHex(colorCode, 179)));
        } else {
            return FLUID_TYPES.register(name, () -> new ChemicalLiquidType(TintColor.fromHex(colorCode, 179)));
        }
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


    public static final RegistryObject<FluidType> PISS_WATER_FLUID_TYPE = registerType("piss_water_fluid", "liquid", "#ebba34");
    public static final RegistryObject<FlowingFluid> SOURCE_PISS_WATER = FLUIDS.register("piss_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PISS_WATER = FLUIDS.register("flowing_piss_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PISS_WATER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PISS_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            PISS_WATER_FLUID_TYPE, SOURCE_PISS_WATER, FLOWING_PISS_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("piss_water", SOURCE_PISS_WATER))
            .bucket(ModItems.registerBucketItem("piss_water", SOURCE_PISS_WATER));

    public static final RegistryObject<FluidType> ethane_FLUID_TYPE = registerType("ethane_fluid", "gas", "#f5f5f5");
    public static final RegistryObject<FlowingFluid> SOURCE_ethane = FLUIDS.register("ethane_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ethane_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ethane = FLUIDS.register("flowing_ethane",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ethane_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties ethane_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ethane_FLUID_TYPE, SOURCE_ethane, FLOWING_ethane)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("ethane", SOURCE_ethane))
            .bucket(ModItems.registerBucketItem("ethane", SOURCE_ethane));

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);

        eventBus.addListener(ModFluids::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());
    }
}
