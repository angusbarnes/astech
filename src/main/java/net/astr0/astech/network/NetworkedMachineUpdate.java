package net.astr0.astech.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkedMachineUpdate {

    // A networked machine should always have a valid position
    // We should only send machine updates to those who are tracking the chunk
    // The update may be variable length and must be interpreted by each machine
    // as per their requirements. This is bidirectional, so care should be taken with it
    // On the server, we must guard against chunk loading (only accept messages for tiles
    // which are already loaded) and on the client we will only recieve a message if we
    // are tracking the affected chunks. The update tag system should still be used to handle
    // block saves and 'true' block updates (Rotation, placement, deletion) and this is all
    // handled by forge as long as the correct methods are in place (getUpdateTag). We will, however,
    // be able to replace the forced block updates on every change with simple network packet ping
    public NetworkedMachineUpdate(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf msg) {

        msg.writeBlockPos(pos);

    }

    public static NetworkedMachineUpdate decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        return new NetworkedMachineUpdate(pos);
    }

    public static void handle(NetworkedMachineUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            var player = ctx.get().getSender();

            if (player != null) {
                var level = player.getCommandSenderWorld();
                var tile = level.getBlockEntity(msg.pos);

                // Not sure if this is the most suitable method for determining chunk coord
                if(level.hasChunk(player.chunkPosition().x, player.chunkPosition().z)) {

                    // NOTE: reduce nesting
                    if(tile instanceof INetworkedMachine machine) {
                        machine.updateServer(msg);
                    }
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }

    private BlockPos pos;

    public NetworkedMachineUpdate(BlockPos pBlockPos) {
        pos = pBlockPos;
    }
}
