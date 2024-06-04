package net.astr0.astech.network;

public interface INetworkedMachine {

    public void updateServer(NetworkedMachineUpdate msg);
    public void updateClient(NetworkedMachineUpdate msg);

    // This should only be allowed on server
    public void SetNetworkDirty();
    public boolean IsNetworkDirty();
}
