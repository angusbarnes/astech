package net.astr0.astech.gui.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class HazardTooltipComponent implements TooltipComponent {
    private final boolean hazardous;

    public HazardTooltipComponent(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public boolean isHazardous() {
        return hazardous;
    }
}
