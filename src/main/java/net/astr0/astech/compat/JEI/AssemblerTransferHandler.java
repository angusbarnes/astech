package net.astr0.astech.compat.JEI;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.astr0.astech.block.Assembler.AssemblerMenu;
import net.astr0.astech.recipe.AssemblerRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


// This will require a minor (potentially major) re-write of the slot config stuff
// We need a way to directly set a filter on a slot lock and ensure that the server stays in sync
// Maybe we need IConfigurable interface which handles this or something.
// Basically I think that these machine settings can get synched without necessarily having to go
// through the block entity. But we will see. Either way, once we can directly set slot filters,
// we can then simply loop through the recipe ingredients and set the filters in the slots
public class AssemblerTransferHandler implements IRecipeTransferHandler<AssemblerMenu, AssemblerRecipe> {
    @Override
    public Class<? extends AssemblerMenu> getContainerClass() {
        return null;
    }

    @Override
    public Optional<MenuType<AssemblerMenu>> getMenuType() {
        return Optional.empty();
    }

    @Override
    public RecipeType<AssemblerRecipe> getRecipeType() {
        return null;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(AssemblerMenu assemblerMenu, AssemblerRecipe assemblerRecipe, IRecipeSlotsView iRecipeSlotsView, Player player, boolean b, boolean b1) {


        return null; // Null represents success
    }
}
