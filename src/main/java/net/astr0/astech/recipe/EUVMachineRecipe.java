package net.astr0.astech.recipe;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;


/*
    We really only need the JSON seriaise and network sync feature, we can otherwise do everything however we want.
    We must implment a dummy container to make vanilla happy, but we can call our own match functions and loop through until
    We find a match. That shouldn't be too hard on the server. This is what PneumaticCraft does. We just requrest all recipes of
    a certain type, and loop through these, rather than requesting with a specific inventory
 */
public class EUVMachineRecipe extends AsTechRecipeBase {

    private final Ingredient input;
    private final Ingredient catalyst;
    private final ItemStack output;
    private final int processingTime;

    public EUVMachineRecipe(ResourceLocation id, Ingredient input, Ingredient catalyst, ItemStack outputItem, int processingTime) {
        super(id);
        this.input = input;
        this.catalyst = catalyst;
        this.output = outputItem;
        this.processingTime = processingTime;
    }
    
    public boolean matches(ItemStack input, ItemStack catalyst) {
        return this.input.test(input) && (this.catalyst.isEmpty() || this.catalyst.test(catalyst));
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public ItemStack getOutputItem() {
        return output.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        input.toNetwork(buffer);
        catalyst.toNetwork(buffer);
        buffer.writeItem(output);
        buffer.writeVarInt(processingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EUV_MACHINE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EUV_MACHINE_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<EUVMachineRecipe> {
        protected static final Serializer INSTANCE = new Serializer();

        public static Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public EUVMachineRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            LogUtils.getLogger().info("RECIPE_DEBUG: attempting to create recipe for id: {}", recipeId);

            Ingredient input = Ingredient.fromJson(json.get("input"));
            Ingredient catalyst = json.has("catalyst") ? Ingredient.fromJson(json.get("catalyst")) : Ingredient.EMPTY;

            ItemStack outputItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int processingTime = GsonHelper.getAsInt(json, "time", 2000);

            return new EUVMachineRecipe(recipeId, input, catalyst, outputItem, processingTime);
        }

        @Nullable
        @Override
        public EUVMachineRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient catalyst = Ingredient.fromNetwork(buffer);
            ItemStack outputItem = buffer.readItem();
            int processingTime = buffer.readVarInt();

            return new EUVMachineRecipe(recipeId, input, catalyst, outputItem, processingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EUVMachineRecipe recipe) {
            recipe.write(buffer);
        }
    }
}
