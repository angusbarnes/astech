package net.astr0.astech.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/*
    We really only need the JSON seriaise and network sync feature, we can otherwise do everything however we want.
    We must implment a dummy container to make vanilla happy, but we can call our own match functions and loop through until
    We find a match. That shouldn't be too hard on the server. This is what PneumaticCraft does. We just requrest all recipes of
    a certain type, and loop through these, rather than requesting with a specific inventory
 */
public class ChemicalReactorRecipe extends AsTechRecipeBase {

    private final FluidIngredient input1;
    private final FluidIngredient input2;
    private final FluidStack outputFluid1;
    private final FluidStack outputFluid2;

    private final int processingTime;

    public ChemicalReactorRecipe(ResourceLocation id, FluidIngredient input1, FluidIngredient input2, FluidStack outputFluid1, FluidStack outputFluid2, int processingTime) {
        super(id);


        this.input1 = input1;
        this.input2 = input2;
        this.outputFluid1 = outputFluid1;
        this.outputFluid2 = outputFluid2;
        this.processingTime = processingTime;
    }
    
    public boolean matches(FluidStack fluid1, FluidStack fluid2) {

        boolean fluidMatches = input1.testFluid(fluid1) && input1.testFluid(fluid2);

        return fluidMatches;
    }


    public int calculateConsumedAmount(FluidStack input) {
        if(input1.testFluid(input)) {
            return input1.getAmount();
        }

        return 0;
    }

    public int calculateConsumedAmountItems(ItemStack input) {

        for(Ingredient ingredient : inputItems) {
            if(ingredient.test(input) && ingredient.getItems().length > 0) {
                return ingredient.getItems()[0].getCount();
            }
        }

        return 0;
    }

    public FluidIngredient getInput1() {
        return input1;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        input1.toNetwork(buffer);
        buffer.writeVarInt(inputItems.size());
        inputItems.forEach(i -> i.toNetwork(buffer));
        buffer.writeItem(outputItem);
        buffer.writeVarInt(processingTime);
        buffer.writeBoolean(isAdvanced);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ASSEMBLER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ASSEMBLER_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ChemicalReactorRecipe> {
        protected static Serializer INSTANCE = new Serializer();

        public static Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public ChemicalReactorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            LogUtils.getLogger().info("RECIPE_DEBUG: attempting to create recipe for id: {}", recipeId);

            JsonArray inputs = json.get("inputs").getAsJsonArray();
            List<Ingredient> inputIngredients = new ArrayList<>();
            for (JsonElement e : inputs) {
                inputIngredients.add(Ingredient.fromJson(e.getAsJsonObject()));
            }

            Ingredient input1 = json.has("fluid_input") ? FluidIngredient.fromJson(json.get("fluid_input")) : FluidIngredient.EMPTY;

            ItemStack outputItem = json.has("item_output") ?
                    ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "item_output")) :
                    ItemStack.EMPTY;
            int processingTime = GsonHelper.getAsInt(json, "time", 200);
            boolean isAdvanced = GsonHelper.getAsBoolean(json, "advanced", false);

            return new ChemicalReactorRecipe(recipeId, (FluidIngredient) input1, inputIngredients, outputItem, processingTime, isAdvanced);
        }

        @Nullable
        @Override
        public ChemicalReactorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidIngredient input1 = (FluidIngredient) Ingredient.fromNetwork(buffer);

            int nInputs = buffer.readVarInt();
            List<Ingredient> in = new ArrayList<>();
            for (int i = 0; i < nInputs; i++) {
                in.add(Ingredient.fromNetwork(buffer));
            }

            ItemStack outputItem = buffer.readItem();
            int processingTime = buffer.readVarInt();
            boolean isAdvanced = buffer.readBoolean();

            return new ChemicalReactorRecipe(recipeId, input1, in, outputItem, processingTime, isAdvanced);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ChemicalReactorRecipe recipe) {
            recipe.write(buffer);
        }
    }
}
