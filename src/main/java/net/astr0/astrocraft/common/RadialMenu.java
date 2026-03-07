package net.astr0.astrocraft.common;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A RadialMenu holds a list of {@link RadialMenuEntry} instances and tracks
 * which entry is currently selected.
 *
 * Build one with the fluent builder, then pass it to
 * {@link com.example.radialmenu.client.gui.RadialMenuScreen}.
 */
public class RadialMenu {

    private final Component title;
    private final List<RadialMenuEntry> entries;
    /** Index of the entry that was active when the menu was opened (pre-selected). */
    private final int defaultIndex;

    private RadialMenu(Builder builder) {
        this.title = builder.title;
        this.entries = Collections.unmodifiableList(new ArrayList<>(builder.entries));
        this.defaultIndex = Math.max(0, Math.min(builder.defaultIndex, builder.entries.size() - 1));
    }

    public Component getTitle() {
        return title;
    }

    public List<RadialMenuEntry> getEntries() {
        return entries;
    }

    public int getEntryCount() {
        return entries.size();
    }

    public RadialMenuEntry getEntry(int index) {
        return entries.get(index);
    }

    public int getDefaultIndex() {
        return defaultIndex;
    }

    /**
     * Given a mouse/cursor angle in radians (0 = right, increasing clockwise),
     * returns the index of the slice the cursor is pointing at, or -1 if
     * the cursor is within the dead-zone radius.
     *
     * @param dx          cursor delta-x from menu centre
     * @param dy          cursor delta-y from menu centre
     * @param deadZone    radius in pixels below which no selection occurs
     */
    public int getHoveredIndex(double dx, double dy, double deadZone) {
        if (entries.isEmpty()) return -1;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < deadZone) return -1;

        // atan2 gives -PI..PI; rotate so 0 = top, increasing clockwise
        double angle = Math.atan2(dy, dx); // -PI..PI, 0 = right
        // Shift so 0 = top (-PI/2) and wrap to 0..2PI
        angle += Math.PI / 2;
        if (angle < 0) angle += 2 * Math.PI;

        double sliceAngle = (2 * Math.PI) / entries.size();
        int index = (int) (angle / sliceAngle);
        return Math.min(index, entries.size() - 1);
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static Builder builder(Component title) {
        return new Builder(title);
    }

    public static class Builder {
        private final Component title;
        private final List<RadialMenuEntry> entries = new ArrayList<>();
        private int defaultIndex = 0;

        private Builder(Component title) {
            this.title = title;
        }

        public Builder addEntry(RadialMenuEntry entry) {
            entries.add(entry);
            return this;
        }

        public Builder defaultIndex(int index) {
            this.defaultIndex = index;
            return this;
        }

        public RadialMenu build() {
            if (entries.isEmpty()) {
                throw new IllegalStateException("RadialMenu must have at least one entry");
            }
            return new RadialMenu(this);
        }
    }
}
