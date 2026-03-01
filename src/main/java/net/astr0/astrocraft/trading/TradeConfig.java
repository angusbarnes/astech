package net.astr0.astrocraft.trading;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class TradeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> INPUT_CURRENCIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> OUTPUT_CURRENCIES;

    static {
        BUILDER.push("Tiered Currency Settings");

        BUILDER.comment("Format: 'min-max=modid:item_id'. Applies to the cost slots.");
        INPUT_CURRENCIES = BUILDER.defineList("input_currencies",
                Arrays.asList(
                        "1-8=minecraft:gold_ingot",
                        "9-17=minecraft:emerald_ingot",
                        "18-64=minecraft:diamond"
                ),
                obj -> obj instanceof String && ((String) obj).contains("="));

        BUILDER.comment("Format: 'min-max=modid:item_id'. Applies to the result slot.");
        OUTPUT_CURRENCIES = BUILDER.defineList("output_currencies",
                Arrays.asList(
                        "1-8=minecraft:gold_ingot",
                        "9-17=minecraft:emerald_ingot",
                        "18-64=minecraft:diamond"
                ),
                obj -> obj instanceof String && ((String) obj).contains("="));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}