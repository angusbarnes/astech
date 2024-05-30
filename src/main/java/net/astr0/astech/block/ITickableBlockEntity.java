package net.astr0.astech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {
    void tick(Level pLevel, BlockPos pPos, BlockState pState);

    static <T extends BlockEntity> BlockEntityTicker<T> getTickerHelper(Level level) {
        return getTickerHelper(level, false);
    }

    static <T extends BlockEntity> BlockEntityTicker<T> getTickerHelper(Level level, boolean allowClient) {
        return level.isClientSide() && !allowClient ? null : (level0, pos0, state0, blockEntity) -> ((ITickableBlockEntity)blockEntity).tick(level0, pos0, state0);
    }
}
