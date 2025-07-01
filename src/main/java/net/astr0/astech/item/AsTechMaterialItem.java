package net.astr0.astech.item;

import net.astr0.astech.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

// Optimise by creating HazardousItem and HazardousBucket subclasses so only nasty materials actually tick
public class AsTechMaterialItem extends Item {

    private final String materialName;
    private final HazardBehavior hazardBehavior;
    public AsTechMaterialItem(Properties pProperties, String name, HazardBehavior.BehaviorType hazard) {
        super(pProperties);
        materialName = name;

        hazardBehavior = new HazardBehavior(hazard);
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
                    hazardBehavior.apply(stack, livingEntity, level);
                    return;
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flagIn) {
        if (hazardBehavior.hasBehaviour()) {
            tooltip.add(Component.literal("§c§n*HAZARDOUS*§r"));
        }
        tooltip.add(Component.translatable(String.format("tooltip.%s.material", materialName)));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
