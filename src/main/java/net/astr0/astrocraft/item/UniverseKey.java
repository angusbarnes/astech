package net.astr0.astrocraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;

public class UniverseKey extends DimensionKeyItem {

    public UniverseKey(TagKey<Block> keyTag, String dimension, ChatFormatting color, String[] allowedDims) {
        super(keyTag, dimension, color, allowedDims);
    }

    @Override
    protected boolean isValidDimension(ResourceKey<DimensionType> dimension) {
        return true;
    }
}
