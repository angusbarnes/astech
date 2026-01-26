package net.astr0.astrocraft.block.CoolantBlock;

import net.astr0.astrocraft.block.BlockEntityProperties;
import net.astr0.astrocraft.block.ITickableBlockEntity;
import net.astr0.astrocraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoolantBlock extends HorizontalDirectionalBlock implements EntityBlock {


    public CoolantBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        registerDefaultState(stateDefinition.any().setValue(BlockEntityProperties.ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(BlockEntityProperties.ACTIVE);
    }

    // This is called when the block is destroyed, it over-rides BaseEntityBlock
    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    // Over-rides our baseBlockEntity. Caled when this block is right-clicked on
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof CoolantBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (CoolantBlockEntity)entity, pPos);
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
        return new CoolantBlockEntity(pPos, pState);
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

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(ModBlocks.BLOCKSTATE_ACTIVE)) {
            double d0 = (double)pPos.getX() + 0.5D;
            double d1 = pPos.getY();
            double d2 = (double)pPos.getZ() + 0.5D;
//            if (pRandom.nextDouble() < 0.05D) {
//                pLevel.playLocalSound(d0, d1, d2, SoundRegistry.laser_machine.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
//            }

            Direction direction = pState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = pRandom.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
            double d6 = pRandom.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
            pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }
}