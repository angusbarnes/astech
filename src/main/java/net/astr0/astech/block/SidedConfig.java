package net.astr0.astech.block;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SidedConfig {
    private final CapabilityType[] capMap;

    public SidedConfig() {
        capMap = new CapabilityType[Direction.values().length];

        Arrays.fill(capMap, CapabilityType.NONE);
    }

    public void setCap(Direction dir, CapabilityType config) {
        capMap[dir.ordinal()] = config;
    }

    public CapabilityType getCap(@NotNull Direction dir) {
        return capMap[dir.ordinal()];
    }
}
