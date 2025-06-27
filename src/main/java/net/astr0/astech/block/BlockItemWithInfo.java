package net.astr0.astech.block;

import net.astr0.astech.NbtPrettyPrinter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class BlockItemWithInfo extends BlockItem {
    public BlockItemWithInfo(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.AQUA);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        if(Screen.hasShiftDown()) {
            pTooltip.add(Component.literal("Use this controller to build a multiblock machine to unlock new recipes."));
        } else {
            pTooltip.add(1, Component.literal("MULTIBLOCK CONTROLLER").withStyle(NbtPrettyPrinter.YELLOW_STYLE));
            pTooltip.add(Component.literal("--> Hold shift for details").withStyle(NbtPrettyPrinter.GRAY_ITALICS_STYLE));
        }
    }
}
