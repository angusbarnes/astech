package net.astr0.astech.block.ChemicalMixer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.astr0.astech.AsTech;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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
        drawFluidTank(guiGraphics, tank, 34);

        tank = this.menu.blockEntity.getFluidTank(1);
        drawFluidTank(guiGraphics, tank, 48);

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

    private static int getFluidHeight(IFluidTank tank) {
        return  (int) (56 * ((float)tank.getFluidAmount()/tank.getCapacity()));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, Component.literal("Chemical Mixer"), 4, 4, 4210752, false);
        //pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        FluidTank tank = this.menu.blockEntity.getFluidTank(0);
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = getFluidHeight(tank);

        if(!isHovering(34, getFluidY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        Component component = MutableComponent.create(fluidStack.getDisplayName().getContents()).append(" (%s/%s mB)".formatted(tank.getFluidAmount(), tank.getCapacity()));
        guiGraphics.renderTooltip(this.font, component, mouseX, mouseY);

        tank = this.menu.blockEntity.getFluidTank(0);
        fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        fluidHeight = getFluidHeight(tank);

        if(!isHovering(34, getFluidY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        component = MutableComponent.create(fluidStack.getDisplayName().getContents()).append(" (%s/%s mB)".formatted(tank.getFluidAmount(), tank.getCapacity()));
        guiGraphics.renderTooltip(this.font, component, mouseX, mouseY);
    }
}
