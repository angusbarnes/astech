package net.astr0.astech.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IconButton extends AbstractButton {

    protected Icons.Icon iconToDraw;
    private final IconButton.OnPress onPress;

    public IconButton(int pX, int pY, Icons.Icon icon, IconButton.OnPress pOnPress) {
        super(pX, pY, icon.width(), icon.height(), Component.empty());
        this.onPress = pOnPress;
        iconToDraw = icon;
        setTooltip(Tooltip.create(Component.literal("ToolTip Attempt")));
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        if (this. isHovered) {
            pGuiGraphics.setColor(0.9F, 0.9F, 0.9F, this.alpha);
        } else {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Icons.DrawIcon(pGuiGraphics, iconToDraw, this.getX(), this.getY());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setIcon(Icons.Icon icon) {
        iconToDraw = icon;
        LogUtils.getLogger().info("Button was clicked -> set icon was called {}", icon.toString());
    }

    public Icons.Icon getIcon() {
        return iconToDraw;
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(IconButton button);
    }
}
