package net.astr0.astech.block.ChemicalMixer;

import com.mojang.logging.LogUtils;
import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.DirectionTranslator;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.astr0.astech.recipe.ChemicalMixerRecipe;
import net.astr0.astech.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Todo:
// - We need to prevent output handlers from being able to be input into
// - We need to tidy up side config stuff
// - Implement Recipe Type, Recipe Process and JEI Recipe Categories and transfer handlers
// - Clean up print statements
// - Enable fluid draining and filling from buckets in GUI
//   ---> this might involve some custom fluidSlot type bullshit. IDK how to do that
// - Somehow support AE2 style click and drag to set filters from JEI
// - add OK button to only update side config when pressed, or menu closed
// Data generators would be good to add too
// Add fluid texture variations
// Tie hazardous materials to actual underlying data
// Fix energy percentage synced by ContainerData
public class ChemicalMixerBlockEntity extends AbstractMachineBlockEntity {

    // ItemStackHandler is a naive implementation of IItemHandler which is a Forge Capability
    private final FilteredItemStackHandler inputItemHandler = new FilteredItemStackHandler(3) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

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

        LogUtils.getLogger().info("Side Config Updated");

        packet.writeSidedConfig(i == 0 ? sidedItemConfig : sidedFluidConfig);

        if(level != null && level.isClientSide()) {
            AsTechNetworkHandler.INSTANCE.sendToServer(packet);
        }
    }

    private final MachineFluidHandler inputFluidTank = new MachineFluidHandler(2,24000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            LogUtils.getLogger().warn("Contents of input fluid inventory updated");

            SetNetworkDirty();

        }
    };

    private final MachineFluidHandler outputFluidTank = new MachineFluidHandler(1,24000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            LogUtils.getLogger().warn("Contents of output fluid inventory updated");

            SetNetworkDirty();
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(300000, 750, 0);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 3;

    // This is the provider that allows networked data to be lazily updates
    // We must invalidate this every time a change occurs so the server re-syncs
    private final LazyOptional<IItemHandler> lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);
    private final LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);
    private final LazyOptional<IFluidHandler> lazyOutputFluidHandler = LazyOptional.of(() -> outputFluidTank);

    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public ChemicalMixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHEMICAL_MIXER_BE.get(), pPos, pBlockState);

        // Init our simple container data
        // this is synced for us every tick automatically,
        // this is limited to the signed short range
        // ItemHandlerSlot's in the menu screen sync the item stacks for us.
        // We should use this for basic, simply serializable data.
        // Energy level, and crafting progress for example
        // Fluid updates and setting changes should be reflected by sending custom packets
        // to the client. We could do this by sending a packet every X ticks (probably 1)
        // if we mark ourselves as having updated state. (OnContents changed from fluid for example)
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    // This will probably cause a bug, this can only sync signed shorts,
                    // but we may set the energy higher than the 32,000 limit.
                    // TODO: Turn this into a synced percentage and reconvert to relative amount on client side
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> ChemicalMixerBlockEntity.this.progress;
                    case 3 -> ChemicalMixerBlockEntity.this.maxProgress;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> ChemicalMixerBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> ChemicalMixerBlockEntity.this.progress = pValue;
                    case 3 -> ChemicalMixerBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        sidedItemConfig.setCap(Direction.UP, SidedConfig.ITEM_INPUT);
        sidedFluidConfig.setCap(Direction.EAST, SidedConfig.FLUID_INPUT);
    }

    // This function is user defined and is used to get an ItemStack to render
    // This only works if a rendered is used with this block
    public ItemStack getRenderStack() {
        if(inputItemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            return inputItemHandler.getStackInSlot(INPUT_SLOT);
        } else {
            return inputItemHandler.getStackInSlot(OUTPUT_SLOT);
        }
    }

    // Forge API which allows for queries related to our abilities,
    // The requesting party will call this function with their capability type.
    // It is our job to check if we have what they are looking for our not.
    // Don't get fancy, for performance reasons manual if-elses or switch statements are recommended
    // THERE ARE TWO OVERLOADS FOR THIS. THE ONE SHOWN BELOW IS THE FULL IMPLEMENTATION WHICH TAKE A DIRECTIONALITY
    // THIS CAN BE USED TO LINK SIDES TO CERTAIN ITEM OR FLUID HANDLERS
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (side == null) {
            return super.getCapability(cap, null);
        }

        side = DirectionTranslator.translate(side, this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));


        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return getItemCapability(side);
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return getFluidCapability(side);
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side); // Allow previous caps in the stack to be checked
    }

    public <T> LazyOptional<T> getFluidCapability(Direction side) {
        int type = sidedFluidConfig.getCap(side);
        LogUtils.getLogger().info("Fluid cap was requested for {} side {}", type, side);
        return switch (type) {
            case SidedConfig.FLUID_INPUT -> lazyInputFluidHandler.cast();
            case SidedConfig.FLUID_OUTPUT -> lazyOutputFluidHandler.cast();
            default -> LazyOptional.empty();
        };
    }

    public <T> LazyOptional<T> getItemCapability(Direction side) {
        int type = sidedItemConfig.getCap(side);

        return switch (type) {
            case SidedConfig.ITEM_INPUT -> lazyInputItemHandler.cast();
            case SidedConfig.ITEM_OUTPUT -> lazyOutputItemHandler.cast();
            default -> LazyOptional.empty();
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    // should be called when a change is made that should invalidate the cached caps of this block
    // Seems to be working with just setChanged calls, but should be done any time some kind of updates
    // are made to the BlockEntities caps, such as a change of config.
    // At the moment I'm not really sure who or where this gets called from
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInputItemHandler.invalidate();
        lazyInputFluidHandler.invalidate();
        lazyOutputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyEnergyHandler.invalidate();

        LogUtils.getLogger().info("Caps have been invalidated somehow");
    }

    // User defined helper to get a list of all the items we are holding,
    // this is used to drop those items when this block is destroyed
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(inputItemHandler.getSlots());
        for(int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Chemical Mixer");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChemicalMixerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public int[][] getCapTypes() {
        return new int[][] {
                {SidedConfig.NONE, SidedConfig.ITEM_INPUT, SidedConfig.ITEM_OUTPUT},
                {SidedConfig.NONE, SidedConfig.FLUID_INPUT, SidedConfig.FLUID_OUTPUT},
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
        pTag.put("inventory", inputItemHandler.serializeNBT());
        pTag.putInt("chemical_mixer.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("fluidTank", inputFluidTank.writeToNBT(new CompoundTag()));
        pTag.put("outputFluidTank", outputFluidTank.writeToNBT(new CompoundTag()));
        pTag.put("itemConfig", sidedItemConfig.writeToNBT(new CompoundTag()));
        pTag.put("fluidConfig", sidedFluidConfig.writeToNBT(new CompoundTag()));
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inputItemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("chemical_mixer.progress");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        inputFluidTank.readFromNBT(pTag.getCompound("fluidTank"));
        sidedItemConfig.readFromNBT(pTag.getCompound("itemConfig"));
        sidedFluidConfig.readFromNBT(pTag.getCompound("fluidConfig"));
        outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
        outputFluidTank.readFromNBT(pTag.getCompound("outputFluidTank"));
    }

    private void SipPower(int amount) {
        this.energyStorage.removeEnergy(amount);
        setChanged();
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(hasRecipe()) {
            if(this.energyStorage.getEnergyStored() < 256) {
                decreaseCraftingProgress();
            } else {
                increaseCraftingProgress();
                SipPower(256);
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
        }

        IncrementNetworkTickCount();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        ChemicalMixerRecipe recipe = getRecipe();

        if(recipe == null) return;

        LogUtils.getLogger().info("Crafting Item....");

        this.inputItemHandler.extractItem(0, 1, false);
        this.inputItemHandler.extractItem(1, 1, false);
        this.inputItemHandler.extractItem(2, 1, false);

        int tank0consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(0));
        int tank1consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(1));

        inputFluidTank.getTank(0).drain(tank0consumed, IFluidHandler.FluidAction.EXECUTE);
        inputFluidTank.getTank(1).drain(tank1consumed, IFluidHandler.FluidAction.EXECUTE);

        ItemStack result = recipe.getOutputItem();

        if(result != null && !result.isEmpty()) {
            this.outputItemHandler.setStackInSlot(0, new ItemStack(result.getItem(),
                    this.outputItemHandler.getStackInSlot(0).getCount() + result.getCount()));
        }
    }

    private boolean hasRecipe() {
        ChemicalMixerRecipe recipe = getRecipe();

        if(recipe == null) {
            return false;
        }
        ItemStack result = recipe.getOutputItem();

        return canInsertAmountIntoOutputSlot(result.getCount())
                && canInsertItemIntoOutputSlot(result.getItem())
                && isOutputFluidValid(recipe.getOutputFluid());
    }

    private ChemicalMixerRecipe cachedRecipe = null;
    private ChemicalMixerRecipe getRecipe() {

        ItemStack[] inputs = new ItemStack[] { inputItemHandler.getStackInSlot(0), inputItemHandler.getStackInSlot(1), inputItemHandler.getStackInSlot(2) };

        if(cachedRecipe != null && cachedRecipe.matches(inputFluidTank.getFluidInTank(0), inputFluidTank.getFluidInTank(1), inputs)) {
            return cachedRecipe;
        }

        List<ChemicalMixerRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.CHEMICAL_MIXER_RECIPE_TYPE.get());

        for(ChemicalMixerRecipe recipe : recipes) {
            if(recipe.matches(inputFluidTank.getFluidInTank(0), inputFluidTank.getFluidInTank(1), inputs)) {
                cachedRecipe = recipe;
                return recipe;
            }
        }

        cachedRecipe = null;
        return null;
    }

    private boolean isOutputFluidValid(FluidStack output) {

        FluidStack stack = outputFluidTank.getFluidInTank(0);

        boolean result = (stack.isEmpty() || stack.containsFluid(output)) && stack.getAmount() + output.getAmount() <= inputFluidTank.getTankCapacity(0);

        //LogUtils.getLogger().info("Comparing {} mB of {} with {} mB of {} with result: {}", output.getAmount(), output.getFluid(), stack.getAmount(), stack.getFluid(), result);

        return result;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.outputItemHandler.getStackInSlot(0).isEmpty() || this.outputItemHandler.getStackInSlot(0).is(item);
    }


    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.outputItemHandler.getStackInSlot(0).getCount() + count <= this.outputItemHandler.getStackInSlot(0).getMaxStackSize();
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

    public FluidTank getFluidInputTank(int i) {
        return inputFluidTank.getTank(i);
    }

    public FluidTank getFluidOutputTank() {
        return outputFluidTank.getTank(0);
    }

    public LazyOptional<IItemHandler> getInputItemHandler() {
        return lazyInputItemHandler;
    }

    public LazyOptional<IFluidHandler> getLazyInputFluidHandler() {
        return lazyInputFluidHandler;
    }

    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return lazyOutputItemHandler;
    }

    public MachineFluidHandler getInputFluidHandler() {
        return inputFluidTank;
    }

    public FilteredItemStackHandler getInputStackHandler() {
        return inputItemHandler;
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

            inputItemHandler.ToggleSlotLock(slot);

            LogUtils.getLogger().info("There was an update to slot locks on the server, slot: {}", slot);
            FlexiPacket packet = new FlexiPacket(this.getBlockPos(), 36);
            inputItemHandler.WriteToFlexiPacket(packet);
            // Rebroadcast the change to listening clients
            AsTechNetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::getLevelChunk), packet);
        } else if (code == 37) {
            // Get the toggled slot lock
            int slot = msg.readInt();

            inputFluidTank.ToggleSlotLock(slot);

            LogUtils.getLogger().info("There was an update to slot locks on the server, slot: {}", slot);
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
            inputItemHandler.ReadFromFlexiPacket(msg);
        } else if (code == 37) {
            LogUtils.getLogger().info("There was an update to slot locks on the client");
            inputFluidTank.ReadFromFlexiPacket(msg);
        }
    }

    @Override
    protected void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet) {
        packet.writeFluidStack(inputFluidTank.getFluidInTank(0));
        packet.writeFluidStack(inputFluidTank.getFluidInTank(1));
        packet.writeFluidStack(outputFluidTank.getFluidInTank(0));
    }

    @Override
    protected void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet) {
        inputFluidTank.getTank(0).setFluid(packet.readFluidStack());
        inputFluidTank.getTank(1).setFluid(packet.readFluidStack());
        outputFluidTank.getTank(0).setFluid(packet.readFluidStack());
    }
}