package net.astr0.astrocraft.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class CropSticksRenderer implements BlockEntityRenderer<CropSticksBlockEntity> {

    public CropSticksRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CropSticksBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (be.getSeed().isEmpty()) return;

        BlockState plantState = be.getSimulatedPlantState();
        if (plantState.isAir()) return;

        poseStack.pushPose();

        // Offset logic:
        // Some crops (like tall flowers) might need translation.
        // Standard crops usually render at 0,0,0 relative to the block.

        // Scale/Safety:
        // We render it as a "Cutout" to support transparent textures (like wheat)
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                plantState,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                ModelData.EMPTY,
                RenderType.cutout()
        );

        poseStack.popPose();
    }
}
