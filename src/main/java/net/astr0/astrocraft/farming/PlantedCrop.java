package net.astr0.astrocraft.farming;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.IPlantable;

public record PlantedCrop(Item seed, IPlantable Plant, CropGenetics genetics) {
}
