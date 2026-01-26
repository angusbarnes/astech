package net.astr0.astrocraft;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.Fluid.ModFluids;
import net.astr0.astrocraft.block.AdvancedAssembler.AdvancedAssemblerScreen;
import net.astr0.astrocraft.block.Assembler.AssemblerScreen;
import net.astr0.astrocraft.block.ChemicalMixer.ChemicalMixerScreen;
import net.astr0.astrocraft.block.CoolantBlock.CoolantBlockScreen;
import net.astr0.astrocraft.block.EUVMachine.EUVMachineScreen;
import net.astr0.astrocraft.block.ElectrolyticSeperator.ElectrolyticSeperatorScreen;
import net.astr0.astrocraft.block.GemPolisher.GemPolishingBlockEntityRenderer;
import net.astr0.astrocraft.block.GemPolisher.GemPolishingStationScreen;
import net.astr0.astrocraft.block.ModBlockEntities;
import net.astr0.astrocraft.block.ModBlocks;
import net.astr0.astrocraft.block.PyrolysisChamber.PyrolysisChamberScreen;
import net.astr0.astrocraft.block.ReactionChamber.ChemicalReactorScreen;
import net.astr0.astrocraft.compat.mek.AstrocraftSlurries;
import net.astr0.astrocraft.gui.ModMenuTypes;
import net.astr0.astrocraft.item.AsTechBucketItem;
import net.astr0.astrocraft.item.CableToolScreen;
import net.astr0.astrocraft.item.FluidCellItem;
import net.astr0.astrocraft.item.ModItems;
import net.astr0.astrocraft.network.AsTechNetworkHandler;
import net.astr0.astrocraft.recipe.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Astrocraft.MODID)
public class Astrocraft
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "astrocraft";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();



    public Astrocraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);


        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModFluids.register(modEventBus);
        ModCreativeModTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        SoundRegistry.register(modEventBus);

        if(ModList.get().isLoaded("mekanism")) {
            AstrocraftSlurries.SLURRIES.register(modEventBus);
        }

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(Astrocraft::addDangerToolTips);
        forgeEventBus.addListener(EventPriority.LOW, (PlayerInteractEvent.RightClickBlock event) -> {
            InteractionResult result = EventHandlers.HandleBrickPlacement(event);
            if (result != null) {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        });
        forgeEventBus.addListener(EventPriority.LOW, EventHandlers::PreventTreePunching);
        forgeEventBus.addListener(EventPriority.LOW, EventHandlers::DoCampfireConversion);
        forgeEventBus.addListener(EventPriority.LOW, EventHandlers::BlockPlaceListener);
        forgeEventBus.addListener(EventPriority.HIGH, EventHandlers::restrictBlockEntityAccess);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        AsTechNetworkHandler.onCommonSetup();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.DEEZ_NUTS_ITEM);
        }
    }

    public static void addDangerToolTips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if(item instanceof AsTechBucketItem || item instanceof FluidCellItem) {
            if(stack.getTag() != null && stack.getTag().contains("danger_ttl")) {
                MutableComponent timer = MutableComponent.create(ComponentContents.EMPTY);
                timer.append(String.format("§c%d...", stack.getTag().getInt("danger_ttl")));
                event.getToolTip().add(timer);
            }
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_POLISHING_BE.get(), GemPolishingBlockEntityRenderer::new);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);
            MenuScreens.register(ModMenuTypes.CHEMICAL_MIXER_MENU.get(), ChemicalMixerScreen::new);
            MenuScreens.register(ModMenuTypes.ASSEMBLER_MENU.get(), AssemblerScreen::new);
            MenuScreens.register(ModMenuTypes.ADVANCED_ASSEMBLER_MENU.get(), AdvancedAssemblerScreen::new);
            MenuScreens.register(ModMenuTypes.CHEMICAL_REACTOR_MENU.get(), ChemicalReactorScreen::new);
            MenuScreens.register(ModMenuTypes.ELECTROLYTIC_SEPERATOR_MENU.get(), ElectrolyticSeperatorScreen::new);
            MenuScreens.register(ModMenuTypes.PYROLYSIS_CHAMBER_MENU.get(), PyrolysisChamberScreen::new);
            MenuScreens.register(ModMenuTypes.EUV_MACHINE_MENU.get(), EUVMachineScreen::new);
            MenuScreens.register(ModMenuTypes.COOLANT_BLOCK_MENU.get(), CoolantBlockScreen::new);
            MenuScreens.register(ModMenuTypes.CABLE_TOOL_MENU.get(), CableToolScreen::new);

        }

    }
}
