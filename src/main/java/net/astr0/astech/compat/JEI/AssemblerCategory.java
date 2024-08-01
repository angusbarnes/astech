package net.astr0.astech.compat.JEI;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.astr0.astech.AsTech;
import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.recipe.AssemblerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class AssemblerCategory implements IRecipeCategory<AssemblerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(AsTech.MODID, "assembler");
    public static final ResourceLocation TEXTURE = new ResourceLocation(AsTech.MODID,
            "textures/gui/assembler.png");

    public static final RecipeType<AssemblerRecipe> ASSEMBLER_TYPE =
            new RecipeType<>(UID, AssemblerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AssemblerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ASSEMBLER.get()));
    }

    @Override
    public RecipeType<AssemblerRecipe> getRecipeType() {
        return ASSEMBLER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Chemical Mixing");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AssemblerRecipe recipe, IFocusGroup focuses) {
        FluidStack in1 = recipe.getInput1().getFluidStacks().get(0);
        int[] amounts = new int[] { in1.getAmount() };
        int max = Arrays.stream(amounts).max().getAsInt();

        int inH1 = Math.min(56, in1.getAmount() * 56 / max);


        builder.addSlot(RecipeIngredientRole.INPUT, 14, 15 + (56 - inH1))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput1().getFluidStacks())
                .setFluidRenderer(in1.getAmount(), false, 10, inH1);

        Ingredient input1 = recipe.getInputItems().get(0);
        if(input1 != null && !input1.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 36, 26).addIngredients(input1);
        }

        Ingredient input2 = recipe.getInputItems().get(1);
        if(input2 != null && !input2.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 54, 26)
                    .addIngredients(input2);
        }

        Ingredient input3 = recipe.getInputItems().get(2);
        if(input3 != null && !input3.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 72, 26)
                    .addIngredients(input3);
        }

        Ingredient input4 = recipe.getInputItems().get(3);
        if(input4 != null && !input4.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 36, 44)
                    .addIngredients(input4);
        }

        Ingredient input5 = recipe.getInputItems().get(4);
        if(input5 != null && !input5.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 54, 44)
                    .addIngredients(input5);
        }

        if (!recipe.getOutputItem().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 26)
                    .addItemStack(recipe.getOutputItem());
        }
    }
}