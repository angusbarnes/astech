package net.astr0.astrocraft.network;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientToServerStateUpdatePacket {
    private final BlockPos pos;
    private final int stateIndex;
    private final FriendlyByteBuf data;

    public ClientToServerStateUpdatePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.stateIndex = buf.readVarInt();
        int readable = buf.readableBytes();
        this.data = new FriendlyByteBuf(Unpooled.buffer(readable));
        this.data.writeBytes(buf.readBytes(readable));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeVarInt(stateIndex);
        buf.writeBytes(data.copy());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerLevel level = (ServerLevel) context.get().getSender().level();
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof IHasStateManager stateManagedBE) {
                stateManagedBE.getStateManager().receiveClientUpdateOnServer(pos, stateIndex, data);
            }
        });
        context.get().setPacketHandled(true);
    }
}

