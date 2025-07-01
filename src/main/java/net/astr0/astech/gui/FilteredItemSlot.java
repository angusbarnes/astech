package net.astr0.astech.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.compat.JEI.GhostIngredientHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FilteredItemSlot extends AbstractGuiSlot {

    private final int slotIndex;
    private final FilteredItemStackHandler handler;


    public FilteredItemSlot(FilteredItemStackHandler handler, int itemSlotIndex, int x, int y) {
        super(x, y, 16, 16);
        this.handler = handler;
        slotIndex = itemSlotIndex;
    }

    @Override
    public boolean canAcceptGhostIngredient(GhostIngredientHandler.DraggedIngredient ingredient) {
        if(ingredient instanceof GhostIngredientHandler.DraggedIngredient.Item && handler.getStackInSlot(slotIndex) == ItemStack.EMPTY) {
            LogUtils.getLogger().info("Slot was checked for drag potential: True");

            return true;

        }

        LogUtils.getLogger().info("Slot was checked for drag potential: False, {}", ingredient);
        return false;
    }

    @Override
    public void handleFilterDrop(GhostIngredientHandler.DraggedIngredient ingredient) {
        if(ingredient instanceof GhostIngredientHandler.DraggedIngredient.Item item) {
            // Here we would set the filter and update the server
            LogUtils.getLogger().info("We reached the item slot with item{}", item.stack().getItem());
            handler.setFilterOnClient(slotIndex, item.stack());
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        if (handler.checkSlot(slotIndex)){
            guiGraphics.blit(WIDGET_TEXTURE, this.x - 1, this.y - 1, 41, 238, 18, 18);
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.30f);
            guiGraphics.renderItem(handler.getFilterForSlot(slotIndex), this.x, this.y);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
    }


    @Override
    public boolean handleClick(BlockEntity be, double mouseX, double mouseY, int mouseButton, boolean isShifting) {
        if(!isHovering(this.x, this.y, 16, 16, mouseX, mouseY)) return false;

        if (handler.checkSlot(slotIndex)) {
            handler.clearFilterOnClient(slotIndex);
        } else {
            // HERE WE NEED TO INSTEAD SET THE SLOT LOCALLY AND THEN SEND AN UPDATE PACKET
            handler.setFilterOnClient(slotIndex, handler.getStackInSlot(slotIndex));
        }

        return true;
    }

}
