package net.astr0.astrocraft;

import com.mojang.logging.LogUtils;
import io.netty.buffer.Unpooled;
import net.astr0.astrocraft.network.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class BlockEntityStateManager {

    private final BlockEntity _blockEntity;
    private final BlockPos pos;

    private final List<IStateManaged> stateList;
    private final Map<String, IStateManaged> stateMap = new HashMap<>();

    public BlockEntityStateManager(BlockEntity blockEntity, int stateCountHint) {
        _blockEntity = blockEntity;
        pos = blockEntity.getBlockPos();
        stateList = new ArrayList<>(stateCountHint);
    }

    public BlockEntityStateManager(BlockEntity blockEntity) {
        this(blockEntity, 5);
    }

    public int getManagedStateCount() {
        return stateList.size();
    }

    public IStateManaged getManagedState(String name) {
        return stateMap.get(name);
    }

    public void sendClientUpdateByName(String name) {
        IStateManaged state = stateMap.get(name);
        if (state != null) {
            int index = stateList.indexOf(state);
            if (index >= 0) sendClientUpdate(index);
        }

        LogUtils.getLogger().info("Client update was sent for: {}", name);
    }

    public void sendClientUpdate(int index) {
        if (!_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("Client updates should only be sent from the client side");
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(_blockEntity.getBlockPos());
        buf.writeVarInt(index);

        stateList.get(index).writeClientUpdate(buf);
        AsTechNetworkHandler.INSTANCE.sendToServer(new ClientToServerStateUpdatePacket(buf));
    }

    public void receiveClientUpdateOnServer(BlockPos pos, int index, FriendlyByteBuf buf) {
        if (_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("Received a client packet on client? That's not right.");
        }

        if (index < 0 || index >= stateList.size()) {
            LogUtils.getLogger().warn("Received invalid index {} for block entity at {}", index, pos);
            return;
        }

        stateList.get(index).applyClientUpdate(buf);
        _blockEntity.setChanged();
    }

    public void UpdateStateOnClient(int index, FriendlyByteBuf stateData) {
        stateList.get(index).readNetworkEncoding(stateData);
    }

    public void PerformNetworkServerSynchronisation() {
        if (_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("This should only run on the server");
        }

        List<ServerToClientStateSyncPacket.StateUpdate> dirtyUpdates = new ArrayList<>();

        for (int i = 0; i < stateList.size(); i++) {
            IStateManaged state = stateList.get(i);
            if (state.isNetworkDirty()) {
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
        if (!_blockEntity.getLevel().isClientSide()) {
            throw new IllegalStateException("This shit should never be running on the server side. Please check your code");
        }
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        for (IStateManaged state : stateList) {
            tag.put(state.getStateName(), state.writeToTag());
        }
        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        for (IStateManaged state : stateList) {
            String name = state.getStateName();
            if (tag.contains(name)) {
                state.loadFromTag(tag.getCompound(name));
            } else {
                LogUtils.getLogger().warn("Unable to load NBT data for block entity {} tag={}", _blockEntity.getBlockPos().toShortString(), name);
            }
        }
    }

    public <T extends IStateManaged> T addManagedState(T state) {
        String name = state.getStateName();
        if (stateMap.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate state name '" + name + "' not allowed in " + _blockEntity.getBlockPos());
        }

        stateList.add(state);
        stateMap.put(name, state);
        return state;
    }
}

