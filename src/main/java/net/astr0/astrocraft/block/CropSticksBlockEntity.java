package net.astr0.astrocraft.block;

import com.mojang.logging.LogUtils;
import net.astr0.astrocraft.farming.CropGenetics;
import net.astr0.astrocraft.farming.CropUtils;
import net.astr0.astrocraft.farming.PlantedCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CropSticksBlockEntity extends BlockEntity {

    private PlantedCrop planted;
    private ItemStack seedStack = ItemStack.EMPTY;
    public CropSticksBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CROP_STICKS.get(), pos, state);
    }


    public void setPlanted(PlantedCrop plant) {
        planted = plant;
        seedStack = new ItemStack(plant.seed(), 1);
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public ItemStack getSeed() {
        return seedStack;
    }

    // --- Core Logic: The "Hybrid" Tick ---
    // Called by the Block's randomTick()
    public void performGrowthTick(ServerLevel level, RandomSource random) {
        if (seedStack.isEmpty()) return;

        CropGenetics stats = planted.genetics();

        // 1. Calculate Growth Chance based on Stats
        // Base chance 10% + (Growth Stat * 5%)
        float chance = 0.10f + (stats.growth() * 0.05f);

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

        BlockState internalState = planted.Plant().getPlant(this.level, this.worldPosition);

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

    public List<ItemStack> simulateDrops(ServerLevel level) {
        List<ItemStack> drops = new ArrayList<>();
        if (seedStack.isEmpty()) return drops;

        BlockState simulatedState = getSimulatedPlantState();

        // Use LootContext to ask the internal block what it drops
        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);

        List<ItemStack> rawDrops = simulatedState.getDrops(params);

        // --- Critical: Re-apply Stats ---
        // The loot table returns "generic" seeds. We must replace them with our "stat-keeping" seed.
        for (ItemStack drop : rawDrops) {
            LogUtils.getLogger().info("++++++++ Dropped: {}", drop);
            // If the drop matches our seed type, apply the stats
            if (drop.getItem() == seedStack.getItem()) {
                planted.genetics().applyToStack(drop);
            }
            drops.add(drop);
        }
        return drops;
    }

    // Standard NBT Save/Load...
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Seed")) this.seedStack = ItemStack.of(tag.getCompound("Seed"));
        planted = CropUtils.getPlantedCrop(seedStack);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!seedStack.isEmpty()) tag.put("Seed", seedStack.save(new CompoundTag()));
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