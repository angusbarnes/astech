package net.astr0.astrocraft.farming;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record CropGenetics(int growth, int gain, int resistance) {
    public static final CropGenetics DEFAULT = new CropGenetics(1, 1, 1);
    public static final String NBT_KEY = "CropStats";

    public static CropGenetics fromStack(ItemStack stack) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains(NBT_KEY)) {
                CompoundTag tag = stack.getTag().getCompound(NBT_KEY);
                return new CropGenetics(
                        tag.getInt("Growth"),
                        tag.getInt("Gain"),
                        tag.getInt("Resistance")
                );
            }
        }
        return DEFAULT;
    }

    public ItemStack applyToStack(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        // Check if we are at default levels (1, 1, 1)
        if (this.growth <= 1 && this.gain <= 1 && this.resistance <= 1) {
            // Clean up: If the tag exists, remove it so the item is "vanilla" again
            if (stack.hasTag()) {
                assert stack.getTag() != null;
                stack.getTag().remove(NBT_KEY);

                // If the root tag is now empty, remove it entirely to keep the stack clean
                if (stack.getTag().isEmpty()) {
                    stack.setTag(null);
                }
            }
            return stack;
        }

        // Otherwise, write the non-default stats
        CompoundTag root = stack.getOrCreateTag();
        CompoundTag stats = new CompoundTag();
        stats.putInt("Growth", growth);
        stats.putInt("Gain", gain);
        stats.putInt("Resistance", resistance);
        root.put(NBT_KEY, stats);

        return stack;
    }
}
