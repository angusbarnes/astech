package net.astr0.astech.Fluid;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class AsTechMaterialItem extends Item {

    private final String materialName;
    public AsTechMaterialItem(Properties pProperties, String name) {
        super(pProperties);
        materialName = name;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flagIn) {

        tooltip.add(Component.translatable(String.format("tooltip.%s.material", materialName)));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
