package net.astr0.astrocraft.block;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockEntityProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty LOG_COUNT = IntegerProperty.create("log_count", 1, 3);
    public static final IntegerProperty BRICK_COUNT = IntegerProperty.create("brick_count", 1, 18);
}
