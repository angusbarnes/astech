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
import net.astr0.astrocraft.ModTags;
import net.astr0.astrocraft.block.ModBlocks;
import net.astr0.astrocraft.recipe.EUVMachineRecipe;
import net.astr0.astrocraft.recipe.FluidIngredient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class EUVMachineCategory implements IRecipeCategory<EUVMachineRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Astrocraft.MODID, "euv");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Astrocraft.MODID,
            "textures/gui/jei/lithography_machine.png");

    public static final RecipeType<EUVMachineRecipe> EUV_MACHINE_TYPE =
            new RecipeType<>(UID, EUVMachineRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public EUVMachineCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.EUV_MACHINE.get()));

        var arrow = helper.createDrawable(TEXTURE, 183, 0, 28, 8);
        this.arrow = helper.createAnimatedDrawable(arrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<EUVMachineRecipe> getRecipeType() {
        return EUV_MACHINE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Extreme Ultraviolet Lithography");
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
    public void setRecipe(IRecipeLayoutBuilder builder, EUVMachineRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 33, 16)
                .addIngredients(ForgeTypes.FLUID_STACK, FluidIngredient.of(100, ModTags.PHOTORESIST_TAG).getFluidStacks())
                .setFluidRenderer(100, false, 10, 56);


        Ingredient input = recipe.getInput();
        if(input != null && !input.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 54, 34).addIngredients(input);
        }

        Ingredient catalyst = recipe.getCatalyst();
        if(catalyst != null && !catalyst.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 81, 47)
                    .addIngredients(catalyst);
        }

        if (!recipe.getOutputItem().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 34)
                    .addItemStack(recipe.getOutputItem());
        }

    }

    @Override
    public void draw(EUVMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        this.arrow.draw(guiGraphics, 75, 38);
    }
}