package net.astr0.astech.gui;

import com.mojang.datafixers.kinds.IdF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class UIButton {

    private Icons.Icon iconToDraw;
    private TintColor color;
    private net.minecraft.client.gui.Font font;
    private int x;
    private int y;

    private MutableComponent tooltipString;

    private UIButton.OnPress onPress;

    public UIButton(Icons.Icon iconToDraw, TintColor color, int x, int y) {
        this.iconToDraw = iconToDraw;
        this.color = color;
        this.x = x;
        this.y = y;

        this.font = Minecraft.getInstance().font;

        tooltipString = Component.empty();
    }

    public UIButton(Icons.Icon iconToDraw, TintColor color, int x, int y, String name) {
        this.iconToDraw = iconToDraw;
        this.color = color;
        this.x = x;
        this.y = y;

        this.font = Minecraft.getInstance().font;

        tooltipString = Component.literal(name);
    }

    public UIButton(Icons.Icon iconToDraw, TintColor color, int x, int y, String name, OnPress onPress) {
        this.iconToDraw = iconToDraw;
        this.color = color;
        this.x = x;
        this.y = y;

        this.font = Minecraft.getInstance().font;

        tooltipString = Component.literal(name);

        this.onPress = onPress;
    }


    public void Render(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (isMouseWithinBounds(mouseX, mouseY)) {
            Icons.DrawIcon(guiGraphics, iconToDraw, color.darkened(40), x, y);

            guiGraphics.renderTooltip(font, tooltipString, mouseX, mouseY);
        } else {
            Icons.DrawIcon(guiGraphics, iconToDraw, color, x, y);
        }

    }

    public boolean isMouseWithinBounds(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + 17 && mouseY >= y && mouseY <= y + 17;
    }

    public void clicked() {
        if (onPress != null) {
            onPress.onPress(this);
        }
    }

    public void SetOnPress(UIButton.OnPress onPress) {
        this.onPress = onPress;
    }

    public void SetIconToDraw(Icons.Icon iconToDraw) {
        this.iconToDraw = iconToDraw;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public TintColor getColor() {
        return color;
    }

    public void setColor(TintColor color) {
        this.color = color;
    }

    public void SetTooltip(String tooltip) {
        tooltipString = Component.literal(tooltip);
    }

    public interface OnPress {
        void onPress(UIButton button);
    }
}
