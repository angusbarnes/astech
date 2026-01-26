package net.astr0.astrocraft.gui.tooltips;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ClientHazardTooltipComponent implements ClientTooltipComponent {
    private final HazardTooltipComponent component;

    public ClientHazardTooltipComponent(HazardTooltipComponent component) {
        this.component = component;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(getText());
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, new ResourceLocation("astech", component.isHazardous()
                ? "textures/gui/hazard_icon.png"
                : "textures/gui/tick_icon.png"));
        //pGuiGraphics.blit(poseStack, x, y, 0, 0, 8, 8, 8, 8);
    }

    @Override
    public void renderText(Font font, int pMouseX, int pMouseY, Matrix4f matrix, MultiBufferSource.BufferSource buffers) {
        font.drawInBatch(getText(), pMouseX + 10, pMouseY, component.isHazardous() ? 0xFF5555 : 0x55FF55, false, matrix, buffers, Font.DisplayMode.NORMAL, 0, 0);
    }

    private String getText() {
        return component.isHazardous() ? "HAZARDOUS" : "Safe to handle";
    }
}

