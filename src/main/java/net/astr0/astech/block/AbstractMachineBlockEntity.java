package net.astr0.astech.block;

import net.astr0.astech.network.ClientBoundFlexiPacket;
import net.astr0.astech.network.INetworkedMachine;
import net.astr0.astech.network.NetworkedMachineUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider, ITickableBlockEntity, INetworkedMachine {
    public AbstractMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

    }

    @Override
    public void updateServer(NetworkedMachineUpdate msg) {

    }

    @Override
    public void updateClient(ClientBoundFlexiPacket msg) {

    }

    @Override
    public void SetNetworkDirty() {

    }

    @Override
    public boolean IsNetworkDirty() {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    abstract public int[] getCapTypes();
}
