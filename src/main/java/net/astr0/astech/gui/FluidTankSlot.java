package net.astr0.astech.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.compat.JEI.GhostIngredientHandler;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.UIFluidActionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class FluidTankSlot extends AbstractGuiSlot {

    public static final int TANK_HEIGHT = 56;
    protected final FluidTank fluidTank;
    protected final BlockEntity BLOCK_ENTITY;
    protected final int SLOT_INDEX;
    protected final String STATE_NAME;
    private boolean fillingAllowed = false;


    public FluidTankSlot(BlockEntity be, MachineFluidHandler tankHandler, int slot_index, int x, int y) {

        super(x, y, 10, TANK_HEIGHT);

        BLOCK_ENTITY = be;
        SLOT_INDEX = slot_index;
        STATE_NAME = tankHandler.getStateName();
        this.fluidTank = tankHandler.getTank(slot_index);
    }

    public FluidTankSlot(BlockEntity be, MachineFluidHandler tankHandler, int slot_index, int x, int y, boolean fillingAllowed) {

        super(x, y, 10, TANK_HEIGHT);

        BLOCK_ENTITY = be;
        SLOT_INDEX = slot_index;
        STATE_NAME = tankHandler.getStateName();
        this.fluidTank = tankHandler.getTank(slot_index);
        this.fillingAllowed = fillingAllowed;
    }

    @Override
    public boolean canAcceptGhostIngredient(GhostIngredientHandler.DraggedIngredient ingredient) {
        return false;
    }

    @Override
    public void handleFilterDrop(GhostIngredientHandler.DraggedIngredient ingredient) {
    }

    public void renderBackground(GuiGraphics guiGraphics) {
        FluidStack fluidStack = fluidTank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = (int) (TANK_HEIGHT * ((float)fluidTank.getFluidAmount()/fluidTank.getCapacity()));

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);

        if (stillTexture == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        Vector4f tintColor = new TintColor(fluidTypeExtensions.getTintColor(fluidStack)).getAsNormalisedRenderColor();

        guiGraphics.setColor(tintColor.x, tintColor.y, tintColor.z, tintColor.w);
        GraphicsUtils.drawTiledSprite(guiGraphics, x, y + TANK_HEIGHT, 0, 10, fluidHeight, sprite, 16, 16, 1);
        guiGraphics.setColor(1f, 1f, 1f, 1f);
    }

    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        FluidStack fluidStack = fluidTank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = (int) (TANK_HEIGHT * ((float)fluidTank.getFluidAmount()/fluidTank.getCapacity()));

        if(!isHovering(x, getFluidY(fluidHeight), 10, fluidHeight, mouseX, mouseY)) return;

        List<Component> tips = new ArrayList<>(2);
        tips.add(MutableComponent.create(fluidStack.getDisplayName().getContents()));
        tips.add(Component.literal("§7%s/%s mB".formatted(fluidTank.getFluidAmount(), fluidTank.getCapacity())));
        guiGraphics.renderComponentTooltip(this.font, tips, mouseX, mouseY);
    }

    @Override
    public boolean isMouseInBounds(double mouseX, double mouseY) {
        return isHovering(this.x, this.y, 10, 58, mouseX, mouseY);
    }

    public boolean handleClick(ItemStack carried, int mouseButton, boolean isScreenLocked) {

        // We should only bother sending a drain packet if the held item can actually handle it
        if (carried.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {


            LogUtils.getLogger().info("Mouse clicked: {}. Filling allowed={}", mouseButton, fillingAllowed);

            if (fillingAllowed && mouseButton == InputConstants.MOUSE_BUTTON_LEFT) {
                AsTechNetworkHandler.INSTANCE.sendToServer(new UIFluidActionPacket(BLOCK_ENTITY.getBlockPos(), STATE_NAME, SLOT_INDEX, UIFluidActionPacket.FluidAction.DRAIN_ITEM));
                return true;
            }

            AsTechNetworkHandler.INSTANCE.sendToServer(new UIFluidActionPacket(BLOCK_ENTITY.getBlockPos(), STATE_NAME, SLOT_INDEX, UIFluidActionPacket.FluidAction.FILL_ITEM));
            return true;
        }
        return false;
    }

    private int getFluidY(int fluidHeight) {
        return y + TANK_HEIGHT - fluidHeight;
    }
}
