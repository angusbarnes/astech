package net.astr0.astech.item;

import net.astr0.astech.IConfigurable;
import net.astr0.astech.NbtPrettyPrinter;
import net.astr0.astech.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TabletItem extends Item {
    public TabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false; // Don't allow breaking either
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS; // Tell client "I handled this"
        }

        BlockPos pos = context.getClickedPos();
        BlockState block = level.getBlockState(pos);

        if (block.hasBlockEntity()) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be == null) {
                return InteractionResult.FAIL;
            }

            level.playSound(null, pos, SoundRegistry.beep.get(), SoundSource.MASTER);

            if (be instanceof IConfigurable configurableBe) {

                player.displayClientMessage(configurableBe.getTabletSummary(), false);
            } else {
                Component message = NbtPrettyPrinter.prettyPrint(be.saveWithFullMetadata());
                player.displayClientMessage(message, false);
            }
        }

        return InteractionResult.CONSUME; // ← This stops the GUI from opening!
    }
}
