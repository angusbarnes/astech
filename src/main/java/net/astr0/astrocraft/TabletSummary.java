package net.astr0.astrocraft;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class TabletSummary {


    private List<Component> componentList = new ArrayList<>();


    public TabletSummary(String title) {
        componentList.add(NbtPrettyPrinter.formatHeader(title));
    }

    private void add(Component comp) {
        componentList.add(comp);
    }

    public void addField(String name, Object value) {
        if (value instanceof Integer i) {
            add(NbtPrettyPrinter.formatVar(name, i));
        } else if (value instanceof Boolean b) {
            add(NbtPrettyPrinter.formatVar(name, b));
        } else if (value instanceof Float f) {
            add(NbtPrettyPrinter.formatVar(name, f));
        } else if (value instanceof Double d) {
            add(NbtPrettyPrinter.formatVar(name, d));
        } else if (value instanceof String s) {
            add(NbtPrettyPrinter.formatVar(name, s));
        } else {
            add(NbtPrettyPrinter.formatVar(name, value.toString()));
        }
    }

    public Component getAsComponent() {
        MutableComponent comp = MutableComponent.create(ComponentContents.EMPTY);
        for(Component _comp : componentList) {
            comp.append(_comp);
            comp.append("\n");
        }

        return comp;
    }



}
