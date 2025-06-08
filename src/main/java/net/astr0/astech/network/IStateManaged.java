package net.astr0.astech.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface IStateManaged {
    // State name for NBT serialization
    String getStateName();

    // Serialization to NBT
    CompoundTag writeToTag();
    void loadFromTag(CompoundTag tag);

    // Networking
    boolean isNetworkDirty(); // used for automatic server->client
    void writeNetworkEncoding(FriendlyByteBuf buf);
    void readNetworkEncoding(FriendlyByteBuf buf); // for server->client sync

    // For client→server update flow
    void writeClientUpdate(FriendlyByteBuf buf);
    void applyClientUpdate(FriendlyByteBuf buf);
}
