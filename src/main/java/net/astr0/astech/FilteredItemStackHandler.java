package net.astr0.astech;

import net.astr0.astech.network.IHasStateManager;
import net.astr0.astech.network.IStateManaged;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;


// We are not responsible for managing inventory synchronisation. That gets handled by the container
// menu implementation in Forge. We must sync any additional extraneous state however,
// such as our filter settings
public class FilteredItemStackHandler extends ItemStackHandler implements IStateManaged {

    final BasicFilter[] filters;
    private boolean _networkDirty = false;
    private final IHasStateManager stateManager;


    public FilteredItemStackHandler(IHasStateManager manager, int size) {
        super(size);

        filters = new BasicFilter[size];

        for(int i = 0; i < size; i++) {
            filters[i] = new BasicFilter(false, ItemStack.EMPTY);
        }

        stateManager = manager;
    }

    public void setFilterOnClient(int slot, ItemStack filter) {
        if(slot > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters[slot].Lock(filter);

        //TODO: Can optimise these look ups be telling filtered item handlers what their ID's are
        stateManager.getStateManager().sendClientUpdateByName(getStateName());
    }

    public void clearFilterOnClient(int slot) {
        if(slot > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters[slot].Unlock();

        stateManager.getStateManager().sendClientUpdateByName(getStateName());
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        return filters[slot].TestFilterForMatch(stack);
    }

    public void LockSot(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters[i].Lock(getStackInSlot(i));
    }

    public void UnlockSot(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        filters[i].Unlock();
    }

    public void ToggleSlotLock(int i) {
        if(i > this.getSlots()) {
            throw new IndexOutOfBoundsException("Request lock index is outside the range of this StackHandler");
        }

        if (filters[i].isLocked()) {
            UnlockSot(i);
        } else {
            LockSot(i);

        }
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag filterTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (filters[i].isLocked())
            {
                CompoundTag filterTag = new CompoundTag();
                filterTag.putInt("Slot", i);
                filters[i].GetFilter().save(filterTag);
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
        return filters[i].isLocked();
    }

    public ItemStack getFilterForSlot(int slot) {
        return filters[slot].GetFilter();
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
                filters[slot].Lock(stack);

                //LogUtils.getLogger().info("Loaded slot {} with filter {}", slot, stack);
            }
        }
        onLoad();
    }

    @Override
    public boolean isNetworkDirty() {
        return _networkDirty;
    }

    @Override
    public void writeNetworkEncoding(FriendlyByteBuf buf) {
        writeClientUpdate(buf);
    }

    // ItemHandler is a special case where the server data that needs to be
    // synched to the client is the same as the client to server data (Filters only)
    @Override
    public void readNetworkEncoding(FriendlyByteBuf buf) {
        applyClientUpdate(buf);
    }

    @Override
    // Item Handler stuff is handled by forge and container menus,
    // so to issue an update from the client for this state, we only care about filters
    // TODO: this could be optimised to only send changed slots
    public void writeClientUpdate(FriendlyByteBuf buf) {
        for (BasicFilter filter : filters) {

            boolean lock = filter.isLocked();
            buf.writeBoolean(lock);
            if (lock) {
                // This might not support NBT filtering and will only do basic filters
                // May need to come back to this if noticing bugs with complex ingreients
                buf.writeItemStack(filter.GetFilter(), true);
                //LogUtils.getLogger().info("Wrote {} to flexipacket for slot {}", filters.get(i).GetFilter(), i);
            }
        }
    }

    @Override
    public void applyClientUpdate(FriendlyByteBuf buf) {
        _networkDirty = true; // Only client filter updates can make this dirty

        for (BasicFilter filter : filters) {
            boolean lock = buf.readBoolean();
            filter.setLocked(lock);
            if (lock) {
                filter.Lock(buf.readItem());
            }
        }

    }

    @Override
    public String getStateName() {
        return "SH_FILTERED";
    }

    @Override
    public CompoundTag writeToTag() {
        return serializeNBT();
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        deserializeNBT(tag);
    }
}
