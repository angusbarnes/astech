package net.astr0.astech.gui;

import net.astr0.astech.AsTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Icons {

    public record Icon(int left, int top, int width, int height) {
        public String toString() {
            return "Icon(%d, %d, %d, %d)".formatted(left, top, width, height);
        }
    }

    public static final Icon SETTINGS = new Icon(0, 0, 17, 17);
    public static final Icon LOCKED = new Icon(0, 20, 17, 17);
    public static final Icon UNLOCKED = new Icon(0, 40, 17, 17);


    protected static final ResourceLocation WIDGET_TEXTURE = new ResourceLocation(AsTech.MODID, "textures/gui/widgets.png");

    public static void DrawIcon(GuiGraphics guiGraphics, Icon icon, int x, int y) {
        guiGraphics.blit(WIDGET_TEXTURE, x, y, icon.left, icon.top, icon.width, icon.height);
    }
}
