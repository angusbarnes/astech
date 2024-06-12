package net.astr0.astech;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtils {

    // Method to get surrounding blocks relative to the target block's orientation
    public static BlockState[] getSurroundingBlocks(Level world, BlockPos targetPos, Direction orientation) {
        BlockState[] surroundingBlocks = new BlockState[4]; // Array to store the 4 surrounding blocks

        // Define the relative positions based on the orientation
        BlockPos[] relativePositions;
        switch (orientation) {
            case NORTH:
                relativePositions = new BlockPos[]{
                        targetPos.north(), // Front
                        targetPos.south(), // Back
                        targetPos.west(),  // Left
                        targetPos.east()   // Right
                };
                break;
            case SOUTH:
                relativePositions = new BlockPos[]{
                        targetPos.south(), // Front
                        targetPos.north(), // Back
                        targetPos.east(),  // Left
                        targetPos.west()   // Right
                };
                break;
            case WEST:
                relativePositions = new BlockPos[]{
                        targetPos.west(),  // Front
                        targetPos.east(),  // Back
                        targetPos.south(), // Left
                        targetPos.north()  // Right
                };
                break;
            case EAST:
                relativePositions = new BlockPos[]{
                        targetPos.east(),  // Front
                        targetPos.west(),  // Back
                        targetPos.north(), // Left
                        targetPos.south()  // Right
                };
                break;
            default:
                throw new IllegalArgumentException("Unsupported orientation: " + orientation);
        }

        // Retrieve the blocks at the relative positions
        for (int i = 0; i < relativePositions.length; i++) {
            surroundingBlocks[i] = world.getBlockState(relativePositions[i]);
        }

        return surroundingBlocks;
    }
}
