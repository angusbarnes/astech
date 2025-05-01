package net.astr0.astech.block.EnergyInputHatch;

import net.astr0.astech.CustomEnergyStorage;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.astr0.astech.block.SidedConfig;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyInputHatchBlockEntity extends AbstractMachineBlockEntity {


    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(1000000, 100000);

    private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> energyStorage);
    protected final ContainerData data;
    public EnergyInputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENERGY_INPUT_HATCH_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> energyStorage.getMaxEnergyStored();
                    case 1 -> energyStorage.getEnergyStored();
                    default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                if (pIndex == 1) {
                    EnergyInputHatchBlockEntity.this.energyStorage.setEnergy(pValue);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // We can use this in other parts of our multiblock to drain from the input
    // Updates should automatically be synced
    public CustomEnergyStorage getFluidHandler() {
        return energyStorage;
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
        lazyEnergyStorage.invalidate();
    }

    // We only support fluid handlers on all sides for this block
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void SERVER_WriteUpdateToFlexiPacket(FlexiPacket packet) { }

    @Override
    protected void CLIENT_ReadUpdateFromFlexiPacket(FlexiPacket packet) { }
}
