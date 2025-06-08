package net.astr0.astech.network;

import net.astr0.astech.IConfigurable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigUpdatePacketHandler {

    public static void handlePacket(ConfigUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            var player = Minecraft.getInstance().player;

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(msg.getPos());

                // Not sure if this is the most suitable method for determining chunk coord
                if(level.hasChunk(player.chunkPosition().x, player.chunkPosition().z)) {

                    // NOTE: reduce nesting
                    if(tile instanceof IConfigurable machine) {
                        //machine.updateConfig(msg);
                    }
                }
            }

        });


    }
}
