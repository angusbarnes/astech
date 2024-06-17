package net.astr0.astech.block.ChemicalMixer;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.astr0.astech.AsTech;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.Fluid.MachineFluidHandler;
import net.astr0.astech.gui.*;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;
import java.util.*;

// This only gets registered on the client side
public class ChemicalMixerStationScreen extends AbstractContainerScreen<ChemicalMixerStationMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/chemical_mixer.png");
    private static final ResourceLocation WIDGET_TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/widgets.png");

    public ChemicalMixerStationScreen(ChemicalMixerStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    FluidTankSlot inputTankSlot1;
    FluidTankSlot inputTankSlot2;
    FluidTankSlot outputTankSlot;

    @Override
    protected void init() {
        super.init();
        setup();

        inputTankSlot1 = new FluidTankSlot(this.menu.blockEntity.getFluidInputTank(0), 0, this.leftPos + 34, this.topPos + 18);
        inputTankSlot2 = new FluidTankSlot(this.menu.blockEntity.getFluidInputTank(1), 1, this.leftPos + 48, this.topPos + 18);
        outputTankSlot = new FluidTankSlot(this.menu.blockEntity.getFluidOutputTank(), 2, this.leftPos + 133, this.topPos + 18);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        inputTankSlot1.renderBackground(guiGraphics);
        inputTankSlot2.renderBackground(guiGraphics);
        outputTankSlot.renderBackground(guiGraphics);

        renderEnergyBar(guiGraphics, 154);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean isShiftHeld = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (!isLocked) {
            if(isHovering(62, 18, 16, 16, pMouseX, pMouseY)) {
                LogUtils.getLogger().info("We clicked inside the slot area");

                FlexiPacket packet = new FlexiPacket(this.menu.blockEntity.getBlockPos(), 36);
                packet.writeInt(0);
                AsTechNetworkHandler.INSTANCE.sendToServer(packet);

                return true;
            } else if(isHovering(62, 38, 16, 16, pMouseX, pMouseY)) {
                LogUtils.getLogger().info("We clicked inside the slot area");

                FlexiPacket packet = new FlexiPacket(this.menu.blockEntity.getBlockPos(), 36);
                packet.writeInt(1);
                AsTechNetworkHandler.INSTANCE.sendToServer(packet);

                return true;
            } else if(isHovering(62, 58, 16, 16, pMouseX, pMouseY)) {
                LogUtils.getLogger().info("We clicked inside the slot area");

                FlexiPacket packet = new FlexiPacket(this.menu.blockEntity.getBlockPos(), 36);
                packet.writeInt(2);
                AsTechNetworkHandler.INSTANCE.sendToServer(packet);

                return true;
            } else if(inputTankSlot1.handleClick(this.menu.blockEntity, pMouseX, pMouseY, pButton, isShiftHeld)) {
                return true;
            } else if(inputTankSlot2.handleClick(this.menu.blockEntity, pMouseX, pMouseY, pButton, isShiftHeld)) {
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private int getEnergyY(int energyHeight) {
        return this.topPos + 9 + (69-energyHeight);
    }


    private static int getEnergyHeight(int max, int value) {
        return  (int) (69 * ((float)value/max));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, Component.literal("Chemical Mixer"), 5, 5, GraphicsUtils.DEFAULT_INVENTORY_TEXT_COLOR, false);
        //pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }

    private void renderEnergyBar(GuiGraphics guiGraphics, int x) {

        int height = getEnergyHeight(menu.getMaxEnergy(), menu.getEnergy());
        guiGraphics.blit(TEXTURE,
                this.leftPos + x,
                getEnergyY(height),
                178,
                9,
                10,
                height
        );
    }

    private boolean isLocked = true;
    protected void setup() {

        MachineCapConfiguratorWidget config = new MachineCapConfiguratorWidget(this.leftPos - 40, this.topPos + 30, this.menu.blockEntity);

        IconButton SETTINGS_BUTTON = new IconButton(this.leftPos + 11, this.topPos + 30, Icons.SETTINGS, (button) -> {
            LogUtils.getLogger().info("Button Pressed");
            config.ToggleRender();
        });

        //AsTechNetworkHandler.INSTANCE.sendToServer(new NetworkedMachineUpdate(menu.blockEntity.getBlockPos()));
        IconButton LOCK_BUTTON = new IconButton(this.leftPos + 11, this.topPos + 49, Icons.UNLOCKED, (button) -> {
            LogUtils.getLogger().info("Button 2 Pressed");
            button.setIcon(button.getIcon() == Icons.UNLOCKED ? Icons.LOCKED : Icons.UNLOCKED);
            isLocked = !isLocked;
        });

        this.addRenderableWidget(LOCK_BUTTON);
        this.addRenderableWidget(SETTINGS_BUTTON);
        this.addRenderableWidget(config);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);


        this.menu.blockEntity.getInputItemHandler().ifPresent(iItemHandler -> {
            if(iItemHandler instanceof FilteredItemStackHandler filteredHandler) {
                if (filteredHandler.checkSlot(0)){
                    guiGraphics.blit(WIDGET_TEXTURE, this.leftPos+61, this.topPos+17, 41, 238, 18, 18);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 0.30f);
                    guiGraphics.renderItem(filteredHandler.getFilterForSlot(0), this.leftPos+62, this.topPos+18);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                }
            }
        });

        this.menu.blockEntity.getInputItemHandler().ifPresent(iItemHandler -> {
            if(iItemHandler instanceof FilteredItemStackHandler filteredHandler) {
                if (filteredHandler.checkSlot(1)){
                    guiGraphics.blit(WIDGET_TEXTURE, this.leftPos+61, this.topPos+37, 41, 238, 18, 18);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 0.30f);
                    guiGraphics.renderItem(filteredHandler.getFilterForSlot(1), this.leftPos+62, this.topPos+38);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                }
            }
        });

        this.menu.blockEntity.getInputItemHandler().ifPresent(iItemHandler -> {
            if(iItemHandler instanceof FilteredItemStackHandler filteredHandler) {
                if (filteredHandler.checkSlot(2)){
                    guiGraphics.blit(WIDGET_TEXTURE, this.leftPos+61, this.topPos+57, 41, 238, 18, 18);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 0.30f);
                    guiGraphics.renderItem(filteredHandler.getFilterForSlot(2), this.leftPos+62, this.topPos+58);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                }
            }
        });

        this.menu.blockEntity.getLazyInputFluidHandler().ifPresent(iFluidHandler -> {
            if(iFluidHandler instanceof MachineFluidHandler filteredHandler) {
                if (filteredHandler.checkSlot(0)){
                    guiGraphics.blit(WIDGET_TEXTURE, this.leftPos+33, this.topPos+17, 61, 198, 12, 58);
                }
            }
        });

        this.menu.blockEntity.getLazyInputFluidHandler().ifPresent(iFluidHandler -> {
            if(iFluidHandler instanceof MachineFluidHandler filteredHandler) {
                if (filteredHandler.checkSlot(1)){
                    LogUtils.getLogger().info("Attemting to blit the lock for fluid slot 1");
                    guiGraphics.blit(WIDGET_TEXTURE, this.leftPos+47, this.topPos+17, 61, 198, 12, 58);
                }
            }
        });

        renderTooltip(guiGraphics, mouseX, mouseY);

        inputTankSlot1.renderTooltip(guiGraphics, mouseX, mouseY);
        inputTankSlot2.renderTooltip(guiGraphics, mouseX, mouseY);
        outputTankSlot.renderTooltip(guiGraphics, mouseX, mouseY);

        renderEnergyTooltip(guiGraphics, mouseX, mouseY, 154);
    }

    private void renderEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x) {

        int fluidHeight = getEnergyHeight(menu.getMaxEnergy(), menu.getEnergy());

        if(!isHovering(x, getEnergyY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        List<Component> tips = new ArrayList<>(2);
        tips.add(Component.literal("Energy"));
        tips.add(Component.literal("§7%.2f%%".formatted(((float)menu.getEnergy()/ menu.getMaxEnergy()) * 100)));
        guiGraphics.renderComponentTooltip(this.font, tips, mouseX, mouseY);
    }
}
