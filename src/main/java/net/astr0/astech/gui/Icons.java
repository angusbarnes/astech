package net.astr0.astech.gui;

import net.astr0.astech.AsTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

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
    public static final Icon BLANK = new Icon(0, 60, 17, 17);
    public static final Icon FLUID = new Icon(0, 80, 17, 17);
    public static final Icon ITEM = new Icon(0, 100, 17, 17);

    protected static final ResourceLocation WIDGET_TEXTURE = new ResourceLocation(AsTech.MODID, "textures/gui/widgets.png");

    public static void DrawIcon(GuiGraphics guiGraphics, Icon icon, int x, int y) {
        guiGraphics.blit(WIDGET_TEXTURE, x, y, icon.left, icon.top, icon.width, icon.height);
    }

    public static void DrawIcon(GuiGraphics guiGraphics, Icon icon, TintColor color, int x, int y) {
        Vector4f tintColor = color.getAsNormalisedRenderColor();
        guiGraphics.setColor(tintColor.x, tintColor.y, tintColor.z, tintColor.w);
        guiGraphics.blit(WIDGET_TEXTURE, x, y, icon.left, icon.top, icon.width, icon.height);
        guiGraphics.setColor(1f, 1f, 1f, 1f);
    }
}
