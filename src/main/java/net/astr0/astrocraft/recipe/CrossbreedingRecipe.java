package net.astr0.astrocraft.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class CrossbreedingRecipe extends AsTechRecipeBase {

    private final ResourceLocation id;
    private final int priority;

    // For specific overrides
    private final Ingredient inputA;
    private final Ingredient inputB;
    private final ItemStack resultItem;

    // For group/matrix rules
    private final String groupA;
    private final String groupB;
    private final String resultGroup;

    public CrossbreedingRecipe(ResourceLocation id, int priority, Ingredient inputA, Ingredient inputB, ItemStack resultItem, String groupA, String groupB, String resultGroup) {
        super(id);
        this.id = id;
        this.priority = priority;
        this.inputA = inputA;
        this.inputB = inputB;
        this.resultItem = resultItem;
        this.groupA = groupA;
        this.groupB = groupB;
        this.resultGroup = resultGroup;
    }

    public boolean isSpecific() {
        return !resultItem.isEmpty();
    }

    // Getters
    public int getPriority() { return priority; }
    public Ingredient getInputA() { return inputA; }
    public Ingredient getInputB() { return inputB; }
    public ItemStack getResultItem() { return resultItem; }
    public String getGroupA() { return groupA; }
    public String getGroupB() { return groupB; }
    public String getResultGroup() { return resultGroup; }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; }
    @Override public ItemStack getResultItem(RegistryAccess pRegistryAccess) { return resultItem; }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return Serializer.INSTANCE; }
    @Override public RecipeType<?> getType() { return ModRecipes.CROSSBREADING_RECIPE_TYPE.get(); }

    public static class Serializer implements RecipeSerializer<CrossbreedingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static CrossbreedingRecipe.Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public CrossbreedingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            int priority = GsonHelper.getAsInt(json, "priority", 10);

            if (json.has("result_item")) {
                // Parse Specific Recipe
                Ingredient inA = Ingredient.fromJson(json.get("input_a"));
                Ingredient inB = Ingredient.fromJson(json.get("input_b"));
                ItemStack out = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result_item"));
                return new CrossbreedingRecipe(recipeId, priority, inA, inB, out, null, null, null);
            } else {
                // Parse Group Matrix Rule
                String grpA = GsonHelper.getAsString(json, "group_a");
                String grpB = GsonHelper.getAsString(json, "group_b");
                String resGrp = GsonHelper.getAsString(json, "result_group");
                return new CrossbreedingRecipe(recipeId, priority, Ingredient.EMPTY, Ingredient.EMPTY, ItemStack.EMPTY, grpA, grpB, resGrp);
            }
        }

        @Nullable
        @Override
        public CrossbreedingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int priority = buffer.readVarInt();
            boolean isSpecific = buffer.readBoolean();

            if (isSpecific) {
                return new CrossbreedingRecipe(recipeId, priority, Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), buffer.readItem(), null, null, null);
            } else {
                return new CrossbreedingRecipe(recipeId, priority, Ingredient.EMPTY, Ingredient.EMPTY, ItemStack.EMPTY, buffer.readUtf(), buffer.readUtf(), buffer.readUtf());
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrossbreedingRecipe recipe) {
            buffer.writeVarInt(recipe.priority);
            buffer.writeBoolean(recipe.isSpecific());
            if (recipe.isSpecific()) {
                recipe.inputA.toNetwork(buffer);
                recipe.inputB.toNetwork(buffer);
                buffer.writeItem(recipe.resultItem);
            } else {
                buffer.writeUtf(recipe.groupA);
                buffer.writeUtf(recipe.groupB);
                buffer.writeUtf(recipe.resultGroup);
            }
        }
    }
}
