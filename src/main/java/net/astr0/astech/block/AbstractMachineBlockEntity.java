package net.astr0.astech.block;

import net.astr0.astech.SoundRegistry;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.astr0.astech.network.INetworkedMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider, ITickableBlockEntity, INetworkedMachine {
    public AbstractMachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }


    protected void updateActiveState(boolean activeState) {

        // We only change the block state if there was an actual change
        if(activeState != isActive) {
            isActive = activeState;

            if (getBlockState().hasProperty(ModBlocks.BLOCKSTATE_ACTIVE)) {
                if(level != null) {
                    level.setBlock(worldPosition, getBlockState().setValue(ModBlocks.BLOCKSTATE_ACTIVE, isActive), 2 | 8 | 16);
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
            FlexiPacket update = new FlexiPacket(this.getBlockPos(), 69);
            SERVER_WriteUpdateToFlexiPacket(update);
            AsTechNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::getLevelChunk), update);
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

    @Override
    abstract public void updateServer(FlexiPacket msg);

    @Override
    abstract public void updateClient(FlexiPacket msg);

    private boolean _isNetworkDirty = false;
    private int _networkTickCount = 0;

    protected void IncrementNetworkTickCount() {
        _networkTickCount++;
    }

    protected void ResetNetworkTick() {
        _networkTickCount = 0;
        _isNetworkDirty = false;
    }

    protected boolean IsNetworkUpdateDue() {
        return _isNetworkDirty && _networkTickCount > 2;
    }

    @Override
    public void SetNetworkDirty() {
        _isNetworkDirty = true;
    }

    @Override
    public boolean IsNetworkDirty() {
        return _isNetworkDirty;
    }

    protected void TriggerBlockUpdate() {
        if(level!= null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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

    abstract public int[][] getCapTypes();

    abstract public SidedConfig getSidedConfig(int mode);

    protected abstract void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet);
    protected abstract void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet);
}
