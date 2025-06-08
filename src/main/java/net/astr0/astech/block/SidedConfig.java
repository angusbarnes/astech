package net.astr0.astech.block;

import net.astr0.astech.gui.MachineCapConfiguratorWidget.SlotFormat;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.network.IHasStateManager;
import net.astr0.astech.network.IStateManaged;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;
import java.util.EnumMap;

public class SidedConfig implements IStateManaged {

    private final IHasStateManager stateManager;
    private Capability[] supportedCapabilities = new Capability[0];

    @Override
    public String getStateName() {
        return name;
    }

    @Override
    public CompoundTag writeToTag() {
        CompoundTag tag = new CompoundTag();
        int[] capOrdinals = Arrays.stream(Direction.values())
                .mapToInt(dir -> get(dir).ordinal())
                .toArray();
        tag.putIntArray("capMap", capOrdinals);
        return tag;
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        if (tag.contains("capMap", Tag.TAG_INT_ARRAY)) {
            int[] data = tag.getIntArray("capMap");
            Direction[] directions = Direction.values();
            for (int i = 0; i < Math.min(data.length, directions.length); i++) {
                caps.put(directions[i], Capability.fromOrdinal(data[i]));
            }
        }
    }


    private boolean isDirty = false;
    @Override
    public boolean isNetworkDirty() {
        return isDirty;
    }

    @Override
    public void writeNetworkEncoding(FriendlyByteBuf buf) {
        isDirty = false;
        for (Direction dir : Direction.values()) {
            buf.writeEnum(get(dir));
        }
    }

    @Override
    public void readNetworkEncoding(FriendlyByteBuf buf) {
        for (Direction dir : Direction.values()) {
            setNoUpdate(dir, buf.readEnum(Capability.class));
        }
    }

    @Override
    public void writeClientUpdate(FriendlyByteBuf buf) {
        writeNetworkEncoding(buf);
    }

    @Override
    public void applyClientUpdate(FriendlyByteBuf buf) {
        isDirty = true;

        readNetworkEncoding(buf);
    }

    public enum Capability {
        NONE,
        ITEM_INPUT,
        ITEM_OUTPUT,
        FLUID_INPUT,
        FLUID_OUTPUT,
        FLUID_OUTPUT_ONE,
        FLUID_OUTPUT_TWO,
        UNKNOWN;

        public static Capability fromOrdinal(int ordinal) {
            return ordinal < 0 || ordinal >= values().length ? UNKNOWN : values()[ordinal];
        }
    }

    private final EnumMap<Direction, Capability> caps = new EnumMap<>(Direction.class);

    private final String name;
    public SidedConfig(IHasStateManager manager, String identifier) {
        for (Direction dir : Direction.values()) {
            caps.put(dir, Capability.NONE);
        }

        name = "SC_" + identifier;

        stateManager = manager;
    }

    public void set(Direction dir, Capability cap) {
        caps.put(dir, cap);
    }

    public void setNoUpdate(Direction dir, Capability cap) {
        caps.put(dir, cap);
    }

    public Capability get(Direction dir) {
        return caps.getOrDefault(dir, Capability.NONE);
    }

    public void SetCapOnClient(Direction dir, Capability cap) {
        caps.put(dir, cap);
        stateManager.getStateManager().sendClientUpdateByName(getStateName());
    }


    public void setSupportedCaps(Capability... cap) {
        supportedCapabilities = cap;
    }

    public Capability[] getSupportedCaps() {
        return supportedCapabilities;
    }


    private static final SlotFormat[] formats = new SlotFormat[] {
            new SlotFormat(new TintColor(255, 255, 255), "§7NONE"),
            new SlotFormat(new TintColor(255, 255, 85), "§eITEM INPUT"),
            new SlotFormat(new TintColor(85, 85, 255), "§9ITEM OUTPUT"),
            new SlotFormat(new TintColor(255, 170, 0), "§6FLUID INPUT"),
            new SlotFormat(new TintColor(255, 85, 255), "§dFLUID OUTPUT"),
            new SlotFormat(new TintColor(255, 85, 85), "§cFLUID OUTPUT 1"),
            new SlotFormat(new TintColor(0, 170, 170), "§3FLUID OUTPUT 2"),
            new SlotFormat(new TintColor(40, 40, 40), "§4UNKNOWN"),
    };

    public static SlotFormat getFormat(Capability cap) {
        int index = cap.ordinal();
        return formats[Math.min(index, formats.length - 1)];
    }
}
