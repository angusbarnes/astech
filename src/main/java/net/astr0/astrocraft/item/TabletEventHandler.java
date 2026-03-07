package net.astr0.astrocraft.item;

import net.astr0.astrocraft.Astrocraft;
import net.astr0.astrocraft.network.RadialMenuSelectionEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TabletEventHandler {

    public static void onTabletModeToggle(RadialMenuSelectionEvent event) {
        if (!event.getMenuId().equals("astrocraft:tablet_modes")) return;

        ServerPlayer player = event.getPlayer();
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof TabletItem)) return;

        boolean nowActive = TabletModes.toggle(held, event.getEntryId());
        Astrocraft.LOGGER.info("Tablet mode toggled: " + nowActive);
        Astrocraft.LOGGER.info("Side: " + (player.level().isClientSide ? "CLIENT" : "SERVER"));

        // Feedback message
        String modeName = switch (event.getEntryId()) {
            case TabletModes.TRAVEL -> "Travel";
            case TabletModes.CREATE -> "Create Wrench";
            case TabletModes.MEK    -> "Mekanism Config";
            default -> event.getEntryId();
        };
        player.displayClientMessage(
                Component.literal(nowActive ? "§a✔ " : "§c✘ ").append(modeName),
                true // hotbar message
        );
    }
}
