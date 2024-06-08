package net.astr0.astech.gui;

import com.mojang.logging.LogUtils;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.SidedConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class MachineCapConfiguratorWidget extends AbstractWidget {

    public final UIButton MODE_SWITCH_BUTTON;
    public final AbstractMachineBlockEntity machine;

    public final List<SlotSettingToggle> buttons;

    public final int ITEM_MODE = 0;
    public final int FLUID_MODE = 1;

    public int mode = ITEM_MODE;
    private boolean SHOULD_RENDER = false;


    public class SlotSetting {
        public int index;
        public String TYPE_STRING = "§7NONE";
        protected String SLOT_NAME;
        public TintColor COLOR = new TintColor(255, 255, 255);
        public final int x;
        public final int y;

        public Direction dir;

        public String GetFormattedTooltip() {
            return SLOT_NAME + TYPE_STRING;
        }

        public SlotSetting(Direction dir, int x, int y) {
            this.x = x;
            this.y = y;

            switch (dir) {
                case DOWN -> this.SLOT_NAME = "Bottom: ";
                case UP -> this.SLOT_NAME = "Top: ";
                case NORTH -> this.SLOT_NAME = "Front: ";
                case SOUTH -> this.SLOT_NAME = "Back: ";
                case WEST -> this.SLOT_NAME = "Right: ";
                case EAST -> this.SLOT_NAME = "Left: ";
            }

            this.dir = dir;
        }
    }

    private SlotSetting[] slots;

    public int GetMode() {return this.mode;}

    public MachineCapConfiguratorWidget(int pX, int pY, AbstractMachineBlockEntity be) {
        super(pX, pY, 0, 0, Component.empty());

        // HERE WE SET UP OUR MAIN GRID OF BUTTONS
        slots = new SlotSetting[] {
                new SlotSetting(Direction.NORTH, pX, pY),
                new SlotSetting(Direction.SOUTH, pX + 19, pY + 19),
                new SlotSetting(Direction.EAST, pX - 19, pY),
                new SlotSetting(Direction.WEST, pX + 19, pY),
                new SlotSetting(Direction.UP, pX, pY - 19),
                new SlotSetting(Direction.DOWN, pX, pY + 19),
        };

        buttons = new ArrayList<>(6);
        for(SlotSetting setting : slots) {
            buttons.add(new SlotSettingToggle(setting, (button) -> {
                if(button instanceof SlotSettingToggle slotTogggle) {
                    incrementSlotType(this::GetMode, slotTogggle.getDirection());
                    slotTogggle.SetFormatting(getSlotFormat(this::GetMode, slotTogggle.getDirection()));
                }
            }));
        }

        MODE_SWITCH_BUTTON = new UIButton(Icons.ITEM, new TintColor(255, 255, 255, 255), pX - 19, pY - 19, "Mode: §eItems", (button) -> {
            if (mode == ITEM_MODE) {
                mode = FLUID_MODE;
                button.SetTooltip("Mode: §9Fluid");
                button.SetIconToDraw(Icons.FLUID);
            } else if (mode == FLUID_MODE) {
                mode = ITEM_MODE;
                button.SetTooltip("Mode: §eItems");
                button.SetIconToDraw(Icons.ITEM);
            }

            for (SlotSettingToggle slotToggle : buttons) {
                slotToggle.SetFormatting(getSlotFormat(this::GetMode, slotToggle.getDirection()));
            }
        });

        slotSettings.put(ITEM_MODE, be.getCapTypes()[ITEM_MODE]);
        slotSettings.put(FLUID_MODE, be.getCapTypes()[FLUID_MODE]);

        machine = be;
    }

    private HashMap<Integer, int[]> slotSettings = new HashMap<>(2);

    private void incrementSlotType(Supplier<Integer> _mode, Direction dir) {

        LogUtils.getLogger().info("Tried increment with mode: {}", _mode.get());

        int currentCap = machine.getSidedConfig(_mode.get()).getCap(dir);

        int index = 0;
        for(Integer cap :slotSettings.get(_mode.get())) {
            if(cap == currentCap) {
                break;
            }
            index++;
        }

        machine.getSidedConfig(_mode.get()).setCap(dir, slotSettings.get(_mode.get())[(index + 1) % slotSettings.get(_mode.get()).length]);
    }

    private SlotFormat getSlotFormat(Supplier<Integer> mode, Direction dir) {
        //LogUtils.getLogger().info("Retriveing format with mode: {}", mode.get());
        return SidedConfig.GetCapFormat(machine.getSidedConfig(mode.get()).getCap(dir));
    }

    public void RefreshSlotUI() {
        for (SlotSettingToggle slotToggle : buttons) {
            slotToggle.SetFormatting(getSlotFormat(this::GetMode, slotToggle.getDirection()));
        }
    }

    public record SlotFormat(TintColor color, String slotType) {}

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        if (!SHOULD_RENDER) return;

        MODE_SWITCH_BUTTON.Render(pGuiGraphics, pMouseX, pMouseY);

        for (UIButton button : buttons) {
            button.Render(pGuiGraphics, pMouseX, pMouseY);
        }

        RefreshSlotUI();
    }

    public void ToggleRender() {
        SHOULD_RENDER = !SHOULD_RENDER;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            for (UIButton button : buttons) {
                if(button.isMouseWithinBounds((int)pMouseX, (int)pMouseY)) {
                    button.clicked();
                    return true;
                }
            }

            if(MODE_SWITCH_BUTTON.isMouseWithinBounds((int)pMouseX, (int)pMouseY)) {
                MODE_SWITCH_BUTTON.clicked();
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    protected void onConfigUpdated() {
        // This function should be called any time a button icon is clicked
        // We should update our internal state, then call a callback from our
        // owner object which can dispatch a network update
    }
}
