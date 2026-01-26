package net.astr0.astrocraft.block.EnergyInputHatch;

import net.astr0.astrocraft.CustomEnergyStorage;
import net.astr0.astrocraft.IConfigurable;
import net.astr0.astrocraft.TabletSummary;
import net.astr0.astrocraft.block.AbstractMachineBlockEntity;
import net.astr0.astrocraft.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyInputHatchBlockEntity extends AbstractMachineBlockEntity implements IConfigurable {


    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(1000000, 100000);

    private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> energyStorage);
    protected final ContainerData data;
    public EnergyInputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENERGY_INPUT_HATCH_BE.get(), pPos, pBlockState, 0);

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
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("energy",energyStorage.getEnergyStored());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        energyStorage.setEnergy(pTag.getInt("energy"));

        super.load(pTag);
    }

    @Override
    public Component getTabletSummary() {

        TabletSummary summary = new TabletSummary("Fluid Input Hatch");
        summary.addField("Current Charge (FE): ", energyStorage.getEnergyStored());
        summary.addField("Max Charge (kFE): ", energyStorage.getMaxEnergyStored()/1000);
        return summary.getAsComponent();
    }
}
