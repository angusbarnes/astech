package net.astr0.astech;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public static float modifyBreakSpeed(Player player, BlockState state, @Nullable BlockPos pos, float speed)
    {
        // We only care about preventing tree chopping
        if (!state.is(BlockTags.LOGS)) return speed;

        ItemStack tool = player.getMainHandItem();

        // This might be a bit of a hack but we will generally assume that all tools with durability are fine
        if (tool.isDamageableItem()) {
            return speed;
        }

        return 0;
    }

    public static void PreventTreePunching(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(modifyBreakSpeed(event.getEntity(), event.getState(), event.getPosition().orElse(null), event.getNewSpeed()));
    }

    public static void DoCampfireConversion(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide() || !event.level.dimensionTypeId().location().getPath().equals("overworld")) return;

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
}
