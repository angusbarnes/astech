package net.astr0.astrocraft.network;

import net.minecraft.server.level.ServerPlayer;

public interface IClientActionHandler {
    void handleClientAction(ClientActionPacket packet, ServerPlayer player);
}
