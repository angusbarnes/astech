package net.astr0.astrocraft.block;

import net.astr0.astrocraft.IConfigurable;
import net.astr0.astrocraft.TabletSummary;
import net.astr0.astrocraft.block.multiblock.MultiblockMatcher;
import net.astr0.astrocraft.block.multiblock.MultiblockMatchers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public class VacuumFreezerController extends AbstractMultiblockControllerBlockEntity implements IConfigurable, ITickableBlockEntity {

    public VacuumFreezerController(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VACUUM_FREEZER.get(), pos, state);
    }

    @Override
    protected Map<BlockPos, MultiblockMatcher> getStructurePattern() {
        List<String[]> layers = List.of(
                new String[]{
                        "III", // y = -1
                        "III",
                        "III"
                },
                new String[]{ // y = 0 — Controller Layer
                        "III",
                        "III",
                        "ICI"
                },
                new String[]{ // y = +1
                        "III", // y = +1
                        "III",
                        "III"
                }
        );

        Map<Character, MultiblockMatcher> matcherMap = Map.of(
                'I', MultiblockMatchers.casingOrHatch(Blocks.IRON_BLOCK),
                'C', MultiblockMatchers.ofBlock(ModBlocks.VACUUM_FREEZER_CONTROLLER.get())
        );

        return parseStructure(layers, matcherMap);
    }

    @Override
    public Component getTabletSummary() {

        TabletSummary summary = new TabletSummary("Vacuum Freezer");
        summary.addField("Has Formed: ", validateMultiblock());
        summary.addField("Inputs: ", validationContext.getInputHatchPositions().size());
        summary.addField("Outputs: ", validationContext.getOutputHatchPositions().size());
        summary.addField("Has Formed", validateMultiblock());
        summary.addField("Has Formed", validateMultiblock());
        return summary.getAsComponent();
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

    }
}
