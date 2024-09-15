package net.astr0.astech.recipe;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;


/*
    We really only need the JSON seriaise and network sync feature, we can otherwise do everything however we want.
    We must implment a dummy container to make vanilla happy, but we can call our own match functions and loop through until
    We find a match. That shouldn't be too hard on the server. This is what PneumaticCraft does. We just requrest all recipes of
    a certain type, and loop through these, rather than requesting with a specific inventory
 */
public class ElectrolyticSeperatorRecipe extends AsTechRecipeBase {

    private final FluidIngredient input1;
    private final FluidStack outputFluid1;
    private final FluidStack outputFluid2;

    private final int processingTime;

    public ElectrolyticSeperatorRecipe(ResourceLocation id, FluidIngredient input1, FluidStack outputFluid1, FluidStack outputFluid2, int processingTime) {
        super(id);
        this.input1 = input1;
        this.outputFluid1 = outputFluid1;
        this.outputFluid2 = outputFluid2;
        this.processingTime = processingTime;
    }
    
    public boolean matches(FluidStack fluid) {
        return input1.testFluid(fluid);
    }


    public int calculateConsumedAmount() {

        return input1.getAmount();
    }

    public FluidIngredient getInput1() {
        return input1;
    }
    public FluidStack getOutput1() {
        return outputFluid1;
    }
    public FluidStack getOutput2() {
        return outputFluid2;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        input1.toNetwork(buffer);
        outputFluid1.writeToPacket(buffer);
        outputFluid2.writeToPacket(buffer);
        buffer.writeVarInt(processingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELECTROLYTIC_SEPERATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ELECTROLYTIC_SEPERATOR_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ElectrolyticSeperatorRecipe> {
        protected static Serializer INSTANCE = new Serializer();

        public static Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public ElectrolyticSeperatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            LogUtils.getLogger().info("REACTOR RECIPE_DEBUG: attempting to create recipe for id: {}", recipeId);

            FluidIngredient input1 = (FluidIngredient) FluidIngredient.fromJson(json.get("fluid_input"));

            FluidStack output1 = json.has("fluid_output_1") ?
                    ModCraftingHelper.fluidStackFromJson(json.getAsJsonObject("fluid_output_1")):
                    FluidStack.EMPTY;

            FluidStack output2 = json.has("fluid_output_2") ?
                    ModCraftingHelper.fluidStackFromJson(json.getAsJsonObject("fluid_output_2")):
                    FluidStack.EMPTY;

            int processingTime = GsonHelper.getAsInt(json, "time", 200);

            return new ElectrolyticSeperatorRecipe(recipeId, input1, output1, output2, processingTime);
        }

        @Nullable
        @Override
        public ElectrolyticSeperatorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidIngredient input1 = (FluidIngredient) Ingredient.fromNetwork(buffer);
            FluidStack outputFluid1 = FluidStack.readFromPacket(buffer);
            FluidStack outputFluid2 = FluidStack.readFromPacket(buffer);
            int processingTime = buffer.readVarInt();

            return new ElectrolyticSeperatorRecipe(recipeId, input1, outputFluid1, outputFluid2, processingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ElectrolyticSeperatorRecipe recipe) {
            recipe.write(buffer);
        }
    }
}
