package net.astr0.astrocraft.compat.JEI;

import com.mojang.logging.LogUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.astr0.astrocraft.gui.AbstractGuiSlot;
import net.astr0.astrocraft.gui.AsTechGuiElement;
import net.astr0.astrocraft.gui.AsTechGuiScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GhostIngredientHandler implements IGhostIngredientHandler<AsTechGuiScreen> {

    @SuppressWarnings("unchecked")
    @Override
    public <I> @NotNull List<Target<I>> getTargetsTyped(AsTechGuiScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();

        DraggedIngredient ing = wrapDraggedItem(ingredient.getIngredient());

        List<AsTechGuiElement> elements = gui.getGuiElements();
        for(AsTechGuiElement element : elements) {

            if (element instanceof AbstractGuiSlot guiSlot && guiSlot.canAcceptGhostIngredient(ing)) {
                Rect2i area = guiSlot.getRect();

                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return area;
                    }

                    @Override
                    public void accept(I ingredient) {
                        System.out.println("Ghost ingredient dropped: " + ingredient);
                        DraggedIngredient ing = wrapDraggedItem(ingredient);
                        guiSlot.handleFilterDrop(ing);
                    }
                });
            }

        }

        return targets;
    }

    @Nullable
    private static DraggedIngredient wrapDraggedItem(Object ingredient) {
        Optional<ItemStack> maybeItem = VanillaTypes.ITEM_STACK.castIngredient(ingredient);
        if (maybeItem.isPresent()) {
            return new DraggedIngredient.Item(maybeItem.get());
        }

        Optional<FluidStack> maybeFluid = ForgeTypes.FLUID_STACK.castIngredient(ingredient);
        if (maybeFluid.isPresent()) {
            return new DraggedIngredient.Fluid(maybeFluid.get());
        }

        // Fallbacks for raw types if not provided by castIngredient
        if (ingredient instanceof ItemStack stack) {
            return new DraggedIngredient.Item(stack);
        } else if (ingredient instanceof Item item) {
            return new DraggedIngredient.Item(new ItemStack(item));
        } else if (ingredient instanceof FluidStack fluid) {
            return new DraggedIngredient.Fluid(fluid);
        }

        LogUtils.getLogger().info("Unhandled dragged ingredient: {}", ingredient);
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
