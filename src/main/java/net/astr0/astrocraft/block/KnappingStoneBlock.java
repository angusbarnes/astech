package net.astr0.astrocraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.astr0.astrocraft.block.BlockEntityProperties.FACING;
import static net.astr0.astrocraft.block.BlockEntityProperties.LOG_COUNT;

public class KnappingStoneBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);

    public KnappingStoneBlock(Properties pProperties) {
        super(pProperties.strength(1f, 10));

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(LOG_COUNT, 1);
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link BlockStateBase#getRenderShape}
     * whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

}
