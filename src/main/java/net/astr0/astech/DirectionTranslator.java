package net.astr0.astech;

import net.minecraft.core.Direction;

public class DirectionTranslator {

    /**
     * Translates a direction relative to the block's orientation direction.
     *
     * @param original      The original direction to translate.
     * @param orientation   The orientation of the block.
     * @return              The translated direction.
     */
    public static Direction translate(Direction original, Direction orientation) {
        // Rotate the original direction around the block's orientation

        if (original == Direction.UP || original == Direction.DOWN) {
            return original;
        }
        return switch (orientation) {
            case NORTH -> original;
            case SOUTH -> original.getOpposite();
            case WEST -> original.getClockWise();
            case EAST -> original.getCounterClockWise();
            default -> original;
        };
    }

    public static void main(String[] args) {
        Direction orientation = Direction.NORTH; // Block's orientation
        Direction original = Direction.EAST; // Original direction to translate

        Direction translated = translate(original, orientation);
        System.out.println("Translated Direction: " + translated);
    }
}
