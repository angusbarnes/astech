package net.astr0.astech.block.Assembler;

import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.DirectionTranslator;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.recipe.AssemblerRecipe;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AssemblerBlockEntity extends AbstractMachineBlockEntity {

    // ItemStackHandler is a naive implementation of IItemHandler which is a Forge Capability
    private final FilteredItemStackHandler inputItemHandler;

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final SidedConfig sidedItemConfig;
    protected final SidedConfig sidedFluidConfig;


    private final MachineFluidHandler inputFluidTank;

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(300000, 750, 0);

    private final LazyOptional<IItemHandler> lazyInputItemHandler;
    private final LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);
    private final LazyOptional<IFluidHandler> lazyInputFluidHandler;
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public AssemblerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ASSEMBLER_BE.get(), pPos, pBlockState, 4);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    // TODO: Turn this into a synced percentage and reconvert to relative amount on client side
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> AssemblerBlockEntity.this.progress;
                    case 3 -> AssemblerBlockEntity.this.maxProgress;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> AssemblerBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> AssemblerBlockEntity.this.progress = pValue;
                    case 3 -> AssemblerBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        sidedItemConfig = StateManager.addManagedState(new SidedConfig(this, "ITEM"));
        sidedFluidConfig = StateManager.addManagedState(new SidedConfig(this, "FLUID"));

        inputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "INPUT",1, 10000));
        lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);

        inputItemHandler = StateManager.addManagedState(new FilteredItemStackHandler(this,5));
        lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);

        sidedFluidConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.FLUID_INPUT
        );

        sidedItemConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.ITEM_INPUT,
                SidedConfig.Capability.ITEM_OUTPUT
        );

        sidedItemConfig.set(Direction.UP, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.set(Direction.EAST, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.set(Direction.DOWN, SidedConfig.Capability.ITEM_OUTPUT);
        sidedItemConfig.set(Direction.WEST, SidedConfig.Capability.ITEM_OUTPUT);

        sidedFluidConfig.set(Direction.UP, SidedConfig.Capability.FLUID_INPUT);
        sidedFluidConfig.set(Direction.EAST, SidedConfig.Capability.FLUID_INPUT);
    }

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
        SidedConfig.Capability type = sidedFluidConfig.get(side);
        return switch (type) {
            case FLUID_INPUT -> lazyInputFluidHandler.cast();
            default -> LazyOptional.empty();
        };
    }

    public <T> LazyOptional<T> getItemCapability(Direction side) {
        SidedConfig.Capability type = sidedItemConfig.get(side);

        return switch (type) {
            case ITEM_INPUT -> lazyInputItemHandler.cast();
            case ITEM_OUTPUT -> lazyOutputItemHandler.cast();
            default -> LazyOptional.empty();
        };
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInputItemHandler.invalidate();
        lazyInputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    // User defined helper to get a list of all the items we are holding,
    // this is used to drop those items when this block is destroyed
    // TODO: FIX BUG IN ALL BLOCK ENTITIES TO ALSO DROP ITEM IN OUTPUT HANDLER
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(inputItemHandler.getSlots() + outputItemHandler.getSlots());
        for(int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }

        for(int i = 0; i < outputItemHandler.getSlots(); i++) {
            inventory.setItem(inputItemHandler.getSlots() + i, outputItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Assembler");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AssemblerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("assembler.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("sm_data", StateManager.saveToNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        progress = pTag.getInt("assembler.progress");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        StateManager.loadFromNBT(pTag.getCompound("sm_data"));
        outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
    }

    private void ConsumePower(int amount) {
        this.energyStorage.removeEnergy(amount);
        setChanged();
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(hasRecipe()) {
            if(this.energyStorage.getEnergyStored() < 256) {
                decreaseCraftingProgress();
                updateActiveState(false);
            } else {
                increaseCraftingProgress();
                ConsumePower(256);
                updateActiveState(true);
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
            updateActiveState(false);
        }

        IncrementNetworkTickCount();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        AssemblerRecipe recipe = getRecipe();

        if(recipe == null) return;

        int itemsConsumed0 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(0));
        int itemsConsumed1 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(1));
        int itemsConsumed2 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(2));
        int itemsConsumed3 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(3));
        int itemsConsumed4 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(4));

        this.inputItemHandler.extractItem(0, itemsConsumed0, false);
        this.inputItemHandler.extractItem(1, itemsConsumed1, false);
        this.inputItemHandler.extractItem(2, itemsConsumed2, false);
        this.inputItemHandler.extractItem(3, itemsConsumed3, false);
        this.inputItemHandler.extractItem(4, itemsConsumed4, false);

        int tank0consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(0));

        inputFluidTank.getTank(0).drain(tank0consumed, IFluidHandler.FluidAction.EXECUTE);

        ItemStack result = recipe.getOutputItem();

        if(result != null && !result.isEmpty()) {
            insertItemStackIntoOutputSlot(result);
        }
    }

    private boolean hasRecipe() {


        AssemblerRecipe recipe = getRecipe();

        if(recipe == null) {
            return false;
        }

        ItemStack result = recipe.getOutputItem();

        return canInsertItemStackIntoOutputSlot(result);
    }

    private AssemblerRecipe cachedRecipe = null;
    private AssemblerRecipe getRecipe() {

        ItemStack[] inputs = new ItemStack[] {
                inputItemHandler.getStackInSlot(0),
                inputItemHandler.getStackInSlot(1),
                inputItemHandler.getStackInSlot(2),
                inputItemHandler.getStackInSlot(3),
                inputItemHandler.getStackInSlot(4)
        };

        if(cachedRecipe != null && cachedRecipe.matches(inputFluidTank.getFluidInTank(0), inputs, false)) {
            return cachedRecipe;
        }

        List<AssemblerRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.ASSEMBLER_RECIPE_TYPE.get());

        for(AssemblerRecipe recipe : recipes) {
            assert inputFluidTank.getFluidInTank(0) == FluidStack.EMPTY;
            if(recipe.matches(inputFluidTank.getFluidInTank(0), inputs, false)) {
                cachedRecipe = recipe;
                this.maxProgress = recipe.getProcessingTime();
                return recipe;
            }
        }

        cachedRecipe = null;
        return null;
    }

    private boolean canInsertItemStackIntoOutputSlot(ItemStack item) {
        for(int i = 0; i < this.outputItemHandler.getSlots(); i++) {
            ItemStack remainder = this.outputItemHandler.insertItem(i, item, true);

            if (remainder == ItemStack.EMPTY) {
                return true;
            }
        }

        return false;
    }

    private void insertItemStackIntoOutputSlot(ItemStack item) {
        for(int i = 0; i < this.outputItemHandler.getSlots(); i++) {
            ItemStack remainder = this.outputItemHandler.insertItem(i, item, true);

            if (remainder == ItemStack.EMPTY) {
                this.outputItemHandler.insertItem(i, item, false);
                return;
            }
        }
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

    public LazyOptional<IItemHandler> getInputItemHandler() {
        return lazyInputItemHandler;
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

}
