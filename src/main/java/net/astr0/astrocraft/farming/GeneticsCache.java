package net.astr0.astrocraft.farming;

import net.astr0.astrocraft.Astrocraft;
import net.astr0.astrocraft.common.StringUtils;
import net.astr0.astrocraft.recipe.CrossbreedingRecipe;
import net.astr0.astrocraft.recipe.ModRecipes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GeneticsCache {

    // All seeds belonging to a specific group tag
    private static final Map<String, List<Item>> GROUP_CACHE = new ConcurrentHashMap<>();

    // O(1) Reverse lookup: Given a seed Item, what group is it?
    private static final Map<Item, String> REVERSE_LOOKUP = new ConcurrentHashMap<>();

    // Specific Recipes Cache
    private static final Map<ItemPair, List<CrossbreedingRecipe>> SPECIFIC_CACHE = new ConcurrentHashMap<>();

    // Group Recipes Cache
    private static final Map<GroupPair, List<CrossbreedingRecipe>> GROUP_MATRIX_CACHE = new ConcurrentHashMap<>();

    public static void rebuild(RecipeManager recipeManager) {
        GROUP_CACHE.clear();
        REVERSE_LOOKUP.clear();
        SPECIFIC_CACHE.clear();
        GROUP_MATRIX_CACHE.clear();

        // 1. Build Group Caches from Tags
        // We look for any tag containing "astrocraft:seed_groups/"
        for (Item item : ForgeRegistries.ITEMS) {
            item.builtInRegistryHolder().tags().forEach(tagKey -> {
                String tagName = tagKey.location().toString();
                if (tagName.startsWith("astrocraft:seed_groups/")) {
                    GROUP_CACHE.computeIfAbsent(tagName, k -> new ArrayList<>()).add(item);
                    REVERSE_LOOKUP.put(item, tagName);
                }
            });
        }

        List<CrossbreedingRecipe> allRecipes = recipeManager.getAllRecipesFor(ModRecipes.CROSSBREADING_RECIPE_TYPE.get());

        for (CrossbreedingRecipe recipe : allRecipes) {
            if (recipe.isSpecific()) {
                // THE GOTCHA FIX: Unpack the ingredients into exact Item combinations
                ItemStack[] itemsA = recipe.getInputA().getItems();
                ItemStack[] itemsB = recipe.getInputB().getItems();

                for (ItemStack stackA : itemsA) {
                    for (ItemStack stackB : itemsB) {
                        ItemPair pair = new ItemPair(stackA.getItem(), stackB.getItem());
                        SPECIFIC_CACHE.computeIfAbsent(pair, k -> new ArrayList<>()).add(recipe);
                    }
                }
            } else {
                // Group matrix is much simpler
                GroupPair pair = new GroupPair(recipe.getGroupA(), recipe.getGroupB());
                GROUP_MATRIX_CACHE.computeIfAbsent(pair, k -> new ArrayList<>()).add(recipe);
            }
        }

        // Sort all the lists by priority descending
        Comparator<CrossbreedingRecipe> prioritySorter = Comparator.comparingInt(CrossbreedingRecipe::getPriority).reversed();
        SPECIFIC_CACHE.values().forEach(list -> list.sort(prioritySorter));
        Astrocraft.LOGGER.info("========= CROP STICKS HASH MAP =========");
        for (Map.Entry<ItemPair, List<CrossbreedingRecipe>> entry : SPECIFIC_CACHE.entrySet()) {
            Astrocraft.LOGGER.info("  " + entry.getKey() + ": " + entry.getValue().toString());
        }
        GROUP_MATRIX_CACHE.values().forEach(list -> list.sort(prioritySorter));
        Astrocraft.LOGGER.info("========= CROP STICKS HASH MAP =========");
        for (Map.Entry<GroupPair, List<CrossbreedingRecipe>> entry : GROUP_MATRIX_CACHE.entrySet()) {
            Astrocraft.LOGGER.info("  " + entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    // --- QUERY METHODS ---
    public static List<CrossbreedingRecipe> getSpecificRecipes(ItemStack itemStackA, ItemStack itemStackB) {
        return getSpecificRecipes(itemStackA.getItem(), itemStackB.getItem());
    }

    public static List<CrossbreedingRecipe> getSpecificRecipes(Item itemA, Item itemB) {
        ItemPair key = new ItemPair(itemA, itemB);
        Astrocraft.LOGGER.info("Getting specific crossbreeding recipe for " + key.toString());
        return SPECIFIC_CACHE.getOrDefault(new ItemPair(itemA, itemB), Collections.emptyList());
    }

    public static List<CrossbreedingRecipe> getGroupRecipes(String groupA, String groupB) {
        return GROUP_MATRIX_CACHE.getOrDefault(new GroupPair(groupA, groupB), Collections.emptyList());
    }

    public static String getGroupForItem(Item item) {
        return REVERSE_LOOKUP.get(item);
    }

    public static Item getRandomSeedFromGroup(String groupName, RandomSource random) {

        if (StringUtils.isNullOrEmpty(groupName)) return null;

        List<Item> candidates = GROUP_CACHE.get(groupName);
        if (candidates == null || candidates.isEmpty()) return Items.AIR;

        return candidates.get(random.nextInt(candidates.size()));
    }
}
