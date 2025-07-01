package net.astr0.astech.block.EUVMachine;

import net.astr0.astech.*;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.recipe.EUVMachineRecipe;
import net.astr0.astech.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EUVMachineBlockEntity extends AbstractMachineBlockEntity {


    protected final SidedConfig sidedItemConfig;
    protected final SidedConfig sidedFluidConfig;


    private final MachineFluidHandler inputFluidTank;

    // ItemStackHandler is a naive implementation of IItemHandler which is a Forge Capability
    private final FilteredItemStackHandler inputItemHandler;

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(3000000, 300000, 0);

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler;
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<IItemHandler> lazyInputItemHandler;
    private final LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    private int currentTemperature = 0;
    private final int _baseTemp = 0;

    public EUVMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.EUV_MACHINE_BE.get(), pPos, pBlockState, 4);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> EUVMachineBlockEntity.this.progress;
                    case 3 -> EUVMachineBlockEntity.this.maxProgress;
                    case 4 -> EUVMachineBlockEntity.this.currentTemperature;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> EUVMachineBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> EUVMachineBlockEntity.this.progress = pValue;
                    case 3 -> EUVMachineBlockEntity.this.maxProgress = pValue;
                    case 4 -> EUVMachineBlockEntity.this.currentTemperature = pValue;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };

        setSoundEvent(SoundRegistry.laser_machine.get());
        setSoundPlaytime(9);

        inputItemHandler = StateManager.addManagedState(new FilteredItemStackHandler(this,2));
        lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);
        inputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "INPUT",2, 10000));
        lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);

        sidedFluidConfig = StateManager.addManagedState(new SidedConfig(this, "FLUID"));
        sidedItemConfig = StateManager.addManagedState(new SidedConfig(this, "ITEM"));

        sidedItemConfig.set(Direction.UP, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.set(Direction.EAST, SidedConfig.Capability.ITEM_INPUT);
        sidedItemConfig.set(Direction.DOWN, SidedConfig.Capability.ITEM_OUTPUT);
        sidedItemConfig.set(Direction.WEST, SidedConfig.Capability.ITEM_OUTPUT);

        sidedFluidConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.FLUID_INPUT
        );

        sidedItemConfig.setSupportedCaps(
                SidedConfig.Capability.NONE,
                SidedConfig.Capability.ITEM_INPUT,
                SidedConfig.Capability.ITEM_OUTPUT
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
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return getItemCapability(side);
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
        lazyInputFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyInputItemHandler.invalidate();
        lazyOutputItemHandler.invalidate();
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
        return Component.literal("EUV Lithographer");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new EUVMachineMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("outputInventory", outputItemHandler.serializeNBT());
        pTag.putInt("euv.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.putInt("temp", currentTemperature);
        pTag.put("sm_data", StateManager.saveToNBT());
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        outputItemHandler.deserializeNBT(pTag.getCompound("outputInventory"));
        progress = pTag.getInt("euv.progress");
        currentTemperature = pTag.getInt("temp");
        energyStorage.deserializeNBT(pTag.get("Energy"));
        StateManager.loadFromNBT(pTag.getCompound("sm_data"));
    }

    private void ConsumePower(int amount) {
        this.energyStorage.removeEnergy(amount);
        setChanged();
    }


    public void Cool(int amount) {
        currentTemperature -= amount;
        if (currentTemperature < 0) currentTemperature = 0;
    }


    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(hasRecipe()) {

            if(this.energyStorage.getEnergyStored() < 204800) {
                decreaseCraftingProgress();
                updateActiveState(false);
                //LogUtils.getLogger().info("ENERGY DEPLETE TICK TEMP: {}", currentTemperature);
            } else {
                increaseCraftingProgress();
                ConsumePower(204800);
                updateActiveState(true);
                //LogUtils.getLogger().info("CRAFT INCREASE TEMP: {}", currentTemperature);
                //level.playSound(null, getBlockPos(), SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 3f, 1.0F);
            }

            // every time we change some shit, call setChanged
            // This set changes ensures this block is queried for changes when the levekChunk is saved to disk
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
                //LogUtils.getLogger().info("CRAFT COMPLETE: {}", currentTemperature);
            }

            if(currentTemperature > 300) {
                pLevel.explode(null, this.getBlockPos().getX() +0.5, this.getBlockPos().getY() +0.5, this.getBlockPos().getZ() +0.5, 8, true, Level.ExplosionInteraction.BLOCK);

            }
        } else {
            resetProgress();
            updateActiveState(false);
           // LogUtils.getLogger().info("HAS NO RECIPE TEMP: {}", currentTemperature);
        }

//        LogUtils.getLogger().info("END TICK TEMP: {}", currentTemperature);
//        LogUtils.getLogger().info("========================");

        IncrementNetworkTickCount();
    }

    private void resetProgress() {
        progress = 0;
        currentTemperature -= 1;

        if(currentTemperature < 0) {
            currentTemperature = 0;
        }
    }

    private static final RandomSource randomRandomSource = RandomSource.create();

    private void craftItem() {
        EUVMachineRecipe recipe = getRecipe(); //TODO can replace all of these calls with a cached recipe check

        if(recipe == null) return;


        if(inputFluidTank.getTank(0).getFluidAmount() < 100) {
            return; // We do not work if we have no photoresist
        }

//        LogUtils.getLogger().info("Craft Recipe");
//        LogUtils.getLogger().info("Fluid has tags: {}", inputFluidTank.getTank(0).getFluid().getFluid().builtInRegistryHolder().tags().toList().toString());

        if(inputFluidTank.getTank(0).getFluid().getFluid().is(ModTags.TIER_1_PHOTORESIST)) {

            //LogUtils.getLogger().info("Tier 1");

            inputFluidTank.getTank(0).drain(100, IFluidHandler.FluidAction.EXECUTE);


            inputItemHandler.extractItem(0, 1, false);

            ItemStack catalyst = inputItemHandler.getStackInSlot(1);
            //if (catalyst.isDamageableItem()) catalyst.setDamageValue(catalyst.getDamageValue() + 1);
            catalyst.hurt(1, randomRandomSource, null );

            // This will theoretically make it a 50/50
            if(randomRandomSource.nextBoolean()) {
                outputItemHandler.insertItem(0, recipe.getOutputItem(), false);
            }

        } else if (inputFluidTank.getTank(0).getFluid().getFluid().is(ModTags.TIER_2_PHOTORESIST)) {

            //LogUtils.getLogger().info("Tier 2");

            //LogUtils.getLogger().info("Tier 2 Recipe");
            inputFluidTank.getTank(0).drain(100, IFluidHandler.FluidAction.EXECUTE);


            inputItemHandler.extractItem(0, 1, false);

            ItemStack catalyst = inputItemHandler.getStackInSlot(1);
            //if (catalyst.isDamageableItem()) catalyst.setDamageValue(catalyst.getDamageValue() + 1);
            catalyst.hurt(1, randomRandomSource, null );

            outputItemHandler.insertItem(0, recipe.getOutputItem(), false);

            //LogUtils.getLogger().info("We should have output an item");

        }
    }

    private boolean hasRecipe() {


        EUVMachineRecipe recipe = getRecipe();

        if(recipe == null) {
            return false;
        }

        //maxProgress = recipe.getProcessingTime() * 20;

        //TODO: This could be simplified a lot
        return canInsertAmountIntoOutputSlot(recipe.getOutputItem().getCount())
                && canInsertItemIntoOutputSlot(recipe.getOutputItem().getItem())
                && (!inputItemHandler.getStackInSlot(1).isDamageableItem() || inputItemHandler.getStackInSlot(1).getDamageValue()  < inputItemHandler.getStackInSlot(1).getMaxDamage());
    }

    private EUVMachineRecipe cachedRecipe = null;
    private EUVMachineRecipe getRecipe() {

        if(cachedRecipe != null && cachedRecipe.matches(inputItemHandler.getStackInSlot(0), inputItemHandler.getStackInSlot(1))) {
            return cachedRecipe;
        }

        if (inputFluidTank.getTank(0).getFluidAmount() < 100 || !inputFluidTank.getTank(0).getFluid().getFluid().is(ModTags.PHOTORESIST_TAG)) {
            return null; // We dont got the facilities
        }

        List<EUVMachineRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.EUV_MACHINE_RECIPE_TYPE.get());

        for(EUVMachineRecipe recipe : recipes) {
            if(recipe.matches(inputItemHandler.getStackInSlot(0), inputItemHandler.getStackInSlot(1))) {
                cachedRecipe = recipe;
                this.maxProgress = recipe.getProcessingTime() * 20;
                //LogUtils.getLogger().info("Found Recipe");
                return recipe;
            }
        }

        //LogUtils.getLogger().info("Did not Found Recipe");
        cachedRecipe = null;
        return null;
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
        currentTemperature += 1;
    }

    private void decreaseCraftingProgress() {
        progress--;

        if(progress < 0) progress = 0;

        currentTemperature -= 1;
        if(currentTemperature <0) currentTemperature = 0;
    }

    public MachineFluidHandler getInputFluidHandler() {
        return inputFluidTank;
    }

    public LazyOptional<IItemHandler> getInputItemHandler() {
        return lazyInputItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return lazyOutputItemHandler;
    }

    public FilteredItemStackHandler getInputStackHandler() {
        return inputItemHandler;
    }

}
