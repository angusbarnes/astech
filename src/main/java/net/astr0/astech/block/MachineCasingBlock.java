package net.astr0.astech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCasingBlock extends Block {

    protected AbstractMultiblockControllerBlockEntity controllerBlock = null;

    public MachineCasingBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        destroyNotifyController();
    }

    @Override
    public void wasExploded(Level pLevel, BlockPos pPos, Explosion pExplosion) {
        destroyNotifyController();
    }

    private void destroyNotifyController() {
        if (controllerBlock != null) {
            controllerBlock.invalidateMultiblock();
        }
    }

    public void setController(AbstractMultiblockControllerBlockEntity pControllerBlock) {
        controllerBlock = pControllerBlock;
    }
}
