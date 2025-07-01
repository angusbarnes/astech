package net.astr0.astech.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class AsTechGuiScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public AsTechGuiScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    protected final List<AsTechGuiElement> guiElements = new ArrayList<>();

    protected void addElement(AsTechGuiElement element) {
        guiElements.add(element);
    }

    public List<AsTechGuiElement> getGuiElements() {
        return guiElements;
    }
}
