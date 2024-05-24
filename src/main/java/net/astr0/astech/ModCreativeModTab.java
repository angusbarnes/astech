package net.astr0.astech;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AsTech.MODID);

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> ASTECH_TAB = CREATIVE_MODE_TABS.register("astech_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DEEZ_NUTS_ITEM.get()))
                    .title(Component.translatable("creativetab.astech_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DEEZ_NUTS_ITEM.get());
                        output.accept(ModItems.DEEZ_BUTTS_ITEM.get());
                        output.accept(ModBlocks.NIC_BLOCK.get());
                    }).build());
}
