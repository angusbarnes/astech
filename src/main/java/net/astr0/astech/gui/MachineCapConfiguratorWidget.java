package net.astr0.astech.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class MachineCapConfiguratorWidget extends AbstractWidget {

    public final UIButton FRONT_BUTTON;
    public final UIButton BACK_BUTTON;
    public final UIButton LEFT_BUTTON;
    public final UIButton RIGHT_BUTTON;
    public final UIButton UP_BUTTON;
    public final UIButton DOWN_BUTTON;


    public final UIButton[] buttons;

    public MachineCapConfiguratorWidget(int pX, int pY) {
        super(pX, pY, 0, 0, Component.empty());

        // HERE WE SET UP OUR MAIN GRID OF BUTTONS

        FRONT_BUTTON = new UIButton(Icons.BLANK, new TintColor(255, 0, 0, 255), pX, pY, "Front");
        BACK_BUTTON = new UIButton(Icons.BLANK, new TintColor(0, 0, 255, 255), pX + 19, pY + 19, "Back");
        LEFT_BUTTON = new UIButton(Icons.BLANK, new TintColor(255, 0, 255, 255), pX - 19, pY, "Left");
        RIGHT_BUTTON = new UIButton(Icons.BLANK, new TintColor(0, 255, 0, 255), pX + 19, pY, "Right");
        UP_BUTTON = new UIButton(Icons.BLANK, new TintColor(255, 125, 87, 255), pX, pY - 19, "Up");
        DOWN_BUTTON = new UIButton(Icons.BLANK, new TintColor(255, 255, 0, 255), pX, pY + 19, "Down");

        buttons = new UIButton[] {
            FRONT_BUTTON,
            BACK_BUTTON,
            LEFT_BUTTON,
            RIGHT_BUTTON,
            UP_BUTTON,
            DOWN_BUTTON
        };

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        for (UIButton button : buttons) {
            button.Render(pGuiGraphics, pMouseX, pMouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            for (UIButton button : buttons) {
                if(button.isMouseWithinBounds((int)pMouseX, (int)pMouseY)) {
                    button.clicked();
                    return true;
                }
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    protected void onConfigUpdated() {
        // This function should be called any time a button icon is clicked
        // We should update our internal state, then call a callback from our
        // owner object which can dispatch a network update
    }
}
