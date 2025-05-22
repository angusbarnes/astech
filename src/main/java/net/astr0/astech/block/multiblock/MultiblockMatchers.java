package net.astr0.astech.block.multiblock;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;

public class MultiblockMatchers {

    public static MultiblockMatcher ofBlock(Block block) {
        return (level, pos, state, be) -> state.is(block);
    }

    public static MultiblockMatcher ofTag(TagKey<Block> tag) {
        return (level, pos, state, be) -> state.is(tag);
    }

    public static MultiblockMatcher ofBlockEntityType(BlockEntityType<?> type) {
        return (level, pos, state, be) -> be != null && be.getType() == type;
    }

    public static MultiblockMatcher anyOf(MultiblockMatcher... matchers) {
        return (level, pos, state, be) -> Arrays.stream(matchers).anyMatch(m -> m.matches(level, pos, state, be));
    }
}