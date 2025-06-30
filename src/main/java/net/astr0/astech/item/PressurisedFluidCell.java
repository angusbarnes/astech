package net.astr0.astech.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PressurisedFluidCell extends FluidCellItem {
    public PressurisedFluidCell(Properties properties) {
        super(properties);
    }

    // We override this to prevent the hazard behaviours being applied
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
}
