package net.astr0.astech.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface MultiblockMatcher {
    boolean matches(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, MultiblockValidationContext context);
}

