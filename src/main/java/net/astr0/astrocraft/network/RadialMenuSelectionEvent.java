package net.astr0.astrocraft.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/**
 * Forge event fired on the SERVER MinecraftForge.EVENT_BUS when a player
 * confirms a radial menu selection.
 *
 * Subscribe to this event in your item or capability handler:
 *
 * <pre>{@code
 * @SubscribeEvent
 * public void onRadialSelect(RadialMenuSelectionEvent event) {
 *     if (!event.getMenuId().equals("mytool:mode_select")) return;
 *     // apply mode to player's held item / capability
 * }
 * }</pre>
 */
public class RadialMenuSelectionEvent extends Event {

    private final ServerPlayer player;
    private final String menuId;
    private final String entryId;

    public RadialMenuSelectionEvent(ServerPlayer player, String menuId, String entryId) {
        this.player  = player;
        this.menuId  = menuId;
        this.entryId = entryId;
    }

    /** The player who made the selection. */
    public ServerPlayer getPlayer() { return player; }

    /** ID of the radial menu (e.g. {@code "mytool:mode_select"}). */
    public String getMenuId()  { return menuId;  }

    /** ID of the chosen entry (matches {@link com.example.radialmenu.common.menu.RadialMenuEntry#getId()}). */
    public String getEntryId() { return entryId; }
}
