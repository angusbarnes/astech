package net.astr0.astech.block.ChemicalMixer;

import com.mojang.logging.LogUtils;
import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.ITickableBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.network.NetworkedMachineUpdate;
import net.astr0.astech.network.INetworkedMachine;
import net.astr0.astech.recipe.GemPolishingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChemicalMixerStationBlockEntity extends BlockEntity implements MenuProvider, ITickableBlockEntity, INetworkedMachine {

    // ItemStackHandler is a naive implementation of IItemHandler which is a Forge Capability
    private final ItemStackHandler inputItemHandler = new ItemStackHandler(4) {

        // This over-rides the ItemStackHandler method which gets called on an update.
        // Here we call this::setChanged() which marks this block entity as dirty.
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            // If we are server side, we also send a blockUpdate. The docs for this function can
            // be found here: https://docs.minecraftforge.net/en/1.20.x/blockentities/#synchronizing-on-block-update
            // This if it is passed i=2 or 3, it will query this block entity by calling getPacketUpdate()
            if(level!= null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    private final MachineFluidHandler inputFluidTank = new MachineFluidHandler(2,10000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(level!= null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }

            LogUtils.getLogger().warn("Contents of fluid inventory updated");
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(30000, 500, 0);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 3;

    // This is the provider that allows networked data to be lazily updates
    // We must invalidate this every time a change occurs so the server re-syncs
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> inputItemHandler);

    private final LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> inputFluidTank);;

    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    // Container data is simple data which is syncrhonised by default over the network
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public ChemicalMixerStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHEMICAL_MIXER_BE.get(), pPos, pBlockState);

        // Init our simple container data
        // this is synced for us every tick automatically,
        // this is limited to the signed short range
        // ItemHandlerSlot's in the menu screen sync the item stacks for us
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
                    case 1 -> energyStorage.getEnergyStored();
                    case 2 -> ChemicalMixerStationBlockEntity.this.progress;
                    case 3 -> ChemicalMixerStationBlockEntity.this.maxProgress;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 1 -> ChemicalMixerStationBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 2 -> ChemicalMixerStationBlockEntity.this.progress = pValue;
                    case 3 -> ChemicalMixerStationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
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
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side); // Allow previous caps in the stack to be checked
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    // Called by forge (I think in the underlying blockEntity's setChanged()) to mark our caps as old
    // Each capability registers in this class should be invalidated
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
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
        return new ChemicalMixerStationMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", inputItemHandler.serializeNBT());
        pTag.putInt("chemical_mixer.progress", progress);
        pTag.put("Energy", this.energyStorage.serializeNBT());
        pTag.put("fluidTank", inputFluidTank.writeToNBT(new CompoundTag()));

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
    }

    // This logic is a userDefined name for a tick function
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

        // Only tick on the server, the server should still sync
        if(this.level == null || this.level.isClientSide())
            return;

        if(hasRecipe()) {
            increaseCraftingProgress();

            // every time we change some shit, call setChanged
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }

    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);

        this.inputItemHandler.extractItem(INPUT_SLOT, 2, false);

        this.inputItemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.inputItemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        Optional<GemPolishingRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<GemPolishingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.inputItemHandler.getSlots());
        for(int i = 0; i < inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, this.inputItemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GemPolishingRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.inputItemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.inputItemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.inputItemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.inputItemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    public FluidTank getFluidTank(int i) {
        return inputFluidTank.getTank(i);
    }

    public ItemStackHandler getInputItemHandler() {
        return inputItemHandler;
    }

    public LazyOptional<IFluidHandler> getLazyFluidHandler() {
        return lazyFluidHandler;
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
        return saveWithoutMetadata();
    }

    @Override
    public void updateServer(NetworkedMachineUpdate msg) {
        if(this.level !=null && this.level.isClientSide()) {
            throw new IllegalStateException("updateServer() should never run on the client! The level is either null or client side.");
        }
    }

    @Override
    public void updateClient(NetworkedMachineUpdate msg) {
        if(this.level !=null && !this.level.isClientSide()) {
            throw new IllegalStateException("updateClient() should never run on the server! The level is either null or server side.");
        }
    }

    // A networking re-write can use custom packets to synchronise data more effectively
}
