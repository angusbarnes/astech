package net.astr0.astrocraft.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UIFluidActionPacket extends ClientActionPacket {
    public final BlockPos blockPos;
    public final String tankName;
    public final int index;
    public final FluidAction action;
    public UIFluidActionPacket(BlockPos pos, String name, int index, FluidAction action) {
        this.blockPos = pos;
        this.index = index;
        this.tankName = name;
        this.action = action;
    }

    public UIFluidActionPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.tankName = buf.readUtf();
        this.index = buf.readVarInt();
        this.action = buf.readEnum(FluidAction.class);

    }

    public static void encode(UIFluidActionPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeUtf(msg.tankName);
        buf.writeVarInt(msg.index);
        buf.writeEnum(msg.action);
    }

    public static UIFluidActionPacket decode(FriendlyByteBuf buf) {
        return new UIFluidActionPacket(buf.readBlockPos(), buf.readUtf(), buf.readVarInt(), buf.readEnum(FluidAction.class));
    }

    public static void handle(UIFluidActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (!(level.getBlockEntity(msg.blockPos) instanceof IClientActionHandler be)) return;
            be.handleClientAction(msg, player);
        });
        ctx.get().setPacketHandled(true);
    }

    public enum FluidAction {
        DRAIN_ITEM,
        DUMP_SLOT,
        FILL_ITEM
    }
}

