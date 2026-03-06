package net.astr0.astrocraft.compat.enderio;

import com.enderio.base.common.config.BaseConfig;
import com.enderio.base.common.handler.TravelHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TravelStaffPatches {

    /**
     * Perform your action
     * @return true if it was a success and you want to consume the resources
     */
    public static boolean performAction(Item item, Level level, Player player, ItemStack stack) {

        if (TravelHandler.blockTeleport(level, player)) {
            player.getCooldowns().addCooldown(item, BaseConfig.COMMON.ITEMS.TRAVELLING_BLINK_DISABLED_TIME.get());
            return true;
        }

        return false;
    }
}
