package net.astr0.astech;

import com.mojang.logging.LogUtils;
import net.astr0.astech.network.FlexiPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilteredItemStackHandler extends ItemStackHandler {

    final List<BasicFilter> filters;

    public FilteredItemStackHandler(int size) {
        super(size);

        filters = new ArrayList<>(size);

        for(int i = 0; i < size; i++) {
            // We must load the correct values into each filter when we read the NBT
            // Each one will contain a bool, if the bool is true, then we also read
            // a string the represents the filter.

            // Filters should only exist on the server?????
            // I think we should be allowed to send the slot index over the network
            // to lock the underling itemhandler.
            // If the fields only exist correctly on the server then we might see some strange behaviour
            // when clicking items into slots (See if maybe they rebound??
            // It will be safest to sync filter updates back to tracking clients after applying the lock

            // Visually, the UI can check with the itemHandler to see if this slot has a lock
            // THIS WILL ACTUALLY REQUIRE CORRECT DATA SYNC ANYWAY
            // This may require some small reworks of the UI code to create some high level
            // helpers/abstract API to simplify the filtering state
            // Eventually we would like to also be able to set filters from JEI, which seems plausible
            // using some kind of GhostTransferHandler or something
            filters.add(new BasicFilter(false, ItemStack.EMPTY));

            LogUtils.getLogger().info("New FilteredItemStackHandler created on thread: {}", Thread.currentThread().getId());
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        return filters.get(slot).TestFilterForMatch(stack);
    }

    public void LockSot(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters.get(i).Lock(getStackInSlot(i));
    }

    public void UnlockSot(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters.get(i).Unlock();
    }

    public void ToggleSlotLock(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        if (filters.get(i).isLocked()) {
            UnlockSot(i);
        } else {
            LockSot(i);

        }
    }

    @Override
    public CompoundTag serializeNBT()
    {
        ListTag filterTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (filters.get(i).isLocked())
            {
                CompoundTag filterTag = new CompoundTag();
                filterTag.putInt("Slot", i);
                filters.get(i).GetFilter().save(filterTag);
                filterTagList.add(filterTag);
            }
        }

        ListTag itemTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                stacks.get(i).save(itemTag);
                itemTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", itemTagList);
        nbt.put("Filters", filterTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
    }

    public boolean checkSlot(int i) {
        return filters.get(i).isLocked();
    }

    public ItemStack getFilterForSlot(int slot) {
        return filters.get(slot).GetFilter();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, ItemStack.of(itemTags));
            }
        }

        ListTag filterTagList = nbt.getList("Filters", Tag.TAG_COMPOUND);
        for (int i = 0; i < filterTagList.size(); i++)
        {
            CompoundTag itemTags = filterTagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                ItemStack stack = ItemStack.of(itemTags);
                filters.get(slot).Lock(stack);

                LogUtils.getLogger().info("Loaded slot {} with filter {}", slot, stack);
            }
        }
        onLoad();
    }

    public void WriteToFlexiPacket(FlexiPacket packet) {
        for(int i = 0; i < filters.size(); i++) {

            boolean lock = filters.get(i).isLocked();
            packet.writeBool(lock);
            if(lock) {
                packet.writeItemStack(filters.get(i).GetFilter());
                LogUtils.getLogger().info("Wrote {} to flexipacket for slot {}", filters.get(i).GetFilter(), i);
            }
        }
    }

    public void ReadFromFlexiPacket(FlexiPacket packet) {
        for(int i = 0; i < filters.size(); i++) {

            boolean lock = packet.readBool();
            filters.get(i).setLocked(lock);
            if(lock) {
                filters.get(i).Lock(packet.readItemStack());
                LogUtils.getLogger().info("Read {} from flexipacket for slot {}", filters.get(i).GetFilter(), i);
            }
        }
    }
}
