package net.astr0.astech.network;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlexiPacket {

    private final BlockPos pos;
    private final int code;
    private final FriendlyByteBuf internalBuffer;

    public FlexiPacket(BlockPos position, int message_code) {
        pos = position;
        internalBuffer = new FriendlyByteBuf(Unpooled.buffer());
        code = message_code;
    }

    public FlexiPacket(BlockPos position, int message_code, FriendlyByteBuf buffer) {
        pos = position;
        code = message_code;
        internalBuffer = buffer;
    }

    public FlexiPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.code = buffer.readInt();
        int bufferSize = buffer.readableBytes(); // Get the size of the remaining bytes in the buffer
        this.internalBuffer = new FriendlyByteBuf(buffer.readBytes(bufferSize)); // Initialize and read the remaining bytes into the internal buffer
    }

    public int GetCode() {
        return code;
    }

    public FlexiPacket Copy() {
        return new FlexiPacket(pos, code, (FriendlyByteBuf) internalBuffer.copy());
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
    public int readInt() {
        return internalBuffer.readInt();
    }


    public FluidStack readFluidStack() {
        return internalBuffer.readFluidStack();
    }

    public static void handle(FlexiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)  {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientFlexiPacketHandler.handlePacket(msg, ctx));
            } else {
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
            }


        });

        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(code);
        buffer.writeBytes(internalBuffer.copy());
    }

    public void writeBool(boolean b) {
        internalBuffer.writeBoolean(b);
    }

    public boolean readBool() {
        return internalBuffer.readBoolean();
    }

    public void writeItemStack(ItemStack stack) {
        internalBuffer.writeItemStack(stack, false);
    }

    public ItemStack readItemStack() {
        return internalBuffer.readItem();
    }
}
