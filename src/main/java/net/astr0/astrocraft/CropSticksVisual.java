//package net.astr0.astrocraft;
//
//
//import dev.engine_room.flywheel.api.instance.Instance;
//import dev.engine_room.flywheel.api.instance.InstanceType;
//import dev.engine_room.flywheel.api.model.Model;
//import dev.engine_room.flywheel.api.task.Plan;
//import dev.engine_room.flywheel.api.visual.DynamicVisual;
//import dev.engine_room.flywheel.api.visual.TickableVisual;
//import dev.engine_room.flywheel.api.visual.Visual;
//import dev.engine_room.flywheel.api.visualization.VisualizationContext;
//import dev.engine_room.flywheel.lib.instance.InstanceTypes;
//import dev.engine_room.flywheel.lib.instance.OrientedInstance;
//import dev.engine_room.flywheel.lib.instance.TransformedInstance;
//import dev.engine_room.flywheel.lib.model.Models;
//import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
//import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
//import net.astr0.astrocraft.block.CropSticksBlock;
//import net.astr0.astrocraft.block.CropSticksBlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.client.model.data.ModelData;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.function.Consumer;
//
///// Implement Visual.
//// Optional: Implement TickableVisual if you need per-tick updates (e.g., smoothing).
//// Optional: Implement DynamicVisual if the crop sways in the wind.
//public class CropSticksVisual implements Visual, SimpleTickableVisual {
//
//    private final VisualizationContext context;
//    private final CropSticksBlockEntity blockEntity;
//    private final BlockPos pos;
//
//    // The actual object on the GPU.
//    // TransformedInstance is a standard type that supports position/rotation/scaling.
//    private TransformedInstance cropInstance;
//
//    // State tracking to know when to rebuild
//    private ItemStack lastSeed = ItemStack.EMPTY;
//    private int lastAge = -1;
//
//    public CropSticksVisual(VisualizationContext context, CropSticksBlockEntity blockEntity, float partialTick) {
//        this.context = context;
//        this.blockEntity = blockEntity;
//        this.pos = blockEntity.getBlockPos();
//    }
//
//    public void init(float partialTick) {
//        // Initial setup
//        updateInstance();
//    }
//
//
//    @Override
//    public void update(float partialTick) {
//        // This method is called when the Visual needs to refresh generic data (like lighting)
//        // or if the chunk is moved.
//        if (cropInstance != null) {
//            // Relight the instance based on the block's current position
//            cropInstance.light(context.level().getBrightness(context.lightLayer(), pos))
//                    .setChanged();
//        }
//    }
//
//    private void updateInstance() {
//        // 1. Clean up the old instance if it exists
//        if (cropInstance != null) {
//            cropInstance.delete();
//            cropInstance = null;
//        }
//
//        // 2. Determine what to draw
//        BlockState stateToRender = blockEntity.getSimulatedPlantState();
//        if (stateToRender.isAir()) return;
//
//        // 3. Create the new Instance
//        // We ask the context for an Instancer capable of drawing this specific BlockState
//        // InstanceTypes.TRANSFORMED gives us an instance we can move/rotate/scale.
//        cropInstance = context.instancerProvider()
//                .instancer(InstanceTypes.TRANSFORMED, Models.block(stateToRender))
//                .createInstance();
//
//        // 4. Position the instance
//        // Flywheel 1.0 often works in relative coordinates, but depending on the backend,
//        // we usually set the translation to the block's center or corner.
//        cropInstance.loadIdentity()
//                .translate(pos.getX(), pos.getY(), pos.getZ())
//                // .translate(0.5f, 0.0f, 0.5f) // Adjust if your model needs centering
//                .light(context.level().getBrightness(context.lightLayer(), pos))
//                .setChanged(); // IMPORTANT: Mark as changed to upload to GPU
//    }
//
//    @Override
//    public void delete() {
//        // Cleanup when the chunk unloads or the block is broken
//        if (cropInstance != null) {
//            cropInstance.delete();
//            cropInstance = null;
//        }
//    }
//
//    // If you want to support crumbling (breaking particles on the instance), implement this:
//    @Override
//    public void collectCrumblingInstances(com.jozufozu.flywheel.api.visual.CrumblingRenderer renderer) {
//        if (cropInstance != null) {
//            // This tells Flywheel to render the breaking overlay on our custom instance
//            // when the player hits the block
//            // renderer.add(cropInstance);
//        }
//    }
//
//    @Override
//    public void tick(Context context) {
//        ItemStack currentSeed = blockEntity.getSeed();
//        int currentAge = blockEntity.getBlockState().getValue(CropSticksBlock.AGE);
//
//        // Dirty check: only touch the GPU if something actually changed
//        if (!ItemStack.matches(lastSeed, currentSeed) || lastAge != currentAge) {
//            updateInstance();
//            lastSeed = currentSeed.copy();
//            lastAge = currentAge;
//        }
//    }
//
//    @Override
//    public Plan<Context> planTick() {
//        return null;
//    }
//}