package net.astr0.astrocraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtils {

    // Method to get surrounding blocks relative to the target block's orientation
    public static BlockState[] getSurroundingBlocks(Level world, BlockPos targetPos, Direction orientation) {
        BlockState[] surroundingBlocks = new BlockState[4]; // Array to store the 4 surrounding blocks

        // Define the relative positions based on the orientation
        BlockPos[] relativePositions = switch (orientation) {
            case NORTH -> new BlockPos[]{
                    targetPos.north(), // Front
                    targetPos.south(), // Back
                    targetPos.west(),  // Left
                    targetPos.east()   // Right
            };
            case SOUTH -> new BlockPos[]{
                    targetPos.south(), // Front
                    targetPos.north(), // Back
                    targetPos.east(),  // Left
                    targetPos.west()   // Right
            };
            case WEST -> new BlockPos[]{
                    targetPos.west(),  // Front
                    targetPos.east(),  // Back
                    targetPos.south(), // Left
                    targetPos.north()  // Right
            };
            case EAST -> new BlockPos[]{
                    targetPos.east(),  // Front
                    targetPos.west(),  // Back
                    targetPos.north(), // Left
                    targetPos.south()  // Right
            };
            default -> throw new IllegalArgumentException("Unsupported orientation: " + orientation);
        };

        // Retrieve the blocks at the relative positions
        for (int i = 0; i < relativePositions.length; i++) {
            surroundingBlocks[i] = world.getBlockState(relativePositions[i]);
        }

        return surroundingBlocks;
    }

}