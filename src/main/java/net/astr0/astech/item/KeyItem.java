package net.astr0.astech.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class KeyItem extends Item {
    public KeyItem(Properties pProperties) {
        super(pProperties.stacksTo(8));
    }

    protected TagKey<Block> BLOCK_UNLOCK_TAG;

    public KeyItem(TagKey<Block> keyTag) {
        this(new Properties());
        BLOCK_UNLOCK_TAG = keyTag;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    public boolean Unlocks(BlockState block) {
        return block.is(BLOCK_UNLOCK_TAG);
    }
}
