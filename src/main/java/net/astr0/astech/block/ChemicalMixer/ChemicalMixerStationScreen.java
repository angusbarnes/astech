package net.astr0.astech.block.ChemicalMixer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.astr0.astech.AsTech;
import net.astr0.astech.FilteredItemStackHandler;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.GraphicsUtils;
import net.astr0.astech.gui.IconButton;
import net.astr0.astech.gui.Icons;
import net.astr0.astech.gui.MachineCapConfiguratorWidget;
import net.astr0.astech.network.AsTechNetworkHandler;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jline.utils.Log;
import org.joml.Vector4f;

import java.util.*;

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

        FluidTank tank = this.menu.blockEntity.getFluidInputTank(0);
        drawFluidTankV2(guiGraphics, tank, 34, 74);

        tank = this.menu.blockEntity.getFluidInputTank(1);
        drawFluidTankV2(guiGraphics, tank, 48, 74);

        tank = this.menu.blockEntity.getFluidOutputTank();
        drawFluidTankV2(guiGraphics, tank, 133, 74);

        renderEnergyBar(guiGraphics, 154);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        if (!isLocked) {
            if(isHovering(62, 18, 16, 16, pMouseX, pMouseY)) {
                LogUtils.getLogger().info("We clicked inside the slot area");

                this.menu.blockEntity.getInputItemHandler().ifPresent(iItemHandler -> {
                    if(iItemHandler instanceof FilteredItemStackHandler filteredHandler) {
                        FlexiPacket packet = new FlexiPacket(this.menu.blockEntity.getBlockPos(), 36);

                        packet.writeInt(0);

                        AsTechNetworkHandler.INSTANCE.sendToServer(packet);
                    }
                });

                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
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


        renderTooltip(guiGraphics, mouseX, mouseY);

        FluidTank tank = this.menu.blockEntity.getFluidInputTank(0);
        renderTankTooltip(guiGraphics, mouseX, mouseY, tank, 34);

        FluidTank tank2 = this.menu.blockEntity.getFluidInputTank(1);
        renderTankTooltip(guiGraphics, mouseX, mouseY, tank2, 48);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY, 154);

        FluidTank tank3 = this.menu.blockEntity.getFluidOutputTank();
        renderTankTooltip(guiGraphics, mouseX, mouseY, tank2, 134);

        renderEnergyTooltip(guiGraphics, mouseX, mouseY, 154);


    }

    ResourceLocation WIDGET_TEXTURE = new ResourceLocation(AsTech.MODID, "textures/gui/widgets.png");

    private void renderTankTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, FluidTank tank, int x) {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty())
            return;

        int fluidHeight = getFluidHeight(tank);

        if(!isHovering(x, getFluidY(fluidHeight) -this.topPos, 10, fluidHeight, mouseX, mouseY)) return;

        List<Component> tips = new ArrayList<>(2);
        tips.add(MutableComponent.create(fluidStack.getDisplayName().getContents()));
        tips.add(Component.literal("§7%s/%s mB".formatted(tank.getFluidAmount(), tank.getCapacity())));
        guiGraphics.renderComponentTooltip(this.font, tips, mouseX, mouseY);
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
