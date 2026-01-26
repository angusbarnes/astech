package net.astr0.astrocraft.gui.tooltips;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "yourmodid", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientTooltips {
    @SubscribeEvent
    public static void registerComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HazardTooltipComponent.class, ClientHazardTooltipComponent::new);
    }
}
