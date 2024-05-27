package net.astr0.astech;

import io.netty.util.Attribute;
import net.astr0.astech.Fluid.ChemicalGasType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(entity instanceof LivingEntity livingEntity && livingEntity.tickCount % 20 == 0) {}
            //GTUtil.applyHazardEffects(material, livingEntity, () -> true);

    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable CompoundTag nbt) {
        return this.getClass() == AsTechBucketItem.class ? new FluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean emptyContents(@org.jetbrains.annotations.Nullable Player pPlayer, Level pLevel, BlockPos pPos, @org.jetbrains.annotations.Nullable BlockHitResult pResult, @org.jetbrains.annotations.Nullable ItemStack container) {
        if (!(this.getFluid() instanceof FlowingFluid))  return false;

        BlockState blockstate = pLevel.getBlockState(pPos);
        Block block = blockstate.getBlock();
        boolean flag = blockstate.canBeReplaced(this.getFluid());
        boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, this.getFluid());
        java.util.Optional<net.minecraftforge.fluids.FluidStack> containedFluidStack = java.util.Optional.ofNullable(container).flatMap(net.minecraftforge.fluids.FluidUtil::getFluidContained);
        if (!flag1) {
            return pResult != null && this.emptyContents(pPlayer, pLevel, pResult.getBlockPos().relative(pResult.getDirection()), null, container);
        } else if (containedFluidStack.isPresent() && this.getFluid().getFluidType().isVaporizedOnPlacement(pLevel, pPos, containedFluidStack.get())) {
            this.getFluid().getFluidType().onVaporize(pPlayer, pLevel, pPos, containedFluidStack.get());
            return true;
        } else if (pLevel.dimensionType().ultraWarm() && this.getFluid().is(FluidTags.WATER)) {
            int i = pPos.getX();
            int j = pPos.getY();
            int k = pPos.getZ();
            pLevel.playSound(pPlayer, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
            }

            return true;
        } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(pLevel, pPos, blockstate, getFluid())) {
            ((LiquidBlockContainer) block).placeLiquid(pLevel, pPos, blockstate, ((FlowingFluid) this.getFluid()).getSource(false));
            this.playEmptySound(pPlayer, pLevel, pPos);
            return true;
        }
        return false;
    }
}
