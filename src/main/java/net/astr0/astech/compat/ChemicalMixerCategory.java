package net.astr0.astech.compat;

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
import net.astr0.astech.recipe.ChemicalMixerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;

public class ChemicalMixerCategory implements IRecipeCategory<ChemicalMixerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(AsTech.MODID, "chemical_mixer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(AsTech.MODID,
            "textures/gui/chemical_mixer.png");

    public static final RecipeType<ChemicalMixerRecipe> CHEMICAL_MIXER_TYPE =
            new RecipeType<>(UID, ChemicalMixerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ChemicalMixerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CHEMICAL_MIXER.get()));
    }

    @Override
    public RecipeType<ChemicalMixerRecipe> getRecipeType() {
        return CHEMICAL_MIXER_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, ChemicalMixerRecipe recipe, IFocusGroup focuses) {
        FluidStack in1 = recipe.getInput1().getFluidStacks().get(0);
        FluidStack in2 = recipe.getInput2().getFluidStacks().get(0);
        FluidStack outF = recipe.getOutputFluid();
        int[] amounts = new int[] { in1.getAmount(), in2.getAmount(), outF.getAmount() };
        int max = Arrays.stream(amounts).max().getAsInt();

        int inH1 = Math.min(56, in1.getAmount() * 56 / max);
        int inH2 = Math.min(56, in2.getAmount() * 56 / max);
        int outH = Math.min(56, outF.getAmount() * 56 / max);

        builder.addSlot(RecipeIngredientRole.INPUT, 34, 18 + (56 - inH1))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput1().getFluidStacks())
                .setFluidRenderer(in1.getAmount(), false, 10, inH1);
                //.setOverlay(Helpers.makeTankOverlay(inH1), 0, 0);
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 18 + (56 - inH2))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput2().getFluidStacks())
                .setFluidRenderer(in2.getAmount(), false, 10, inH2);
                //.setOverlay(Helpers.makeTankOverlay(inH2), 0, 0);

        Ingredient input1 = recipe.getInputItems().get(0);
        if(input1 != null && !input1.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 62, 18).addIngredients(input1);
        }

        Ingredient input2 = recipe.getInputItems().get(1);
        if(input2 != null && !input2.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 62, 38)
                    .addIngredients(input2);
        }

        Ingredient input3 = recipe.getInputItems().get(2);
        if(input3 != null && !input3.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 62, 58)
                    .addIngredients(input3);
        }

        if (!recipe.getOutputFluid().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 133, 18 + (56 - outH))
                    .addIngredients(ForgeTypes.FLUID_STACK, Collections.singletonList(outF))
                    .setFluidRenderer(outF.getAmount(), false, 10, outH);
                    //.setOverlay(Helpers.makeTankOverlay(outH), 0, 0);
        }
        if (!recipe.getOutputItem().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 38)
                    .addItemStack(recipe.getOutputItem());
        }
    }
}