package net.astr0.astrocraft.farming;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.IPlantable;

public record PlantedCrop(ItemStack seed, IPlantable Plant, CropGenome genetics) {
}
