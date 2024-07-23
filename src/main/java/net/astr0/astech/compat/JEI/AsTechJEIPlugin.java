package net.astr0.astech.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.astr0.astech.AsTech;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerScreen;
import net.astr0.astech.recipe.ChemicalMixerRecipe;
import net.astr0.astech.recipe.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class AsTechJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AsTech.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ChemicalMixerCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ChemicalMixerRecipe> polishingRecipes = recipeManager.getAllRecipesFor(ModRecipes.CHEMICAL_MIXER_RECIPE_TYPE.get());
        registration.addRecipes(ChemicalMixerCategory.CHEMICAL_MIXER_TYPE, polishingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalMixerScreen.class, 85, 30, 20, 30,
                ChemicalMixerCategory.CHEMICAL_MIXER_TYPE);
    }
}