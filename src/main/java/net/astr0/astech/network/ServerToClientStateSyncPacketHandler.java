package net.astr0.astech.network;

import net.astr0.astech.BlockEntityStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerToClientStateSyncPacketHandler {

    public static void handlePacket(ServerToClientStateSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            if (level == null) return;

            BlockEntity be = level.getBlockEntity(msg.getPos());
            if (be instanceof IHasStateManager stateBE) {
                BlockEntityStateManager manager = stateBE.getStateManager();

                for (ServerToClientStateSyncPacket.StateUpdate update : msg.getUpdates()) {
                    if (update.index >= 0 && update.index < manager.getManagedStateCount()) {
                        manager.UpdateStateOnClient(update.index, update.data);
                    }
                }
            }

        });


    }
}
