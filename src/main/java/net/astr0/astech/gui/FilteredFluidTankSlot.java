package net.astr0.astech.gui;

import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FilteredFluidTankSlot extends FluidTankSlot {

    private MachineFluidHandler handler;
    private final int tankIndex;

    public FilteredFluidTankSlot(MachineFluidHandler filteredHandler, int index, int x, int y) {
        super(filteredHandler.getTank(index), x, y);
        tankIndex = index;
        handler = filteredHandler;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (handler.checkSlot(this.tankIndex)){
            guiGraphics.blit(WIDGET_TEXTURE, this.x-1, this.y - 1, 61, 198, 12, 58);
        }

        super.render(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean handleClick(BlockEntity be, double mouseX, double mouseY, int mouseButton, boolean isShifting) {
        // We will not handle any click events that occur outside our bounds
        if(!isHovering(this.x, this.y, 10, 58, mouseX, mouseY)) return false;

        int message_code = isShifting ? 38 : 37;

        FlexiPacket packet = new FlexiPacket(be.getBlockPos(), message_code);
        packet.writeInt(this.tankIndex);
        AsTechNetworkHandler.INSTANCE.sendToServer(packet);

        return true;
    }
}
