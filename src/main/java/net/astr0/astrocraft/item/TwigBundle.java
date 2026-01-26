package net.astr0.astrocraft.item;

import net.astr0.astrocraft.block.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;

import static net.astr0.astrocraft.block.BlockEntityProperties.FACING;
import static net.astr0.astrocraft.block.BlockEntityProperties.LOG_COUNT;
import static net.minecraft.world.level.block.CampfireBlock.LIT;

public class TwigBundle extends Item {
    public TwigBundle(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Called when this item is used when targeting a Block
     */
    public InteractionResult useOn(UseOnContext pContext) {

        BlockState blockState = pContext.getLevel().getBlockState(pContext.getClickedPos());

        if(blockState.is(ModBlocks.LOG_PILE.get())) {
            int count = blockState.getValue(LOG_COUNT);
            ItemStack twigBundle = pContext.getItemInHand();
            Player player = pContext.getPlayer();
            Level level = pContext.getLevel();
            BlockPos blockPos = pContext.getClickedPos();

            if (count < 3) {
                blockState = blockState.setValue(LOG_COUNT, count + 1); // <-- FIXED
                twigBundle.shrink(1);
                pContext.getLevel().setBlock(pContext.getClickedPos(), blockState, Block.UPDATE_ALL); // clear flag

                playSoundForBlock(blockState, level, blockPos, player);

            }else {
                twigBundle.shrink(1);
                BlockState campfire = Blocks.CAMPFIRE.defaultBlockState();
                campfire = campfire.setValue(FACING, blockState.getValue(FACING)).setValue(LIT, false);
                pContext.getLevel().setBlock(pContext.getClickedPos(), campfire, Block.UPDATE_ALL);
                playSoundForBlock(campfire, level, blockPos, player);

            }
            return InteractionResult.SUCCESS;

        }


        InteractionResult interactionresult = this.place(new BlockPlaceContext(pContext));
        if (!interactionresult.consumesAction() && this.isEdible()) {
            InteractionResult interactionresult1 = this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
        } else {
            return interactionresult;
        }
    }

    private void playSoundForBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        SoundType soundtype = state.getSoundType(level, blockPos, player);
        level.playSound(player, blockPos,
                this.getPlaceSound(state, level, blockPos, player),
                SoundSource.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F,
                soundtype.getPitch() * 0.8F
        );
    }

    public InteractionResult place(BlockPlaceContext pContext) {
        if (!pContext.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(pContext);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }

                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, pContext.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    if (player == null || !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    @Deprecated //Forge: Use more sensitive version {@link BlockItem#getPlaceSound(BlockState, IBlockReader, BlockPos, Entity) }
    protected SoundEvent getPlaceSound(BlockState pState) {
        return pState.getSoundType().getPlaceSound();
    }

    //Forge: Sensitive version of BlockItem#getPlaceSound
    protected SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext pContext) {
        return pContext;
    }


    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        BlockState blockstate = ModBlocks.LOG_PILE.get().getStateForPlacement(pContext);
        return blockstate != null && this.canPlace(pContext, blockstate) ? blockstate : null;
    }

    private BlockState updateBlockStateFromTag(BlockPos pPos, Level pLevel, ItemStack pStack, BlockState pState) {
        BlockState blockstate = pState;
        CompoundTag compoundtag = pStack.getTag();
        if (compoundtag != null) {
            CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();

            for(String s : compoundtag1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = compoundtag1.get(s).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != pState) {
            pLevel.setBlock(pPos, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState pState, Property<T> pProperty, String pValueIdentifier) {
        return pProperty.getValue(pValueIdentifier).map((p_40592_) -> {
            return pState.setValue(pProperty, p_40592_);
        }).orElse(pState);
    }

    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        Player player = pContext.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (pState.canSurvive(pContext.getLevel(), pContext.getClickedPos())) && pContext.getLevel().isUnobstructed(pState, pContext.getClickedPos(), collisioncontext);
    }


    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        return pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
    }

}
