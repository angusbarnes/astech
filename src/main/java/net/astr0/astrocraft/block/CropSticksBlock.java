package net.astr0.astrocraft.block;

import mekanism.common.util.VoxelShapeUtils;
import net.astr0.astrocraft.farming.CropUtils;
import net.astr0.astrocraft.farming.PlantedCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

import java.util.ArrayList;
import java.util.List;

public class CropSticksBlock extends CropBlock implements EntityBlock, BonemealableBlock {

    private final VoxelShape STICK1 = Block.box(1.0D, 0.0D,  1.0D, 2.0D, 16.0D, 2.0D);
    private final VoxelShape STICK2 = Block.box(14.0D,0.0D, 14.0D, 15.0D, 16.0D, 15.0D);
    private final VoxelShape STICK3 = Block.box(1.0D, 0.0D, 14.0D, 2.0D, 16.0D, 15.0D);
    private final VoxelShape STICK4 = Block.box(14.0D,0.0D,  1.0D, 15.0D, 16.0D, 2.0D);
    private final  VoxelShape SHAPE = VoxelShapeUtils.combine(STICK1, STICK2, STICK3, STICK4);
    private final VoxelShape INTERACTION_SHAPE = Block.box(0,0,0,16,16,16);
    //public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public CropSticksBlock(Properties props) {
        super(Properties.copy(Blocks.WHEAT));
       // this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {

        if (pLevel.getBlockEntity(pPos) instanceof CropSticksBlockEntity be) {
            return be.getSeed();
        }

        return super.getCloneItemStack(pLevel, pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CropSticksBlockEntity cropBE)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);
        ItemStack currentSeed = cropBE.getSeed();
        PlantedCrop heldPlant = CropUtils.getPlantedCrop(held);
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, player.getItemInHand(hand))
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, cropBE);

        // 1. SWAP LOGIC: Player is holding a valid, DIFFERENT seed
        if (heldPlant != null && !currentSeed.isEmpty() && !ItemStack.isSameItemSameTags(held, currentSeed)) {

            if (state.getValue(AGE) == 7) {
                // It's mature: Perform a full harvest first
                List<ItemStack> drops = cropBE.simulateDrops(builder);
                for (ItemStack drop : drops) {
                    popResource(level, pos, drop);
                }
            } else {
                // Not mature: Just pop the current seed back out
                popResource(level, pos, currentSeed);
            }

            // Plant the new seed
            cropBE.setSeed(held);
            if (!player.isCreative()) held.shrink(1);

            // Reset age to 0 for the new plant
            level.setBlock(pos, state.setValue(AGE, 0), 2);
            return InteractionResult.SUCCESS;
        }

        // 2. PLANTING LOGIC: Stick is empty
        if (currentSeed.isEmpty() && heldPlant != null) {
            cropBE.setSeed(held);
            if (!player.isCreative()) held.shrink(1);
            return InteractionResult.SUCCESS;
        }

        // 3. STANDARD HARVEST LOGIC: Stick is mature, player isn't trying to swap
        if (!currentSeed.isEmpty() && state.getValue(AGE) == 7) {
            List<ItemStack> drops = cropBE.simulateDrops((builder));
            Item seedItem = currentSeed.getItem();

            for (ItemStack drop : drops) {
                // Filter logic: In a standard harvest, we reset AGE but keep the seed in the BE.
                // Therefore, we don't drop the "base" seed stack.
                if (drop.is(currentSeed.getItem())) {
                    drop.shrink(1);
                    if (drop.getCount() == 0) continue;
                }
                popResource(level, pos, drop);
            }

            level.setBlock(pos, state.setValue(AGE, 0), 2);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();

        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (be == null) {
            Vec3 origin = builder.getOptionalParameter(LootContextParams.ORIGIN);
            if (origin != null) {
                BlockPos pos = BlockPos.containing(origin);
                be = builder.getLevel().getBlockEntity(pos);
            }
        }

        if (be instanceof CropSticksBlockEntity cropBE) {
            // Pass the builder directly
            List<ItemStack> simulated = cropBE.simulateDrops(builder);

            if (simulated.isEmpty() && !cropBE.getSeed().isEmpty()) {
                drops.add(cropBE.getSeed());
            } else {
                drops.addAll(simulated);
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

//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(AGE);
//    }
}