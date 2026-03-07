package net.astr0.astrocraft.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.astr0.astrocraft.common.RadialMenu;
import net.astr0.astrocraft.common.RadialMenuEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Set;

public class RadialMenuRenderer {

    // -------------------------------------------------------------------------
    // Layout
    // -------------------------------------------------------------------------

    public static final float OUTER_RADIUS = 85f;
    public static final float INNER_RADIUS = 22f;
    public static final float DEAD_ZONE    = INNER_RADIUS;

    private static final float ICON_RADIUS      = (OUTER_RADIUS + INNER_RADIUS) / 2f;
    private static final float INDICATOR_RADIUS = OUTER_RADIUS - 12f;
    private static final int   ARC_SEGMENTS     = 64;

    // -------------------------------------------------------------------------
    // Palette
    // -------------------------------------------------------------------------

    private static final int COL_INACTIVE_FILL  = color(0.08f, 0.08f, 0.08f, 0.15f);
    private static final int COL_ACTIVE_FILL    = color(0.08f, 0.725f, 0.871f, 0.2f);
    private static final int COL_HOVER_INACTIVE = color(1.0f, 0.24f, 0.24f, 0.30f);
    private static final int COL_HOVER_ACTIVE   = color(0.24f, 1.0f, 0.24f, 0.30f);
    private static final int COL_GLOW_INACTIVE  = color(0.40f, 0.50f, 1.00f, 0.80f);
    private static final int COL_GLOW_ACTIVE    = color(0.25f, 0.90f, 0.45f, 0.80f);
    private static final int COL_DIVIDER        = color(0.30f, 0.30f, 0.50f, 0.55f);
    private static final int COL_RING_OUTER     = color(0.35f, 0.35f, 0.60f, 0.70f);
    private static final int COL_HUB_BASE       = color(0.05f, 0.05f, 0.14f, 0.1f);
    private static final int COL_HUB_RING       = color(0.45f, 0.45f, 0.75f, 0.15f);

    private static final int COL_LABEL_ACTIVE   = 0xFF90EE90;
    private static final int COL_LABEL_INACTIVE = 0xFFCCCCDD;
    private static final int COL_TITLE          = 0xFF99BBFF;
    private static final int COL_STRIP_BG       = 0xCC050510;
    private static final int COL_STRIP_TEXT     = 0xFFEEEEFF;
    private static final int COL_TICK           = 0xFF55FF88;
    private static final int COL_CROSS          = 0xFFFF5555;

    // -------------------------------------------------------------------------
    // Render Entry Point
    // -------------------------------------------------------------------------

    public void render(GuiGraphics graphics, RadialMenu menu,
                       float cx, float cy,
                       int hoveredIndex,
                       int selectedIndex,
                       Set<String> activeModeIds) {

        int n = menu.getEntryCount();
        if (n == 0) return;

        graphics.pose().pushPose();
        Matrix4f pose = graphics.pose().last().pose();

        // 1 — base slice fills
        for (int i = 0; i < n; i++) {
            boolean active = activeModeIds.contains(menu.getEntry(i).getId());
            drawSlice(pose, cx, cy, i, n, INNER_RADIUS, OUTER_RADIUS,
                    active ? COL_ACTIVE_FILL : COL_INACTIVE_FILL);
        }

        // 2 — hover overlay
        if (hoveredIndex >= 0) {
            boolean active = activeModeIds.contains(menu.getEntry(hoveredIndex).getId());
            drawSlice(pose, cx, cy, hoveredIndex, n, INNER_RADIUS, OUTER_RADIUS,
                    active ? COL_HOVER_ACTIVE : COL_HOVER_INACTIVE);
        }

        // 3 — outer glow arc (converted to thick quad)
        if (hoveredIndex >= 0) {
            boolean active = activeModeIds.contains(menu.getEntry(hoveredIndex).getId());
            drawThickArcEdge(pose, cx, cy, hoveredIndex, n, OUTER_RADIUS, 5f,
                    active ? COL_GLOW_ACTIVE : COL_GLOW_INACTIVE);
        }

        // 4 — dividers (1px standard line is fine here)
        drawDividers(pose, cx, cy, n, INNER_RADIUS, OUTER_RADIUS);

        // 5 — outer boundary ring (converted to thick quad)
        drawThickFullRing(pose, cx, cy, OUTER_RADIUS, 2.5f, COL_RING_OUTER);

        // 6 — hub
        drawFilledCircle(pose, cx, cy, INNER_RADIUS, COL_HUB_BASE);
        drawThickFullRing(pose, cx, cy, INNER_RADIUS, 2.5f, COL_HUB_RING);

        // 7 — icons / labels
        for (int i = 0; i < n; i++) {
            RadialMenuEntry entry = menu.getEntry(i);
            boolean active = activeModeIds.contains(entry.getId());
            float mid = sliceMidAngle(i, n);
            float ix = cx + (float) Math.cos(mid) * ICON_RADIUS;
            float iy = cy + (float) Math.sin(mid) * ICON_RADIUS;

            if (entry.hasIcon()) {
                renderItemIcon(graphics, entry, ix, iy);
            } else {
                renderTextLabel(graphics, entry, ix, iy, active);
            }
            renderIndicator(graphics, cx, cy, i, n, active);
        }

        // 8 & 9 — Overlays
        renderInfoStrip(graphics, menu, cx, cy, hoveredIndex, activeModeIds);
        renderTitle(graphics, menu, cx, cy);

        graphics.pose().popPose();
    }

    // -------------------------------------------------------------------------
    // Geometry — slices
    // -------------------------------------------------------------------------

    private void drawSlice(Matrix4f pose, float cx, float cy, int i, int n,
                           float inner, float outer, int argb) {
        float start = sliceStartAngle(i, n);
        float end   = sliceStartAngle(i + 1, n);

        int a = (argb >> 24) & 0xFF, r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF,  b = argb & 0xFF;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull(); // Fix for inverted UI Y-axis culling

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segs = Math.max(6, ARC_SEGMENTS / n * 2);
        for (int s = 0; s <= segs; s++) {
            float angle = start + (end - start) * s / segs;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            buf.vertex(pose, cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void drawThickArcEdge(Matrix4f pose, float cx, float cy, int i, int n,
                                  float radius, float thickness, int argb) {
        float start = sliceStartAngle(i, n);
        float end   = sliceStartAngle(i + 1, n);

        int a = (argb >> 24) & 0xFF, r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF,  b = argb & 0xFF;

        float inner = radius - thickness / 2f;
        float outer = radius + thickness / 2f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segs = Math.max(6, ARC_SEGMENTS / n * 2);
        for (int s = 0; s <= segs; s++) {
            float angle = start + (end - start) * s / segs;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            buf.vertex(pose, cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void drawDividers(Matrix4f pose, float cx, float cy, int n, float inner, float outer) {
        int a = (COL_DIVIDER >> 24) & 0xFF, r = (COL_DIVIDER >> 16) & 0xFF;
        int g = (COL_DIVIDER >> 8) & 0xFF,  b = COL_DIVIDER & 0xFF;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < n; i++) {
            float angle = sliceStartAngle(i, n);
            float cos = (float) Math.cos(angle), sin = (float) Math.sin(angle);
            buf.vertex(pose, cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private void drawFilledCircle(Matrix4f pose, float cx, float cy, float radius, int argb) {
        int a = (argb >> 24) & 0xFF, r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF,  b = argb & 0xFF;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        float prevX = cx + radius;
        float prevY = cy;

        for (int i = 1; i <= ARC_SEGMENTS; i++) {
            float angle = (float) (2 * Math.PI * i / ARC_SEGMENTS);
            float nextX = cx + (float) Math.cos(angle) * radius;
            float nextY = cy + (float) Math.sin(angle) * radius;

            buf.vertex(pose, cx, cy, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, prevX, prevY, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, nextX, nextY, 0).color(r, g, b, a).endVertex();

            prevX = nextX;
            prevY = nextY;
        }

        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void drawThickFullRing(Matrix4f pose, float cx, float cy, float radius, float thickness, int argb) {
        int a = (argb >> 24) & 0xFF, r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF,  b = argb & 0xFF;

        float inner = radius - thickness / 2f;
        float outer = radius + thickness / 2f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i <= ARC_SEGMENTS; i++) {
            float angle = (float) (2 * Math.PI * i / ARC_SEGMENTS);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            buf.vertex(pose, cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(pose, cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    // -------------------------------------------------------------------------
    // Icons, labels, indicators (Unchanged, functioning perfectly)
    // -------------------------------------------------------------------------

    private void renderItemIcon(GuiGraphics graphics, RadialMenuEntry entry, float ix, float iy) {
        graphics.renderItem(entry.getIcon(), Math.round(ix) - 8, Math.round(iy) - 8);
    }

    private void renderTextLabel(GuiGraphics graphics, RadialMenuEntry entry, float ix, float iy, boolean active) {
        Minecraft mc = Minecraft.getInstance();
        int colour = active ? COL_LABEL_ACTIVE : COL_LABEL_INACTIVE;
        int maxW = (int) (OUTER_RADIUS - INNER_RADIUS) - 4;
        List<net.minecraft.util.FormattedCharSequence> lines = mc.font.split(entry.getLabel(), maxW);

        int totalH = lines.size() * (mc.font.lineHeight + 1);
        float baseY = iy - totalH / 2f;
        for (int li = 0; li < lines.size(); li++) {
            var line = lines.get(li);
            int lw = mc.font.width(line);
            graphics.drawString(mc.font, line,
                    (int) (ix - lw / 2f),
                    (int) (baseY + li * (mc.font.lineHeight + 1)),
                    colour, true);
        }
    }

    private void renderIndicator(GuiGraphics graphics, float cx, float cy, int i, int n, boolean active) {
        Minecraft mc = Minecraft.getInstance();
        float mid = sliceMidAngle(i, n);
        float sx = cx + (float) Math.cos(mid) * INDICATOR_RADIUS;
        float sy = cy + (float) Math.sin(mid) * INDICATOR_RADIUS;

        String symbol = active ? "✔" : "✘";
        int colour    = active ? COL_TICK : COL_CROSS;
        int w = mc.font.width(symbol);
        graphics.drawString(mc.font, symbol, Math.round(sx) - w / 2, Math.round(sy) - mc.font.lineHeight / 2, colour, true);
    }

    private void renderInfoStrip(GuiGraphics graphics, RadialMenu menu, float cx, float cy, int hoveredIndex, Set<String> activeModeIds) {
        if (hoveredIndex < 0) return;

        RadialMenuEntry entry = menu.getEntry(hoveredIndex);
        boolean active = activeModeIds.contains(entry.getId());

        String statusSuffix = active ? "  §a[ON]" : "  §c[OFF]";
        String label = entry.getLabel().getString() + statusSuffix;

        Minecraft mc = Minecraft.getInstance();
        int textW = mc.font.width(label);
        int padX = 10, padY = 4;
        int sx = Math.round(cx) - (textW + padX * 2) / 2;
        int sy = Math.round(cy) + (int) OUTER_RADIUS + 10;

        graphics.fill(sx - 1, sy - 1, sx + textW + padX * 2 + 1, sy + mc.font.lineHeight + padY * 2 + 1, color(0.20f, 0.20f, 0.40f, 0.50f));
        graphics.fill(sx, sy, sx + textW + padX * 2, sy + mc.font.lineHeight + padY * 2, COL_STRIP_BG);
        graphics.drawString(mc.font, label, sx + padX, sy + padY, COL_STRIP_TEXT, true);
    }

    private void renderTitle(GuiGraphics graphics, RadialMenu menu, float cx, float cy) {
        Minecraft mc = Minecraft.getInstance();
        String title = menu.getTitle().getString();
        int w = mc.font.width(title);
        graphics.drawString(mc.font, title, Math.round(cx) - w / 2, Math.round(cy) - (int) OUTER_RADIUS - mc.font.lineHeight - 8, COL_TITLE, true);
    }

    // -------------------------------------------------------------------------
    // Angle helpers
    // -------------------------------------------------------------------------

    public static float sliceStartAngle(int i, int n) {
        return (float) (-Math.PI / 2) + i * (float) (2 * Math.PI / n);
    }

    public static float sliceMidAngle(int i, int n) {
        return sliceStartAngle(i, n) + (float) (Math.PI / n);
    }

    // -------------------------------------------------------------------------
    // Colour helpers
    // -------------------------------------------------------------------------

    private static int color(float r, float g, float b, float a) {
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }
}