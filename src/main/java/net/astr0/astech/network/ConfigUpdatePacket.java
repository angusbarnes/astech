package net.astr0.astech.network;

import net.astr0.astech.IConfigurable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigUpdatePacket {
    private final BlockPos pos;

    public ConfigUpdatePacket(BlockPos position, int message_code, FriendlyByteBuf buffer) {
        pos = position;
    }

    public ConfigUpdatePacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
    }

    public BlockPos getPos() {
        return pos;
    }

    public static void handle(ConfigUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)  {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ConfigUpdatePacketHandler.handlePacket(msg, ctx));
            } else {
                var player = ctx.get().getSender();

                if (player != null) {
                    var level = player.getCommandSenderWorld();
                    var tile = level.getBlockEntity(msg.pos);

                    // Not sure if this is the most suitable method for determining chunk coord
                    if(level.hasChunk(player.chunkPosition().x, player.chunkPosition().z)) {

                        // NOTE: reduce nesting
                        if(tile instanceof IConfigurable configurableMachine) {
                            //configurableMachine.updateConfig(msg);
                        }
                    }
                }
            }


        });

        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }
}
