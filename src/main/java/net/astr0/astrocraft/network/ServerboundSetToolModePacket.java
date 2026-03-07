package net.astr0.astrocraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from client → server when the player releases the radial menu key
 * and confirms a selection.
 *
 * Contains:
 *  - menuId  : which radial menu was open (e.g. "my_tool:mode_select")
 *  - entryId : the id of the chosen {@link com.example.radialmenu.common.menu.RadialMenuEntry}
 *
 * Your item / capability handler should listen for this packet and apply
 * the mode change on the server side.
 */
public class ServerboundSetToolModePacket {

    /** Registry name of this packet channel. */
    public static final ResourceLocation ID =
            new ResourceLocation("radialmenu", "set_tool_mode");

    private final String menuId;
    private final String entryId;

    public ServerboundSetToolModePacket(String menuId, String entryId) {
        this.menuId  = menuId;
        this.entryId = entryId;
    }

    // -------------------------------------------------------------------------
    // Codec
    // -------------------------------------------------------------------------

    public static ServerboundSetToolModePacket decode(FriendlyByteBuf buf) {
        String menuId  = buf.readUtf(64);
        String entryId = buf.readUtf(64);
        return new ServerboundSetToolModePacket(menuId, entryId);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(menuId,  64);
        buf.writeUtf(entryId, 64);
    }

    // -------------------------------------------------------------------------
    // Handler (runs on server thread)
    // -------------------------------------------------------------------------

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            // Fire a Forge event so your item/capability code can react
            // without coupling directly to this packet class.
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
                    new RadialMenuSelectionEvent(player, menuId, entryId));
        });
        ctx.setPacketHandled(true);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getMenuId()  { return menuId;  }
    public String getEntryId() { return entryId; }
}
