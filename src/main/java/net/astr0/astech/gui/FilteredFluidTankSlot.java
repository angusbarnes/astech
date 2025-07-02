package net.astr0.astech.gui;

import com.mojang.logging.LogUtils;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.compat.JEI.GhostIngredientHandler;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.UIFluidActionPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class FilteredFluidTankSlot extends FluidTankSlot {

    private final MachineFluidHandler handler;
    

    public FilteredFluidTankSlot(BlockEntity be, MachineFluidHandler tankHandler, int slot_index, int x, int y) {
        super(be, tankHandler, slot_index, x, y, true);
        handler = tankHandler;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (handler.isSlotLocked(this.SLOT_INDEX)){
            guiGraphics.blit(WIDGET_TEXTURE, this.x-1, this.y - 1, 61, 198, 12, 58);
        }

        super.render(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void handleFilterDrop(GhostIngredientHandler.DraggedIngredient ingredient) {
        if(ingredient instanceof GhostIngredientHandler.DraggedIngredient.Fluid fluid) {
            handler.setFluidFilterOnClient(SLOT_INDEX, fluid.stack());// Here we would set the filter and update the server
            LogUtils.getLogger().info("We reached the item slot with fluid {}", fluid.stack().getFluid().toString());
        }
    }

    @Override
    public boolean canAcceptGhostIngredient(GhostIngredientHandler.DraggedIngredient ingredient) {
        return ingredient instanceof GhostIngredientHandler.DraggedIngredient.Fluid && !handler.isSlotLocked(this.SLOT_INDEX);
    }

    @Override
    public boolean handleClick(ItemStack carried, int mouseButton, boolean isScreenLocked){

        // Item filling/draining takes precedence
        if(super.handleClick(carried, mouseButton, isScreenLocked)) {
            return true;
        }

        if (Screen.hasShiftDown()) {
            AsTechNetworkHandler.INSTANCE.sendToServer(new UIFluidActionPacket(BLOCK_ENTITY.getBlockPos(), STATE_NAME, SLOT_INDEX, UIFluidActionPacket.FluidAction.DUMP_SLOT));
            return true;
        }

        // We can only set filters if the UI is unlocked
        if (!isScreenLocked) {
            if (handler.isSlotLocked(this.SLOT_INDEX)) {
                handler.clearFluidFilterOnClient(SLOT_INDEX);
            } else {
                handler.setFluidFilterOnClient(SLOT_INDEX, handler.getFluidInTank(SLOT_INDEX));
            }

            return true;
        }

        return false;
    }
}
