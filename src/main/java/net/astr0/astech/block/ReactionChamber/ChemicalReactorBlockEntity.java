package net.astr0.astech.block.ReactionChamber;

import com.mojang.logging.LogUtils;
import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.DirectionTranslator;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.astr0.astech.recipe.AssemblerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChemicalReactorBlockEntity extends AbstractMachineBlockEntity {


    protected final SidedConfig sidedItemConfig = new SidedConfig() {
        @Override
        protected void onContentsChanged() {
            CLIENT_sideConfigUpdated(0);
        }
    };
    protected final SidedConfig sidedFluidConfig = new SidedConfig() {
        @Override
        protected void onContentsChanged() {
            CLIENT_sideConfigUpdated(1);
        }
    };

    private void CLIENT_sideConfigUpdated(int i) {

        if(level != null && !level.isClientSide()) {
            throw new IllegalStateException("This shit should not run on the server");
        }

        FlexiPacket packet = new FlexiPacket(this.getBlockPos(), 430);
        packet.writeInt(i);

        packet.writeSidedConfig(i == 0 ? sidedItemConfig : sidedFluidConfig);

        if(level != null && level.isClientSide()) {
            AsTechNetworkHandler.INSTANCE.sendToServer(packet);
        }
    }

    private final MachineFluidHandler inputFluidTank = new MachineFluidHandler(2,10000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            SetNetworkDirty();

        }
    };

    private final MachineFluidHandler outputFluidTank = new MachineFluidHandler(2,20000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            SetNetworkDirty();
        }
    };

    public FluidTank getOutputTank1() {
        return outputFluidTank.getTank(0);
    }

    public FluidTank getOutputTank2() {
        return outputFluidTank.getTank(1);
    }

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(3000000, 7500, 0);

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);
    private final LazyOptional<IFluidHandler> lazyOuputFluidHandler = LazyOptional.of(() -> outputFluidTank);
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public ChemicalReactorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHEMICAL_REACTOR_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> ChemicalReactorBlockEntity.this.progress;
                    case 3 -> ChemicalReactorBlockEntity.this.maxProgress;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> ChemicalReactorBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> ChemicalReactorBlockEntity.this.progress = pValue;
                    case 3 -> ChemicalReactorBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        sidedItemConfig.setCap(Direction.UP, SidedConfig.ITEM_INPUT);
        sidedItemConfig.setCap(Direction.EAST, SidedConfig.ITEM_INPUT);
        sidedItemConfig.setCap(Direction.DOWN, SidedConfig.ITEM_OUTPUT);
        sidedItemConfig.setCap(Direction.WEST, SidedConfig.ITEM_OUTPUT);

        sidedFluidConfig.setCap(Direction.UP, SidedConfig.FLUID_INPUT);
        sidedFluidConfig.setCap(Direction.EAST, SidedConfig.FLUID_INPUT);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (side == null) {
            return super.getCapability(cap, null);
        }

        side = DirectionTranslator.translate(side, this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return getFluidCapability(side);
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side); // Allow previous caps in the stack to be checked
    }

    public <T> LazyOptional<T> getFluidCapability(Direction side) {
        int type = sidedFluidConfig.getCap(side);
        return switch (type) {
            case SidedConfig.FLUID_INPUT -> lazyInputFluidHandler.cast();
            case SidedConfig.FLUID_OUTPUT -> lazyOuputFluidHandler.cast();
            default -> LazyOptional.empty();
        };
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyOuputFluidHandler.invalidate();
    }

    // User defined helper to get a list of all the items we are holding,
    // this is used to drop those items when this block is destroyed
    public void drops() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Chemical Reaction Chamber");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChemicalReactorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public int[][] getCapTypes() {
        return new int[][] {
                {SidedConfig.NONE, SidedConfig.ITEM_INPUT, SidedConfig.ITEM_OUTPUT},
                {SidedConfig.NONE, SidedConfig.FLUID_INPUT, SidedConfig.FLUID_OUTPUT_ONE, SidedConfig.FLUID_OUTPUT_TWO},
        };
    }

    @Override
    public SidedConfig getSidedConfig(int mode) {
        if(mode == 0) {
            return sidedItemConfig;
        } else {
            return sidedFluidConfig;
        }
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("assembler.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("fluidTank", inputFluidTank.writeToNBT(new CompoundTag()));
        pTag.put("outputFluidTank", outputFluidTank.writeToNBT(new CompoundTag()));
        pTag.put("itemConfig", sidedItemConfig.writeToNBT(new CompoundTag()));
        pTag.put("fluidConfig", sidedFluidConfig.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        progress = pTag.getInt("assembler.progress");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        inputFluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        outputFluidTank.readFromNBT(pTag.getCompound("outputFluidTank"));
        sidedItemConfig.readFromNBT(pTag.getCompound("itemConfig"));
        sidedFluidConfig.readFromNBT(pTag.getCompound("fluidConfig"));
    }

    private void ConsumePower(int amount) {
        this.energyStorage.removeEnergy(amount);
        setChanged();
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(hasRecipe()) {
            pLevel.setBlock(pPos, pState.setValue(ChemicalReactorBlock.ACTIVE, true), 2 | 8);
            if(this.energyStorage.getEnergyStored() < 256) {
                decreaseCraftingProgress();
            } else {
                increaseCraftingProgress();
                ConsumePower(256);
            }

            // every time we change some shit, call setChanged
            // This set changes ensures this block is queried for changes when the levekChunk is saved to disk
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            pLevel.setBlock(pPos, pState.setValue(ChemicalReactorBlock.ACTIVE, false), 2 | 8);
        }

        IncrementNetworkTickCount();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        AssemblerRecipe recipe = getRecipe();

        if(recipe == null) return;


        int tank0consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(0));

        inputFluidTank.getTank(0).drain(tank0consumed, IFluidHandler.FluidAction.EXECUTE);


        SetNetworkDirty(); // Make sure we mark this block for an update now that we changed inventory content
    }

    private boolean hasRecipe() {


        AssemblerRecipe recipe = getRecipe();

        if(recipe == null) {
            return false;
        }

        ItemStack result = recipe.getOutputItem();

        return canInsertAmountIntoOutputSlot(result.getCount())
                && canInsertItemIntoOutputSlot(result.getItem());
    }

    private AssemblerRecipe cachedRecipe = null;
    private AssemblerRecipe getRecipe() {

        return null;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return false;
    }


    private boolean canInsertAmountIntoOutputSlot(int count) {
        return false;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void decreaseCraftingProgress() {
        progress--;

        if(progress < 0) progress = 0;
    }

    public MachineFluidHandler getInputFluidHandler() {
        return inputFluidTank;
    }

    @Override
    public void updateServer(FlexiPacket msg) {

        int code = msg.GetCode();

        // Throw if we don't have loaded level on server
        if(!(this.level != null && !this.level.isClientSide())) {
            throw new IllegalStateException("updateServer() should never run on the client! The level is either null or client side.");
        }

        // This is the filter update code
        if (code == 430) {
            int _mode = msg.readInt();

            // Handle sided update
            if(_mode == 0) {
                msg.readSidedConfig(sidedItemConfig);
            } else {
                msg.readSidedConfig(sidedFluidConfig);
            }

            // Rebroadcast update to tracking clients
            AsTechNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::getLevelChunk), msg);
        }

        if (code == 36) {
            // Get the toggled slot lock
            int slot = msg.readInt();
            FlexiPacket packet = new FlexiPacket(this.getBlockPos(), 36);
            // Rebroadcast the change to listening clients
            AsTechNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::getLevelChunk), packet);
        } else if (code == 37) {
            // Get the toggled slot lock
            int slot = msg.readInt();

            inputFluidTank.ToggleSlotLock(slot);

            FlexiPacket packet = new FlexiPacket(this.getBlockPos(), 37);
            inputFluidTank.WriteToFlexiPacket(packet);
            // Rebroadcast the change to listening clients
            AsTechNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::getLevelChunk), packet);
        } else if (code == 38) {
            // Get the toggled slot lock
            int slot = msg.readInt();

            inputFluidTank.getTank(slot).setFluid(FluidStack.EMPTY);
            SetNetworkDirty(); // Make sure the fluid changes is synced on next update
        }

        // A server update should always mark this block as dirty for saves
        setChanged();
    }

    @Override
    public void updateClient(FlexiPacket msg) {
        if(this.level !=null && !this.level.isClientSide()) {
            throw new IllegalStateException("updateClient() should never run on the server! The level is either null or server side.");
        }

        int code = msg.GetCode();

        // This is the filter update code
        if (code == 430) {
            int _mode = msg.readInt();

            // Handle sided update
            if(_mode == 0) {
                msg.readSidedConfig(sidedItemConfig);
            } else {
                msg.readSidedConfig(sidedFluidConfig);
            }
        } else if (code == 69) {
            CLIENT_ReadUpdateFromFlexiPacket(msg);
        } else if (code == 36) {
            LogUtils.getLogger().info("There was an update to slot locks on the client");
        } else if (code == 37) {
            LogUtils.getLogger().info("There was an update to slot locks on the client");
            inputFluidTank.ReadFromFlexiPacket(msg);
        }
    }

    @Override
    protected void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet) {
        packet.writeFluidStack(inputFluidTank.getFluidInTank(0));
        packet.writeFluidStack(inputFluidTank.getFluidInTank(1));
    }

    @Override
    protected void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet) {
        inputFluidTank.getTank(0).setFluid(packet.readFluidStack());
        inputFluidTank.getTank(1).setFluid(packet.readFluidStack());
    }
}
