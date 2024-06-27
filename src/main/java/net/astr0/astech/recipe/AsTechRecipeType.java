package net.astr0.astech.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class AsTechRecipeType<T extends AsTechRecipeBase> implements RecipeType<T> {

    private final String typeName;

    public AsTechRecipeType(String name) {
        this.typeName = "AsTechRecipeType[" + new ResourceLocation(name) + "]";
    }

    @Override
    public String toString() {
        return typeName;
    }

}
