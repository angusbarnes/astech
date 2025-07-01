package net.astr0.astech.network;

import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrainFluidPacket extends ClientActionPacket {
    public final BlockPos blockPos;
    public final String tankName;
    public final int index;
    public DrainFluidPacket(BlockPos pos, String name, int index) {
        this.blockPos = pos;
        this.index = index;
        this.tankName = name;
    }

    public DrainFluidPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.tankName = buf.readUtf();
        this.index = buf.readVarInt();

    }

    public static void encode(DrainFluidPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeUtf(msg.tankName);
        buf.writeVarInt(msg.index);
    }

    public static DrainFluidPacket decode(FriendlyByteBuf buf) {
        return new DrainFluidPacket(buf.readBlockPos(), buf.readUtf(), buf.readVarInt());
    }

    public static void handle(DrainFluidPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (!(level.getBlockEntity(msg.blockPos) instanceof AbstractMachineBlockEntity be)) return;

            be.handleClientAction(msg, player);


        });
        ctx.get().setPacketHandled(true);
    }
}

