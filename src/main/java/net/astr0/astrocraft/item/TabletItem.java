package net.astr0.astrocraft.item;

import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismItems;
import net.astr0.astrocraft.client.gui.RadialMenuScreen;
import net.astr0.astrocraft.common.RadialMenu;
import net.astr0.astrocraft.common.RadialMenuEntry;
import net.astr0.astrocraft.compat.CompatManager;
import net.astr0.astrocraft.compat.create.CreateCompat;
import net.astr0.astrocraft.compat.enderio.TravelStaffPatches;
import net.astr0.astrocraft.compat.mek.MekCompat;
import net.astr0.astrocraft.network.AsTechNetworkHandler;
import net.astr0.astrocraft.network.ServerboundSetToolModePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

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

            if (TabletModes.isActive(stack, TabletModes.TRAVEL) && TravelStaffPatches.performAction(this,level, player, stack)) {
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

        if (CompatManager.isCreateLoaded && TabletModes.isActive(stack, TabletModes.CREATE)) {
            InteractionResult result = CreateCompat.handleWrench(context);
            if (result.consumesAction()) return result;
        }

        if (CompatManager.isMekanismLoaded && TabletModes.isActive(stack, TabletModes.MEK)) {
            TransmissionType activeType = TransmissionType.ITEM;
            InteractionResult result = MekCompat.handleConfigurator(context, activeType);
            if (result.consumesAction()) return result;
        }

//        Collection<Property<?>> props = block.getProperties();
//        for(Property<?> prop : props) {
//            player.displayClientMessage(Component.literal(prop.toString()), false);
//        }
//
//        if (block.hasBlockEntity()) {
//            BlockEntity be = level.getBlockEntity(pos);
//
//            if (be == null) {
//                return InteractionResult.FAIL;
//            }
//
//            level.playSound(null, pos, SoundRegistry.beep.get(), SoundSource.MASTER);
//
//            if (be instanceof IConfigurable configurableBe) {
//
//                player.displayClientMessage(configurableBe.getTabletSummary(), false);
//            } else {
//                Component message = NbtPrettyPrinter.prettyPrint(be.saveWithFullMetadata());
//                player.displayClientMessage(message, false);
//            }
//        } else {
//            player.displayClientMessage(Component.literal(String.format("%f", level.getBiome(player.getOnPos()).get().getBaseTemperature())), true);
//        }

        return InteractionResult.PASS; // ← This stops the GUI from opening!
    }

    // Client-only — annotate or guard with isClientSide before calling
    @net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
    public void openTabletRadialMenu(ItemStack stack) {
        Set<String> active = TabletModes.getActiveModes(stack);



        RadialMenu menu = RadialMenu.builder(Component.literal("Tablet Modes"))
                .addEntry(RadialMenuEntry.builder(TabletModes.TRAVEL, Component.literal("Travel"))
                        .icon(new ItemStack(stack.getItem())) // or a dedicated icon item
                        //.onSelect(() -> sendToggle(TabletModes.TRAVEL))
                        .build())
                .addEntry(RadialMenuEntry.builder(TabletModes.CREATE, Component.literal("Create"))
                        //.onSelect(() -> sendToggle(TabletModes.CREATE))
                        .build())
                .addEntry(RadialMenuEntry.builder(TabletModes.MEK, Component.literal("Mekanism"))
                        .icon(new ItemStack(MekanismItems.CONFIGURATOR)) // swap for a Mek item if accessible
                        //.onSelect(() -> sendToggle(TabletModes.MEK))
                        .build())
                .defaultIndex(0)
                .build();

        RadialMenuScreen screen = new RadialMenuScreen(menu, "astrocraft:tablet_modes");
        screen.setActiveModes(TabletModes.getActiveModes(stack));
        Minecraft.getInstance().setScreen(screen);
    }

    @net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
    private void sendToggle(String modeId) {
        AsTechNetworkHandler.INSTANCE.sendToServer(
                new ServerboundSetToolModePacket("astrocraft:tablet_modes", modeId));
    }
}
