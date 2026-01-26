package net.astr0.astrocraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.api.SlotContext;

public class DimensionKeyItem extends KeyItem{

    public DimensionKeyItem(TagKey<Block> keyTag, String dimension, ChatFormatting color, String[] allowedDims) {
        super(keyTag, dimension, color, allowedDims);
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
