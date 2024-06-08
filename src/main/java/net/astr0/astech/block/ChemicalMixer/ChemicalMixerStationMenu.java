package net.astr0.astech.block.ChemicalMixer;

import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.gui.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;


// The menu acts as the logic behind the screen, which utimately performs the screenspace drawing
public class ChemicalMixerStationMenu extends AbstractContainerMenu {
    public final ChemicalMixerStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ChemicalMixerStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public ChemicalMixerStationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.CHEMICAL_MIXER_MENU.get(), pContainerId);

        // An inventory is a container type. I don't know why we would be checking this as I believe
        // it refers to the player inventory. I'm guessing it was probably meant to be data instead,
        // as that would correspond to the dataContainer size of two we created in the block entity
        checkContainerSize(inv, 2);
        blockEntity = ((ChemicalMixerStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Get the item handler capability from our block entity, if it exists,
        // create (menu)Slot objects for it. We map these slots to corresponding item handler indexes
        // The ItemHanlder cap is the ultimate authority on what can go in which index,
        // These are high level helper classes which wrap an IItemHandler
        // This uses a top left origin on the textures
        this.blockEntity.getInputItemHandler().ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 62, 18));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 62, 38));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 62, 58));
        });

        this.blockEntity.getOutputItemHandler().ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 113, 38) {
                @Override

                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(2) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(2);
        int maxProgress = this.data.get(3);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!
    private static final int TE_INVENTORY_INPUT_COUNT = 3;
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_INPUT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.CHEMICAL_MIXER.get());
    }

    public int getEnergy() {
        return this.data.get(1);
    }

    public int getMaxEnergy() {
        return this.data.get(0);
    }

    // Helper functions to add visual slots to display the player inventory.
    // Could easily prank the player by not displaying a random amount of slots
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
