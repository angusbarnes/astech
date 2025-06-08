package net.astr0.astech.network;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ServerToClientStateSyncPacket {
    private final BlockPos pos;
    private final List<StateUpdate> updates;

    // Internal record class to hold update data
    public static class StateUpdate {
        final int index;
        final FriendlyByteBuf data;

        public StateUpdate(int index, FriendlyByteBuf data) {
            this.index = index;
            this.data = data;
        }
    }

    public ServerToClientStateSyncPacket(BlockPos pos, List<StateUpdate> updates) {
        this.pos = pos;
        this.updates = updates;
    }

    public ServerToClientStateSyncPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        int count = buf.readVarInt();
        this.updates = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            int index = buf.readVarInt();
            int dataLength = buf.readVarInt(); // read the length we wrote earlier
            FriendlyByteBuf stateData = new FriendlyByteBuf(Unpooled.buffer(dataLength));
            buf.readBytes(stateData, dataLength);
            updates.add(new StateUpdate(index, stateData));
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public List<StateUpdate> getUpdates() {
        return updates;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeVarInt(updates.size());
        for (StateUpdate update : updates) {
            buf.writeVarInt(update.index);
            byte[] data = update.data.copy().array(); // extract full buffer content
            buf.writeVarInt(data.length);             // write length first
            buf.writeBytes(data);                     // then write bytes
        }
    }

    public static void handle(ServerToClientStateSyncPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {

            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)  {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ServerToClientStateSyncPacketHandler.handlePacket(msg, context));
            }

        });
        context.get().setPacketHandled(true);
    }
}


