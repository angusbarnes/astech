package net.astr0.astech.block.CoolantBlock;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.astr0.astech.AsTech;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.gui.AsTechGuiElement;
import net.astr0.astech.gui.FilteredFluidTankSlot;
import net.astr0.astech.gui.IconButton;
import net.astr0.astech.gui.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

// This only gets registered on the client side
public class CoolantBlockScreen extends AbstractContainerScreen<CoolantBlockMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/coolant_block_gui.png");

    public CoolantBlockScreen(CoolantBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public final List<AsTechGuiElement> guiElements = new ArrayList<>();

    protected void addElement(AsTechGuiElement element) {
        guiElements.add(element);
    }

    private boolean isLocked = true;

    @Override
    protected void init() {
        super.init();

        addElement(new FilteredFluidTankSlot(this.menu.blockEntity, this.menu.blockEntity.getInputFluidHandler(), 0, this.leftPos + 44, this.topPos + 16));

        IconButton LOCK_BUTTON = new IconButton(this.leftPos + 5, this.topPos + 43, Icons.UNLOCKED, (button) -> {
            button.setIcon(button.getIcon() == Icons.UNLOCKED ? Icons.LOCKED : Icons.UNLOCKED);
            isLocked = !isLocked;
        });
        this.addRenderableWidget(LOCK_BUTTON);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        for(AsTechGuiElement element : guiElements) {
            element.renderBackground(guiGraphics);
        }

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean isShiftHeld = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (!isLocked) {
            for(AsTechGuiElement element : guiElements) {
                if(element.handleClick(this.menu.blockEntity, pMouseX, pMouseY, pButton, isShiftHeld)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private int getEnergyY(int energyHeight) {
        return this.topPos + 9 + (69-energyHeight);
    }

    private static int getEnergyHeight(int max, int value) {
        return  (int) (69 * ((float)value/max));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, Component.literal("Coolant Block"), 5, 5, GraphicsUtils.DEFAULT_INVENTORY_TEXT_COLOR, false);
    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, delta);

        for(AsTechGuiElement element : guiElements) {
            element.render(guiGraphics, mouseX, mouseY);
        }

        for(AsTechGuiElement element : guiElements) {
            element.renderTooltip(guiGraphics, mouseX, mouseY);
        }


        renderTooltip(guiGraphics, mouseX, mouseY);


        guiGraphics.drawString(this.font, String.format("Temp: %.2f%%", ((float)this.menu.getTemp()/3f)), this.leftPos + 90, this.topPos + 69, GraphicsUtils.DEFAULT_INVENTORY_TEXT_COLOR);
    }


}