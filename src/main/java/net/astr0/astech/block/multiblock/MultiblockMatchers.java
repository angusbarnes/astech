package net.astr0.astech.block.multiblock;

import com.mojang.logging.LogUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;

public class MultiblockMatchers {

    public static MultiblockMatcher ofBlock(Block block) {

        return (level, pos, state, be, ctx) -> {
            LogUtils.getLogger().info("Looking for {} at {}, found {}", block.getName(), pos.toShortString(), state.getBlock().getName());
            return state.is(block);
        };
    }

    public static MultiblockMatcher ofTag(TagKey<Block> tag) {
        return (level, pos, state, be, ctx) -> state.is(tag);
    }

    public static MultiblockMatcher ofBlockEntityType(BlockEntityType<?> type) {
        return (level, pos, state, be, ctx) -> be != null && be.getType() == type;
    }

    public static MultiblockMatcher anyOf(MultiblockMatcher... matchers) {
        return (level, pos, state, be, ctx) -> Arrays.stream(matchers).anyMatch(m -> m.matches(level, pos, state, be, ctx));
    }

    public static MultiblockMatcher casingOrHatch(Block casingBlock) {
        return (level, pos, state, be, context) -> {
            LogUtils.getLogger().info("Looking for Casing OR Hatch at {}, found {}", pos.toShortString(), state.getBlock().getName());
            if (state.is(casingBlock)) return true;

            return context.registerHatch(pos, state);
        };
    }
}