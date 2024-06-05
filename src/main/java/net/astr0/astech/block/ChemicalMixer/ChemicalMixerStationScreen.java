package net.astr0.astech.block.ChemicalMixer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.astr0.astech.AsTech;
import net.astr0.astech.Fluid.helpers.TintColor;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.gui.IconButton;
import net.astr0.astech.gui.Icons;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.NetworkedMachineUpdate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.joml.Vector4f;

// This only gets registered on the client side
public class ChemicalMixerStationScreen extends AbstractContainerScreen<ChemicalMixerStationMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AsTech.MODID, "textures/gui/chemical_mixer.png");

    public ChemicalMixerStationScreen(ChemicalMixerStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        setup();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        FluidTank tank = this.menu.blockEntity.getFluidTank(0);
        drawFluidTankV2(guiGraphics, tank, 34, 74);

        tank = this.menu.blockEntity.getFluidTank(1);
        drawFluidTankV2(guiGraphics, tank, 48, 74);
        renderEnergyBar(guiGraphics, 154);

    }

    private void drawFluidTankV2(GuiGraphics guiGraphics, FluidTank tank, int x, int y) {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = getFluidHeight(tank);

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);

        if (stillTexture == null) return;

        TextureAtlasSprite sprite = this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        Vector4f tintColor = new TintColor(fluidTypeExtensions.getTintColor(fluidStack)).getAsNormalisedRenderColor();

        guiGraphics.setColor(tintColor.x, tintColor.y, tintColor.z, tintColor.w);
        GraphicsUtils.drawTiledSprite(guiGraphics, this.leftPos + x, this.topPos + y, 0, 10, getFluidHeight(tank), sprite, 16, 16, 1);
        guiGraphics.setColor(1f, 1f, 1f, 1f);
    }

    private void drawFluidTank(GuiGraphics guiGraphics, FluidTank tank, int x) {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = getFluidHeight(tank);

        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);

        if (stillTexture == null) return;

        TextureAtlasSprite sprite = this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int tintColor = fluidTypeExtensions.getTintColor(fluidStack);
        float alpha = ((tintColor >> 24) & 0xFF) / 255.0f;
        float red = ((tintColor >> 16) & 0xFF) / 255.0f;
        float green = ((tintColor >> 8) & 0xFF) / 255.0f;
        float blue = ((tintColor) & 0xFF) / 255.0f;

        guiGraphics.setColor(red, green, blue, alpha);

        guiGraphics.blit(
                this.leftPos + x,
                getFluidY(fluidHeight),
                0,
                10,
                fluidHeight,
                sprite
        );

        guiGraphics.setColor(1f, 1f, 1f, 1f);
    }

    private int getFluidY(int fluidHeight) {
        return this.topPos + 18 + (56-fluidHeight);
    }

    private int getEnergyY(int energyHeight) {
        return this.topPos + 9 + (69-energyHeight);
    }

    private static int getFluidHeight(IFluidTank tank) {
        return  (int) (56 * ((float)tank.getFluidAmount()/tank.getCapacity()));
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

    protected void setup() {
        SETTINGS_BUTTON = new IconButton(this.leftPos + 11, this.topPos + 30, Icons.SETTINGS, (button) -> {
            LogUtils.getLogger().info("Button Pressed");
        });

        LOCK_BUTTON = new IconButton(this.leftPos + 11, this.topPos + 49, Icons.UNLOCKED, (button) -> {
            LogUtils.getLogger().info("Button 2 Pressed");
            button.setIcon(button.getIcon() == Icons.UNLOCKED ? Icons.LOCKED : Icons.UNLOCKED);

            AsTechNetworkHandler.INSTANCE.sendToServer(new NetworkedMachineUpdate(menu.blockEntity.getBlockPos()));
        });

    }

    private IconButton SETTINGS_BUTTON;

    private IconButton LOCK_BUTTON;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        FluidTank tank = this.menu.blockEntity.getFluidTank(0);
        renderTankTooltip(guiGraphics, mouseX, mouseY, tank, 34);

        FluidTank tank2 = this.menu.blockEntity.getFluidTank(1);
        renderTankTooltip(guiGraphics, mouseX, mouseY, tank2, 48);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY, 154);

        this.addRenderableWidget(SETTINGS_BUTTON);
        this.addRenderableWidget(LOCK_BUTTON);
    }

    private void renderTankTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, FluidTank tank, int x) {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = getFluidHeight(tank);

        if(!isHovering(x, getFluidY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        Component component = MutableComponent.create(fluidStack.getDisplayName().getContents()).append(" (%s/%s mB)".formatted(tank.getFluidAmount(), tank.getCapacity()));
        guiGraphics.renderTooltip(this.font, component, mouseX, mouseY);
    }

    private void renderEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x) {

        int fluidHeight = getEnergyHeight(menu.getMaxEnergy(), menu.getEnergy());

        if(!isHovering(x, getEnergyY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        Component component = MutableComponent.create(Component.literal("Energy ").getContents()).append("%.2f%%".formatted(((float)menu.getEnergy()/ menu.getMaxEnergy()) * 100));
        guiGraphics.renderTooltip(this.font, component, mouseX, mouseY);
    }
}
