package net.astr0.astrocraft.block;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.farming.CropGenome;
import net.astr0.astrocraft.farming.CropUtils;
import net.astr0.astrocraft.farming.PlantedCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CropSticksBlockEntity extends BlockEntity {

    // 1. Single Source of Truth. This Stack MUST contain the NBT tags.
    private ItemStack seedStack = ItemStack.EMPTY;

    // Cache the helper to avoid resolving IPlantable every tick
    // Nullable because the stick might be empty
    private @Nullable PlantedCrop cachedPlant;

    public CropSticksBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CROP_STICKS.get(), pos, state);
    }


    public void setSeed(ItemStack stack) {
        // Always store a COPY to prevent external modification
        this.seedStack = stack.copy();
        this.seedStack.setCount(1); // Force count 1

        // Refresh cache immediately
        this.cachedPlant = CropUtils.getPlantedCrop(this.seedStack);
        cachedPlant.genetics().mutate(new Random()); //TODO: THIS IS TEMPORARY
        cachedPlant.genetics().applyToStack(seedStack);

        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);

    }

    public ItemStack getSeed() {
        // Return a copy so the caller can't mutate our internal state
        return seedStack.isEmpty() ? ItemStack.EMPTY : seedStack.copy();
    }

    public CropGenome getGenes() {
        return cachedPlant.genetics();
    }

    // --- Core Logic: The "Hybrid" Tick ---
    // Called by the Block's randomTick()
    // TODO: Support flowers and shit
    public void performGrowthTick(ServerLevel level, RandomSource random) {
        if (cachedPlant == null) return; // Fast fail

        CropGenome stats = cachedPlant.genetics();

        // 1. Calculate Growth Chance based on Stats
        // Base chance 10% + (Growth Stat * 5%)
        float chance = (float) (0.10f + (stats.getGrowth() * 0.05f));

        // 2. Check Light/Water logic here (omitted for brevity)
        if (level.getRawBrightness(this.worldPosition.above(), 0) >= 9) {
            if (random.nextFloat() < chance) {
                int currentAge = this.getBlockState().getValue(CropSticksBlock.AGE);
                if (currentAge < 7) {
                    level.setBlock(this.worldPosition, this.getBlockState().setValue(CropSticksBlock.AGE, currentAge + 1), 2);
                }
            }
        }
    }

    // --- Compatibility: Simulation ---
    // Returns the BlockState that the internal seed *would* look like
    public BlockState getSimulatedPlantState() {
        if (cachedPlant == null) return Blocks.AIR.defaultBlockState();

        // Safe access via cached object
        BlockState internalState = cachedPlant.Plant().getPlant(this.level, this.worldPosition);

        // Map Stick Age (0-7) to Internal Plant Age
        if (internalState.hasProperty(BlockStateProperties.AGE_7)) {
            return internalState.setValue(BlockStateProperties.AGE_7, this.getBlockState().getValue(CropSticksBlock.AGE));
        }
        // Handle crops with max age 3 (like beetroot)
        else if (internalState.hasProperty(BlockStateProperties.AGE_3)) {
            int age = (this.getBlockState().getValue(CropSticksBlock.AGE) * 3) / 7;
            return internalState.setValue(BlockStateProperties.AGE_3, age);
        }
        return internalState;
    }

    public List<ItemStack> simulateDrops(LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        if (cachedPlant == null) return drops;

        BlockState simulatedState = getSimulatedPlantState();

        // CRITICAL: We override the BLOCK_STATE parameter.
        // The internal plant's loot table uses this to determine its drops.
        builder.withParameter(LootContextParams.BLOCK_STATE, simulatedState);
        // This might return Wheat + Seeds
        List<ItemStack> rawDrops = simulatedState.getDrops(builder);

        if (rawDrops.isEmpty()) {
            LogUtils.getLogger().warn("CROP STICKS: Loot table for {} returned NOTHING! Age: {}",
                    simulatedState.getBlock(),
                    this.getBlockState().getValue(CropSticksBlock.AGE));
        }
        for (ItemStack drop : rawDrops) {
            // Check if the drop is the seed
            if (ItemStack.isSameItem(drop, seedStack)) {
                // IMPORTANT: Apply the genetics to the dropped seed
                cachedPlant.genetics().applyToStack(drop);
            }
            drops.add(drop);
        }
        return drops;
    }

    //PLACEHOLDER (do not implement): Check if a crop can be crafted
    private boolean isValidRecipe() {
        return false;
    }

    //PLACEHOLDER (do not implement): Get crafted crop based on recipe system
    private PlantedCrop getCraftedCrop() {
        return null;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Seed")) {
            this.seedStack = ItemStack.of(tag.getCompound("Seed"));
            // Re-init cache on load
            this.cachedPlant = CropUtils.getPlantedCrop(this.seedStack);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!seedStack.isEmpty()) {
            // This saves the stack WITH the NBT data inside it automatically
            tag.put("Seed", seedStack.save(new CompoundTag()));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}