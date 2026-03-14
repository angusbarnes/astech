package net.astr0.astrocraft.common;

import net.astr0.astrocraft.Astrocraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.helpers.MessageFormatter;

public class StringUtils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    public static Component stringAsComponent(String string) {
        return Component.literal(string);
    }

    public static Component stringAsComponent(String string, Object... objects) {
        return Component.literal(MessageFormatter.arrayFormat(string, objects).getMessage());
    }

    public static String BASIC_BLACK_COLOR = "§0";
    public static String BASIC_DARK_BLUE_COLOR = "§1";
    public static String BASIC_DARK_GREEN_COLOR = "§2";
    public static String BASIC_DARK_AQUA_COLOR = "§3";
    public static String BASIC_DARK_RED_COLOR = "§4";
    public static String BASIC_DARK_PURPLE_COLOR = "§5";
    public static String BASIC_GOLD_COLOR = "§6";
    public static String BASIC_GRAY_COLOR = "§7";
    public static String BASIC_DARK_GRAY_COLOR = "§8";
    public static String BASIC_BLUE_COLOR = "§9";
    public static String BASIC_GREEN_COLOR = "§a";
    public static String BASIC_AQUA_COLOR = "§b";
    public static String BASIC_RED_COLOR = "§c";
    public static String BASIC_LIGHT_PURPLE_COLOR = "§d";
    public static String BASIC_YELLOW_COLOR = "§e";
    public static String BASIC_WHITE_COLOR = "§f";

    public static ResourceLocation getRL(String base, String path) {
        return ResourceLocation.fromNamespaceAndPath(base, path);
    }

    public static ResourceLocation getRL(String path) {
        return getRL(Astrocraft.MODID, path);
    }
}
