package net.astr0.astrocraft.item;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.NbtPrettyPrinter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class KeyItem extends Item implements ICurioItem {

    protected TagKey<Block> BLOCK_UNLOCK_TAG;
    protected String unlockedDimension;
    protected ChatFormatting dimensionColor;
    protected final String[] allowedDimensions;

    public KeyItem(TagKey<Block> keyTag, String dimension, ChatFormatting color, String[] allowedDims) {
        super(new Properties().stacksTo(8));
        BLOCK_UNLOCK_TAG = keyTag;
        unlockedDimension = dimension;
        dimensionColor = color;
        allowedDimensions = allowedDims;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    protected boolean isValidDimension(ResourceKey<DimensionType> dimension) {
        LogUtils.getLogger().debug("Dimension Type: {}", dimension.location().getPath());

        return Arrays.stream(allowedDimensions).anyMatch(dim -> dim.equals(dimension.location().getPath()));
    }

    public boolean Unlocks(BlockState block, ResourceKey<DimensionType> dimensionType) {
        return block.is(BLOCK_UNLOCK_TAG) && isValidDimension(dimensionType);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        AtomicBoolean success = new AtomicBoolean(false);

        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
        curiosInventory.getStacksHandler("key").ifPresent(slotInventory -> {
            ItemStack swapStack = slotInventory.getStacks().getStackInSlot(0);
            slotInventory.getStacks().setStackInSlot(0, stack);
            player.setItemInHand(hand, swapStack);
            success.set(true);
        });



        if (success.get()) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, player, hand);
    }

    @Override
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(dimensionColor);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        if(Screen.hasShiftDown()) {
            pTooltip.add(Component.literal("Use this key to unlock the mysteries of: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(unlockedDimension).withStyle(dimensionColor)));
            pTooltip.add(Component.literal("Place in curios slot or right click in main hand to equip").withStyle(NbtPrettyPrinter.GRAY_ITALICS_STYLE));
            pTooltip.add(Component.literal("Allowed dimensions: " + Arrays.toString(allowedDimensions)));
        } else {
            pTooltip.add(Component.literal("Use this key to unlock the mysteries of: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(unlockedDimension).withStyle(dimensionColor)));
            pTooltip.add(Component.literal("--> Hold shift for details").withStyle(NbtPrettyPrinter.GRAY_ITALICS_STYLE));
        }
    }
}
