package net.astr0.astrocraft.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/*
    This is stolen from PneumaticCraft. It exists as the base recipe type for all our mod recipes
    so they meet the vanilla requirements. We will not be using most of these, we simply need good
    load from JSON and sync over network. We will loop through and manually match new recipes.

    If a matching recipe is found, it should be cached and tested against until it fails.
 */
public abstract class AsTechRecipeBase implements Recipe<AsTechRecipeBase.DummyIInventory> {
    private final ResourceLocation id;

    protected AsTechRecipeBase(ResourceLocation id) {
        this.id = id;
    }

    /**
     * Writes this recipe to a PacketBuffer.
     *
     * @param buffer The buffer to write to.
     */
    public abstract void write(FriendlyByteBuf buffer);

    @Override
    public boolean matches(DummyIInventory inv, Level worldIn) {
        return true;
    }

    @Override
    public ItemStack assemble(DummyIInventory inv, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * Just to keep vanilla happy...
     */
    public static class DummyIInventory implements Container {
        private static final DummyIInventory INSTANCE = new DummyIInventory();

        public static DummyIInventory getInstance() {
            return INSTANCE;
        }

        @Override
        public int getContainerSize() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ItemStack getItem(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int index, int count) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int index, ItemStack stack) {
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(Player player) {
            return false;
        }

        @Override
        public void clearContent() {
        }
    }
}
