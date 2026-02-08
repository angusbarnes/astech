package net.astr0.astrocraft.farming;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record CropGenetics(int growth, int gain, int resistance) {
    public static final CropGenetics DEFAULT = new CropGenetics(1, 1, 1);
    private static final String NBT_KEY = "CropStats";

    // Read from an ItemStack
    public static CropGenetics fromStack(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NBT_KEY)) {
            CompoundTag tag = stack.getTag().getCompound(NBT_KEY);
            return new CropGenetics(
                    tag.getInt("Growth"),
                    tag.getInt("Gain"),
                    tag.getInt("Resistance")
            );
        }
        return DEFAULT;
    }

    // Write to an ItemStack
    public ItemStack applyToStack(ItemStack stack) {
        CompoundTag root = stack.getOrCreateTag();
        CompoundTag stats = new CompoundTag();
        stats.putInt("Growth", growth);
        stats.putInt("Gain", gain);
        stats.putInt("Resistance", resistance);
        root.put(NBT_KEY, stats);
        return stack;
    }
}
