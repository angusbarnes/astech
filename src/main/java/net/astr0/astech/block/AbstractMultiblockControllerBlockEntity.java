package net.astr0.astech.block;

import net.astr0.astech.RotationUtil;
import net.astr0.astech.block.multiblock.MultiblockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class AbstractMultiblockControllerBlockEntity extends BlockEntity {

    private boolean isMultiblockValid = false;
    private boolean needsValidation = true;


    public AbstractMultiblockControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void markForValidation() {
        needsValidation = true;
    }

    public void invalidateMultiblock() {
        isMultiblockValid = false;
        needsValidation = true;
    }

    public boolean validateMultiblock() {
        if (level == null) return false;

        BlockState controllerState = level.getBlockState(worldPosition);
        Direction facing = controllerState.getValue(BlockEntityProperties.FACING);

        for (Map.Entry<BlockPos, MultiblockMatcher> entry : getStructurePattern().entrySet()) {
            BlockPos relativeOffset = entry.getKey();
            MultiblockMatcher matcher = entry.getValue();

            BlockPos rotatedOffset = RotationUtil.rotateOffset(relativeOffset, facing);
            BlockPos targetPos = worldPosition.offset(rotatedOffset);

            BlockState actualState = level.getBlockState(targetPos);
            BlockEntity be = level.getBlockEntity(targetPos);

            if (!matcher.matches(level, targetPos, actualState, be)) {
                invalidateMultiblock();
                return false;
            }
        }

        needsValidation = false;
        isMultiblockValid = true;
        return true;
    }


    /**
     * Defines the multiblock structure relative to the controller's default NORTH-facing orientation.
     * Can include positions above/below using Y offset.
     */
    protected abstract Map<BlockPos, MultiblockMatcher> getStructurePattern();

}


