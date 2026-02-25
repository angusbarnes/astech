package net.astr0.astrocraft;

import com.mojang.logging.LogUtils;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import net.astr0.astrocraft.block.CropSticksBlockEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;


// Implement Visual.
// Optional: Implement TickableVisual if you need per-tick updates (e.g., smoothing).
// Optional: Implement DynamicVisual if the crop sways in the wind.
public class CropSticksVisual extends AbstractBlockEntityVisual<CropSticksBlockEntity> {

    private TransformedInstance transformedInstance;

    // The actual object on the GPU.
    // TransformedInstance is a standard type that supports position/rotation/scaling.
    private TransformedInstance cropInstance;

    // State tracking to know when to rebuild
    private ItemStack lastSeed = ItemStack.EMPTY;
    private int lastAge = -1;

    // This will be reconstructed on every block state change so we alledegly do not have to track changes outselves
    public CropSticksVisual(VisualizationContext context, CropSticksBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        Instancer<TransformedInstance> cropModelInstancer = instancerProvider().instancer(
                InstanceTypes.TRANSFORMED,
                Models.block(blockEntity.getSimulatedPlantState())
        );
        transformedInstance = cropModelInstancer.createInstance();

        setupVisual(partialTick);
        LogUtils.getLogger().info(">>>>>>>>>> A visual instance was created");
    }

    @Override
    protected void _delete() {
        transformedInstance.delete();
    }

    // We might not need this
    private void setupVisual(float partialTicks) {
        transformedInstance.setIdentityTransform().translate(getVisualPosition()).setChanged();
//        float lidAngle = blockEntity.lid.getValue(partialTicks);
//        float drawerOffset = blockEntity.drawers.getValue(partialTicks);
//
//        if (lidAngle != lastLidAngle) {
//            lid.setIdentityTransform()
//                    .translate(getVisualPosition())
//                    .center()
//                    .rotateYDegrees(-facing.toYRot())
//                    .uncenter()
//                    .translate(0, 6 / 16f, 12 / 16f)
//                    .rotateXDegrees(135 * lidAngle)
//                    .translateBack(0, 6 / 16f, 12 / 16f)
//                    .setChanged();
//        }
//
//        if (drawerOffset != lastDrawerOffset) {
//            for (int offset : Iterate.zeroAndOne) {
//                drawers[offset].setIdentityTransform()
//                        .translate(getVisualPosition())
//                        .center()
//                        .rotateYDegrees(-facing.toYRot())
//                        .uncenter()
//                        .translate(0, offset * 1 / 8f, -drawerOffset * .175f * (2 - offset))
//                        .setChanged();
//            }
//        }
//
//        lastLidAngle = lidAngle;
//        lastDrawerOffset = drawerOffset;
    }

    @Override
    public void updateLight(float partialTick) {
        relight(transformedInstance);
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(transformedInstance);
    }
}