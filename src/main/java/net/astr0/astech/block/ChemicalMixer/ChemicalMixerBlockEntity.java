package net.astr0.astech.block.ChemicalMixer;

import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.DirectionTranslator;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
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
    private final FilteredItemStackHandler inputItemHandler;

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        } //TODO: review if this is actually necessary
    };

    protected final SidedConfig sidedItemConfig;
    protected final SidedConfig sidedFluidConfig;

    private final MachineFluidHandler inputFluidTank;

    private final MachineFluidHandler outputFluidTank;

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(300000, 750, 0);

    private final LazyOptional<IItemHandler> lazyInputItemHandler;
    private final LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);
    private final LazyOptional<IFluidHandler> lazyInputFluidHandler;
    private final LazyOptional<IFluidHandler> lazyOutputFluidHandler;
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public ChemicalMixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHEMICAL_MIXER_BE.get(), pPos, pBlockState, 3);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
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

        inputItemHandler = StateManager.addManagedState(new FilteredItemStackHandler(this,3));
        lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);

        inputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this,"INPUT",2,10000));
        lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);

        outputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "OUTPUT",1,20000, true));
        lazyOutputFluidHandler = LazyOptional.of(() -> outputFluidTank);

        sidedItemConfig = StateManager.addManagedState(new SidedConfig(this, "ITEM"));
        sidedFluidConfig = StateManager.addManagedState(new SidedConfig(this, "FLUID"));

        sidedFluidConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.FLUID_INPUT,
                SidedConfig.Capability.FLUID_OUTPUT
        );

        sidedItemConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.ITEM_INPUT,
                SidedConfig.Capability.ITEM_OUTPUT
        );

        sidedItemConfig.setNoUpdate(Direction.UP, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.setNoUpdate(Direction.EAST, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.setNoUpdate(Direction.DOWN, SidedConfig.Capability.ITEM_OUTPUT);
        sidedItemConfig.setNoUpdate(Direction.WEST, SidedConfig.Capability.ITEM_OUTPUT);

        sidedFluidConfig.setNoUpdate(Direction.UP, SidedConfig.Capability.FLUID_INPUT);
        sidedFluidConfig.setNoUpdate(Direction.EAST, SidedConfig.Capability.FLUID_INPUT);
        sidedFluidConfig.setNoUpdate(Direction.DOWN, SidedConfig.Capability.FLUID_OUTPUT);
        sidedFluidConfig.setNoUpdate(Direction.WEST, SidedConfig.Capability.FLUID_OUTPUT);
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
            case FLUID_OUTPUT -> lazyOutputFluidHandler.cast();
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
        lazyOutputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    // User defined helper to get a list of all the items we are holding,
    // this is used to drop those items when this block is destroyed
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
        return Component.literal("Chemical Mixer");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ChemicalMixerMenu(pContainerId, pPlayerInventory, this, this.data);
    }


    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("chemical_mixer.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("sm_data", StateManager.saveToNBT());
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        progress = pTag.getInt("chemical_mixer.progress");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
        StateManager.loadFromNBT(pTag.getCompound("sm_data"));
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
        ChemicalMixerRecipe recipe = getRecipe();

        if(recipe == null) return;

        int itemsConsumed0 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(0));
        int itemsConsumed1 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(1));
        int itemsConsumed2 = recipe.calculateConsumedAmountItems(this.inputItemHandler.getStackInSlot(2));

        this.inputItemHandler.extractItem(0, itemsConsumed0, false);
        this.inputItemHandler.extractItem(1, itemsConsumed1, false);
        this.inputItemHandler.extractItem(2, itemsConsumed2, false);

        int tank0consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(0));
        int tank1consumed = recipe.calculateConsumedAmount(inputFluidTank.getFluidInTank(1));

        inputFluidTank.getTank(0).drain(tank0consumed, IFluidHandler.FluidAction.EXECUTE);
        inputFluidTank.getTank(1).drain(tank1consumed, IFluidHandler.FluidAction.EXECUTE);

        ItemStack result = recipe.getOutputItem();

        if(result != null && !result.isEmpty()) {
            this.outputItemHandler.setStackInSlot(0, new ItemStack(result.getItem(),
                    this.outputItemHandler.getStackInSlot(0).getCount() + result.getCount()));
        }

        FluidStack resultFluid = recipe.getOutputFluid();

        if(resultFluid != null && !resultFluid.isEmpty()) {
            outputFluidTank.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
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

        boolean result = (stack.isEmpty() || stack.containsFluid(output)) && stack.getAmount() + output.getAmount() <= outputFluidTank.getTankCapacity(0);

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

    public FluidTank getFluidOutputTank() {
        return outputFluidTank.getTank(0);
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
