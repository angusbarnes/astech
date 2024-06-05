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
        switch (orientation) {
            case NORTH:
                return original;
            case SOUTH:
                return original.getOpposite();
            case WEST:
                return original.getClockWise();
            case EAST:
                return original.getCounterClockWise();
            default:
                return original;
        }
    }

    public static void main(String[] args) {
        Direction orientation = Direction.NORTH; // Block's orientation
        Direction original = Direction.EAST; // Original direction to translate

        Direction translated = translate(original, orientation);
        System.out.println("Translated Direction: " + translated);
    }
}
