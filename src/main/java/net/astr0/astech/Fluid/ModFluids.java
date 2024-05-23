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

    private static RegistryObject<FluidType> registerType(String name) {
        return FLUID_TYPES.register(name, () -> new ChemicalLiquidType(new TintColor(255, 54, 111)));
    }


    // work on finding a generic way to create fluids, associated buckets and source blocks
    // Maybe a factory which maintains its own registers for all of these areas
    // Need to create a generic bucket or textures. The block should be easy.
    // Maybe cache these in a Hashmap if they need to be looked up, but all recipes should
    // use tags anyways.
    // A central fluid definitions file could be used which has a plain text list,
    // the factory could read this static list, using the key to generate the registry entries,
    // and then set values in the list as it generates the block, bucket, etc
    // The Tag datagen task can then also read these definitions and tag them
    private static void registerLiquid(String name, TintColor color) {
        RegistryObject<FluidType> type = FLUID_TYPES.register(name, () -> new ChemicalLiquidType(color));
        ForgeFlowingFluid.Properties props = new ForgeFlowingFluid.Properties(
                SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
                .slopeFindDistance(1).levelDecreasePerBlock(1).block(ModBlocks.SOAP_WATER_BLOCK);
    }

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation SOAP_OVERLAY_RL = new ResourceLocation(AsTech.MODID, "misc/in_soap_water");

    public static final RegistryObject<FlowingFluid> SOURCE_SOAP_WATER = FLUIDS.register("soap_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SOAP_WATER = FLUIDS.register("flowing_soap_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SOAP_WATER_FLUID_PROPERTIES));

    public static final RegistryObject<FluidType> SOAP_WATER_FLUID_TYPE = registerType("soap_water_fluid");


    public static final ForgeFlowingFluid.Properties SOAP_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SOAP_WATER_BLOCK)
            .bucket(ModItems.SOAP_WATER_BUCKET);


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
