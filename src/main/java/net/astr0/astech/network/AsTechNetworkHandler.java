package net.astr0.astech.network;

import net.astr0.astech.AsTech;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class AsTechNetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AsTech.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void onCommonSetup() {
        INSTANCE.registerMessage(0, NetworkedMachineUpdate.class, NetworkedMachineUpdate::encode, NetworkedMachineUpdate::decode, NetworkedMachineUpdate::handle);
    }

}
