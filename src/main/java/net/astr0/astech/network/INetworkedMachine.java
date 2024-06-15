package net.astr0.astech.network;

public interface INetworkedMachine {

    void updateServer(FlexiPacket msg);
    void updateClient(FlexiPacket msg);

    // This should only be allowed on server
    void SetNetworkDirty();
    boolean IsNetworkDirty();
}
