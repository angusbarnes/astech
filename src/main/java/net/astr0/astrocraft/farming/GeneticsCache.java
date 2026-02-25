package net.astr0.astrocraft.farming;

import net.astr0.astrocraft.recipe.CrossbreedingRecipe;
import net.astr0.astrocraft.recipe.ModRecipes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeneticsCache {

    // All seeds belonging to a specific group tag
    private static final Map<String, List<Item>> GROUP_CACHE = new ConcurrentHashMap<>();

    // O(1) Reverse lookup: Given a seed Item, what group is it?
    private static final Map<Item, String> REVERSE_LOOKUP = new ConcurrentHashMap<>();

    // Matrix cache. Key is sorted "groupA|groupB" to ensure order invariance
    private static final Map<String, String> COMBINATION_MATRIX = new ConcurrentHashMap<>();

    // A sorted list of specific recipes for fast iteration during ticks
    private static final List<CrossbreedingRecipe> SPECIFIC_RECIPES = new ArrayList<>();

    public static void rebuild(RecipeManager recipeManager) {
        GROUP_CACHE.clear();
        REVERSE_LOOKUP.clear();
        COMBINATION_MATRIX.clear();
        SPECIFIC_RECIPES.clear();

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

        // 2. Build Recipe Matrices & Specific Overrides
        List<CrossbreedingRecipe> allRecipes = recipeManager.getAllRecipesFor(ModRecipes.CROSSBREADING_RECIPE_TYPE.get());

        for (CrossbreedingRecipe recipe : allRecipes) {
            if (recipe.isSpecific()) {
                SPECIFIC_RECIPES.add(recipe);
            } else {
                String key = getMatrixKey(recipe.getGroupA(), recipe.getGroupB());
                COMBINATION_MATRIX.put(key, recipe.getResultGroup());
            }
        }

        // Sort specific recipes by priority descending so high-priority takes precedence
        SPECIFIC_RECIPES.sort(Comparator.comparingInt(CrossbreedingRecipe::getPriority).reversed());
    }

    // Helper to enforce order invariance for matrix lookup
    private static String getMatrixKey(String a, String b) {
        if (a == null || b == null) return "";
        return a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a;
    }

    // --- PUBLIC ACCESSORS FOR CROP STICKS TO CALL ---

    public static String getGroupForItem(Item item) {
        return REVERSE_LOOKUP.get(item);
    }

    public static Item getRandomSeedFromGroup(String groupName, RandomSource random) {
        List<Item> candidates = GROUP_CACHE.get(groupName);
        if (candidates == null || candidates.isEmpty()) return Items.AIR;
        return candidates.get(random.nextInt(candidates.size()));
    }

    public static String getMatrixResult(String groupA, String groupB) {
        return COMBINATION_MATRIX.get(getMatrixKey(groupA, groupB));
    }

    public static List<CrossbreedingRecipe> getSpecificRecipes() {
        return SPECIFIC_RECIPES;
    }
}
