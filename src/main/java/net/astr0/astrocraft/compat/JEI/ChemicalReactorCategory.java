package net.astr0.astrocraft.compat.JEI;

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
import net.astr0.astrocraft.Astrocraft;
import net.astr0.astrocraft.block.ModBlocks;
import net.astr0.astrocraft.recipe.ChemicalReactorRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class ChemicalReactorCategory implements IRecipeCategory<ChemicalReactorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Astrocraft.MODID, "reactor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Astrocraft.MODID,
            "textures/gui/jei/chemical_reactor.png");

    public static final RecipeType<ChemicalReactorRecipe> REACTOR_TYPE =
            new RecipeType<>(UID, ChemicalReactorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public ChemicalReactorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CHEMICAL_REACTOR.get()));

        var arrow = helper.createDrawable(TEXTURE, 183, 0, 28, 8);
        this.arrow = helper.createAnimatedDrawable(arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<ChemicalReactorRecipe> getRecipeType() {
        return REACTOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Chemical Reaction");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ChemicalReactorRecipe recipe, IFocusGroup focuses) {

        FluidStack in1 = recipe.getInput1().getFluidStacks().get(0);
        FluidStack in2 = recipe.getInput2().getFluidStacks().get(0);
        FluidStack outF1 = recipe.getOutput1();
        FluidStack outF2 = recipe.getOutput2();
        int[] amounts = new int[] { in1.getAmount(), in2.getAmount(), outF1.getAmount(), outF2.getAmount() };
        int max = Arrays.stream(amounts).max().getAsInt();

        int inH1 = Math.min(56, in1.getAmount() * 56 / max);
        int inH2 = Math.min(56, in2.getAmount() * 56 / max);
        int outH1 = Math.min(56, outF1.getAmount() * 56 / max);
        int outH2 = Math.min(56, outF2.getAmount() * 56 / max);

        builder.addSlot(RecipeIngredientRole.INPUT, 36, 16 + (56 - inH1))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput1().getFluidStacks())
                .setFluidRenderer(in1.getAmount(), false, 10, inH1)
                .addTooltipCallback(AstrocraftJEIPlugin.defaultOutputTooltip(recipe.getInput1()));
        //.setOverlay(Helpers.makeTankOverlay(inH1), 0, 0);
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 16 + (56 - inH2))
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.getInput2().getFluidStacks())
                .setFluidRenderer(in2.getAmount(), false, 10, inH2)
                .addTooltipCallback(AstrocraftJEIPlugin.defaultOutputTooltip(recipe.getInput2()));
        //.setOverlay(Helpers.makeTankOverlay(inH2), 0, 0);


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
    public void draw(ChemicalReactorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        this.arrow.draw(guiGraphics, 74, 40);
    }
}