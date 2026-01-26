package net.astr0.astrocraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireStarterItem extends TieredItem
{
    public FireStarterItem()
    {
        super(Tiers.WOOD, new Properties().durability(10));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        if (entity instanceof Player player)
        {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

            if (result.getType() == HitResult.Type.BLOCK)
            {
                // If looking at a block
                BlockPos pos = result.getBlockPos();
                if (!level.isClientSide)
                {
                    stack.hurtAndBreak(1, player, (broken)->{});

                    BlockState stateAt = level.getBlockState(pos);
                    if (CampfireBlock.canLight(stateAt))
                    {
                        // Light campfire
                        level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                        level.setBlock(pos, stateAt.setValue(BlockStateProperties.LIT, true), 11);
                    }
                    else if (level.getRandom().nextFloat() < 0.3) {
                                level.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 30;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks)
    {
        if (level.isClientSide && entity instanceof Player player)
        {
            BlockHitResult result = getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.NONE);
            if (player.level().getRandom().nextInt(5) == 0)
            {
                player.level().addParticle(ParticleTypes.SMOKE, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0.0F, 0.1F, 0.0F);
            }
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category == EnchantmentCategory.BREAKABLE;
    }

    private void removeItems(List<ItemEntity> itemEntities, int removeAmount)
    {
        for (ItemEntity logEntity : itemEntities)
        {
            ItemStack logStack = logEntity.getItem();
            int shrink = Math.min(logStack.getCount(), removeAmount);
            removeAmount -= shrink;
            logStack.shrink(shrink);
            if (logStack.getCount() == 0)
            {
                logEntity.remove(Entity.RemovalReason.KILLED);
            }
        }
    }
}
