package net.astr0.astech.block.FluidInputHatch;

import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.network.FlexiPacket;
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


    private final MachineFluidHandler fluidHandler = new MachineFluidHandler(1, 10000, false) {
        @Override
        protected void onContentsChanged() {
            SetNetworkDirty();
        }
    };

    private final LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> fluidHandler);

    public FluidInputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_INPUT_HATCH_BE.get(), pPos, pBlockState);
    }

    // We can use this in other parts of our multiblock to drain from the input
    // Updates should automatically be synced
    public MachineFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public void tickOnServer(Level pLevel, BlockPos pPos, BlockState pState) {}

    @Override
    public void updateServer(FlexiPacket msg) {}

    @Override
    public void updateClient(FlexiPacket msg) {}

    @Override
    public int[][] getCapTypes() {
        return new int[0][];
    }

    @Override
    public SidedConfig getSidedConfig(int mode) {
        return null;
    }

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

    @Override
    protected void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet) {
        fluidHandler.WriteToFlexiPacket(packet);
    }

    @Override
    protected void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet) {
        fluidHandler.ReadFromFlexiPacket(packet);
    }
}
