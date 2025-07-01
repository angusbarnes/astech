package net.astr0.astech.gui;

import net.astr0.astech.compat.JEI.GhostIngredientHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractGuiSlot extends AsTechGuiElement {

    private final Rect2i _rect;


    public AbstractGuiSlot(int x, int y, int width, int height) {
        super(x, y);
        _rect = new Rect2i(x, y, width, height);
    }

    public abstract boolean canAcceptGhostIngredient(GhostIngredientHandler.DraggedIngredient ingredient);

    public abstract void handleFilterDrop(GhostIngredientHandler.DraggedIngredient ingredient);

    public Rect2i getRect() {
        return _rect;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    public boolean handleClick(BlockEntity be, double mouseX, double mouseY, int mouseButton, boolean isShifting) {
        return false;
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }
}
