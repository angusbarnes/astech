package net.astr0.astech.block;

import net.astr0.astech.RotationUtil;
import net.astr0.astech.block.multiblock.MultiblockMatcher;
import net.astr0.astech.block.multiblock.MultiblockValidationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.astr0.astech.block.BlockEntityProperties.FACING;

public abstract class AbstractMultiblockControllerBlockEntity extends BlockEntity {

    private boolean isMultiblockValid = false;
    private boolean needsValidation = true;

    protected MultiblockValidationContext validationContext;

    public AbstractMultiblockControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        validationContext = new MultiblockValidationContext(worldPosition, state.getValue(FACING), 1, 1, 2, 2);
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
        Direction facing = controllerState.getValue(FACING);

        validationContext = new MultiblockValidationContext(worldPosition, facing, 1, 1, 2, 2);

        for (Map.Entry<BlockPos, MultiblockMatcher> entry : getStructurePattern().entrySet()) {
            BlockPos relativeOffset = entry.getKey();
            MultiblockMatcher matcher = entry.getValue();

            BlockPos rotatedOffset = RotationUtil.rotateOffset(relativeOffset, facing);
            BlockPos targetPos = worldPosition.offset(rotatedOffset);

            BlockState actualState = level.getBlockState(targetPos);
            BlockEntity be = level.getBlockEntity(targetPos);

            if (!matcher.matches(level, targetPos, actualState, be, validationContext)) {
                invalidateMultiblock();
                return false;
            }
        }

        if (!validationContext.finalizeValidation()) {
            invalidateMultiblock();
            return false;
        }

        needsValidation = false;
        isMultiblockValid = true;
        return true;
    }

    public static Map<BlockPos, MultiblockMatcher> parseStructure(
            List<String[]> layers,
            Map<Character, MultiblockMatcher> matcherMap
    ) {
        Map<BlockPos, MultiblockMatcher> pattern = new HashMap<>();

        int controllerY = -1;
        int controllerZ = -1;
        int controllerX = -1;

        // First pass: find controller position
        outer:
        for (int y = 0; y < layers.size(); y++) {
            String[] layer = layers.get(y);
            for (int z = 0; z < layer.length; z++) {
                String row = layer[z];
                for (int x = 0; x < row.length(); x++) {
                    if (row.charAt(x) == 'C') {
                        controllerY = y;
                        controllerZ = z;
                        controllerX = x;
                        break outer;
                    }
                }
            }
        }

        if (controllerY == -1) {
            throw new IllegalArgumentException("Structure definition must contain exactly one 'C' (controller) block.");
        }

        for (int y = 0; y < layers.size(); y++) {
            String[] layer = layers.get(y);
            for (int z = 0; z < layer.length; z++) {
                String row = layer[z];
                for (int x = 0; x < row.length(); x++) {
                    char symbol = row.charAt(x);
                    MultiblockMatcher matcher = matcherMap.get(symbol);

                    if (matcher == null) {
                        throw new IllegalArgumentException("No matcher for symbol: '" + symbol + "'");
                    }


                    BlockPos relativePos = new BlockPos(
                            -(x - controllerX),
                            y - controllerY,
                            -(z - controllerZ)
                    );
                    pattern.put(relativePos, matcher);
                }
            }
        }

        return pattern;
    }


    /**
     * Defines the multiblock structure relative to the controller's default NORTH-facing orientation.
     * Can include positions above/below using Y offset.
     */
    protected abstract Map<BlockPos, MultiblockMatcher> getStructurePattern();

}


