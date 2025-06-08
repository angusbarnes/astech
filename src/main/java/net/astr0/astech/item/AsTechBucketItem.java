package net.astr0.astech.item;

import net.astr0.astech.Fluid.AsTechChemicalFluidType;
import net.astr0.astech.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public class AsTechBucketItem extends BucketItem {
    private final String _tooltip_key;
    private final Supplier<HazardBehavior> _typeSupplier;

    public AsTechBucketItem(Supplier<? extends Fluid> supplier, Properties builder, String tooltip_key) {
        super(supplier, builder);
        _tooltip_key = tooltip_key;
        _typeSupplier = () -> ((AsTechChemicalFluidType) supplier.get().getFluidType()).getHazardBehavior();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(_tooltip_key));
        if (_typeSupplier.get().hasBehaviour()) {
            pTooltipComponents.add(Component.literal("§c§n*HAZARDOUS*§r"));
        }
        pTooltipComponents.add(Component.literal("§9AsTech Industrial§r"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if(level.isClientSide() || !(entity instanceof LivingEntity livingEntity)) return;

        if(livingEntity.tickCount % 20 == 0) {
            for(ItemStack armorPiece : livingEntity.getArmorSlots()) {
                // If even a single piece isnt chemically protective, apply hazard effect
                if (!armorPiece.is(ModTags.CHEMICAL_PROTECTION)) {
//                    LogUtils.getLogger().info("Checked {} against {} and found that it failed. Had {}",
//                        armorPiece.getDisplayName().getString(), myItemTag.toString(), armorPiece.getTags().toList().toString()
//                    );
                    _typeSupplier.get().apply(stack, (LivingEntity) entity, level);
                    return;
                }
            }
        }
    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable CompoundTag nbt) {
        return this.getClass() == AsTechBucketItem.class ? new FluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean emptyContents(@Nullable Player pPlayer, Level pLevel, BlockPos pPos, @Nullable BlockHitResult pResult, @Nullable ItemStack container) {
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
