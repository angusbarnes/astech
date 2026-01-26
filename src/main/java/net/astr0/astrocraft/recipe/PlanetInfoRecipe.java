package net.astr0.astrocraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/*
    We really only need the JSON seriaise and network sync feature, we can otherwise do everything however we want.
    We must implment a dummy container to make vanilla happy, but we can call our own match functions and loop through until
    We find a match. That shouldn't be too hard on the server. This is what PneumaticCraft does. We just requrest all recipes of
    a certain type, and loop through these, rather than requesting with a specific inventory
 */
public class PlanetInfoRecipe extends AsTechRecipeBase {

    private final FluidIngredient rocket_fuel;
    private final List<Ingredient> planetResources;
    private final int planet_tier;
    private final String planet_name;
    private final String flavour_text;
    private final ItemStack rocket_item;

    public PlanetInfoRecipe(ResourceLocation id, FluidIngredient rocket_fuel, List<Ingredient> planetResources,
                            int planet_tier, String planet_name, String flavour_text, ItemStack rocket_item) {
        super(id);
        this.rocket_fuel = rocket_fuel;
        this.planetResources = planetResources;
        this.planet_tier = planet_tier;
        this.planet_name = planet_name;
        this.flavour_text = flavour_text;
        this.rocket_item = rocket_item;
    }

    public FluidIngredient getRocketFuel() {
        return rocket_fuel;
    }

    public List<Ingredient> getPlanetResources() {
        return planetResources;
    }

    public int getPlanetTier() {
        return planet_tier;
    }

    public String getPlanetName() {
        return planet_name;
    }

    public String getFlavourText() {
        return flavour_text;
    }

    public ItemStack getRocketItem() {
        return rocket_item.copy();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        rocket_fuel.toNetwork(buffer);
        buffer.writeVarInt(planetResources.size());
        for (Ingredient ingredient : planetResources) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeVarInt(planet_tier);
        buffer.writeUtf(planet_name);
        buffer.writeUtf(flavour_text);
        buffer.writeItem(rocket_item);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PLANET_INFO_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PLANET_INFO_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PlanetInfoRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static PlanetInfoRecipe.Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public PlanetInfoRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidIngredient fuel = json.has("rocket_fuel") ? (FluidIngredient) FluidIngredient.fromJson(json.get("rocket_fuel")) : FluidIngredient.EMPTY;

            List<Ingredient> resources = new ArrayList<>();
            JsonArray jsonResources = json.getAsJsonArray("planet_resources");
            for (JsonElement el : jsonResources) {
                resources.add(Ingredient.fromJson(el));
            }

            int tier = GsonHelper.getAsInt(json, "planet_tier");
            String name = GsonHelper.getAsString(json, "planet_name");
            String flavour = GsonHelper.getAsString(json, "flavour_text");

            ItemStack rocket = ItemStack.EMPTY;
            if (json.has("rocket_item")) {
                rocket = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "rocket_item"));
            }

            return new PlanetInfoRecipe(recipeId, fuel, resources, tier, name, flavour, rocket);
        }

        @Nullable
        @Override
        public PlanetInfoRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidIngredient fuel = (FluidIngredient) FluidIngredient.fromNetwork(buffer);

            int count = buffer.readVarInt();
            List<Ingredient> resources = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                resources.add(Ingredient.fromNetwork(buffer));
            }

            int tier = buffer.readVarInt();
            String name = buffer.readUtf();
            String flavour = buffer.readUtf();
            ItemStack rocket = buffer.readItem();

            return new PlanetInfoRecipe(recipeId, fuel, resources, tier, name, flavour, rocket);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PlanetInfoRecipe recipe) {
            recipe.write(buffer);
        }
    }
}

