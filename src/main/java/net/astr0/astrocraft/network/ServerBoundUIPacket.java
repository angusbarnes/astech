package net.astr0.astrocraft.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundUIPacket {

    private BlockPos pos;

    public ServerBoundUIPacket() {

    }

    public static void handle(ServerBoundUIPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            var player = ctx.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(msg.pos);

                // Not sure if this is the most suitable method for determining chunk coord
                if(level.hasChunk(player.chunkPosition().x, player.chunkPosition().z)) {

                    // NOTE: reduce nesting
                    if(tile instanceof INetworkedMachine machine) {
                        //machine.updateServer(msg);
                    }
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public static ServerBoundUIPacket decode(FriendlyByteBuf buffer) {
        return null;
    }

}