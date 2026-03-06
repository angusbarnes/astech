package net.astr0.astrocraft.mixin;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.EventHandlers;
import net.astr0.astrocraft.item.ModItems;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// 1. Define the class we are modifying
@Mixin(EnderMan.class)
public abstract class MixinEnderman {

    // 2. Inject into the isLookingAtMe method
    @Inject(
            method = "isLookingAtMe",
            at = @At("HEAD"),         // Run this code at the very start of the method
            cancellable = true        // Allow us to return a value early
    )
    private void onCheckLookingAtMe(Player player, CallbackInfoReturnable<Boolean> cir) {
        LogUtils.getLogger().warn("HELLO FROM ENDERMAN MIXIN");
        if(EventHandlers.hasCurio(player, ModItems.END_KEY.get())) {
            cir.setReturnValue(false);
        }
    }
}
