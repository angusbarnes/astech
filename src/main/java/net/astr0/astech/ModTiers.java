package net.astr0.astech;

import net.astr0.astech.item.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ModTiers {

    public static class InfinityTier implements Tier {

        @Override
        public int getUses() {
            return Integer.MAX_VALUE;
        }

        @Override
        public float getSpeed() {
            return Float.POSITIVE_INFINITY;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public int getLevel() {
            return 9;
        }

        @Override
        public int getEnchantmentValue() {
            return 99999;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.INFINITY_INGOT.get());
        }
    }


    public static final InfinityTier INFINITY_TIER = new InfinityTier();
}
