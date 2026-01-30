package net.astr0.astrocraft.block;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class BrickKilnBlockEntity extends BlockEntity implements ITickableBlockEntity {

    // Inventory
    private ItemStack inputStack = ItemStack.EMPTY;
    private ItemStack outputStack = ItemStack.EMPTY;

    // State Variables
    private int temperature = 0;
    private int progress = 0;
    private int maxProgress = 200; // Default cook time (10 seconds)

    // Configuration
    private static final int MAX_TEMP = 1000;
    private static final int REQUIRED_TEMP = 300; // Temp required to start cooking
    private static final int HEAT_RATE = 2; // How fast it heats up
    private static final int COOL_RATE = 1; // How fast it cools down

    public BrickKilnBlockEntity(BlockPos pos, BlockState state) {
        // Replace ModBlockEntities.BRICK_KILN.get() with your actual RegistryObject
        super(ModBlockEntities.BRICK_KILN.get(), pos, state);
    }


    // --- Interaction Logic ---
    // This is called from your Block class's onUse / use method
    public boolean onPlayerInteract(Player player, InteractionHand hand) {
        if (this.level.isClientSide) return true; // Let server handle logic

        LogUtils.getLogger().info("=================== Kiln interaction on server");

        ItemStack heldItem = player.getItemInHand(hand);

        LogUtils.getLogger().info("=================== [Kiln]: Player is holding: " + heldItem);

        // Case 1: Retrieve Output
        if (!this.outputStack.isEmpty()) {
            LogUtils.getLogger().info("=================== [Kiln]: case 1");
            player.getInventory().add(this.outputStack);
            this.outputStack = ItemStack.EMPTY;
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            return true;
        }

        // Case 2: Insert Input
        if (this.inputStack.isEmpty() && !heldItem.isEmpty()) {
            LogUtils.getLogger().info("=================== [Kiln]: case 2");
            // Check if item has a valid SMELTING recipe
            SimpleContainer tempInv = new SimpleContainer(heldItem);
            Optional<SmeltingRecipe> recipe = this.level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, tempInv, this.level);

            // OPTIONAL: If you want to strictly ban Blast Furnace exclusives,
            // you usually don't need to do anything. Standard SMELTING covers generic cooking.
            // If you wanted to PREVENT ores, you would check tags here.

            if (recipe.isPresent()) {
                // Take 1 item from player
                ItemStack insertStack = heldItem.copy();
                insertStack.setCount(1);
                this.inputStack = insertStack;
                this.maxProgress = recipe.get().getCookingTime(); // Dynamic cooking time based on recipe

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }

                this.setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
                return true;
            }
        }

        // Case 3: Retrieve Input (if user changes mind)
        if (!this.inputStack.isEmpty() && heldItem.isEmpty()) {
            LogUtils.getLogger().info("=================== [Kiln]: case 3");
            player.setItemInHand(hand, this.inputStack);
            this.inputStack = ItemStack.EMPTY;
            this.progress = 0;
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            return true;
        }

        LogUtils.getLogger().info("=================== [Kiln]: no case");
        return false;
    }

    private void ejectItem(ItemStack stack) {
        if (!stack.isEmpty() && level != null) {
            ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, stack);
            level.addFreshEntity(itemEntity);
        }
    }

    // --- NBT Save/Load ---

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inputStack = ItemStack.of(tag.getCompound("InputItem"));
        this.outputStack = ItemStack.of(tag.getCompound("OutputItem"));
        this.temperature = tag.getInt("Temperature");
        this.progress = tag.getInt("Progress");
        this.maxProgress = tag.getInt("MaxProgress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("InputItem", this.inputStack.save(new CompoundTag()));
        tag.put("OutputItem", this.outputStack.save(new CompoundTag()));
        tag.putInt("Temperature", this.temperature);
        tag.putInt("Progress", this.progress);
        tag.putInt("MaxProgress", this.maxProgress);
    }

    // --- Client Synchronization (Crucial for Rendering Items) ---

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Getters for your Renderer
    public ItemStack getInputStack() { return inputStack; }
    public ItemStack getOutputStack() { return outputStack; }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean isHeated = isHeated(level, pos);
        boolean dirty = false; // "dirty" means data changed and needs saving

        // --- Temperature Logic ---
        if (isHeated) {
            if (temperature < MAX_TEMP) {
                temperature += HEAT_RATE;
                dirty = true;
            }
        } else {
            if (temperature > 0) {
                temperature -= COOL_RATE;
                dirty = true;
            }
        }

        // --- Cooking Logic ---
        // Can only cook if hot enough, has input, and output slot is empty
        if (temperature >= REQUIRED_TEMP && !inputStack.isEmpty() && outputStack.isEmpty()) {
            progress++;

            // Optional: Spawn smoke particles to show it is cooking
            if (level.random.nextFloat() < 0.1f) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0, 0.05, 0);
            }

            if (progress >= maxProgress) {
                craftItem();
                dirty = true;
            }
        } else {
            // Reset progress if conditions are lost (e.g. fire goes out or item removed)
            if (progress > 0) {
                progress = 0;
                dirty = true;
            }
        }

        if (dirty) {
            setChanged();
            // Sync to client for rendering (optional, but recommended for visual mods)
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private boolean isHeated(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        // Checks for standard Campfire or Soul Campfire, and ensures it is LIT
        return (belowState.is(Blocks.CAMPFIRE) || belowState.is(Blocks.SOUL_CAMPFIRE))
                && belowState.getValue(CampfireBlock.LIT);
    }

    private void craftItem() {
        if (this.level == null) return;

        SimpleContainer inventory = new SimpleContainer(this.inputStack);
        Optional<SmeltingRecipe> recipe = this.level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, inventory, this.level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().assemble(inventory, this.level.registryAccess());
            this.outputStack = result.copy();
            this.inputStack = ItemStack.EMPTY;
            this.progress = 0;

            // Play a sound to indicate completion
            this.level.playSound(null, this.worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.0f);
        } else {
            // Recipe became invalid during cooking (edge case), eject item
            ejectItem(this.inputStack);
            this.inputStack = ItemStack.EMPTY;
            this.progress = 0;
        }
    }
}