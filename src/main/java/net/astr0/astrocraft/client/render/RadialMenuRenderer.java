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

import java.util.List;
import java.util.Set;

/**
 * Handles all low-level GL/vertex rendering for the radial menu.
 *
 * The renderer draws:
 *   1. Semi-transparent slice backgrounds (fan triangles)
 *   2. Divider lines between slices
 *   3. Hover highlight on the active slice
 *   4. Item icons or text labels inside each slice
 *   5. A centre hub circle
 *   6. A label strip at the bottom showing the hovered entry name
 */
public class RadialMenuRenderer {

    // -------------------------------------------------------------------------
    // Layout
    // -------------------------------------------------------------------------

    public static final float OUTER_RADIUS = 85f;
    public static final float INNER_RADIUS = 22f;
    public static final float DEAD_ZONE    = INNER_RADIUS;

    private static final float ICON_RADIUS      = (OUTER_RADIUS + INNER_RADIUS) / 2f;
    private static final float INDICATOR_RADIUS = OUTER_RADIUS - 12f; // tick/cross orbit
    private static final int   ARC_SEGMENTS     = 64; // smoother arcs

    // -------------------------------------------------------------------------
    // Palette
    // -------------------------------------------------------------------------

    // Slice base fills
    private static final int COL_INACTIVE_FILL  = color(0.08f, 0.08f, 1f, 0.88f);
    private static final int COL_ACTIVE_FILL    = color(1f, 0.5f, 0.5f, 0.92f);

    // Hover overlays (blended on top of the base fill)
    private static final int COL_HOVER_INACTIVE = color(0.20f, 0.25f, 0.55f, 0.55f);
    private static final int COL_HOVER_ACTIVE   = color(0.15f, 0.55f, 0.28f, 0.55f);

    // Glow ring on hovered slice outer edge
    private static final int COL_GLOW_INACTIVE  = color(0.40f, 0.50f, 1.00f, 0.80f);
    private static final int COL_GLOW_ACTIVE    = color(0.25f, 0.90f, 0.45f, 0.80f);

    // Dividers
    private static final int COL_DIVIDER        = color(0.30f, 0.30f, 0.50f, 0.55f);

    // Outer ring
    private static final int COL_RING_OUTER     = color(0.35f, 0.35f, 0.60f, 0.70f);

    // Hub
    private static final int COL_HUB_BASE       = color(0.05f, 0.05f, 0.14f, 0.96f);
    private static final int COL_HUB_RING       = color(0.45f, 0.45f, 0.75f, 0.90f);

    // Text
    private static final int COL_LABEL_ACTIVE   = 0xFF90EE90; // light green
    private static final int COL_LABEL_INACTIVE = 0xFFCCCCDD;
    private static final int COL_TITLE          = 0xFF99BBFF;
    private static final int COL_STRIP_BG       = 0xCC050510;
    private static final int COL_STRIP_TEXT     = 0xFFEEEEFF;

    // Indicator symbols
    private static final int COL_TICK           = 0xFF55FF88;
    private static final int COL_CROSS          = 0xFFFF5555;

    // -------------------------------------------------------------------------
    // Public render entry point
    // -------------------------------------------------------------------------

    /**
     * @param activeModeIds set of entry IDs that are currently toggled ON.
     *                      Pass {@link Collections#emptySet()} if not applicable.
     */
    public void render(GuiGraphics graphics, RadialMenu menu,
                       float cx, float cy,
                       int hoveredIndex,
                       int selectedIndex,          // kept for API compat, unused visually
                       Set<String> activeModeIds) {

        int n = menu.getEntryCount();
        if (n == 0) return;

        graphics.pose().pushPose();

        // 1 — base slice fills
        for (int i = 0; i < n; i++) {
            boolean active = activeModeIds.contains(menu.getEntry(i).getId());
            drawSlice(cx, cy, i, n, INNER_RADIUS, OUTER_RADIUS,
                    active ? COL_ACTIVE_FILL : COL_INACTIVE_FILL);
        }

        // 2 — hover overlay (on top of base, before icons so glow is behind text)
        if (hoveredIndex >= 0) {
            boolean active = activeModeIds.contains(menu.getEntry(hoveredIndex).getId());
            drawSlice(cx, cy, hoveredIndex, n, INNER_RADIUS, OUTER_RADIUS,
                    active ? COL_HOVER_ACTIVE : COL_HOVER_INACTIVE);
        }

        // 3 — outer glow arc for hovered slice
        if (hoveredIndex >= 0) {
            boolean active = activeModeIds.contains(menu.getEntry(hoveredIndex).getId());
            drawArcEdge(cx, cy, hoveredIndex, n, OUTER_RADIUS,
                    active ? COL_GLOW_ACTIVE : COL_GLOW_INACTIVE, 12f);
        }

        // 4 — dividers
        drawDividers(cx, cy, n, INNER_RADIUS, OUTER_RADIUS);

        // 5 — outer boundary ring
        drawFullRingArc(cx, cy, OUTER_RADIUS, COL_RING_OUTER, 6f);

        // 6 — hub
        drawFilledCircle(cx, cy, INNER_RADIUS, COL_HUB_BASE);
        drawFullRingArc(cx, cy, INNER_RADIUS, COL_HUB_RING, 6f);

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

            // tick / cross indicator
            renderIndicator(graphics, cx, cy, i, n, active);
        }

        // 8 — bottom info strip
        renderInfoStrip(graphics, menu, cx, cy, hoveredIndex, activeModeIds);

        // 9 — title
        renderTitle(graphics, menu, cx, cy);

        graphics.pose().popPose();
    }

    // -------------------------------------------------------------------------
    // Geometry — slices
    // -------------------------------------------------------------------------

    private void drawSlice(float cx, float cy, int i, int n,
                           float inner, float outer, int argb) {
        float start = sliceStartAngle(i, n);
        float end   = sliceStartAngle(i + 1, n);

        float a = af(argb, 24), r = af(argb, 16), g = af(argb, 8), b = af(argb, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segs = Math.max(6, ARC_SEGMENTS / n * 2);
        for (int s = 0; s <= segs; s++) {
            float angle = start + (end - start) * s / segs;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            buf.vertex(cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    /** Draw just the outer arc edge of one slice — used for the hover glow. */
    private void drawArcEdge(float cx, float cy, int i, int n,
                             float radius, int argb, float lineWidth) {
        float start = sliceStartAngle(i, n);
        float end   = sliceStartAngle(i + 1, n);
        float a = af(argb, 24), r = af(argb, 16), g = af(argb, 8), b = af(argb, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(lineWidth);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        int segs = Math.max(6, ARC_SEGMENTS / n * 2);
        for (int s = 0; s <= segs; s++) {
            float angle = start + (end - start) * s / segs;
            buf.vertex(cx + (float) Math.cos(angle) * radius,
                            cy + (float) Math.sin(angle) * radius, 0)
                    .color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private void drawDividers(float cx, float cy, int n, float inner, float outer) {
        float a = af(COL_DIVIDER, 24), r = af(COL_DIVIDER, 16),
                g = af(COL_DIVIDER, 8),  b = af(COL_DIVIDER, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(1.2f);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < n; i++) {
            float angle = sliceStartAngle(i, n);
            float cos = (float) Math.cos(angle), sin = (float) Math.sin(angle);
            buf.vertex(cx + cos * inner, cy + sin * inner, 0).color(r, g, b, a).endVertex();
            buf.vertex(cx + cos * outer, cy + sin * outer, 0).color(r, g, b, a).endVertex();
        }

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private void drawFilledCircle(float cx, float cy, float radius, int argb) {
        float a = af(argb, 24), r = af(argb, 16), g = af(argb, 8), b = af(argb, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buf.vertex(cx, cy, 0).color(r, g, b, a).endVertex();
        for (int i = 0; i <= ARC_SEGMENTS; i++) {
            float angle = (float) (2 * Math.PI * i / ARC_SEGMENTS);
            buf.vertex(cx + (float) Math.cos(angle) * radius,
                            cy + (float) Math.sin(angle) * radius, 0)
                    .color(r, g, b, a).endVertex();
        }
        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private void drawFullRingArc(float cx, float cy, float radius, int argb, float lineWidth) {
        float a = af(argb, 24), r = af(argb, 16), g = af(argb, 8), b = af(argb, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(lineWidth);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= ARC_SEGMENTS; i++) {
            float angle = (float) (2 * Math.PI * i / ARC_SEGMENTS);
            buf.vertex(cx + (float) Math.cos(angle) * radius,
                            cy + (float) Math.sin(angle) * radius, 0)
                    .color(r, g, b, a).endVertex();
        }
        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    // -------------------------------------------------------------------------
    // Icons, labels, indicators
    // -------------------------------------------------------------------------

    private void renderItemIcon(GuiGraphics graphics, RadialMenuEntry entry,
                                float ix, float iy) {
        graphics.renderItem(entry.getIcon(), Math.round(ix) - 8, Math.round(iy) - 8);
    }

    private void renderTextLabel(GuiGraphics graphics, RadialMenuEntry entry,
                                 float ix, float iy, boolean active) {
        Minecraft mc = Minecraft.getInstance();
        int colour = active ? COL_LABEL_ACTIVE : COL_LABEL_INACTIVE;
        int maxW = (int) (OUTER_RADIUS - INNER_RADIUS) - 4;
        List<net.minecraft.util.FormattedCharSequence> lines =
                mc.font.split(entry.getLabel(), maxW);

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

    /**
     * Renders a small ✔ or ✘ near the outer rim of each slice.
     * The symbol sits at {@link #INDICATOR_RADIUS}, centred on the slice mid-angle.
     */
    private void renderIndicator(GuiGraphics graphics,
                                 float cx, float cy,
                                 int i, int n, boolean active) {
        Minecraft mc = Minecraft.getInstance();
        float mid = sliceMidAngle(i, n);
        float sx = cx + (float) Math.cos(mid) * INDICATOR_RADIUS;
        float sy = cy + (float) Math.sin(mid) * INDICATOR_RADIUS;

        String symbol = active ? "✔" : "✘";
        int colour    = active ? COL_TICK : COL_CROSS;
        int w = mc.font.width(symbol);
        graphics.drawString(mc.font, symbol,
                Math.round(sx) - w / 2,
                Math.round(sy) - mc.font.lineHeight / 2,
                colour, true);
    }

    private void renderInfoStrip(GuiGraphics graphics, RadialMenu menu,
                                 float cx, float cy,
                                 int hoveredIndex, Set<String> activeModeIds) {
        if (hoveredIndex < 0) return;

        RadialMenuEntry entry = menu.getEntry(hoveredIndex);
        boolean active = activeModeIds.contains(entry.getId());

        // "Active" / "Inactive" status suffix
        String statusSuffix = active ? "  §a[ON]" : "  §c[OFF]";
        String label = entry.getLabel().getString() + statusSuffix;

        Minecraft mc = Minecraft.getInstance();
        int textW = mc.font.width(label);
        int padX = 10, padY = 4;
        int sx = Math.round(cx) - (textW + padX * 2) / 2;
        int sy = Math.round(cy) + (int) OUTER_RADIUS + 10;

        // Background pill
        graphics.fill(sx - 1, sy - 1,
                sx + textW + padX * 2 + 1, sy + mc.font.lineHeight + padY * 2 + 1,
                color(0.20f, 0.20f, 0.40f, 0.50f)); // subtle border
        graphics.fill(sx, sy,
                sx + textW + padX * 2, sy + mc.font.lineHeight + padY * 2,
                COL_STRIP_BG);
        graphics.drawString(mc.font, label, sx + padX, sy + padY, COL_STRIP_TEXT, true);
    }

    private void renderTitle(GuiGraphics graphics, RadialMenu menu, float cx, float cy) {
        Minecraft mc = Minecraft.getInstance();
        String title = menu.getTitle().getString();
        int w = mc.font.width(title);
        graphics.drawString(mc.font, title,
                Math.round(cx) - w / 2,
                Math.round(cy) - (int) OUTER_RADIUS - mc.font.lineHeight - 8,
                COL_TITLE, true);
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

    /** Pack normalised floats into an ARGB int. */
    private static int color(float r, float g, float b, float a) {
        return ((int)(a * 255) << 24) | ((int)(r * 255) << 16)
                | ((int)(g * 255) << 8)  |  (int)(b * 255);
    }

    /** Extract a normalised float channel from an ARGB int. */
    private static float af(int argb, int shift) {
        return ((argb >> shift) & 0xFF) / 255f;
    }
}
