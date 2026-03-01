package net.astr0.astrocraft.trading;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CurrencyHelper {

    public static ItemStack processStack(ItemStack original, List<? extends String> rules) {
        // If it's not an emerald, leave it alone
        if (!original.is(Items.EMERALD)) {
            return original;
        }

        int count = original.getCount();

        for (String rule : rules) {
            try {
                // Rule format: "1-8=minecraft:diamond"
                String[] parts = rule.split("=");
                String[] range = parts[0].split("-");

                int min = Integer.parseInt(range[0].trim());
                int max = Integer.parseInt(range[1].trim());

                // Check if the emerald count falls in this tier
                if (count >= min && count <= max) {
                    ResourceLocation itemId = new ResourceLocation(parts[1].trim());
                    Item newItem = ForgeRegistries.ITEMS.getValue(itemId);

                    if (newItem != null && newItem != Items.AIR) {
                        // Return the new item, keeping the original count
                        return new ItemStack(newItem, count);
                    }
                }
            } catch (Exception e) {
                // Catch typos in the config so the game doesn't crash
                System.err.println("Invalid currency rule in config: " + rule);
            }
        }

        // Fallback: If no rules match (e.g., count is higher than configured max), return original
        return original;
    }
}
