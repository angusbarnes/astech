package net.astr0.astrocraft.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a single entry (slice) in a radial menu.
 * Each entry can display either an ItemStack icon, a text label, or both.
 */
public class RadialMenuEntry {

    private final Component label;
    @Nullable
    private final ItemStack icon;
    private final String id;
    private final Runnable onSelect;

    private RadialMenuEntry(Builder builder) {
        this.label = builder.label;
        this.icon = builder.icon;
        this.id = builder.id;
        this.onSelect = builder.onSelect;
    }

    /** The text label shown in the center info area or below the icon. */
    public Component getLabel() {
        return label;
    }

    /** Optional item icon rendered in the slice. Null means text-only. */
    @Nullable
    public ItemStack getIcon() {
        return icon;
    }

    /** Unique string ID for this entry, used for syncing to server. */
    public String getId() {
        return id;
    }

    /** Called client-side when this entry is confirmed (key released). */
    public void onSelect() {
        if (onSelect != null) {
            onSelect.run();
        }
    }

    public boolean hasIcon() {
        return icon != null && !icon.isEmpty();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static Builder builder(String id, Component label) {
        return new Builder(id, label);
    }

    public static class Builder {
        private final String id;
        private final Component label;
        @Nullable
        private ItemStack icon;
        @Nullable
        private Runnable onSelect;

        private Builder(String id, Component label) {
            this.id = id;
            this.label = label;
        }

        public Builder icon(ItemStack stack) {
            this.icon = stack;
            return this;
        }

        public Builder onSelect(Runnable callback) {
            this.onSelect = callback;
            return this;
        }

        public RadialMenuEntry build() {
            return new RadialMenuEntry(this);
        }
    }
}
