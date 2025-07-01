package net.astr0.astech.gui;

import com.mojang.logging.LogUtils;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.compat.JEI.GhostIngredientHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FilteredFluidTankSlot extends FluidTankSlot {

    private MachineFluidHandler handler;
    

    public FilteredFluidTankSlot(BlockEntity be, MachineFluidHandler tankHandler, int slot_index, int x, int y) {
        super(be, tankHandler, slot_index, x, y);
        handler = tankHandler;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (handler.isSlotLocked(this.SLOT_INDEX)){
            guiGraphics.blit(WIDGET_TEXTURE, this.x-1, this.y - 1, 61, 198, 12, 58);
        }

        super.render(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void handleFilterDrop(GhostIngredientHandler.DraggedIngredient ingredient) {
        if(ingredient instanceof GhostIngredientHandler.DraggedIngredient.Fluid fluid) {
            handler.setFluidFilterOnClient(SLOT_INDEX, fluid.stack());// Here we would set the filter and update the server
            LogUtils.getLogger().info("We reached the item slot with fluid {}", fluid.stack().getFluid().toString());
        }
    }

    @Override
    public boolean canAcceptGhostIngredient(GhostIngredientHandler.DraggedIngredient ingredient) {
        if(ingredient instanceof GhostIngredientHandler.DraggedIngredient.Fluid && !handler.isSlotLocked(this.SLOT_INDEX)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean handleClick(BlockEntity be, double mouseX, double mouseY, int mouseButton, boolean isShifting) {
        // We will not handle any click events that occur outside our bounds
        if(!isHovering(this.x, this.y, 10, 58, mouseX, mouseY)) return false;

        if (handler.isSlotLocked(this.SLOT_INDEX)) {
            handler.clearFluidFilterOnClient(SLOT_INDEX);
        } else {
            handler.setFluidFilterOnClient(SLOT_INDEX, handler.getFluidInTank(SLOT_INDEX));
        }

        return true;
    }
}
