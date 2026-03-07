package net.astr0.astrocraft.compat.mek;

import mekanism.api.RelativeSide;
import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.util.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MekCompat {

    public static InteractionResult handleConfigurator(UseOnContext context, TransmissionType transmissionType) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (level.isClientSide || player == null) return InteractionResult.SUCCESS; // Let client think it worked

        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        BlockEntity tile = level.getBlockEntity(pos);

        // PATH 1: Mekanism Machines (Changing I/O colors)
        if (tile instanceof ISideConfiguration config && config.getConfig().supports(transmissionType)) {
            ConfigInfo info = config.getConfig().getConfig(transmissionType);

            if (info != null) {
                RelativeSide relativeSide = RelativeSide.fromDirections(config.getDirection(), side);
                DataType dataType = info.getDataType(relativeSide);

                if (!player.isShiftKeyDown()) {
                    // Just looking at the face: Tell the player what mode it's in
                    player.displayClientMessage(MekanismLang.CONFIGURATOR_VIEW_MODE.translateColored(EnumColor.GRAY, transmissionType, dataType.getColor(), dataType, dataType.getColor().getColoredName()), true);
                } else {
                    // Sneaking: Cycle the mode (e.g., Input -> Output -> None)
                    DataType old = dataType;
                    dataType = info.incrementDataType(relativeSide);

                    if (dataType != old) {
                        player.displayClientMessage(MekanismLang.CONFIGURATOR_TOGGLE_MODE.translateColored(EnumColor.GRAY, transmissionType, dataType.getColor(), dataType, dataType.getColor().getColoredName()), true);
                        config.getConfig().sideChanged(transmissionType, relativeSide);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        // PATH 2: Mekanism Pipes, Tubes, and Transmitters (Connecting/Disconnecting)
        // Note: Mekanism 1.20 still heavily uses their own Capability wrapper for this.
        var capability = CapabilityUtils.getCapability(tile, Capabilities.CONFIGURABLE, side).resolve();
        if (capability.isPresent()) {
            var configCap = capability.get();
            if (player.isShiftKeyDown()) {
                return configCap.onSneakRightClick(player);
            }
            return configCap.onRightClick(player);
        }

        return InteractionResult.PASS; // Not a Mekanism configurable block
    }

}
