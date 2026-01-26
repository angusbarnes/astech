package net.astr0.astrocraft;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        if(energy < 0)
            energy = 0;
        if(energy > this.capacity)
            energy = this.capacity;

        this.energy = energy;
    }

    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }

    public void removeEnergy(int energy) {
        setEnergy(this.energy - energy);
    }

    @Override
    public Tag serializeNBT()
    {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.setEnergy(intNbt.getAsInt());
    }
}
