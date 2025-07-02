package net.astr0.astech.item;

import com.mojang.logging.LogUtils;
import net.astr0.astech.Fluid.AsTechChemicalFluidType;
import net.astr0.astech.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidCellItem extends Item {

    public final int CAPACITY;

    public FluidCellItem(Properties properties) {
        this(properties, 1000);
    }

    public FluidCellItem(Properties properties, int capacity) {
        super(properties.stacksTo(1));
        CAPACITY = capacity;
    }

    // Capability provider for fluid handler
    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemCapability(stack, CAPACITY);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(level.isClientSide() || !(entity instanceof LivingEntity livingEntity)) return;

        Fluid fluid = getFluid(stack).getFluid();

        if (fluid.getFluidType() instanceof AsTechChemicalFluidType hazardousFluid) {
            if(livingEntity.tickCount % 20 == 0) {
                for(ItemStack armorPiece : livingEntity.getArmorSlots()) {
                    if (!armorPiece.is(ModTags.CHEMICAL_PROTECTION)) {
                        hazardousFluid.getHazardBehavior().apply(stack, livingEntity, level);
                        return;
                    }
                }
            }
        }
    }

    // Add tooltip to show fluid contents
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        FluidStack fluid = getFluid(stack);
        if (!fluid.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.fluid_cell.contents",
                            fluid.getDisplayName(), fluid.getAmount(), CAPACITY)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.fluid_cell.empty")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public int getCapacity() {
        return CAPACITY;
    }

    public static int getCapacityFromStack(ItemStack stack) {
        if (stack.getItem() instanceof FluidCellItem cell) {
            return cell.getCapacity();
        }

        return 1;
    }

    public boolean isBarVisible(ItemStack pStack) {
        return FluidCellItem.getFluid(pStack).getAmount() > 0 && FluidCellItem.getFluid(pStack).getAmount() < FluidCellItem.getCapacityFromStack(pStack);
    }

    public int getBarWidth(ItemStack pStack) {
        return Math.round((float)FluidCellItem.getFluid(pStack).getAmount() / (float)FluidCellItem.getCapacityFromStack(pStack) * 13.0F );
    }

    public int getBarColor(ItemStack pStack) {
        return Mth.hsvToRgb(2.04f, 1.0F, 1.0F);
    }

    // Custom fluid handler stored in NBT
    private static class FluidHandlerItemCapability implements ICapabilityProvider, IFluidHandlerItem {

        private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
        private final ItemStack container;
        private final FluidTank tank;
        private final int CAPACITY;

        public FluidHandlerItemCapability(ItemStack stack, int capacity) {
            this.container = stack;
            CAPACITY = capacity;
            this.tank = new FluidTank(capacity) {
                @Override
                protected void onContentsChanged() {
                    // Save to NBT whenever contents change
                    saveToNBT();
                }
            };

            loadFromNBT();
        }

        private void loadFromNBT() {
            CompoundTag nbt = container.getTag();
            if (nbt != null && nbt.contains("fluid", Tag.TAG_COMPOUND)) {
                tank.readFromNBT(nbt.getCompound("fluid"));
            }
        }

        private void saveToNBT() {
            CompoundTag nbt = container.getOrCreateTag();
            CompoundTag fluidTag = new CompoundTag();
            tank.writeToNBT(fluidTag);
            nbt.put("fluid", fluidTag);
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
                return holder.cast();
            }
            return LazyOptional.empty();
        }

        // IFluidHandlerItem implementation
        @Override
        public @NotNull ItemStack getContainer() {
            return container;
        }

        // IFluidHandler implementation
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return this.tank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return this.tank.fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return this.tank.drain(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return this.tank.drain(maxDrain, action);
        }

        // Clean up when capability is invalidated
        public void invalidate() {
            holder.invalidate();
        }
    }

    // Helper method to get fluid from ItemStack
    public static FluidStack getFluid(ItemStack stack) {
        return FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
    }

    // Helper method to check if cell is empty
    public static boolean isEmpty(ItemStack stack) {
        return getFluid(stack).isEmpty();
    }

    // Helper method to check if cell is full
//    public static boolean isFull(ItemStack stack) {
//        FluidStack fluid = getFluid(stack);
//        return fluid.getAmount() >= CAPACITY;
//    }
//
//    // Helper method to get capacity
//    public static int getCapacity() {
//        return CAPACITY;
//    }

    // Override to show different item appearance based on contents
    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack); // Make filled cells have enchantment glint
    }

    // Make sure cells with different fluids don't stack
    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    /**
     * Handles right-click interaction with blocks for fluid operations
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState blockState = level.getBlockState(pos);

        if (player == null) return InteractionResult.PASS;

        // Try to interact with fluid source blocks first
        InteractionResult fluidBlockResult = handleFluidBlockInteraction(level, pos, blockState, stack, player);
        if (fluidBlockResult != InteractionResult.PASS) {
            return fluidBlockResult;
        }

        // Try to interact with tile entities that have fluid capabilities
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            InteractionResult res = handleTileEntityFluidInteraction(blockEntity, stack, player, context.getClickedFace());
            LogUtils.getLogger().info("We got the interaction result of: '{}' for the FluidCell", res.toString());
            return res;
        }

        return InteractionResult.PASS;
    }

    /**
     * Handles interaction with fluid source blocks (water, lava, etc.)
     */
    private InteractionResult handleFluidBlockInteraction(Level level, BlockPos pos, BlockState blockState,
                                                          ItemStack stack, Player player) {
        // Check if block is a fluid source
        if (blockState.getFluidState().isSource()) {
            Fluid fluid = blockState.getFluidState().getType();

            if (isEmpty(stack)) {
                // Try to fill empty cell from fluid source
                FluidStack fluidStack = new FluidStack(fluid, CAPACITY);

                if (tryFillCell(stack, fluidStack)) {
                    // Remove the fluid source block
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                    if (!level.isClientSide) {
                        // Play fill sound
                        level.playSound(null, pos, getFluidFillSound(fluid),
                                SoundSource.BLOCKS, 1.0F, 1.0F);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else {
                // Try to empty cell into world (place fluid source)
                FluidStack cellFluid = getFluid(stack);
                if (cellFluid.getFluid() == fluid && cellFluid.getAmount() >= 1000) {

                    // Check if we can place fluid here
                    if (level.getBlockState(pos).canBeReplaced() ||
                            level.getBlockState(pos).getFluidState().isEmpty()) {

                        // Place fluid source block
                        level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), 3);

                        // Empty the cell
                        if (tryDrainCell(stack, new FluidStack(fluid, 1000))) {
                            if (!level.isClientSide) {
                                level.playSound(null, pos, getFluidEmptySound(fluid),
                                        SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                    }
                }
            }
        }

        // Handle placing fluid in empty space
        if (blockState.canBeReplaced() && !isEmpty(stack)) {
            FluidStack cellFluid = getFluid(stack);
            if (cellFluid.getAmount() >= 1000) {
                Fluid fluid = cellFluid.getFluid();

                // Place fluid source block
                level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), 3);

                // Empty the cell
                if (tryDrainCell(stack, new FluidStack(fluid, 1000))) {
                    if (!level.isClientSide) {
                        level.playSound(null, pos, getFluidEmptySound(fluid),
                                SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Handles interaction with tile entities that have fluid capabilities
     */
    private InteractionResult handleTileEntityFluidInteraction(BlockEntity blockEntity, ItemStack stack,
                                                               Player player, Direction face) {
        LazyOptional<IFluidHandler> fluidHandlerOpt = blockEntity.getCapability(
                ForgeCapabilities.FLUID_HANDLER, face);

        if (!fluidHandlerOpt.isPresent()) {
            return InteractionResult.PASS;
        }

        IFluidHandler tileFluidHandler = fluidHandlerOpt.orElse(null);
        if (tileFluidHandler == null) return InteractionResult.PASS;

        LazyOptional<IFluidHandlerItem> cellHandlerOpt = stack.getCapability(
                ForgeCapabilities.FLUID_HANDLER_ITEM);

        if (!cellHandlerOpt.isPresent()) {
            return InteractionResult.PASS;
        }

        IFluidHandlerItem cellHandler = cellHandlerOpt.orElse(null);
        if (cellHandler == null) return InteractionResult.PASS;

        boolean success = false;

        if (player.isShiftKeyDown()) {
            // Shift + right-click: try to empty cell into tile entity
            success = tryTransferFluid(cellHandler, tileFluidHandler, true);
        } else {
            // Normal right-click: try to fill cell from tile entity
            success = tryTransferFluid(tileFluidHandler, cellHandler, false);
        }

        if (success && !blockEntity.getLevel().isClientSide) {
            // Play appropriate sound
            FluidStack fluid = getFluid(stack);
            if (!fluid.isEmpty()) {
                SoundEvent sound = player.isShiftKeyDown() ?
                        getFluidEmptySound(fluid.getFluid()) : getFluidFillSound(fluid.getFluid());
                blockEntity.getLevel().playSound(null, blockEntity.getBlockPos(), sound,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            // Mark tile entity as changed
            blockEntity.setChanged();
        }

        return success ? InteractionResult.sidedSuccess(blockEntity.getLevel().isClientSide) :
                InteractionResult.PASS;
    }

    /**
     * Transfers fluid between two fluid handlers
     */
    private boolean tryTransferFluid(IFluidHandler source, IFluidHandler destination, boolean isEmptying) {
        // Find the first non-empty tank in source
        for (int i = 0; i < source.getTanks(); i++) {
            FluidStack sourceFluid = source.getFluidInTank(i);
            if (sourceFluid.isEmpty()) continue;

            // Try to drain from source
            FluidStack drained = source.drain(sourceFluid, IFluidHandler.FluidAction.SIMULATE);
            if (drained.isEmpty()) continue;

            // Try to fill destination
            int filled = destination.fill(drained, IFluidHandler.FluidAction.SIMULATE);
            if (filled <= 0) continue;

            // Perform actual transfer
            FluidStack actualDrained = source.drain(filled, IFluidHandler.FluidAction.EXECUTE);
            if (!actualDrained.isEmpty()) {
                int actualFilled = destination.fill(actualDrained, IFluidHandler.FluidAction.EXECUTE);
                return actualFilled > 0;
            }
        }

        return false;
    }

    public static void SetCellFluid(ItemStack stack, Fluid fluid) {
        LazyOptional<IFluidHandlerItem> handlerOpt = stack.getCapability(
                ForgeCapabilities.FLUID_HANDLER_ITEM);

        if (!handlerOpt.isPresent()) return;

        IFluidHandlerItem handler = handlerOpt.orElse(null);
        if (handler == null) return;

        // Todo: Potentially filling bug if we have variable size fluid containers
        handler.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
    }

    /**
     * Helper method to fill a cell with fluid
     */
    private boolean tryFillCell(ItemStack stack, FluidStack fluid) {
        LazyOptional<IFluidHandlerItem> handlerOpt = stack.getCapability(
                ForgeCapabilities.FLUID_HANDLER_ITEM);

        if (!handlerOpt.isPresent()) return false;

        IFluidHandlerItem handler = handlerOpt.orElse(null);
        if (handler == null) return false;

        int filled = handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
        return filled > 0;
    }

    /**
     * Helper method to drain fluid from a cell
     */
    private boolean tryDrainCell(ItemStack stack, FluidStack fluid) {
        LazyOptional<IFluidHandlerItem> handlerOpt = stack.getCapability(
                ForgeCapabilities.FLUID_HANDLER_ITEM);

        if (!handlerOpt.isPresent()) return false;

        IFluidHandlerItem handler = handlerOpt.orElse(null);
        if (handler == null) return false;

        FluidStack drained = handler.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
        return !drained.isEmpty() && drained.getAmount() >= fluid.getAmount();
    }

    /**
     * Gets appropriate fill sound for fluid type
     */
    private SoundEvent getFluidFillSound(Fluid fluid) {
        if (fluid == Fluids.WATER) {
            return SoundEvents.BUCKET_FILL;
        } else if (fluid == Fluids.LAVA) {
            return SoundEvents.BUCKET_FILL_LAVA;
        }
        // Default to water sound for other fluids
        return SoundEvents.BUCKET_FILL;
    }

    /**
     * Gets appropriate empty sound for fluid type
     */
    private SoundEvent getFluidEmptySound(Fluid fluid) {
        if (fluid == Fluids.WATER) {
            return SoundEvents.BUCKET_EMPTY;
        } else if (fluid == Fluids.LAVA) {
            return SoundEvents.BUCKET_EMPTY_LAVA;
        }
        // Default to water sound for other fluids
        return SoundEvents.BUCKET_EMPTY;
    }

    /**
     * Override to prevent cells from being used as fuel when they contain lava
     */
    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        // Don't allow fluid cells to be used as fuel
        return 0;
    }
}
