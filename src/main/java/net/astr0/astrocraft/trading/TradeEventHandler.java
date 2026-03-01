package net.astr0.astrocraft.trading;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;

import java.util.List;

public class TradeEventHandler {



    public static void onVillagerTrades(VillagerTradesEvent event) {
        LogUtils.getLogger().info("+++++++++++ VILLAGER TRADE EVENT ++++++++");
        event.getTrades().forEach((level, listings) -> {
            for (int i = 0; i < listings.size(); i++) {
                listings.set(i, new CurrencyWrapperListing(listings.get(i)));
            }
        });
    }

    public static void onWanderingTrades(WandererTradesEvent event) {
        LogUtils.getLogger().info("+++++++++++ WANDERER TRADE EVENT ++++++++");
        // Wrap Generic Trades
        List<VillagerTrades.ItemListing> generic = event.getGenericTrades();
        for (int i = 0; i < generic.size(); i++) {
            generic.set(i, new CurrencyWrapperListing(generic.get(i)));
        }

        // Wrap Rare Trades
        List<VillagerTrades.ItemListing> rare = event.getRareTrades();
        for (int i = 0; i < rare.size(); i++) {
            rare.set(i, new CurrencyWrapperListing(rare.get(i)));
        }
    }
}
