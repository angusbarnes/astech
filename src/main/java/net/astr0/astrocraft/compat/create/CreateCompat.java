package net.astr0.astrocraft.compat.create;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CreateCompat {
    public static InteractionResult handleWrench(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.mayBuild()) return InteractionResult.PASS;

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.getBlock();

        // Replicating Create's internal logic
        if (block instanceof IWrenchable actor) {
            if (player.isShiftKeyDown()) {
                return actor.onSneakWrenched(state, context);
            }
            return actor.onWrenched(state, context);
        }

        // Optional: Implement the canWrenchPickup logic if you want the "Pick up" feature
        return InteractionResult.PASS;
    }
}
