package net.astr0.astrocraft;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.BlockEntityVisualizer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.astr0.astrocraft.block.CropSticksBlockEntity;

public class CropSticksVisualizer implements BlockEntityVisualizer<CropSticksBlockEntity> {
    @Override
    public BlockEntityVisual<? super CropSticksBlockEntity> createVisual(VisualizationContext ctx, CropSticksBlockEntity blockEntity, float partialTick) {
        return new CropSticksVisual(ctx, blockEntity, partialTick);
    }

    @Override
    public boolean skipVanillaRender(CropSticksBlockEntity blockEntity) {
        return false;
    }
}
