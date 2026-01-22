package net.astr0.astech.compat.JEI;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.astr0.astech.Astrocraft;
import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.recipe.ElectrolyticSeperatorRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class ElectrolyticSeperatorCategory implements IRecipeCategory<ElectrolyticSeperatorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Astrocraft.MODID, "seperator");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Astrocraft.MODID,
            "textures/gui/jei/electrolytic_seperator.png");

    public static final RecipeType<ElectrolyticSeperatorRecipe> SEPERATOR_TYPE =
            new RecipeType<>(UID, ElectrolyticSeperatorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public ElectrolyticSeperatorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ELECTROLYTIC_SEPERATOR.get()));

        var arrow = helper.createDrawable(TEXTURE, 183, 0, 28, 8);
        this.arrow = helper.createAnimatedDrawable(arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<ElectrolyticSeperatorRecipe> getRecipeType() {
        return SEPERATOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Electrolytic Seperation");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ElectrolyticSeperatorRecipe recipe, IFocusGroup focuses) {

        FluidStack in1 = recipe.getInput1().getFluidStacks().get(0);
        FluidStack outF1 = recipe.getOutput1();
        FluidStack outF2 = recipe.getOutput2();
        int[] amounts = new int[] { in1.getAmount(), outF1.getAmount(), outF2.getAmount() };
        int max = Arrays.stream(amounts).max().getAsInt();

        int inH1 = Math.min(56, in1.getAmount() * 56 / max);
        int outH1 = Math.min(56, outF1.getAmount() * 56 / max);
        int outH2 = Math.min(56, outF2.getAmount() * 56 / max);

        builder.addSlot(RecipeIngredientRole.INPUT, 44, 16 + (56 - inH1))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput1().getFluidStacks())
                .setFluidRenderer(in1.getAmount(), false, 10, inH1)
                .addTooltipCallback(AsTechJEIPlugin.defaultOutputTooltip(recipe.getInput1()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 16 + (56 - outH1))
                .addIngredient(ForgeTypes.FLUID_STACK, recipe.getOutput1())
                .setFluidRenderer(outF1.getAmount(), false, 10, outH1);

        if(!(recipe.getOutput2().isEmpty())) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 16 + (56 - outH2))
                    .addIngredient(ForgeTypes.FLUID_STACK, recipe.getOutput2())
                    .setFluidRenderer(outF2.getAmount(), false, 10, outH2);
        }

    }

    @Override
    public void draw(ElectrolyticSeperatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        this.arrow.draw(guiGraphics, 69, 40);
    }
}