package net.astr0.astech.block.CoolantBlock;

import com.mojang.logging.LogUtils;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.EUVMachine.EUVMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoolantBlockEntity extends AbstractMachineBlockEntity {


    private final MachineFluidHandler inputFluidTank = new MachineFluidHandler(1,10000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            SetNetworkDirty();

        }
    };

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int currentTemperature = 0;

    public CoolantBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COOLANT_BLOCK_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CoolantBlockEntity.this.currentTemperature;
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CoolantBlockEntity.this.currentTemperature = pValue;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };

        setSoundEvent(SoundEvents.FIRE_EXTINGUISH);
        setSoundPlaytime(9);

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyInputFluidHandler.cast();
        }

        return super.getCapability(cap, side); // Allow previous caps in the stack to be checked
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyInputFluidHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Coolant Block");
    }

    // Not sure exactly what this over-rides, but it implements MenuProvider
    // Return a new instance of our menu class. ENSURE THIS IS A REGISTERED CLASS
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CoolantBlockMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public int[][] getCapTypes() {
        return new int[0][];
    }

    @Override
    public SidedConfig getSidedConfig(int mode) {
        return null;
    }

    // On chunk load or on updatePacket we can save our basic data to NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("fluidTank", inputFluidTank.writeToNBT(new CompoundTag()));
        pTag.putInt("temp", currentTemperature);
        super.saveAdditional(pTag);
    }

    // When we need to load from NBT we can do the opposite
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        currentTemperature = pTag.getInt("temp");
        inputFluidTank.readFromNBT(pTag.getCompound("fluidTank"));
    }



    //public Block coolantBlock = null;

    private static final int[][] offsets = {
            {0, -1, 0},
            {0, 0, 1},
            {0, 0, -1},
            {1, 0, 0},
            {-1, 0, 0},
            {0, 1, 0},
    };

    private static final int BASE_COOL_AMOUNT = 2;
    private static final int TIER_1_COOL_AMOUNT = 8;
    private static final int TIER_2_COOL_AMOUNT = 11;
    private static final int COOL_TICK_INTERVAL = 10;

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {

        if(inputFluidTank.getTank(0).getFluidAmount() < 60) return;

        // Twice per second we run this

        if(getinternalTickCount() % COOL_TICK_INTERVAL == 0) {
            boolean applied = false;
            for(int[] offset : offsets) {
                BlockEntity be = pLevel.getBlockEntity(pPos.offset(offset[0], offset[1], offset[2]));
                if (be instanceof EUVMachineBlockEntity) {
                    ((EUVMachineBlockEntity) be).Cool(CategorizeFluidGrade(inputFluidTank.getFluidInTank(0).getFluid()));
                    inputFluidTank.getTank(0).drain(10, IFluidHandler.FluidAction.EXECUTE);
                    applied = true;
                }
            }

            updateActiveState(applied);

            if(applied) {
                setChanged();
                SetNetworkDirty();
            }
        }

        IncrementNetworkTickCount();
    }

    public MachineFluidHandler getInputFluidHandler() {
        return inputFluidTank;
    }

    public static final TagKey<Fluid> TIER_1_FLUID = FluidTags.create(new ResourceLocation("forge", "tier_1_coolant"));
    public static final TagKey<Fluid> TIER_2_FLUID = FluidTags.create(new ResourceLocation("forge", "tier_2_coolant"));
    public int CategorizeFluidGrade(Fluid fluid) {
        if(fluid.is(TIER_1_FLUID)) {
            return TIER_1_COOL_AMOUNT;
        } else if(fluid.is(TIER_2_FLUID)) {
            return TIER_2_COOL_AMOUNT;
        } else {
            return BASE_COOL_AMOUNT;
        }
    }


    @Override
    public void updateServer(FlexiPacket msg) {

        int code = msg.GetCode();

        // Throw if we don't have loaded level on server
        if(!(this.level != null && !this.level.isClientSide())) {
            throw new IllegalStateException("updateServer() should never run on the client! The level is either null or client side.");
        }

      if (code == 37) {
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

            inputFluidTank.getTank(0).setFluid(FluidStack.EMPTY);
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

        if (code == 69) {
            CLIENT_ReadUpdateFromFlexiPacket(msg);
        } else if (code == 37) {
            LogUtils.getLogger().info("There was an update to slot locks on the client");
            inputFluidTank.ReadFromFlexiPacket(msg);
        }
    }

    @Override
    protected void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet) {
        packet.writeFluidStack(inputFluidTank.getFluidInTank(0));
    }

    @Override
    protected void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet) {
        inputFluidTank.getTank(0).setFluid(packet.readFluidStack());
    }
}
