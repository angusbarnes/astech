package net.astr0.astech.block;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.AdvancedAssembler.AdvancedAssemblerBlock;
import net.astr0.astech.block.Assembler.AssemblerBlock;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerBlock;
import net.astr0.astech.block.ElectrolyticSeperator.ElectrolyticSeperatorBlock;
import net.astr0.astech.block.GemPolisher.GemPolishingStationBlock;
import net.astr0.astech.block.ReactionChamber.ChemicalReactorBlock;
import net.astr0.astech.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
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
        return BLOCKS.register(String.format("%s_fluid_block", fluidName),
                () -> new LiquidBlock(source, BlockBehaviour.Properties.copy(Blocks.WATER)));
    }

    public static final RegistryObject<Block> NIC_BLOCK = registerBlock("nic_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> TROPHY = registerBlock("trophy",
            () -> new TrophyBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion()));


    public static final RegistryObject<Block> GEM_POLISHING_STATION = registerBlock("gem_polishing_station",
            () -> new GemPolishingStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> CHEMICAL_MIXER = registerBlock("chemical_mixer",
            () -> new ChemicalMixerBlock(ModBlocks.MACHINE_DEFAULT_PROPS));

    public static final RegistryObject<Block> ASSEMBLER = registerBlock("assembler",
            () -> new AssemblerBlock(ModBlocks.MACHINE_DEFAULT_PROPS));

    public static final RegistryObject<Block> ADVANCED_ASSEMBLER = registerBlock("advanced_assembler",
            () -> new AdvancedAssemblerBlock(ModBlocks.MACHINE_DEFAULT_PROPS));

    public static final RegistryObject<Block> CHEMICAL_REACTOR = registerBlock("chemical_reactor",
            () -> new ChemicalReactorBlock(ModBlocks.MACHINE_DEFAULT_PROPS));

    public static final RegistryObject<Block> ELECTROLYTIC_SEPERATOR = registerBlock("electrolytic_seperator",
            () -> new ElectrolyticSeperatorBlock(ModBlocks.MACHINE_DEFAULT_PROPS));


    public static BlockBehaviour.Properties MACHINE_DEFAULT_PROPS = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
            .noOcclusion()
            .sound(SoundType.METAL)
            .strength(2, 12)
            .emissiveRendering((a, b, c) -> true)
            .lightLevel((state) -> state.getValue(ChemicalMixerBlock.ACTIVE) ? 9 : 0);

    //#anchor BLOCK_REGION
public static final RegistryObject<Block> PISS_WATER_BLOCK = registerBlock("piss_water_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> AMMONIA_BLOCK = registerBlock("ammonia_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> BENZENE_BLOCK = registerBlock("benzene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> CHLORINE_BLOCK = registerBlock("chlorine_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> CHLORINE_ORE = registerBlock("chlorine_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_CHLORINE_ORE = registerBlock("deepslate_chlorine_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> METHANOL_BLOCK = registerBlock("methanol_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> NITROGEN_BLOCK = registerBlock("nitrogen_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> NITROGEN_ORE = registerBlock("nitrogen_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_NITROGEN_ORE = registerBlock("deepslate_nitrogen_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> TOLUENE_BLOCK = registerBlock("toluene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> ETHYLENE_GLYCOL_BLOCK = registerBlock("ethylene_glycol_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> ANILINE_BLOCK = registerBlock("aniline_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> SODIUM_HYPOCHLORITE_BLOCK = registerBlock("sodium_hypochlorite_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> HYDROGEN_SULFIDE_BLOCK = registerBlock("hydrogen_sulfide_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> XYLENE_BLOCK = registerBlock("xylene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> BROMINE_BLOCK = registerBlock("bromine_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> BROMINE_ORE = registerBlock("bromine_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_BROMINE_ORE = registerBlock("deepslate_bromine_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> SODIUM_BICARBONATE_BLOCK = registerBlock("sodium_bicarbonate_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> HEXAFLUOROPROPYLENE_BLOCK = registerBlock("hexafluoropropylene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> STYRENE_BLOCK = registerBlock("styrene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> CYANOGEN_BLOCK = registerBlock("cyanogen_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> METHYL_ETHYL_KETONE_BLOCK = registerBlock("methyl_ethyl_ketone_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> AZOTH_BLOCK = registerBlock("azoth_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> DILITHIUM_BLOCK = registerBlock("dilithium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> ADAMANTIUM_BLOCK = registerBlock("adamantium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> ADAMANTIUM_ORE = registerBlock("adamantium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_ADAMANTIUM_ORE = registerBlock("deepslate_adamantium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> CARBONADIUM_BLOCK = registerBlock("carbonadium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> CARBONADIUM_ORE = registerBlock("carbonadium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_CARBONADIUM_ORE = registerBlock("deepslate_carbonadium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> RADON_BLOCK = registerBlock("radon_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> RADON_ORE = registerBlock("radon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_RADON_ORE = registerBlock("deepslate_radon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> NEON_BLOCK = registerBlock("neon_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> NEON_ORE = registerBlock("neon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_NEON_ORE = registerBlock("deepslate_neon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> XENON_BLOCK = registerBlock("xenon_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> XENON_ORE = registerBlock("xenon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_XENON_ORE = registerBlock("deepslate_xenon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> POLYVINYL_CHLORIDE_BLOCK = registerBlock("polyvinyl_chloride_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> TRICHLOROMETHANE_BLOCK = registerBlock("trichloromethane_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> STYRENE_BUTADIENE_BLOCK = registerBlock("styrene_butadiene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> FLUORINE_BLOCK = registerBlock("fluorine_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> STIPNICIUM_BLOCK = registerBlock("stipnicium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> STIPNICIUM_ORE = registerBlock("stipnicium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_STIPNICIUM_ORE = registerBlock("deepslate_stipnicium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> BIOLUMINESCENT_COON_JUICE_BLOCK = registerBlock("bioluminescent_coon_juice_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> GELID_CRYOTHEUM_BLOCK = registerBlock("gelid_cryotheum_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> SODIUM_BLOCK = registerBlock("sodium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> SODIUM_ORE = registerBlock("sodium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_SODIUM_ORE = registerBlock("deepslate_sodium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> POTASSIUM_BLOCK = registerBlock("potassium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> POTASSIUM_ORE = registerBlock("potassium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_POTASSIUM_ORE = registerBlock("deepslate_potassium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> HELIUM_BLOCK = registerBlock("helium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> HELIUM_ORE = registerBlock("helium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_HELIUM_ORE = registerBlock("deepslate_helium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> KRYPTON_BLOCK = registerBlock("krypton_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> KRYPTON_ORE = registerBlock("krypton_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_KRYPTON_ORE = registerBlock("deepslate_krypton_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> GHASTLY_LIQUID_BLOCK = registerBlock("ghastly_liquid_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> FENTANYL_BLOCK = registerBlock("fentanyl_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> GRAPHENE_BLOCK = registerBlock("graphene_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> AEROGEL_BLOCK = registerBlock("aerogel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> HIGH_ENTROPY_ALLOY_BLOCK = registerBlock("high_entropy_alloy_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> IUMIUM_BLOCK = registerBlock("iumium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> IUMIUM_ORE = registerBlock("iumium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_IUMIUM_ORE = registerBlock("deepslate_iumium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> NEUTRONIUM_BLOCK = registerBlock("neutronium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> SODIUM_SULFATE_BLOCK = registerBlock("sodium_sulfate_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> BAUXITE_BLOCK = registerBlock("bauxite_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> BAUXITE_ORE = registerBlock("bauxite_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_BAUXITE_ORE = registerBlock("deepslate_bauxite_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> URANIUM_OXIDE_BLOCK = registerBlock("uranium_oxide_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> URANIUM_OXIDE_ORE = registerBlock("uranium_oxide_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_URANIUM_OXIDE_ORE = registerBlock("deepslate_uranium_oxide_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> COBALT_BLOCK = registerBlock("cobalt_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
public static final RegistryObject<Block> COBALT_ORE = registerBlock("cobalt_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> DEEPSLATE_COBALT_ORE = registerBlock("deepslate_cobalt_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));
public static final RegistryObject<Block> METHANIUM_BLOCK = registerBlock("methanium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    //#end BLOCK_REGION
}
