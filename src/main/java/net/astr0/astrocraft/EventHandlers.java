package net.astr0.astrocraft;

import net.astr0.astrocraft.block.ModBlocks;
import net.astr0.astrocraft.item.KeyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static net.astr0.astrocraft.block.BlockEntityProperties.BRICK_COUNT;
import static net.astr0.astrocraft.block.BlockEntityProperties.FACING;

public class EventHandlers {

    private static final Map<BlockPos, Integer> dryingLogs = new HashMap<>();
    private static final int CONVERT_TICKS = 20 * 60 * 5; // 5 minutes


    // Call this when a block is placed
    public static void tryStartDrying(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        if (state.is(BlockTags.LOGS)) {
            BlockState above = level.getBlockState(pos.above());
            if (above.getBlock() == Blocks.CAMPFIRE) {
                dryingLogs.put(pos.immutable(), 0);
            }
        }
    }

    public static @Nullable KeyItem getKeyFromPlayer(Player player) {
        AtomicReference<KeyItem> equippedKey = new AtomicReference<>();
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
        curiosInventory.getStacksHandler("key").ifPresent(slotInventory -> {
            if(slotInventory.getStacks().getStackInSlot(0).getItem() instanceof KeyItem key) {
                equippedKey.set(key);
            }
        });

        return equippedKey.get();
    }

    public static InteractionResult restrictBlockEntityAccess(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() || event.getPhase() != EventPriority.HIGH) return InteractionResult.PASS;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState block = level.getBlockState(pos);


        if (block.is(ModTags.LOCKED_BLOCK)) {

            InteractionHand hand = event.getHand();
            ItemStack itemUsed = event.getItemStack();
            Player player = event.getEntity();
            KeyItem equippedKey = getKeyFromPlayer(player);

            if (equippedKey != null && equippedKey.Unlocks(block, player.level().dimensionTypeId())) {
                return InteractionResult.SUCCESS;
            }

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.CONSUME);
            return  InteractionResult.CONSUME;
        }

        // if block is be and has tag, then check for specific tag
        //     then check curios slot
        //     if curio present: return a success
        //     if not, cancel the event
        return InteractionResult.PASS;
    }

    public static float modifyBreakSpeed(Player player, BlockState state, @Nullable BlockPos pos, float speed) {
        // We only care about preventing tree chopping
        if (!state.is(BlockTags.LOGS)) return speed;

        ItemStack tool = player.getMainHandItem();

        // This might be a bit of a hack but we will generally assume that all tools with durability are fine
        //TODO: Fix this potential bug which will allow wood access to early, perhaps pivot to curios based thing
        if (tool.isDamageableItem()) {
            return speed;
        }

        return 0;
    }

    public static void PreventTreePunching(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(modifyBreakSpeed(event.getEntity(), event.getState(), event.getPosition().orElse(null), event.getNewSpeed()));
    }

    public static void DoCampfireConversion(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide() || !event.level.dimensionTypeId().location().getPath().equals("overworld"))
            return;

        Level level = event.level;


        Iterator<Map.Entry<BlockPos, Integer>> it = dryingLogs.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = it.next();
            BlockPos pos = entry.getKey();
            int ticks = entry.getValue();


            if (!level.isLoaded(pos)) {
                continue;
            }

            BlockState above = level.getBlockState(pos.above());
            if (above.getBlock() != Blocks.CAMPFIRE) {
                it.remove();
                continue;
            }


            if (ticks >= CONVERT_TICKS) {
                level.setBlockAndUpdate(pos, Blocks.COAL_BLOCK.defaultBlockState());
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.0f);
                it.remove();
            } else {
                entry.setValue(ticks + 1);
            }
        }
    }

    public static void BlockPlaceListener(BlockEvent.EntityPlaceEvent event) {
        BlockState state = event.getPlacedBlock();
        tryStartDrying((Level) event.getLevel(), event.getPos(), state);
    }

    public static InteractionResult HandleBrickPlacement(PlayerInteractEvent.RightClickBlock event) {

        if (event.isCanceled() || event.getPhase() != EventPriority.LOW) return InteractionResult.PASS;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState block = level.getBlockState(pos);
        InteractionHand hand = event.getHand();
        ItemStack itemUsed = event.getItemStack();
        BlockHitResult blockHitResult = event.getHitVec();
        Player player = event.getEntity();

        if (block.is(ModBlocks.BRICK_KILN_PILE.get()) && itemUsed.is(Items.BRICK)) {
            int brick_count = block.getValue(BRICK_COUNT);
            Direction facing = block.getValue(FACING);

            if (brick_count < 18) {
                BlockState brick_kiln_pile = block.setValue(BRICK_COUNT, brick_count + 1);
                itemUsed.shrink(1);
                level.setBlock(pos, brick_kiln_pile, Block.UPDATE_ALL); // clear flag

                playSoundForBlock(brick_kiln_pile, level, pos, player);
                return InteractionResult.SUCCESS;
            } else {
                BlockState brick_kiln = ModBlocks.BRICK_KILN.get().defaultBlockState();
                brick_kiln = brick_kiln.setValue(FACING, facing);

                itemUsed.shrink(1);
                level.setBlock(pos, brick_kiln, Block.UPDATE_ALL); // clear flag

                playSoundForBlock(brick_kiln, level, pos, player);
            }

        } else if (event.getFace() == Direction.UP && level.getBlockState(pos.above()).is(Blocks.AIR) && itemUsed.is(Items.BRICK)) {
            BlockState brick_kiln_pile = ModBlocks.BRICK_KILN_PILE.get().getStateForPlacement(new BlockPlaceContext(player, hand, itemUsed, blockHitResult));
            itemUsed.shrink(1);
            level.setBlock(pos.above(), brick_kiln_pile, Block.UPDATE_ALL); // clear flag

            playSoundForBlock(brick_kiln_pile, level, pos, player);
            return InteractionResult.SUCCESS;
        } else if (itemUsed.is(Items.STICK)  && block.is(Blocks.CAMPFIRE)) {
            ItemStack torches = new ItemStack(Items.TORCH);
            torches.setCount(itemUsed.getCount());
            player.setItemInHand(hand, torches);
        }

        return null;
    }

    private static void playSoundForBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        SoundType soundtype = state.getSoundType(level, blockPos, player);
        level.playSound(player, blockPos,
                state.getSoundType().getPlaceSound(),
                SoundSource.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F,
                soundtype.getPitch() * 0.8F
        );
    }
}
