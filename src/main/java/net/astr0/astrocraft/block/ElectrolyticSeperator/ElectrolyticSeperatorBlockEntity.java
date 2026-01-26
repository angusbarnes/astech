package net.astr0.astrocraft.block.ElectrolyticSeperator;

import net.astr0.astrocraft.CustomEnergyStorage;
import net.astr0.astrocraft.DirectionTranslator;
import net.astr0.astrocraft.Fluid.MachineFluidHandler;
import net.astr0.astrocraft.SoundRegistry;
import net.astr0.astrocraft.block.AbstractMachineBlockEntity;
import net.astr0.astrocraft.block.ModBlockEntities;
import net.astr0.astrocraft.block.SidedConfig;
import net.astr0.astrocraft.recipe.ElectrolyticSeperatorRecipe;
import net.astr0.astrocraft.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectrolyticSeperatorBlockEntity extends AbstractMachineBlockEntity {

    protected final SidedConfig sidedFluidConfig;

    private final MachineFluidHandler inputFluidTank;

    private final MachineFluidHandler outputFluidTank;

    public FluidTank getOutputTank1() {
        return outputFluidTank.getTank(0);
    }

    public FluidTank getOutputTank2() {
        return outputFluidTank.getTank(1);
    }

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(3000000, 7500, 0);

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler;
    private final LazyOptional<IFluidHandler> lazyOuputFluidHandler;
    private final LazyOptional<IFluidHandler> lazyOuput1FluidHandler;
    private final LazyOptional<IFluidHandler> lazyOuput2FluidHandler;
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public ElectrolyticSeperatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ELECTROLYTIC_SEPERATOR_BE.get(), pPos, pBlockState, 5);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> ElectrolyticSeperatorBlockEntity.this.progress;
                    case 3 -> ElectrolyticSeperatorBlockEntity.this.maxProgress;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> ElectrolyticSeperatorBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> ElectrolyticSeperatorBlockEntity.this.progress = pValue;
                    case 3 -> ElectrolyticSeperatorBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        setSoundEvent(SoundRegistry.electric_machine.get());
        setSoundPlaytime(9);

        inputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "INPUT",1,10000));
        lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);
        outputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "OUTPUT",2,10000, true));
        lazyOuputFluidHandler = LazyOptional.of(() -> outputFluidTank);
        lazyOuput1FluidHandler = LazyOptional.of(() -> outputFluidTank.getTank(0));
        lazyOuput2FluidHandler = LazyOptional.of(() -> outputFluidTank.getTank(1));

        sidedFluidConfig = StateManager.addManagedState(new SidedConfig(this, "FLUID"));

        sidedFluidConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.FLUID_INPUT,
                SidedConfig.Capability.FLUID_OUTPUT,
                SidedConfig.Capability.FLUID_OUTPUT_ONE,
                SidedConfig.Capability.FLUID_OUTPUT_TWO
        );


        sidedFluidConfig.set(Direction.UP, SidedConfig.Capability.FLUID_INPUT);
        sidedFluidConfig.set(Direction.EAST, SidedConfig.Capability.FLUID_INPUT);
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
        SidedConfig.Capability type = sidedFluidConfig.get(side);
        return switch (type) {
            case FLUID_INPUT -> lazyInputFluidHandler.cast();
            case FLUID_OUTPUT -> lazyOuputFluidHandler.cast();
            case FLUID_OUTPUT_ONE -> lazyOuput1FluidHandler.cast();
            case FLUID_OUTPUT_TWO -> lazyOuput2FluidHandler.cast();
            default -> LazyOptional.empty();
        };
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyOuputFluidHandler.invalidate();

        lazyOuput1FluidHandler.invalidate();
        lazyOuput2FluidHandler.invalidate();
    }

    // User defined helper to get a list of all the items we are holding,
    // this is used to drop those items when this block is destroyed
    public void drops() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Electrolytic Seperator");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ElectrolyticSeperatorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("seperator.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("sm_data", StateManager.saveToNBT());
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        progress = pTag.getInt("seperator.progress");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        StateManager.loadFromNBT(pTag.getCompound("sm_data"));
    }

    private void ConsumePower(int amount) {
        this.energyStorage.removeEnergy(amount);
        setChanged();
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(hasRecipe()) {
            if(this.energyStorage.getEnergyStored() < 512) {
                decreaseCraftingProgress();
                updateActiveState(false);
            } else {
                increaseCraftingProgress();
                ConsumePower(512);
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
        ElectrolyticSeperatorRecipe recipe = getRecipe();

        if(recipe == null) return;


        int tank0consumed = recipe.calculateConsumedAmount();
        inputFluidTank.getTank(0).drain(tank0consumed, IFluidHandler.FluidAction.EXECUTE);

        outputFluidTank.fill(recipe.getOutput1(), IFluidHandler.FluidAction.EXECUTE);
        outputFluidTank.fill(recipe.getOutput2(), IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean hasRecipe() {


        ElectrolyticSeperatorRecipe recipe = getRecipe();

        if(recipe == null) {
            return false;
        }


        return outputFluidTank.canFluidFit(0, recipe.getOutput1())
                && outputFluidTank.canFluidFit(1, recipe.getOutput2());
    }

    private ElectrolyticSeperatorRecipe cachedRecipe = null;
    private ElectrolyticSeperatorRecipe getRecipe() {

        if(cachedRecipe != null && cachedRecipe.matches(inputFluidTank.getFluidInTank(0))) {
            return cachedRecipe;
        }

        List<ElectrolyticSeperatorRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.ELECTROLYTIC_SEPERATOR_RECIPE_TYPE.get());

        for(ElectrolyticSeperatorRecipe recipe : recipes) {
            if(recipe.matches(inputFluidTank.getFluidInTank(0))) {
                cachedRecipe = recipe;
                this.maxProgress = recipe.getProcessingTime();
                return recipe;
            }
        }

        cachedRecipe = null;
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
    public MachineFluidHandler getOutputFluidHandler() {
        return outputFluidTank;
    }

}
