package net.astr0.astrocraft.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BrickKilnRenderer implements BlockEntityRenderer<BrickKilnBlockEntity> {

    public BrickKilnRenderer(BlockEntityRendererProvider.Context context) {
        // Context is useful if you need to access fonts or model managers
    }

    @Override
    public void render(BrickKilnBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        // 1. Determine which item to render (output takes priority visual-wise)
        ItemStack stack = !entity.getOutputStack().isEmpty() ? entity.getOutputStack() : entity.getInputStack();

        if (stack.isEmpty()) {
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

        // 2. Center the item in the block
        // Coordinates: (0.5, 0.5, 0.5) is the dead center
        poseStack.translate(0.5f, 0.25f, 0.5f);

        // 3. Scale it down so it fits nicely inside the kiln model
        poseStack.scale(0.6f, 0.6f, 0.6f);

        // 4. Rotate to lie flat
        // Standard items render vertically. To lay them flat, we rotate 90 degrees on the X axis.
        poseStack.mulPose(Axis.XP.rotationDegrees(90f));

        // 5. Render the item
        // ItemDisplayContext.FIXED or GROUND is usually best for static BERs
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getLevel(), 1);

        poseStack.popPose();
    }
}