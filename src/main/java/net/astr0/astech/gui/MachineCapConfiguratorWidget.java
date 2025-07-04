package net.astr0.astech.gui;

import com.mojang.logging.LogUtils;
import net.astr0.astech.AsTech;
import net.astr0.astech.BlockUtils;
import net.astr0.astech.block.AbstractMachineBlockEntity;
import net.astr0.astech.block.SidedConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class MachineCapConfiguratorWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/widget_bg.png");
    public final UIButton MODE_SWITCH_BUTTON;
    public final UIButton PUSH_PULL_BUTTON;
    public final AbstractMachineBlockEntity machine;

    public final List<SlotSettingToggle> buttons;

    public final int ITEM_MODE = 0;
    public final int FLUID_MODE = 1;

    public int mode = ITEM_MODE;
    private boolean SHOULD_RENDER = false;


    public static class SlotSetting {
        public String TYPE_STRING = "§7NONE";
        protected String SLOT_NAME;
        public final TintColor COLOR = new TintColor(255, 255, 255);
        public final int x;
        public final int y;
        final ItemStack itemToRender;

        public final Direction dir;

        public String GetFormattedTooltip() {
            return SLOT_NAME + TYPE_STRING;
        }

        public SlotSetting(Direction dir, ItemStack item, int x, int y) {
            this.x = x;
            this.y = y;
            itemToRender = item;
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

    private SidedConfig _itemConfig = null;
    private SidedConfig _fluidConfig = null;

    public int GetMode() {return this.mode;}
    public void SetMode(int mode) {this.mode = mode;}

    public MachineCapConfiguratorWidget(int pX, int pY, AbstractMachineBlockEntity be, SidedConfig itemConfig, SidedConfig fluidConfig) {
        super(pX, pY, 0, 0, Component.empty());

        BlockPos pos = be.getBlockPos();
        Level lvl = be.getLevel();

        ItemStack backBlockItem = null;
        ItemStack frontBlockItem = null;
        ItemStack leftBlockItem = null;
        ItemStack rightBlockItem = null;
        ItemStack topBlockItem = null;
        ItemStack bottomBlockItem = null;
        if(lvl != null) {
            BlockState[] lateralStates = BlockUtils.getSurroundingBlocks(lvl, pos, be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));

            frontBlockItem = new ItemStack(lateralStates[0].getBlock().asItem());
            backBlockItem = new ItemStack(lateralStates[1].getBlock().asItem());
            leftBlockItem = new ItemStack(lateralStates[3].getBlock().asItem());
            rightBlockItem = new ItemStack(lateralStates[2].getBlock().asItem());
            topBlockItem = new ItemStack(lvl.getBlockState(pos.above()).getBlock().asItem());
            bottomBlockItem = new ItemStack(lvl.getBlockState(pos.below()).getBlock().asItem());
        }

        // HERE WE SET UP OUR MAIN GRID OF BUTTONS
        SlotSetting[] slots = new SlotSetting[]{
                new SlotSetting(Direction.NORTH, frontBlockItem, pX, pY),
                new SlotSetting(Direction.SOUTH, backBlockItem, pX + 19, pY + 19),
                new SlotSetting(Direction.EAST, leftBlockItem, pX - 19, pY),
                new SlotSetting(Direction.WEST, rightBlockItem, pX + 19, pY),
                new SlotSetting(Direction.UP, topBlockItem, pX, pY - 19),
                new SlotSetting(Direction.DOWN, bottomBlockItem, pX, pY + 19),
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

        PUSH_PULL_BUTTON = new UIButton(Icons.DENY, new TintColor(255, 255, 255), pX + 19, pY - 19, "Auto Push: §cNo", (button) -> {
            if (incrementAutoPush(this::GetMode)) {
                button.SetIconToDraw(Icons.ACCEPT);
                button.SetTooltip("Auto Push: §3Yes");
            } else {
                button.SetIconToDraw(Icons.DENY);
                button.SetTooltip("Auto Push: §cNo");
            }
        });

        MODE_SWITCH_BUTTON = new UIButton(Icons.ITEM, new TintColor(255, 255, 255, 255), pX - 19, pY - 19, "Mode: §eItems", (button) -> {
            if (mode == ITEM_MODE) {
                if (_fluidConfig == null) return;
                mode = FLUID_MODE;
                button.SetTooltip("Mode: §9Fluid");
                button.SetIconToDraw(Icons.FLUID);
            } else if (mode == FLUID_MODE) {
                if (_itemConfig == null) return;
                mode = ITEM_MODE;
                button.SetTooltip("Mode: §eItems");
                button.SetIconToDraw(Icons.ITEM);
            }

            for (SlotSettingToggle slotToggle : buttons) {
                slotToggle.SetFormatting(getSlotFormat(this::GetMode, slotToggle.getDirection()));
            }

            boolean mode = false;
            if(GetMode() == ITEM_MODE) {
                mode = _itemConfig.getPushSetting();
            } else {
                mode = _fluidConfig.getPushSetting();
            }

            if (mode) {
                PUSH_PULL_BUTTON.SetIconToDraw(Icons.ACCEPT);
                PUSH_PULL_BUTTON.SetTooltip("Auto Push: §3Yes");
            } else {
                PUSH_PULL_BUTTON.SetIconToDraw(Icons.DENY);
                PUSH_PULL_BUTTON.SetTooltip("Auto Push: §cNo");
            }
        });



        if (itemConfig != null) {
            slotSettings.put(ITEM_MODE, itemConfig.getSupportedCaps());
        } else {
            slotSettings.put(ITEM_MODE, new SidedConfig.Capability[0]);
        }

        if (fluidConfig != null) {
            slotSettings.put(FLUID_MODE, fluidConfig.getSupportedCaps());
        } else {
            slotSettings.put(FLUID_MODE, new SidedConfig.Capability[0]);
        }

        _fluidConfig = fluidConfig;
        _itemConfig = itemConfig;

        machine = be;
    }

    private final HashMap<Integer, SidedConfig.Capability[]> slotSettings = new HashMap<>(2);

    private void incrementSlotType(Supplier<Integer> _mode, Direction dir) {

        if (_mode.get() == ITEM_MODE) {
            SidedConfig.Capability currentCap = _itemConfig.get(dir);

            int index = 0;
            for(SidedConfig.Capability cap :slotSettings.get(_mode.get())) {
                if(cap == currentCap) {
                    break;
                }
                index++;
            }

            _itemConfig.SetCapOnClient(dir, slotSettings.get(_mode.get())[(index + 1) % slotSettings.get(_mode.get()).length]);
        } else {
            SidedConfig.Capability currentCap = _fluidConfig.get(dir);

            int index = 0;
            for(SidedConfig.Capability cap :slotSettings.get(_mode.get())) {
                if(cap == currentCap) {
                    break;
                }
                index++;
            }

            _fluidConfig.SetCapOnClient(dir, slotSettings.get(_mode.get())[(index + 1) % slotSettings.get(_mode.get()).length]);
        }

    }

    private boolean incrementAutoPush(Supplier<Integer> _mode) {

        if (_mode.get() == ITEM_MODE) {
            boolean shouldPush = _itemConfig.getPushSetting();

            _itemConfig.SetAutoPushOnClient(!shouldPush);
            LogUtils.getLogger().info("Mode is ITEM, oldShouldPush={}, newShouldPush={}", !shouldPush, shouldPush);
            return !shouldPush;
        } else {
            boolean shouldPush = _fluidConfig.getPushSetting();

            _fluidConfig.SetAutoPushOnClient(!shouldPush);
            LogUtils.getLogger().info("Mode is FLUID, oldShouldPush={}, newShouldPush={}", !shouldPush, shouldPush);
            return !shouldPush;
        }

    }

    private SlotFormat getSlotFormat(Supplier<Integer> mode, Direction dir) {

        if (mode.get() == ITEM_MODE) {
            return SidedConfig.getFormat(_itemConfig.get(dir));
        } else {
            return SidedConfig.getFormat(_fluidConfig.get(dir));
        }
    }

    public void RefreshSlotUI() {
        for (SlotSettingToggle slotToggle : buttons) {
            slotToggle.SetFormatting(getSlotFormat(this::GetMode, slotToggle.getDirection()));
        }
    }

    public record SlotFormat(TintColor color, String slotType) {}

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        if (!SHOULD_RENDER) return;

        pGuiGraphics.blit(TEXTURE, this.getX()-24, this.getY()-24, 0, 0, 64, 64);

        MODE_SWITCH_BUTTON.Render(pGuiGraphics, pMouseX, pMouseY);
        PUSH_PULL_BUTTON.Render(pGuiGraphics, pMouseX, pMouseY);

        for (UIButton button : buttons) {
            button.Render(pGuiGraphics, pMouseX, pMouseY);
        }

        RefreshSlotUI();
    }

    public void ToggleRender() {
        SHOULD_RENDER = !SHOULD_RENDER;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        // If the menu is hidden, we don't handle any clicks
        if(!SHOULD_RENDER) return false;


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

            if(PUSH_PULL_BUTTON.isMouseWithinBounds((int)pMouseX, (int)pMouseY)) {
                PUSH_PULL_BUTTON.clicked();
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
