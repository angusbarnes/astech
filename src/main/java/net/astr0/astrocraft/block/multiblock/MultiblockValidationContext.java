package net.astr0.astrocraft.block.multiblock;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MultiblockValidationContext {
    public final BlockPos origin;
    public final Direction facing;

    private final List<BlockPos> inputHatches = new ArrayList<>();
    private final List<BlockPos> outputHatches = new ArrayList<>();

    private final int maxInputs;
    private final int maxOutputs;
    private final int minInputs;
    private final int minOutputs;

    public MultiblockValidationContext(BlockPos origin, Direction facing, int minInputs, int minOutputs, int maxInputs, int maxOutputs) {
        this.origin = origin;
        this.facing = facing;

        this.maxInputs = maxInputs;
        this.maxOutputs = maxOutputs;

        this.minInputs = minInputs;
        this.minOutputs = minOutputs;
    }

    public boolean registerHatch(BlockPos pos, BlockState state) {
        LogUtils.getLogger().info("Attempting to register hatch");
        if (state.is(ModBlocks.FLUID_INPUT_HATCH_BLOCK.get())) {
            if (inputHatches.size() >= maxInputs) return false;
            inputHatches.add(pos);
            LogUtils.getLogger().info("Successfully registered fluid input hatch");
            return true;
        }
        if (state.is(ModBlocks.FLUID_OUTPUT_HATCH_BLOCK.get())) {
            if (outputHatches.size() >= maxOutputs) return false;
            outputHatches.add(pos);
            LogUtils.getLogger().info("Successfully registered fluid output hatch");
            return true;
        }
        return false;
    }

    public boolean finalizeValidation() {
        // Must have at least 1 input and 1 output
        return inputHatches.size() >= minInputs && outputHatches.size() >= minOutputs;
    }

    public List<BlockPos> getInputHatchPositions() {
        return inputHatches;
    }

    public List<BlockPos> getOutputHatchPositions() {
        return outputHatches;
    }
}

