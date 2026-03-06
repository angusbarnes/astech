package net.astr0.astrocraft.mixin;

import com.enderio.base.common.handler.TravelHandler;
import net.astr0.astrocraft.ModTags;
import net.astr0.astrocraft.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.jade.util.ModIdentification;

@Mixin(TravelHandler.class)
public class EnderIOTravelStaffMixin {

    @Inject(
            method = "canItemTeleport(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void injectMultitoolTeleportCheck(Player player, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModTags.ENDERIO_TELEPORT_ITEM)) {
            cir.setReturnValue(true);
        }
    }
}
