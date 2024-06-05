package net.astr0.astech.network;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundFlexiPacket {

    private BlockPos pos;
    private FriendlyByteBuf internalBuffer;

    public ClientBoundFlexiPacket(BlockPos position) {
        pos = position;
        internalBuffer = new FriendlyByteBuf(Unpooled.buffer());
    }

    public ClientBoundFlexiPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        int bufferSize = buffer.readableBytes(); // Get the size of the remaining bytes in the buffer
        this.internalBuffer = new FriendlyByteBuf(buffer.readBytes(bufferSize)); // Initialize and read the remaining bytes into the internal buffer
    }

    public BlockPos getPos() {
        return pos;
    }

    public FriendlyByteBuf getInternalBuffer() {
        return internalBuffer;
    }

    public void writeFluidStack(FluidStack fluid) {
        internalBuffer.writeFluidStack(fluid);
    }

    public void writeInt(int i) {
        internalBuffer.writeInt(i);
    }

    public FluidStack readFluidStack() {
        return internalBuffer.readFluidStack();
    }

    public static void handle(ClientBoundFlexiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FlexiPacketHandler.handlePacket(msg, ctx));

        });

        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBytes(internalBuffer.copy());
    }

}
