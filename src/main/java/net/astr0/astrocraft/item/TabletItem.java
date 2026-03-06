package net.astr0.astrocraft.item;

import net.astr0.astrocraft.IConfigurable;
import net.astr0.astrocraft.NbtPrettyPrinter;
import net.astr0.astrocraft.SoundRegistry;
import net.astr0.astrocraft.compat.CompatManager;
import net.astr0.astrocraft.compat.create.CreateCompat;
import net.astr0.astrocraft.compat.enderio.TravelStaffPatches;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;

public class TabletItem extends Item {
    public TabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false; // Don't allow breaking either
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

            if (TravelStaffPatches.performAction(this,level, player, stack)) {
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }

        return super.use(level, player, usedHand);
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

        if (CompatManager.isCreateLoaded) {
            InteractionResult result = CreateCompat.handleWrench(context);
            if (result.consumesAction()) return result;
        }

        Collection<Property<?>> props = block.getProperties();
        for(Property<?> prop : props) {
            player.displayClientMessage(Component.literal(prop.toString()), false);
        }

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
        } else {
            player.displayClientMessage(Component.literal(String.format("%f", level.getBiome(player.getOnPos()).get().getBaseTemperature())), true);
        }

        return InteractionResult.CONSUME; // ← This stops the GUI from opening!
    }
}
