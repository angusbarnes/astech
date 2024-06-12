package net.astr0.astech.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SlotSettingToggle extends UIButton{

    MachineCapConfiguratorWidget.SlotSetting settings;
    private ItemStack item;
    public SlotSettingToggle(MachineCapConfiguratorWidget.SlotSetting settings, OnPress onPress) {
        super(Icons.BLANK, settings.COLOR, settings.x, settings.y, settings.GetFormattedTooltip(), onPress);

        this.settings = settings;

        item =  settings.itemToRender;
    }

    @Override
    public void clicked() {

        super.clicked();
    }

    @Override
    public void Render(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.Render(guiGraphics, mouseX, mouseY);

        guiGraphics.renderItem(item, this.getX(), this.getY());
    }

    public MachineCapConfiguratorWidget.SlotSetting getSettings() {
        return settings;
    }

    public Direction getDirection() {
        return settings.dir;
    }

    public void SetFormatting(MachineCapConfiguratorWidget.SlotFormat slotFormat) {
        this.setColor(slotFormat.color());
        this.settings.TYPE_STRING = slotFormat.slotType();
        this.SetTooltip(settings.GetFormattedTooltip());
    }
}
