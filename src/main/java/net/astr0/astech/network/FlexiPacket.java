package net.astr0.astech.network;

import io.netty.buffer.Unpooled;
import net.astr0.astech.block.SidedConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlexiPacket {

    private BlockPos pos;
    private FriendlyByteBuf internalBuffer;

    public FlexiPacket(BlockPos position) {
        pos = position;
        internalBuffer = new FriendlyByteBuf(Unpooled.buffer());
    }

    public FlexiPacket(BlockPos position, FriendlyByteBuf buffer) {
        pos = position;
        internalBuffer = buffer;
    }

    public FlexiPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        int bufferSize = buffer.readableBytes(); // Get the size of the remaining bytes in the buffer
        this.internalBuffer = new FriendlyByteBuf(buffer.readBytes(bufferSize)); // Initialize and read the remaining bytes into the internal buffer
    }

    public FlexiPacket Copy() {
        return new FlexiPacket(pos, internalBuffer);
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

    public void writeSidedConfig(SidedConfig sidedConfig) {
        writeInt(sidedConfig.getCap(Direction.NORTH));
        writeInt(sidedConfig.getCap(Direction.SOUTH));
        writeInt(sidedConfig.getCap(Direction.EAST));
        writeInt(sidedConfig.getCap(Direction.WEST));
        writeInt(sidedConfig.getCap(Direction.UP));
        writeInt(sidedConfig.getCap(Direction.DOWN));
    }

    public void readSidedConfig(SidedConfig sidedConfig) {
        sidedConfig.setNoUpdate(Direction.NORTH, readInt());
        sidedConfig.setNoUpdate(Direction.SOUTH, readInt());
        sidedConfig.setNoUpdate(Direction.EAST, readInt());
        sidedConfig.setNoUpdate(Direction.WEST, readInt());
        sidedConfig.setNoUpdate(Direction.UP, readInt());
        sidedConfig.setNoUpdate(Direction.DOWN, readInt());
    }

    public FluidStack readFluidStack() {
        return internalBuffer.readFluidStack();
    }

    public static void handle(FlexiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)  {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FlexiPacketHandler.handlePacket(msg, ctx));
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
        buffer.writeBytes(internalBuffer.copy());
    }

}
