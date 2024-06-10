package net.astr0.astech;

import com.mojang.logging.LogUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilteredItemStackHandler extends ItemStackHandler {

    record BasicFilter(boolean isLocked, String itemName) {

    }

    List<BasicFilter> filters;

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
            filters.add(new BasicFilter(false, ""));
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        // This should be implemented to check against the filter for this slot
        // The simplest comparison is probably with the ResourceLocation.toString()
        return stack.getItem() == Items.STICK;
    }

    public void LockSot(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }
    }

    @Override
    public CompoundTag serializeNBT()
    {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                stacks.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
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
        onLoad();
    }
}
