package net.astr0.astech;

import com.mojang.logging.LogUtils;
import io.netty.buffer.Unpooled;
import net.astr0.astech.network.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockEntityStateManager {

    private final BlockEntity _blockEntity;
    private final BlockPos pos;

    private final IStateManaged[] managedStates;
    private int stateIndex = 0;

    public BlockEntityStateManager(BlockEntity blockEntity, int state_count) {
        _blockEntity = blockEntity;
        pos = blockEntity.getBlockPos();

        managedStates = new IStateManaged[state_count];
    }


    public int getManagedStateCount() {
        return stateIndex;
    }

    // Warning that this uses state names which may be non-unique which may lead to undefined behaviour
    // You should only use this method if you feeling hella lazy
    public void sendClientUpdateByName(String name) {
        for (int i = 0; i < stateIndex; i++) {
            if (Objects.equals(managedStates[i].getStateName(), name)) {
                sendClientUpdate(i);
            }
        }
    }

    public void sendClientUpdate(int index) {
        if (!_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("Client updates should only be sent from the client side");
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(_blockEntity.getBlockPos());
        buf.writeVarInt(index);

        managedStates[index].writeClientUpdate(buf);

        AsTechNetworkHandler.INSTANCE.sendToServer(new ClientToServerStateUpdatePacket(buf));
    }

    public void receiveClientUpdateOnServer(BlockPos pos, int index, FriendlyByteBuf buf) {
        if (_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("Received a client packet on client? That's not right.");
        }

        if (index < 0 || index >= stateIndex) {
            LogUtils.getLogger().warn("Received invalid index {} for block entity at {}", index, pos);
            return;
        }

        managedStates[index].applyClientUpdate(buf);
        _blockEntity.setChanged(); // mark for saving
    }

    public void UpdateStateOnClient(int index, FriendlyByteBuf stateData) {
        managedStates[index].readNetworkEncoding(stateData);
    }

    // For network synchronisation, loop through all managed states and check if they
    // need synching. If they do, write the state along with the index to a friendlyByteBuf
    // This will mean only one single update packet needs to get sent, regardless of how many things
    // changed. This should only be used for server --> client synching
    // When changes are made on the client, they should be updated locally, then a specfic update
    // packet should be sent to the StateManager which will change server state accordingly.
    // This update will be automatically synchronised to all clients on the next network update event.
    // This means we can fuck with state however we want on the client but it will always be forced back
    // to the server side source of truth when a network update is triggered
    public void PerformNetworkServerSynchronisation() {
        if (_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("This should only run on the server");
        }
        //LogUtils.getLogger().info("Taking Network Synchronisation");

        // Create temporary storage for state updates
        List<ServerToClientStateSyncPacket.StateUpdate> dirtyUpdates = new ArrayList<>();

        for (int i = 0; i < stateIndex; i++) {
            IStateManaged state = managedStates[i];
            if (state.isNetworkDirty()) {

                //LogUtils.getLogger().info("{} was marked dirty. Synching over the network", state.getStateName());
                FriendlyByteBuf stateBuf = new FriendlyByteBuf(Unpooled.buffer());
                state.writeNetworkEncoding(stateBuf);
                dirtyUpdates.add(new ServerToClientStateSyncPacket.StateUpdate(i, stateBuf));
            }
        }

        if (!dirtyUpdates.isEmpty()) {
            ServerToClientStateSyncPacket packet = new ServerToClientStateSyncPacket(_blockEntity.getBlockPos(), dirtyUpdates);
            AsTechNetworkHandler.INSTANCE.send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> _blockEntity.getLevel().getChunkAt(_blockEntity.getBlockPos())),
                    packet
            );
        }
    }

    public void NetworkClientUpdateFromServer(ConfigUpdatePacket packet) {
        if(!(_blockEntity.getLevel().isClientSide())) {
            throw new IllegalStateException("This shit should never be running on the server side. Please check your code");
        }
    }


    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < stateIndex; i++) {
            tag.put(managedStates[i].getStateName(), managedStates[i].writeToTag());
        }

        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        for (int i = 0; i < stateIndex; i++) {
            if (tag.contains(managedStates[i].getStateName())) {
                managedStates[i].loadFromTag(tag.getCompound(managedStates[i].getStateName()));
            } else {
                LogUtils.getLogger().warn("Unable to load NBT data for block entity {} tag={}", _blockEntity.getBlockPos().toShortString(), managedStates[i].getStateName());
            }
        }
    }

    // Intended to be used like MyItemHandler handler = SM.addManagedState(new FilteredItemHandler());
    public <T extends IStateManaged> T addManagedState(T state) {
        managedStates[stateIndex] = state;
        stateIndex++;
        return state;
    }

}
