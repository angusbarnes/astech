package net.astr0.astech.gui.radial;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface IDrawingHelper
{
    void renderTooltip(GuiGraphics graphics, ItemStack stack, int mouseX, int mouseY);
}
