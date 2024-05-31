package net.astr0.astech.block.ChemicalMixer;

import net.astr0.astech.block.ITickableBlockEntity;
import net.astr0.astech.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChemicalMixerStationBlock extends HorizontalDirectionalBlock implements EntityBlock {
    //public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public ChemicalMixerStationBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }


    // Pretty confident we can probably ignore this over-ride for cubic machines, this seems to be for settng the bounding
    // box in non-standard scenarios
//    @Override
//    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//        return SHAPE;
//    }

//    // Not sure if we need this or not, I will leave it for now
//    @Override
//    public RenderShape getRenderShape(BlockState pState) {
//        return RenderShape.MODEL;
//    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    // This is called when the block is destroyed, it over-rides BaseEntityBlock
    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {

        // The block state may changed when a texture is changed,
        // or the block is rotated. We should confirm that the original block has actually been removed,
        // ie; we have a different block
        if (pState.getBlock() != pNewState.getBlock()) {

            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ChemicalMixerStationBlockEntity) {
                // After safety rail checks, use our user defined function
                // to create the dropped items
                ((ChemicalMixerStationBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    // Over-rides our baseBlockEntity. Caled when this block is right-clicked on
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof ChemicalMixerStationBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (ChemicalMixerStationBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        // Return success for both sides. Not that we only perform logic on the server
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    // Implements the EntityBlock interface, which the abstract BaseEntityBlock does not do for us
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChemicalMixerStationBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    // Allows the game to get our tick function, so it can call it every logic frame.
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {

        return ITickableBlockEntity.getTickerHelper(pLevel);
    }
}