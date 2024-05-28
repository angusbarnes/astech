package net.astr0.astech.block;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerStationBlock;
import net.astr0.astech.item.ModItems;
import net.astr0.astech.block.GemPolisher.GemPolishingStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    // This is the global instance of the block registry
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AsTech.MODID);

    // Call this function from the entry point to allow the blocks register to link itself to the eventBus
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static RegistryObject<LiquidBlock> registerFluidBlock(String fluidName, RegistryObject<FlowingFluid> source) {
        return BLOCKS.register(String.format("%s_block", fluidName),
                () -> new LiquidBlock(source, BlockBehaviour.Properties.copy(Blocks.WATER)));
    }

    public static final RegistryObject<Block> NIC_BLOCK = registerBlock("nic_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> TROPHY = registerBlock("trophy",
            () -> new TrophyBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion()));


    public static final RegistryObject<Block> GEM_POLISHING_STATION = registerBlock("gem_polishing_station",
            () -> new GemPolishingStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> CHEMICAL_MIXER = registerBlock("chemical_mixer",
            () -> new ChemicalMixerStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

}
