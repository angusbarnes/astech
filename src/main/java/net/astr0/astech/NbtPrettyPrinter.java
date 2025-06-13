package net.astr0.astech;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public class NbtPrettyPrinter {
    private static final Style BRACE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
    private static final Style KEY_STYLE = Style.EMPTY.withColor(ChatFormatting.GOLD);
    private static final Style STRING_STYLE = Style.EMPTY.withColor(ChatFormatting.AQUA);
    private static final Style NUMBER_STYLE = Style.EMPTY.withColor(ChatFormatting.GREEN);
    private static final Style TYPE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
    private static final Style COLON_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY);

    public static final Style HEADER_STYLE = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE).withUnderlined(true);
    public static final Style HEADER_BAR_STYLE = Style.EMPTY.withColor(ChatFormatting.BLUE);

    // ─── Color Styles ─────────────────────────────────────────
    private static final Style NAME_STYLE = Style.EMPTY.withColor(ChatFormatting.GOLD);
    private static final Style TRUE_STYLE = Style.EMPTY.withColor(ChatFormatting.GREEN);
    private static final Style FALSE_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);

    // ─── Variable Formatters ─────────────────────────────────

    public static Component formatHeader(String name) {
        MutableComponent comp = MutableComponent.create(Component.empty().getContents());
        comp.append(Component.literal("====== ").withStyle(HEADER_BAR_STYLE))
                .append(Component.literal(name).withStyle(HEADER_STYLE))
                .append(Component.literal(" ======").withStyle(HEADER_BAR_STYLE));
        return comp;
    }

    public static Component formatVar(String name, String value) {
        return Component.literal(name)
                .withStyle(NAME_STYLE)
                .append(Component.literal(": ").withStyle(COLON_STYLE))
                .append(Component.literal("\"" + value + "\"").withStyle(STRING_STYLE));
    }

    public static Component formatVar(String name, int value) {
        return Component.literal(name)
                .withStyle(NAME_STYLE)
                .append(Component.literal(": ").withStyle(COLON_STYLE))
                .append(Component.literal(String.valueOf(value)).withStyle(NUMBER_STYLE));
    }

    public static Component formatVar(String name, float value) {
        return Component.literal(name)
                .withStyle(NAME_STYLE)
                .append(Component.literal(": ").withStyle(COLON_STYLE))
                .append(Component.literal(String.valueOf(value)).withStyle(NUMBER_STYLE));
    }

    public static Component formatVar(String name, double value) {
        return Component.literal(name)
                .withStyle(NAME_STYLE)
                .append(Component.literal(": ").withStyle(COLON_STYLE))
                .append(Component.literal(String.valueOf(value)).withStyle(NUMBER_STYLE));
    }

    public static Component formatVar(String name, boolean value) {
        return Component.literal(name)
                .withStyle(NAME_STYLE)
                .append(Component.literal(": ").withStyle(COLON_STYLE))
                .append(Component.literal(String.valueOf(value))
                        .withStyle(value ? TRUE_STYLE : FALSE_STYLE));
    }

    public static Component prettyPrint(Tag tag) {
        return prettyPrint(tag, 0);
    }

    private static Component prettyPrint(Tag tag, int indentLevel) {
        if (tag instanceof CompoundTag compound) {
            return printCompound(compound, indentLevel);
        } else if (tag instanceof ListTag list) {
            return printList(list, indentLevel);
        } else {
            return formatPrimitive(tag);
        }
    }

    private static Component printCompound(CompoundTag tag, int indent) {
        MutableComponent comp = MutableComponent.create(Component.literal("{").withStyle(BRACE_STYLE).getContents());
        comp.append("\n");

        List<String> keys = new ArrayList<>(tag.getAllKeys());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Tag value = tag.get(key);

            comp = comp.append(indent(indent + 1))
                    .append(Component.literal(key).withStyle(KEY_STYLE))
                    .append(Component.literal(": ").withStyle(COLON_STYLE))
                    .append(prettyPrint(value, indent + 1));

            if (i < keys.size() - 1) {
                comp = comp.append(Component.literal(","));
            }
            comp = comp.append("\n");
        }

        comp = comp.append(indent(indent)).append(Component.literal("}").withStyle(BRACE_STYLE));
        return comp;
    }

    private static Component printList(ListTag tag, int indent) {
        MutableComponent comp = MutableComponent.create(Component.literal("[").withStyle(BRACE_STYLE).getContents());
        comp.append("\n");

        for (int i = 0; i < tag.size(); i++) {
            Tag item = tag.get(i);
            comp = comp.append(indent(indent + 1)).append(prettyPrint(item, indent + 1));
            if (i < tag.size() - 1) {
                comp = comp.append(Component.literal(","));
            }
            comp = comp.append("\n");
        }

        comp = comp.append(indent(indent)).append(Component.literal("]").withStyle(BRACE_STYLE));
        return comp;
    }

    private static Component formatPrimitive(Tag tag) {
        String raw = tag.getAsString();
        if (tag.getId() == Tag.TAG_STRING) {
            return Component.literal(raw).withStyle(STRING_STYLE);
        } else {
            return Component.literal(raw).withStyle(NUMBER_STYLE);
        }
    }

    private static Component indent(int level) {
        return Component.literal("  ".repeat(level)); // 2-space indentation
    }
}

