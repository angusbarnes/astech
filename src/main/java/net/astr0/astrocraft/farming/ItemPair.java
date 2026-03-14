package net.astr0.astrocraft.farming;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public record ItemPair(Item first, Item second) {
    public ItemPair {
        // Null-safe registry name retrieval
        String nameA = (first != null && ForgeRegistries.ITEMS.getKey(first) != null)
                ? ForgeRegistries.ITEMS.getKey(first).toString() : "";
        String nameB = (second != null && ForgeRegistries.ITEMS.getKey(second) != null)
                ? ForgeRegistries.ITEMS.getKey(second).toString() : "";

        // Sort alphabetically to ensure A+B == B+A
        if (nameA.compareTo(nameB) > 0) {
            Item temp = first;
            first = second;
            second = temp;
        }
        // Java implicitly assigns the potentially swapped values here:
        // this.first = first; this.second = second;
    }

    @Override
    public String toString() {
        return "ItemPair [first=" + first + ", second=" + second + "]";
    }
}
