package net.astr0.astrocraft.block;

import com.mojang.logging.LogUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.astr0.astrocraft.farming.CropUtils;
import net.astr0.astrocraft.farming.PlantedCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

import java.util.List;

public class CropSticksBlock extends BushBlock implements EntityBlock, BonemealableBlock {

    private final VoxelShape STICK1 = Block.box(1.0D, 0.0D,  1.0D, 2.0D, 16.0D, 2.0D);
    private final VoxelShape STICK2 = Block.box(14.0D,0.0D, 14.0D, 15.0D, 16.0D, 15.0D);
    private final VoxelShape STICK3 = Block.box(1.0D, 0.0D, 14.0D, 2.0D, 16.0D, 15.0D);
    private final VoxelShape STICK4 = Block.box(14.0D,0.0D,  1.0D, 15.0D, 16.0D, 2.0D);
    private final  VoxelShape SHAPE = VoxelShapeUtils.combine(STICK1, STICK2, STICK3, STICK4);
    private final VoxelShape INTERACTION_SHAPE = Block.box(0,0,0,16,16,16);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public CropSticksBlock(Properties props) {
        super(Properties.copy(Blocks.WHEAT));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CropSticksBlockEntity(pos, state);
    }

    // --- 1. Hybrid Ticking ---
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        // Always tick so we can check if the BE needs to grow
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof CropSticksBlockEntity be) {
            be.performGrowthTick(level, random);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).getBlock() instanceof FarmBlock;
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    // --- 2. Interaction (Planting & Harvesting) ---
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CropSticksBlockEntity cropBE) {
            ItemStack held = player.getItemInHand(hand);
            LogUtils.getLogger().info("======= Held Item: {}", held);

            // A. Planting
            if (cropBE.getSeed().isEmpty()) {
                PlantedCrop planted = CropUtils.getPlantedCrop(held);

                if (planted != null) {
                    LogUtils.getLogger().info("======= Item is plantable with these genetics {}", planted.genetics());
                    cropBE.setPlanted(planted);
                    if (!player.isCreative()) held.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
            // B. Harvesting (Right-Click)
            else if (state.getValue(AGE) == 7) {
                // Simulate drops
                LogUtils.getLogger().info("++++++++ We trynna harvest");
                List<ItemStack> drops = cropBE.simulateDrops((ServerLevel) level);
                for (ItemStack drop : drops) {
                    popResource(level, pos, drop);
                }

                // Reset age to 0, keep the seed and stats
                level.setBlock(pos, state.setValue(AGE, 0), 2);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    // --- 3. Breaking (Simulate Drops) ---
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // Standard drop: The Sticks themselves
        List<ItemStack> drops = super.getDrops(state, params);

        // Add the Crop drops
        if (params.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY) instanceof CropSticksBlockEntity be) {
            if (be.getLevel() instanceof ServerLevel serverLevel) {
                drops.addAll(be.simulateDrops(serverLevel));
            } else {
                // Fallback if level isn't server (rare in getDrops)
                drops.add(be.getSeed());
            }
        }
        return drops;
    }

    // --- Bonemeal Support ---
    @Override
    public boolean isValidBonemealTarget(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 7;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof CropSticksBlockEntity be) {
            // Force a growth tick
            be.performGrowthTick(level, random);
        }
    }


//    @Override
//    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//        return SHAPE;
//    }
//
//
//    @Override
//    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
//        return INTERACTION_SHAPE;
//    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}