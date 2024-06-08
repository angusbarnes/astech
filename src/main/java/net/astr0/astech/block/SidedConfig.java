package net.astr0.astech.block;

import net.astr0.astech.gui.MachineCapConfiguratorWidget;
import net.astr0.astech.gui.TintColor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class SidedConfig {
    private final int[] capMap;

    public SidedConfig() {
        capMap = new int[Direction.values().length];

        Arrays.fill(capMap, NONE);
    }

    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.put("capMap", new IntArrayTag(capMap));
        return tag;
    }

    // Method to read from NBT
    public void readFromNBT(CompoundTag tag) {
        if (tag.contains("capMap", Tag.TAG_INT_ARRAY)) {
            int[] loadedCapMap = tag.getIntArray("capMap");
            System.arraycopy(loadedCapMap, 0, capMap, 0, Math.min(loadedCapMap.length, capMap.length));
        }
    }

    public void setCap(Direction dir, int capType) {
        capMap[dir.ordinal()] = capType;
        onContentsChanged();
    }

    public int getCap(@NotNull Direction dir) {
        return capMap[dir.ordinal()];
    }

    protected abstract void onContentsChanged();

    public void setNoUpdate(Direction dir, int capType) {
        capMap[dir.ordinal()] = capType;
    }

    public static final int NONE = 0;
    public static final int ITEM_INPUT = 1;
    public static final int ITEM_OUTPUT = 2;
    public static final int FLUID_INPUT = 3;
    public static final int FLUID_OUTPUT = 4;
    public static final int FLUID_OUTPUT_ONE = 5;
    public static final int FLUID_OUTPUT_TWO = 6;

    private static final MachineCapConfiguratorWidget.SlotFormat[] formats = new MachineCapConfiguratorWidget.SlotFormat[] {
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(255, 255, 255), "§7NONE (0)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(255, 255, 85), "§eINPUT (1)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(85, 85, 255), "§9OUTPUT (2)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(255, 170, 0), "§6INPUT (3)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(255, 85, 255), "§dOUTPUT (4)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(255, 85, 85), "§cOUTPUT 1 (5)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(0, 170, 170), "§3OUTPUT 2 (6)"),
            new MachineCapConfiguratorWidget.SlotFormat(new TintColor(40, 40, 40), "§4FIXME"),
    };
    public static MachineCapConfiguratorWidget.SlotFormat GetCapFormat(int capType) {
        if (capType < 0 || capType >= formats.length) {
            capType = 7;
        }

        return formats[capType];
    }
}
