package net.astr0.astrocraft.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TabletModes {
    public static final String NBT_KEY = "tablet_modes";

    public static final String TRAVEL   = "travel";
    public static final String CREATE   = "create";
    public static final String MEK      = "mek";

    /** All modes in wheel order. */
    public static final List<String> ALL = List.of(TRAVEL, CREATE, MEK);

    // --- Updated NBT helpers ---

    /** * Returns a set of all modes currently set to 'true'.
     * If the NBT doesn't exist, it defaults to ALL being active.
     */
    public static Set<String> getActiveModes(ItemStack stack) {
        Set<String> active = new HashSet<>();
        for (String mode : ALL) {
            if (isActive(stack, mode)) {
                active.add(mode);
            }
        }
        return active;
    }

    /**
     * Explicitly sets the state of every mode.
     */
    public static void setActiveModes(ItemStack stack, Set<String> activeModes) {
        CompoundTag modesTag = new CompoundTag();
        for (String mode : ALL) {
            modesTag.putBoolean(mode, activeModes.contains(mode));
        }
        stack.getOrCreateTag().put(NBT_KEY, modesTag);
    }

    /**
     * Checks if a specific mode is active.
     * Logic: If the key is missing from the sub-tag, default to TRUE.
     */
    public static boolean isActive(ItemStack stack, String mode) {
        CompoundTag root = stack.getTag();
        if (root == null || !root.contains(NBT_KEY, Tag.TAG_COMPOUND)) {
            return true; // Default state when no NBT exists
        }

        CompoundTag modesTag = root.getCompound(NBT_KEY);
        // If the specific mode hasn't been toggled yet, default to true
        if (!modesTag.contains(mode)) return true;

        return modesTag.getBoolean(mode);
    }

    /** Toggles a mode and returns the new active state. */
    public static boolean toggle(ItemStack stack, String mode) {
        boolean newState = !isActive(stack, mode);

        CompoundTag modesTag = stack.getOrCreateTagElement(NBT_KEY);
        modesTag.putBoolean(mode, newState);

        return newState;
    }
}