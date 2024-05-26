package net.astr0.astech;

import io.netty.util.Attribute;
import net.astr0.astech.Fluid.ChemicalGasType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AsTechBucketItem extends BucketItem {
    private final String _tooltip_key;
    private final Fluid content;
    private final Supplier<? extends Fluid> fluidSupplier;

    public AsTechBucketItem(Supplier<? extends Fluid> supplier, Properties builder, String tooltip_key) {
        super(supplier, builder);
        _tooltip_key = tooltip_key;
        this.content = supplier.get();
        this.fluidSupplier = supplier;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(_tooltip_key));
        pTooltipComponents.add(Component.literal("§9AsTech Industrial§r"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (this.content.getFluidType() instanceof ChemicalGasType) {
            return InteractionResultHolder.pass(itemstack);
        }

        return super.use(pLevel, pPlayer, pHand);
    }
}
