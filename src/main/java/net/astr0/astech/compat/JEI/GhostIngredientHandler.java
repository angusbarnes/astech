package net.astr0.astech.compat.JEI;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.astr0.astech.gui.AbstractGuiSlot;
import net.astr0.astech.gui.AsTechGuiElement;
import net.astr0.astech.gui.AsTechGuiScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;



public class GhostIngredientHandler implements IGhostIngredientHandler<AsTechGuiScreen> {

    @SuppressWarnings("unchecked")
    @Override
    public <I> @NotNull List<Target<I>> getTargetsTyped(AsTechGuiScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();

        List<AsTechGuiElement> elements = gui.getGuiElements();
        for(AsTechGuiElement element : elements) {

            if (element instanceof AbstractGuiSlot guiSlot) {
                Rect2i area = guiSlot.getRect();

                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return area;
                    }

                    @Override
                    public void accept(I ingredient) {
                        System.out.println("Ghost ingredient dropped: " + ingredient);

                        DraggedIngredient result = wrapDraggedItem(ingredient);

                        guiSlot.handleFilterDrop(result);
                    }
                });
            }

        }

        return targets;
    }

    @Nullable
    private static DraggedIngredient wrapDraggedItem(Object ingredient) {
        if (ingredient instanceof ItemStack stack) {
            return new DraggedIngredient.Item(stack);
        } else if (ingredient instanceof FluidStack fluid) {
            return new DraggedIngredient.Fluid(fluid);
        }
        return null;
    }

    public sealed interface DraggedIngredient permits DraggedIngredient.Item, DraggedIngredient.Fluid {
        record Item(ItemStack stack) implements DraggedIngredient {}
        record Fluid(FluidStack stack) implements DraggedIngredient {}
    }


    @Override
    public void onComplete() {

    }

    @Override
    public boolean shouldHighlightTargets() {
        return IGhostIngredientHandler.super.shouldHighlightTargets();
    }
}
