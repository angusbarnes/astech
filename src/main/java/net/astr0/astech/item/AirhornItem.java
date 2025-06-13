package net.astr0.astech.item;

import net.astr0.astech.SoundRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AirhornItem extends Item {
    public AirhornItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.playSound(SoundRegistry.airhorn.get(), 2, 1);

        ItemStack airhorn = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            airhorn.hurtAndBreak(1, pPlayer, (player) -> {});
        }

        return InteractionResultHolder.success(airhorn);
    }
}
