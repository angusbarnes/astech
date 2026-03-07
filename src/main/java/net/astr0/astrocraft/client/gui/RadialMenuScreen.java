package net.astr0.astrocraft.client.gui;

import net.astr0.astrocraft.client.render.RadialMenuRenderer;
import net.astr0.astrocraft.common.RadialMenu;
import net.astr0.astrocraft.common.RadialMenuEntry;
import net.astr0.astrocraft.network.AsTechNetworkHandler;
import net.astr0.astrocraft.network.ServerboundSetToolModePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.Collections;
import java.util.Set;

/**
 * The Minecraft {@link Screen} that hosts the radial menu.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Build your menu
 * RadialMenu menu = RadialMenu.builder(Component.literal("Tool Mode"))
 *         .addEntry(RadialMenuEntry.builder("mode_mine", Component.literal("Mine"))
 *                 .icon(new ItemStack(Items.IRON_PICKAXE))
 *                 .onSelect(() -> applyMode("mine"))
 *                 .build())
 *         .addEntry(...)
 *         .defaultIndex(currentModeIndex)
 *         .build();
 *
 * // Open it
 * RadialMenuScreen screen = new RadialMenuScreen(menu, "mytool:mode_select");
 * Minecraft.getInstance().setScreen(screen);
 * }</pre>
 *
 * <h2>Behaviour</h2>
 * <ul>
 *   <li>While open, the mouse is freed from looking around.</li>
 *   <li>Moving the mouse snaps the highlight to whichever slice it points at.</li>
 *   <li>The menu closes when the key is released (handled by the key binding handler)
 *       or when {@link #onClose()} is called for any other reason.</li>
 *   <li>On close, if a valid selection was made the client-side {@code onSelect}
 *       callback fires, and a {@link ServerboundSetToolModePacket} is sent to the server.</li>
 * </ul>
 */
public class RadialMenuScreen extends Screen {

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final RadialMenu menu;
    /** Identifies this menu instance to the server (e.g. "mytool:mode_select"). */
    private final String menuId;

    private final RadialMenuRenderer renderer = new RadialMenuRenderer();

    /** Centre of the wheel in screen coordinates. Updated each frame in render(). */
    private float centreX;
    private float centreY;

    /**
     * Index of the slice the cursor is currently over, or -1 for the dead zone.
     * Snaps immediately — no smooth interpolation needed for a radial snap UI.
     */
    private int hoveredIndex;

    /**
     * The confirmed selection (set on close). -1 = cancelled / no selection.
     */
    private int confirmedIndex = -1;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public RadialMenuScreen(RadialMenu menu, String menuId) {
        super(menu.getTitle());
        this.menu        = menu;
        this.menuId      = menuId;
        this.hoveredIndex = menu.getDefaultIndex(); // pre-snap to current mode
    }

    // -------------------------------------------------------------------------
    // Screen lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void init() {
        // Don't add any widgets; all rendering is custom.
        centreX = (float) width  / 2f;
        centreY = (float) height / 2f;
    }

    @Override
    public boolean isPauseScreen() {
        // We don't want to pause the game
        return false;
    }

    /**
     * Called every frame. Updates the hovered slice based on current mouse position,
     * then delegates all rendering to {@link RadialMenuRenderer}.
     */
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Update hovered slice from cursor position
        double dx = mouseX - centreX;
        double dy = mouseY - centreY;
        hoveredIndex = menu.getHoveredIndex(dx, dy, RadialMenuRenderer.DEAD_ZONE);

        // Dim the world behind the menu
        renderBackground(graphics);

        // Draw the radial wheel
        renderer.render(graphics, menu, centreX, centreY,
                hoveredIndex, menu.getDefaultIndex(), activeModeIds);

        // Don't call super — no vanilla widgets
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    /**
     * Pressing Escape cancels the menu without selection.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            // cancelled — confirmedIndex stays -1
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Left-click also confirms the current hovered selection.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && hoveredIndex >= 0) {
            confirmSelection(hoveredIndex);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // -------------------------------------------------------------------------
    // Selection confirmation
    // -------------------------------------------------------------------------

    /**
     * Call this when the radial-menu key is released to finalise the choice.
     * If the cursor is in the dead zone (hoveredIndex == -1) the selection is
     * treated as cancelled and the menu closes without action.
     */
    public void confirmAndClose() {
        if (hoveredIndex >= 0) {
            confirmSelection(hoveredIndex);
        } else {
            // Dead-zone release — no-op, just close
            Minecraft.getInstance().setScreen(null);
        }
    }

    private void confirmSelection(int index) {
        confirmedIndex = index;
        RadialMenuEntry entry = menu.getEntry(index);

        // 1. Fire client-side callback (local feedback, sound, etc.)
        entry.onSelect();

        // 2. Notify server
        AsTechNetworkHandler.INSTANCE.sendToServer(
                new ServerboundSetToolModePacket(menuId, entry.getId()));

        // 3. Close
        Minecraft.getInstance().setScreen(null);
    }

    private Set<String> activeModeIds = Collections.emptySet();

    // Add setter (call this before opening the screen):
    public void setActiveModes(Set<String> ids) { this.activeModeIds = ids; }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getHoveredIndex()   { return hoveredIndex;   }
    public int getConfirmedIndex() { return confirmedIndex; }
    public RadialMenu getMenu()    { return menu;           }
}
