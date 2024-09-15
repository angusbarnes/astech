package net.astr0.astech.block.PyrolysisChamber;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.astr0.astech.AsTech;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.gui.*;
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
public class PyrolysisChamberScreen extends AbstractContainerScreen<PyrolysisChamberMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/electrolytic_seperator.png");

    public PyrolysisChamberScreen(PyrolysisChamberMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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

        addElement(new FilteredFluidTankSlot(this.menu.blockEntity.getInputFluidHandler(), 0, this.leftPos + 44, this.topPos + 16));

        addElement(new FluidTankSlot(this.menu.blockEntity.getOutputTank1(), this.leftPos + 110, this.topPos + 16));
        addElement(new FluidTankSlot(this.menu.blockEntity.getOutputTank2(), this.leftPos + 129, this.topPos + 16));

        MachineCapConfiguratorWidget config = new MachineCapConfiguratorWidget(this.leftPos - 40, this.topPos + 30, this.menu.blockEntity);
        config.MODE_SWITCH_BUTTON.clicked(); // This is a phat hack

        IconButton SETTINGS_BUTTON = new IconButton(this.leftPos + 5, this.topPos + 21, Icons.SETTINGS, (button) -> {
            config.ToggleRender();
        });

        IconButton LOCK_BUTTON = new IconButton(this.leftPos + 5, this.topPos + 43, Icons.UNLOCKED, (button) -> {
            button.setIcon(button.getIcon() == Icons.UNLOCKED ? Icons.LOCKED : Icons.UNLOCKED);
            isLocked = !isLocked;
        });

        this.addRenderableWidget(LOCK_BUTTON);
        this.addRenderableWidget(SETTINGS_BUTTON);
        this.addRenderableWidget(config);
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

        renderEnergyBar(guiGraphics, 155);
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
        pGuiGraphics.drawString(this.font, Component.literal("Pyrolysis Chamber"), 5, 5, GraphicsUtils.DEFAULT_INVENTORY_TEXT_COLOR, false);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x, y, 183, 0, menu.getScaledProgress(), 8);
        }
    }

    private void renderEnergyBar(GuiGraphics guiGraphics, int x) {

        int height = getEnergyHeight(menu.getMaxEnergy(), menu.getEnergy());
        guiGraphics.blit(TEXTURE,
                this.leftPos + x,
                getEnergyY(height),
                178,
                9,
                10,
                height
        );
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

        renderProgressArrow(guiGraphics, this.leftPos + 69, this.topPos + 40);

        renderTooltip(guiGraphics, mouseX, mouseY);

        renderEnergyTooltip(guiGraphics, mouseX, mouseY, 154);
    }

    private void renderEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x) {

        int fluidHeight = getEnergyHeight(menu.getMaxEnergy(), menu.getEnergy());

        if(!isHovering(x, getEnergyY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        List<Component> tips = new ArrayList<>(2);
        tips.add(Component.literal("Energy"));
        tips.add(Component.literal("§7%.2f%%".formatted(((float)menu.getEnergy()/ menu.getMaxEnergy()) * 100)));
        guiGraphics.renderComponentTooltip(this.font, tips, mouseX, mouseY);
    }
}