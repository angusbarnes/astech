package net.astr0.astrocraft.trading;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

public class CurrencyWrapperListing implements VillagerTrades.ItemListing {
    private final VillagerTrades.ItemListing original;

    public CurrencyWrapperListing(VillagerTrades.ItemListing original) {
        this.original = original;
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource random) {
        MerchantOffer offer = original.getOffer(entity, random);
        if (offer == null) return null;

        // Fetch the active rules from the config
        List<? extends String> inputRules = TradeConfig.INPUT_CURRENCIES.get();
        List<? extends String> outputRules = TradeConfig.OUTPUT_CURRENCIES.get();

        // Process inputs (Costs)
        ItemStack costA = CurrencyHelper.processStack(offer.getBaseCostA(), inputRules);
        ItemStack costB = CurrencyHelper.processStack(offer.getCostB(), inputRules);

        // Process output (Result)
        ItemStack result = CurrencyHelper.processStack(offer.getResult(), outputRules);

        return new MerchantOffer(
                costA, costB, result,
                offer.getUses(), offer.getMaxUses(),
                offer.getXp(),
                offer.getPriceMultiplier(),
                offer.getDemand()
        );
    }
}