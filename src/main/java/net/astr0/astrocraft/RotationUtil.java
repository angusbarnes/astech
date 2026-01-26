package net.astr0.astrocraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class RotationUtil {

    /**
     * Rotates a relative BlockPos around the Y axis depending on controller's horizontal facing.
     * Y is not affected.
     */
    public static BlockPos rotateOffset(BlockPos offset, Direction facing) {
        int x = offset.getX();
        int y = offset.getY();
        int z = offset.getZ();

        return switch (facing) {
            case NORTH -> new BlockPos(x, y, z);             // Default orientation
            case SOUTH -> new BlockPos(-x, y, -z);           // 180° rotation
            case EAST  -> new BlockPos(-z, y, x);            // 90° clockwise
            case WEST  -> new BlockPos(z, y, -x);            // 90° counterclockwise
            default    -> offset; // Shouldn't happen
        };
    }
}
