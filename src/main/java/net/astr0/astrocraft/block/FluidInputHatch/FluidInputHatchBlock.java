package net.astr0.astrocraft.block.FluidInputHatch;

import net.astr0.astrocraft.block.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FluidInputHatchBlock extends Block implements EntityBlock {


    public FluidInputHatchBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FluidInputHatchBlockEntity(pPos, pState);
    }

    // Allows the game to get our tick function, so it can call it every logic frame.
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {

        return ITickableBlockEntity.getTickerHelper(pLevel);
    }
}
