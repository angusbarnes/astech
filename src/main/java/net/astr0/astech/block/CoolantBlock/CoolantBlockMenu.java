package net.astr0.astech.block.CoolantBlock;

import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.gui.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;


// The menu acts as the logic behind the screen, which utimately performs the screenspace drawing
public class CoolantBlockMenu extends AbstractContainerMenu {
    public final CoolantBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public CoolantBlockMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public CoolantBlockMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.COOLANT_BLOCK_MENU.get(), pContainerId);

        // An inventory is a container type. I don't know why we would be checking this as I believe
        // it refers to the player inventory. I'm guessing it was probably meant to be data instead,
        // as that would correspond to the dataContainer size of two we created in the block entity
        checkContainerSize(inv, 1);
        blockEntity = ((CoolantBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    public int getTemp() { return  data.get(0);}


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.COOLANT_BLOCK.get());
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
