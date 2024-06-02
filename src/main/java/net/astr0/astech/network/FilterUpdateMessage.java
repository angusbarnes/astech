package net.astr0.astech.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FilterUpdateMessage {
    public FilterUpdateMessage() {}
    public FilterUpdateMessage(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf msg) {}

    public static FilterUpdateMessage decode(FriendlyByteBuf buffer) {
        return null;
    }

    public static void handle(FilterUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {

    }
}
