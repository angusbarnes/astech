package net.astr0.astech.gui;

import net.astr0.astech.AsTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;


public abstract class AsTechGuiElement {

    protected final int x, y;

    protected final Font font;

    protected static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/widgets.png");

    public AsTechGuiElement(int x, int y) {
        this.x = x;
        this.y = y;
        this.font = Minecraft.getInstance().font;
    }

    public abstract void renderBackground(GuiGraphics guiGraphics);
    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY);
    public abstract boolean handleClick(BlockEntity be, double mouseX, double mouseY, int mouseButton, boolean isShifting);
    public abstract void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY);

    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return pMouseX >= (double)(pX - 1) && pMouseX < (double)(pX + pWidth + 1) && pMouseY >= (double)(pY - 1) && pMouseY < (double)(pY + pHeight + 1);
    }
}
