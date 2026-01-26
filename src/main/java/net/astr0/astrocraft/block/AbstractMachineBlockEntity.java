package net.astr0.astrocraft.block;

import net.astr0.astrocraft.BlockEntityStateManager;
import net.astr0.astrocraft.Fluid.MachineFluidHandler;
import net.astr0.astrocraft.SoundRegistry;
import net.astr0.astrocraft.network.ClientActionPacket;
import net.astr0.astrocraft.network.IClientActionHandler;
import net.astr0.astrocraft.network.UIFluidActionPacket;
import net.astr0.astrocraft.network.IHasStateManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider, ITickableBlockEntity, IHasStateManager, IClientActionHandler {
    public AbstractMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int managedStates) {
        super(pType, pPos, pBlockState);

        StateManager = new BlockEntityStateManager(this, managedStates);
    }

    protected final BlockEntityStateManager StateManager;

    public BlockEntityStateManager getStateManager() {
        return StateManager;
    }


    protected void updateActiveState(boolean activeState) {

        // We only change the block state if there was an actual change
        if(activeState != isActive) {
            isActive = activeState;

            if (getBlockState().hasProperty(BlockEntityProperties.ACTIVE)) {
                if(level != null) {
                    level.setBlock(worldPosition, getBlockState().setValue(BlockEntityProperties.ACTIVE, isActive), 2 | 8 | 16);
                }
            }
        }
    }

    private boolean isActive = false;


    private int _internalTickCount = 0;
    private int _internalSecondCount = 0;

    // This logic is a userDefined name for a tick function
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // Only tick on the server, the server should still sync
        if(this.level == null || this.level.isClientSide())
            return;

        if(IsNetworkUpdateDue()) {
            //LogUtils.getLogger().info("Running network update");
            StateManager.PerformNetworkServerSynchronisation();
            ResetNetworkTick();
        }

        tickOnServer(pLevel, pPos, pState);

        if (_internalTickCount % 20 == 0) {
            _internalSecondCount++;
        }

        if (isActive && _internalSecondCount > soundPlaytime) {
            level.playSound(null, getBlockPos(), machineSound, SoundSource.BLOCKS);
            _internalSecondCount = 0;
        }

        _internalTickCount++;
    }

    private SoundEvent machineSound = SoundRegistry.generic_machine.get();
    protected void setSoundEvent(SoundEvent pSoundEvent) {
        machineSound = pSoundEvent;
    }

    private int soundPlaytime = 4;

    protected void setSoundPlaytime(int pSoundPlaytime) {
        soundPlaytime = pSoundPlaytime;
    }

    public LevelChunk getLevelChunk() {
        return this.level.getChunkAt(this.getBlockPos());
    }

    protected int getinternalTickCount() {
        return _internalTickCount;
    }

    // This only ticks on the server
    public abstract void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState);

    private int _networkTickCount = 0;

    protected void IncrementNetworkTickCount() {
        _networkTickCount++;
    }

    protected void ResetNetworkTick() {
        _networkTickCount = 0;
    }

    // 3 Ticks is a sync latency of 150 ms or roughly a 7 Hz rate
    protected boolean IsNetworkUpdateDue() {
        return _networkTickCount > 3;
    }


    protected void TriggerBlockUpdate() {
        if(level!= null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void handleClientAction(ClientActionPacket packet, ServerPlayer player) {

        if (level.isClientSide()) {
            throw new IllegalStateException("A client action was sent to another client. This is invalid.");
        }

        if (packet instanceof UIFluidActionPacket drainPacket) {

            if (drainPacket.action == UIFluidActionPacket.FluidAction.DRAIN_ITEM) {
                ItemStack carried = player.containerMenu.getCarried();
                if (carried.getCount() != 1) return;

                ItemStack copy = carried.copy();
                copy.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {

                    MachineFluidHandler tankHandler = (MachineFluidHandler) StateManager.getManagedState(drainPacket.tankName);
                    FluidTank tank = tankHandler.getTank(drainPacket.index);


                    int filled = tank.fill(handler.getFluidInTank(0), IFluidHandler.FluidAction.SIMULATE);

                    if (filled > 0) {
                        tank.fill(handler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
                        handler.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                        player.containerMenu.setCarried(handler.getContainer());
                        player.containerMenu.synchronizeCarriedToRemote();
                    }
                });
            } else if (drainPacket.action == UIFluidActionPacket.FluidAction.FILL_ITEM) {
                ItemStack carried = player.containerMenu.getCarried();
                if (carried.getCount() != 1) return;

                ItemStack copy = carried.copy();
                copy.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {

                    MachineFluidHandler tankHandler = (MachineFluidHandler) StateManager.getManagedState(drainPacket.tankName);
                    FluidTank tank = tankHandler.getTank(drainPacket.index);

                    int filled = handler.fill(tank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                    if (filled > 0) {
                        tank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        player.containerMenu.setCarried(handler.getContainer());
                        player.containerMenu.synchronizeCarriedToRemote();
                    }
                });
            } else if (drainPacket.action == UIFluidActionPacket.FluidAction.DUMP_SLOT) {
                MachineFluidHandler tankHandler = (MachineFluidHandler) StateManager.getManagedState(drainPacket.tankName);
                FluidTank tank = tankHandler.getTank(drainPacket.index);

                tank.setFluid(FluidStack.EMPTY);
                tankHandler.SetNetworkDirty();
            }

        }
    }

    // Called by our block update logic, which occurs when the inventory is updated
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {

        // This created a Packet to update clients, if left unspecified,
        // it searches 'this' for  getUpdateTag, which will save the current block tags
        // and send those. These are the block tags saved by saveAdditional()
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        //LogUtils.getLogger().info("A block update tag was requested");
        return saveWithoutMetadata();
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
}
