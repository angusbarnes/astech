package net.astr0.astech.item;


import com.mojang.logging.LogUtils;
import net.astr0.astech.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CableLayingToolItem extends Item {
    public CableLayingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CableToolCapabilityProvider(stack);
    }

    public static LazyOptional<IItemHandler> getHandler(ItemStack stack) {
        if (stack.getItem() instanceof CableLayingToolItem) {
            return stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        }
        return LazyOptional.empty();
    }

    private static final int INVENTORY_SIZE = 18;

    private Item filter_item;

    private Block client_render_block = Blocks.GREEN_STAINED_GLASS;

    public BlockState getRenderBlockState() {
        return client_render_block.defaultBlockState();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        BlockPos clickPos = context.getClickedPos();
        BlockPos targetPos = clickPos.relative(context.getClickedFace());

        if (level.isClientSide || player == null) {
            if (player != null && player.isShiftKeyDown()) {
                client_render_block = level.getBlockState(clickPos).getBlock();
            }
            return InteractionResult.SUCCESS;
        }

        if(!player.isCrouching() && player instanceof ServerPlayer serverPlayer) {

            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (id, inv, p) -> new CableToolMenu(id, inv, stack),
                    Component.literal("Cable Tool")
            ), buffer -> {
                buffer.writeItem(stack);
            });

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide()).getResult();
        }

        CompoundTag tag = stack.getOrCreateTag();


        boolean isCableBlock = level.getBlockState(clickPos).getTags().anyMatch(blockTagKey -> blockTagKey.equals(ModTags.BLOCK_CABLE));


        if (!tag.contains("start") && isCableBlock) {
            tag.put("start", NbtUtils.writeBlockPos(targetPos));
            filter_item = level.getBlockState(clickPos).getBlock().asItem();

            player.displayClientMessage(Component.literal("Start point set."), true);
        } else {
            BlockPos start = NbtUtils.readBlockPos(tag.getCompound("start"));
            player.displayClientMessage(Component.literal(String.format("End point set. Ready to lay cable. %s", start.toShortString())), false);
            if (layCables(level, start, targetPos, stack, player)) {
                tag.remove("start");
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {

        CompoundTag tag = stack.getOrCreateTag();
        // Just a dummy method to trigger Forge's NBT sync for item capabilities.
        return tag.contains("start") || super.isFoil(stack);
    }

    private boolean layCables(Level level, BlockPos start, BlockPos end, ItemStack toolStack, Player player) {

        ItemStackHandler inventory = getInventory(toolStack);
        List<BlockPos> path = getLineBetween(start, end);
        int placed = 0;

        for (BlockPos pos : path) {
            if (!level.getBlockState(pos).isAir()) continue;

            ItemStack toPlace = findFirstPlaceableBlock(inventory, level, pos, stack -> stack.getItem() == filter_item);;
            if (!toPlace.isEmpty()) {
                Block block = Block.byItem(toPlace.getItem());
                level.setBlock(pos, block.defaultBlockState(), 3);
                toPlace.shrink(1);
                placed++;
            }
        }

        player.displayClientMessage(Component.literal("Placed " + placed + " cables."), true);

        return true;
    }


    private List<BlockPos> getLineBetween(BlockPos start, BlockPos end) {
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

    private ItemStack findFirstPlaceableBlock(ItemStackHandler handler, Level level, BlockPos pos) {
        return findFirstPlaceableBlock(handler, level, pos, null);
    }

    private ItemStack findFirstPlaceableBlock(ItemStackHandler handler, Level level, BlockPos pos, @Nullable Predicate<ItemStack> filter) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);

            if (stack.isEmpty()) continue;

            boolean matches = filter != null
                    ? filter.test(stack)
                    : Block.byItem(stack.getItem()) != Blocks.AIR;

            if (matches) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStackHandler getInventory(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .map(handler -> {
                    if (handler instanceof ItemStackHandler itemHandler) {
                        return itemHandler;
                    } else {
                        // This shouldn't happen with your current setup, but is safe to log
                        LogUtils.getLogger().warn("Unexpected handler type: {}", handler.getClass().getName());
                        return new ItemStackHandler(INVENTORY_SIZE);
                    }
                })
                .orElseGet(() -> {
                    LogUtils.getLogger().warn("No ITEM_HANDLER capability found for: {}", stack);
                    return new ItemStackHandler(INVENTORY_SIZE); // Fallback
                });
    }

    public static void saveInventory(ItemStack stack, ItemStackHandler handler) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("inventory", handler.serializeNBT());
    }

    // Optional: open GUI on right-click in air
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (id, inv, p) -> new CableToolMenu(id, inv, stack),
                    Component.literal("Cable Tool")
            ), buffer -> {
                buffer.writeItem(stack);
            });
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public class CableToolCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final ItemStackHandler handler = new ItemStackHandler(INVENTORY_SIZE); // 9-slot internal inventory
        private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

        public CableToolCapabilityProvider(ItemStack stack) {}



        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == ForgeCapabilities.ITEM_HANDLER ? optional.cast() : LazyOptional.empty();
        }


        @Override
        public CompoundTag serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.deserializeNBT(nbt);
        }
    }
}

