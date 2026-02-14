package net.astr0.astrocraft.farming;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

public class CropUtils {

    public static @Nullable IPlantable getPlantableFromSeed(Item seedItem) {
        if (seedItem instanceof BlockItem block && block.getBlock() instanceof IPlantable plant) {
            return plant;
        }

        return null;
    }

    public static @Nullable PlantedCrop getPlantedCrop(ItemStack seed) {
        IPlantable plant = getPlantableFromSeed(seed.getItem());
        ItemStack seedStack = seed.copy();
        seedStack.setCount(1);

        if (plant != null) {
            return new PlantedCrop(seedStack, plant, CropGenetics.fromStack(seedStack));
        }

        return null;
    }
}
