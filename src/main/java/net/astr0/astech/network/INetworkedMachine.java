package net.astr0.astech.network;

public interface INetworkedMachine {

    public void updateServer(FlexiPacket msg);
    public void updateClient(FlexiPacket msg);

    // This should only be allowed on server
    public void SetNetworkDirty();
    public boolean IsNetworkDirty();
}
