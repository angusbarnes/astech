package net.astr0.astrocraft.block.CoolantBlock;

import net.astr0.astrocraft.Fluid.MachineFluidHandler;
import net.astr0.astrocraft.ModTags;
import net.astr0.astrocraft.block.AbstractMachineBlockEntity;
import net.astr0.astrocraft.block.EUVMachine.EUVMachineBlockEntity;
import net.astr0.astrocraft.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoolantBlockEntity extends AbstractMachineBlockEntity {


    private final MachineFluidHandler inputFluidTank;

    private final LazyOptional<IFluidHandler> lazyInputFluidHandler;
    // Container data is simple data which is synchronised by default over the network
    protected final ContainerData data;
    private int currentTemperature = 0;

    public CoolantBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COOLANT_BLOCK_BE.get(), pPos, pBlockState, 1);

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

        inputFluidTank = StateManager.addManagedState(new MachineFluidHandler(this, "INPUT",1,10000, true));
        lazyInputFluidHandler = LazyOptional.of(() -> inputFluidTank);

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
            }
        }

        IncrementNetworkTickCount();
    }

    public MachineFluidHandler getInputFluidHandler() {
        return inputFluidTank;
    }


    public int CategorizeFluidGrade(Fluid fluid) {
        if(fluid.is(ModTags.TIER_1_COOLANT)) {
            return TIER_1_COOL_AMOUNT;
        } else if(fluid.is(ModTags.TIER_2_COOLANT)) {
            return TIER_2_COOL_AMOUNT;
        } else {
            return BASE_COOL_AMOUNT;
        }
    }

}
