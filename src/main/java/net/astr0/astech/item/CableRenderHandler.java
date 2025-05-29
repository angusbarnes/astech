package net.astr0.astech.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.astr0.astech.AsTech;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AsTech.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CableRenderHandler {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {

        // Only render after all world blocks have been rendered correctly
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL)
            return;


        //LogUtils.getLogger().info("Ohhh boy we renderin");

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ItemStack heldItem = player.getMainHandItem();
        CompoundTag tag = heldItem.getOrCreateTag();

        if (heldItem.getItem() instanceof CableLayingToolItem toolItem && tag.contains("start")) {

            BlockPos startPos = NbtUtils.readBlockPos(tag.getCompound("start"));

            // Get target pos (e.g., from raytrace or camera pos)
            BlockHitResult hit = (BlockHitResult) mc.hitResult;
            if (hit == null) return;

            BlockPos endPos = hit.getBlockPos().relative(hit.getDirection());

            List<BlockPos> path = getStraightLinePath(startPos, endPos);

            // Render ghost blocks for each block in the path
            renderGhostBlocks(event.getPoseStack(), path, event.getCamera());
        }
    }

    private static void renderGhostBlocks(PoseStack poseStack, List<BlockPos> path, Camera camera) {
        Vec3 cameraPos = camera.getPosition();
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();

        // Set up rendering state for translucent blocks
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false); // Don't write to depth buffer

        // Create tessellator and buffer builder for rendering
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        poseStack.pushPose();

        // Translate to world position relative to camera
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // Begin rendering translucent quads
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);

        // Define the ghost block state (you can change this to any block you want)
        BlockState ghostBlockState = Blocks.WHITE_STAINED_GLASS.defaultBlockState();

        // Render each ghost block
        for (BlockPos pos : path) {
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            // Render the block model with translucency
            renderGhostBlock(blockRenderer, buffer, poseStack, ghostBlockState, pos);

            poseStack.popPose();
        }

        // Finish rendering
        tessellator.end();

        poseStack.popPose();

        // Restore rendering state
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private static void renderGhostBlock(BlockRenderDispatcher blockRenderer, BufferBuilder buffer,
                                         PoseStack poseStack, BlockState blockState, BlockPos pos) {
        try {
            // Get the block model
            BakedModel model = blockRenderer.getBlockModel(blockState);

            // Create a custom vertex consumer that applies translucency
            VertexConsumer ghostVertexConsumer = new GhostVertexConsumer(buffer, 0.6f); // 80% opacity

            // Render the block model
            blockRenderer.getModelRenderer().renderModel(
                    poseStack.last(),
                    ghostVertexConsumer,
                    blockState,
                    model,
                    1.0f, 1.0f, 1.0f, // RGB values
                    LightTexture.FULL_BRIGHT, // Full brightness
                    OverlayTexture.NO_OVERLAY
            );
        } catch (Exception e) {
            // Fallback to simple cube if model rendering fails
            renderSimpleGhostCube(buffer, poseStack, pos);
        }
    }

    private static void renderSimpleGhostCube(BufferBuilder buffer, PoseStack poseStack, BlockPos pos) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        float x1 = 0.0f;
        float y1 = 0.0f;
        float z1 = 0.0f;
        float x2 = 1.0f;
        float y2 = 1.0f;
        float z2 = 1.0f;

        int light = LightTexture.FULL_BRIGHT;
        float alpha = 0.3f;
        float r = 1.0f, g = 1.0f, b = 1.0f; // White color

        // Render all 6 faces of the cube
        // Bottom face (Y-)
        addQuad(buffer, matrix, normal, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, r, g, b, alpha, light, 0, -1, 0);
        // Top face (Y+)
        addQuad(buffer, matrix, normal, x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, r, g, b, alpha, light, 0, 1, 0);
        // North face (Z-)
        addQuad(buffer, matrix, normal, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, r, g, b, alpha, light, 0, 0, -1);
        // South face (Z+)
        addQuad(buffer, matrix, normal, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, r, g, b, alpha, light, 0, 0, 1);
        // West face (X-)
        addQuad(buffer, matrix, normal, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, r, g, b, alpha, light, -1, 0, 0);
        // East face (X+)
        addQuad(buffer, matrix, normal, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, r, g, b, alpha, light, 1, 0, 0);
    }

    private static void addQuad(BufferBuilder buffer, Matrix4f matrix, Matrix3f normal,
                                float x1, float y1, float z1, float x2, float y2, float z2,
                                float x3, float y3, float z3, float x4, float y4, float z4,
                                float r, float g, float b, float a, int light, float nx, float ny, float nz) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        buffer.vertex(matrix, x4, y4, z4).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
    }

    // Custom vertex consumer to apply translucency to block models
    private static class GhostVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float alpha;

        public GhostVertexConsumer(VertexConsumer delegate, float alpha) {
            this.delegate = delegate;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return delegate.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(int r, int g, int b, int a) {
            return delegate.color(r, g, b, (int)(alpha * 255));
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return delegate.uv(u, v);
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return delegate.overlayCoords(u, v);
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return delegate.uv2(u, v);
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return delegate.normal(x, y, z);
        }

        @Override
        public void endVertex() {
            delegate.endVertex();
        }

        @Override
        public void defaultColor(int r, int g, int b, int a) {
            delegate.defaultColor(r, g, b, (int)(alpha * 255));
        }

        @Override
        public void unsetDefaultColor() {
            delegate.unsetDefaultColor();
        }
    }

    private static List<BlockPos> getStraightLinePath(BlockPos start, BlockPos end) {
        List<BlockPos> path = new ArrayList<>();
        int dx = Integer.compare(end.getX(), start.getX());
        int dy = Integer.compare(end.getY(), start.getY());
        int dz = Integer.compare(end.getZ(), start.getZ());

        BlockPos current = start;
        while (!current.equals(end)) {
            path.add(current);
            if (current.getX() != end.getX()) current = current.offset(dx, 0, 0);
            else if (current.getY() != end.getY()) current = current.offset(0, dy, 0);
            else if (current.getZ() != end.getZ()) current = current.offset(0, 0, dz);
        }
        path.add(end);
        return path;
    }
}