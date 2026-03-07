package net.astr0.astrocraft.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.astr0.astrocraft.client.gui.RadialMenuScreen;
import net.astr0.astrocraft.common.RadialMenu;
import net.astr0.astrocraft.common.RadialMenuEntry;
import net.astr0.astrocraft.item.TabletItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Registers the radial menu key binding and handles press/release events.
 *
 * <h2>Key binding behaviour</h2>
 * <ul>
 *   <li><b>Press</b>: opens the {@link RadialMenuScreen} (if not already open).</li>
 *   <li><b>Release</b>: calls {@link RadialMenuScreen#confirmAndClose()}, finalising
 *       the hovered selection and sending the packet.</li>
 * </ul>
 *
 * This "hold to pick" pattern is common in games (e.g. Minecraft's item swap with F).
 *
 * <h2>Registration</h2>
 * This class is used as a Forge mod-bus event listener. Register it from your
 * client-only event subscriber:
 *
 * <pre>{@code
 * // In your ClientSetup or @EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD):
 * @SubscribeEvent
 * public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
 *     RadialMenuKeyHandler.registerKeys(event);
 * }
 * }</pre>
 *
 * Then, on the FORGE bus (not MOD bus), register an instance:
 * <pre>{@code
 * MinecraftForge.EVENT_BUS.register(new RadialMenuKeyHandler());
 * }</pre>
 */
public class RadialMenuKeyHandler {

    // -------------------------------------------------------------------------
    // Key binding definition
    // -------------------------------------------------------------------------

    /**
     * The key binding that opens the radial menu.
     * Default: R  (GLFW_KEY_R).
     * Shows up in Options → Controls under the "Radial Menu" category.
     */
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
            "key.radialmenu.open",
            InputConstants.Type.MOUSE,          // ← was KEYSYM
            GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
            "key.categories.radialmenu"
    );

    // -------------------------------------------------------------------------
    // Mod-bus: registration
    // -------------------------------------------------------------------------

    /**
     * Called on the MOD event bus during {@link RegisterKeyMappingsEvent}.
     */
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_RADIAL_MENU);
    }

    // -------------------------------------------------------------------------
    // Forge-bus: input handling
    // -------------------------------------------------------------------------

    /**
     * Called every time a key is pressed or released.
     * We use {@link InputEvent.Key} because it fires even when a screen is open,
     * unlike the game-action tick which is suppressed while GUIs are showing.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!OPEN_RADIAL_MENU.matches(event.getKey(), event.getScanCode())) return;

        if (event.getAction() == GLFW.GLFW_PRESS) {
            handlePress(mc);
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            handleRelease(mc);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!OPEN_RADIAL_MENU.matchesMouse(event.getButton())) return;

        if (event.getAction() == GLFW.GLFW_PRESS) {
            handlePress(mc);
        } else if (event.getAction() == GLFW.GLFW_RELEASE) {
            handleRelease(mc);
        }

    }


    // -------------------------------------------------------------------------
    // Press / release logic
    // -------------------------------------------------------------------------

    /**
     * Invoked by your item code to open the menu with a specific {@link RadialMenuScreen}.
     * You can also call {@code Minecraft.getInstance().setScreen(new RadialMenuScreen(...))}
     * directly; this method exists as a convenience hook.
     *
     * @param screen pre-built screen to open
     */
    public static void openMenu(RadialMenuScreen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    private void handlePress(Minecraft mc) {
        if (mc.screen instanceof RadialMenuScreen) return;

        ItemStack held = mc.player.getMainHandItem();
        if (held.getItem() instanceof TabletItem tabletItem) {
            tabletItem.openTabletRadialMenu(held);
        }
    }

    private void handleRelease(Minecraft mc) {
        if (mc.screen instanceof RadialMenuScreen radialScreen) {
            radialScreen.confirmAndClose();
        }
    }

    // -------------------------------------------------------------------------
    // Placeholder — replace with your item-specific factory
    // -------------------------------------------------------------------------

    /**
     * Opens a sample menu so you can test the system without wiring up a real item.
     * Delete this method and replace {@link #handlePress} logic with your item hook.
     */
    private static void openPlaceholderMenu(Minecraft mc) {
        var menu = RadialMenu
                .builder(net.minecraft.network.chat.Component.literal("Tool Mode"))
                .addEntry(RadialMenuEntry
                        .builder("mode_mine", net.minecraft.network.chat.Component.literal("Mine"))
                        .icon(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE))
                        .onSelect(() -> mc.player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§aMine mode selected"), true))
                        .build())
                .addEntry(RadialMenuEntry
                        .builder("mode_till", net.minecraft.network.chat.Component.literal("Till"))
                        .icon(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_HOE))
                        .onSelect(() -> mc.player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§eTill mode selected"), true))
                        .build())
                .addEntry(RadialMenuEntry
                        .builder("mode_chop", net.minecraft.network.chat.Component.literal("Chop"))
                        .icon(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_AXE))
                        .onSelect(() -> mc.player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§6Chop mode selected"), true))
                        .build())
                .addEntry(RadialMenuEntry
                        .builder("mode_shovel", net.minecraft.network.chat.Component.literal("Shovel"))
                        .icon(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_SHOVEL))
                        .onSelect(() -> mc.player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§bShovel mode selected"), true))
                        .build())
                .addEntry(RadialMenuEntry
                        .builder("mode_scan", net.minecraft.network.chat.Component.literal("Scan"))
                        .onSelect(() -> mc.player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§dScan mode selected"), true))
                        .build())
                .defaultIndex(0)
                .build();

        mc.setScreen(new RadialMenuScreen(menu, "astrocraft:tool_modes"));
    }
}
