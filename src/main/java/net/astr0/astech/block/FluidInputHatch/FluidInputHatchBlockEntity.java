package net.astr0.astech.block.FluidInputHatch;

import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidInputHatchBlockEntity extends AbstractMachineBlockEntity {


    private final MachineFluidHandler fluidHandler;

    private final LazyOptional<IFluidHandler> lazyFluidHandler;

    public FluidInputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_INPUT_HATCH_BE.get(), pPos, pBlockState, 1);

        fluidHandler = StateManager.addManagedState(new MachineFluidHandler(this, "INPUT", 1, 10000, true));
        lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }

    // We can use this in other parts of our multiblock to drain from the input
    // Updates should automatically be synced
    public MachineFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {}

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    // We only support fluid handlers on all sides for this block
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

}
