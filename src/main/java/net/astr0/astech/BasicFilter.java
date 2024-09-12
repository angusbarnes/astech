package net.astr0.astech;

import net.minecraft.world.item.ItemStack;

public class BasicFilter {
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    private boolean isLocked;
    private ItemStack filterItemStack;

    public BasicFilter(boolean isLocked, ItemStack hash) {
        this.isLocked = isLocked;
        this.filterItemStack = hash;
    }

    public void Lock(ItemStack stack) {
        isLocked = true;
        filterItemStack = stack.copy();
        //LogUtils.getLogger().info("Filter set as {}", filterItemStack);
    }

    public void Unlock() {
        isLocked = false;
        filterItemStack = ItemStack.EMPTY;
    }


    public ItemStack GetFilter() {
        return filterItemStack;
    }

    public boolean TestFilterForMatch(ItemStack itemStack) {

        if(!this.isLocked) {
            return true;
        } else {
            return filterItemStack.is(itemStack.getItem());
        }
    }
}
