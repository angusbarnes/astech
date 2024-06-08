package net.astr0.astech.gui;

import net.minecraft.core.Direction;

public class SlotSettingToggle extends UIButton{

    MachineCapConfiguratorWidget.SlotSetting settings;

    public SlotSettingToggle(MachineCapConfiguratorWidget.SlotSetting settings, OnPress onPress) {
        super(Icons.BLANK, settings.COLOR, settings.x, settings.y, settings.GetFormattedTooltip(), onPress);

        this.settings = settings;
    }

    @Override
    public void clicked() {

        super.clicked();
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
