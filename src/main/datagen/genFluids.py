from PIL import Image, ImageEnhance, ImageOps
from read_csv import get_chemical_defs
import numpy as np
import hashlib
from TextureUtils import *
from context import Context
import textwrap
import json
import os

def JSON(obj):
    return json.dumps(obj, ensure_ascii=False)

def UNCOMPACTING_RECIPE(from_item, to_item):
    return JSON(
        {
            "type": "minecraft:crafting_shapeless",
            "category": "misc",
            "ingredients": [
                {
                "item": from_item
                }
            ],
            "result": {
                "count": 9,
                "item": to_item
            }
        }
    )

def PLACED_ORE_FEATURE(ore_name, max_veins_per_chunk = 4):
    return JSON(
        {
            "feature": f"astech:{ore_name}_ore",
            "placement": [
                {
                    "type": "minecraft:count",
                    "count": max_veins_per_chunk
                },
                {
                    "type": "minecraft:in_square"
                },
                {
                    "height": {
                    "min_inclusive": {
                        "absolute": -64
                    },
                    "max_inclusive": {
                        "absolute": 36
                    },
                    "type": "minecraft:trapezoid"
                    },
                    "type": "minecraft:height_range"
                },
                {
                    "type": "minecraft:biome"
                }
            ]
        }
    )

def CONFIGURED_ORE_FEATURE(ore_name, max_vein_size = 9):
    return JSON(
        {
            "type": "minecraft:ore",
            "config": {
                "size": max_vein_size,
                "discard_chance_on_air_exposure": 0,
                "targets": [
                    {
                        "target": {
                            "predicate_type": "minecraft:tag_match",
                            "tag": "minecraft:stone_ore_replaceables"
                        },
                        "state": {
                            "Name": f"astech:{ore_name}_ore"
                        }
                    },
                    {
                        "target": {
                            "predicate_type": "minecraft:tag_match",
                            "tag": "minecraft:deepslate_ore_replaceables"
                        },
                        "state": {
                            "Name": f"astech:deepslate_{ore_name}_ore"
                        }
                    }
                ]
            }
        }
    )

def FORGE_BIOME_MODIFIER(ore_name):
    return JSON(
        {
            "type": "forge:add_features",
            "biomes": "#minecraft:is_overworld",
            "features": f"astech:ore_{ore_name}_placed",
            "step": "underground_ores"
        }
    )

def merge_json_file(json_file_path, new_data):
    # Load existing data from the JSON file if it exists
    if os.path.exists(json_file_path):
        with open(json_file_path, 'r', encoding='utf-8') as json_file:
            try:
                existing_data = json.load(json_file)
            except json.JSONDecodeError:
                existing_data = {}
    else:
        existing_data = {}

    # Merge the existing data with the new data
    merged_data = {**existing_data, **new_data}

    # Save the merged data back to the JSON file
    with open(json_file_path, 'w', encoding='utf-8') as json_file:
        json.dump(merged_data, json_file,ensure_ascii=False, indent=4)

file_path = '../java/net/astr0/astech/Fluid/ModFluids.java'  # Replace with your file path
base_image_path = './templates/bucket_base_layer.png'  # Replace with your base image file path
top_image_path = './templates/bucket_fluid_layer.png'   # Replace with your top image file path

ingot_textures = ['./templates/ingot1.png', './templates/ingot2.png', './templates/ingot3.png', './templates/ingot4.png']
dust_textures = ['./templates/dust1.png', './templates/dust2.png', './templates/dust3.png', './templates/dust4.png', './templates/dust5.png', './templates/dust6.png']
plate_textures = ['./templates/plate1.png', './templates/plate2.png']
rod_textures = ['./templates/rod1.png', './templates/rod2.png']
nugget_textures = ['./templates/nugget1.png', './templates/nugget2.png']
raw_ore_textures = ['./templates/raw_ore1.png', './templates/raw_ore2.png', './templates/raw_ore3.png', './templates/raw_ore4.png', './templates/raw_ore5.png', './templates/raw_ore6.png', './templates/raw_ore7.png', './templates/raw_ore8.png']
ore_textures = ['./templates/ore1.png', './templates/ore2.png', './templates/ore3.png', './templates/ore4.png', './templates/ore5.png', './templates/ore6.png', './templates/ore7.png', './templates/ore8.png']
block_textures = ['./templates/block1.png', './templates/block2.png', './templates/block3.png', './templates/block4.png', './templates/block5.png', './templates/block6.png', './templates/block7.png']
gear_textures = ['./templates/gear.png', './templates/gear2.png', './templates/gear2.png']

SHARD_TEXTURE = './templates/shard.png'
SHARD_TEXTURE_OVERLAY = './templates/shard_overlay.png'
CLUMP_TEXTURE = './templates/clump.png'
CLUMP_TEXTURE_OVERLAY = './templates/clump_overlay.png'
DIRTY_DUST_TEXTURE = './templates/dirty_dust.png'
DIRTY_DUST_TEXTURE_OVERLAY = './templates/dirty_dust_overlay.png'
CRYSTAL_TEXTURE = './templates/crystal.png'
CRYSTAL_TEXTURE_OVERLAY = './templates/crystal_overlay.png'

def get_texture(filename, textures):
    # Step 1: Hash the filename using a hash function (e.g., SHA-256)
    hash_object = hashlib.sha256(filename.encode())
    hash_value = int(hash_object.hexdigest(), 16)
    
    # Step 2: Map the hash value to one of three textures
    texture_index = hash_value % len(textures)
    
    return textures[texture_index]

def add_simple_tint_item(ctx: Context, item_id: str, item_name, tint, template_file, material_name):

    TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
    ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
    FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java' 

    template = Image.open(template_file)
    texture = blend_overlay(template, hex_to_rgb(tint))
    texture.save(f'../resources/assets/astech/textures/item/{item_id}.png', format='PNG')
    ctx.add_translation(f"item.astech.{item_id}", f"{item_name}")
    ctx.add_text_to_region(ITEM_FILE, 'MATERIAL_REGION', f"""public static final RegistryObject<AsTechMaterialItem> {item_id.upper()} = ITEMS.register("{item_id}", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{material_name}"));""")
    ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModItems.{item_id.upper()}.get());")
    ctx.add_simple_item_model(f'{item_id}')

def add_mek_tint_item(ctx: Context, item_id: str, item_name, tint, template_file, overlay, material_name):

    TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
    ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
    FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java' 

    layer_images_but_backwards(template_file, overlay, f'../resources/assets/astech/textures/item/{item_id}.png', hex_to_rgb(tint))

    ctx.add_translation(f"item.astech.{item_id}", f"{item_name}")
    ctx.add_text_to_region(ITEM_FILE, 'MATERIAL_REGION', f"""public static final RegistryObject<AsTechMaterialItem> {item_id.upper()} = ITEMS.register("{item_id}", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{material_name}"));""")
    ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModItems.{item_id.upper()}.get());")
    ctx.add_simple_item_model(f'{item_id}')

def add_static_asset_item(ctx: Context, item_id: str, item_name):

    TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
    ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
    FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java'
    ctx.add_translation(f"item.astech.{item_id}", f"{item_name}")
    ctx.add_text_to_region(ITEM_FILE, 'MATERIAL_REGION', f"""public static final RegistryObject<Item> {item_id.upper()} = SimpleIngredientItem("{item_id}", 64);""")
    ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModItems.{item_id.upper()}.get());")
    ctx.add_simple_item_model(f'{item_id}')

def add_simple_tint_block(ctx: Context, block_id: str, block_name, tint, template_file):
    TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
    BLOCK_FILE = '../java/net/astr0/astech/block/ModBlocks.java'

    template = Image.open(template_file)
    texture = blend_overlay(template, hex_to_rgb(tint))
    texture.save(f'../resources/assets/astech/textures/block/{block_id}.png', format='PNG')

    ctx.add_translation(f"block.astech.{block_id}", f"{block_name}")
    ctx.add_text_to_region(BLOCK_FILE, 'BLOCK_REGION', f"""public static final RegistryObject<Block> {block_id.upper()} = registerBlock("{block_id}", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));""")
    ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModBlocks.{block_id.upper()}.get());")
    ctx.add_simple_block_model(f'{block_id}')
    ctx.add_block_item_model(f'{block_id}')


def add_simple_ore_block(ctx: Context, block_id: str, block_name, tint, template_file):
    TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
    BLOCK_FILE = '../java/net/astr0/astech/block/ModBlocks.java'

    STONE_BASE = './templates/deepslate.png' if "deepslate" in block_id else './templates/stone.png'

    layer_images(STONE_BASE, template_file, f'../resources/assets/astech/textures/block/{block_id}.png', hex_to_rgb(tint), 'Liquid')

    ctx.add_translation(f"block.astech.{block_id}", f"{block_name}")
    ctx.add_text_to_region(BLOCK_FILE, 'BLOCK_REGION', f"""public static final RegistryObject<Block> {block_id.upper()} = registerBlock("{block_id}", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(3, 7)));""")
    ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModBlocks.{block_id.upper()}.get());")
    ctx.add_simple_block_model(f'{block_id}')
    ctx.add_block_item_model(f'{block_id}')

headers, chemicals = get_chemical_defs("chems.csv")
datapack = Context('../resources/', 'astech')

datapack.set_base_dictionary({
    "item.astech.deez_nuts": "Deez Nuts",
    "item.astech.deez_butts": "Deez Butts",
    "item.astech.zeolite_catalyst": "Zeolite Catalyst",
    "item.astech.monocrystalline_silicon": "Monocrystalline Silicon Boole",
    "item.astech.god_forged_ingot": "God Forged Ingot",
    "creativetab.astech_tab": "AsTech Items",
    "block.astech.nic_block": "§cNic Block",
    "block.astech.cleanroom_wall": "Cleanroom Wall",
    "block.astech.coolant_block": "Coolant Block",
    "block.astech.gem_polishing_station": "Gem Polishing Station",
    "block.astech.chemical_mixer": "Chemical Mixer",
    "block.astech.assembler": "Assembler",
    "block.astech.advanced_assembler": "Advanced Assembler",
    "block.astech.chemical_reactor": "Chemical Reactor",
    "block.astech.electrolytic_seperator": "Electrolytic Seperator",
    "block.astech.pyrolysis_chamber": "Pyrolysis Chamber",
    "block.astech.euv_machine": "EUV Machine",
    "item.astech.as_an_llm_disc": "Music Disc",
    "item.astech.as_an_llm_disc.desc": "As a Large Language Model - Ol' Yodel",
        "item.astech.run_nic_run_disc": "Music Disc",
    "item.astech.run_nic_run_disc.desc": "STIP NIC! - Grunge Parade",
        "item.astech.help_of_disc": "Music Disc",
    "item.astech.help_of_disc.desc": "Help My Mum - w$tPantyParty",
        "item.astech.stolen_identity_disc": "Music Disc",
    "item.astech.stolen_identity_disc.desc": "Stolen I.D",
            "item.astech.bangarang_disc": "Music Disc",
    "item.astech.bangarang_disc.desc": "Bangarang - Skrillex",
})

static_items = [
    'backwards_ingot',
    'unrefined_substrate', 
    'digital_circuit_board',
    'electronic_circuit_board', 
    'creative_flight_core', 
    'quantum_circuit', 
    'beating_heart',
    'endergem',
    'codex',
    'aerogel',
    'dynamite',
    'diode', 
    'neutron_nugget', 
    'genetic_material_c', 
    'record_fragment', 
    'neutron_gear', 
    'and_gate', 
    'memory_management_unit', 
    'mutated_genetic_material', 
    'digital_circuit',
    'unstable_mutex',
    'genetic_sample',
    'electronic_circuit', 
    'qubit', 
    'singularity',
    'or_gate', 
    'neutron_pile', 
    'analog_circuit_board', 
    'steel_upgrade', 
    'transistor', 
    'antimatter_tormentum',
    'photonic_flux_capacitor',
    'abyssium_ingot',
    'ultradense_metal_ball', 
    'analog_circuit', 
    'genetic_material_a', 
    'capacitor_array', 
    'refined_substrate', 
    'infinity_totem', 
    'infinity_catalyst', 
    'cosmic_stew', 
    'endest_pearl',
    'stellar_catalyst',
    'arithmetic_logic_unit',
    'mosfet', 
    'infinity_ingot', 
    'processing_unit_board', 
    'infinity_nugget', 
    'cooling_cell', 
    'random_access_memory', 
    'combined_bucket', 
    'vial', 
    'matter_cluster',
    'multi_biome_mining_lens', 
    'rubber_sheet', 
    'inductor', 
    'silicon_wafer', 
    'quantum_upgrade', 
    'capacitor', 
    'diamond_lattice', 
    'redstone_control_module_high', 
    'processing_unit', 
    'god_forged_ingot', 
    'op_amp', 
    'beaker', 
    'neutron_ingot', 
    'hyper_dimensional_data_transceiver', 
    'resistor', 
    'interplanetary_mining_lens', 
    'not_gate', 
    'crystal_matrix_ingot', 
    'genetic_material_b', 
    'wrench', 
    'small_heat_exchanger', 
    'cosmic_meatballs', 
    'quantum_circuit_board',
    'blank_record'
]


for item in sorted(static_items):
    add_static_asset_item(datapack, item, item.replace('_', ' ').title())


earth_ores = [
    'chlorine',
    'nitrogen',
    'sodium',
    'potassium'
]

for ore in earth_ores:
    placed_feature = PLACED_ORE_FEATURE(ore)
    configured_feature = CONFIGURED_ORE_FEATURE(ore)
    biome_modifier = FORGE_BIOME_MODIFIER(ore)

    with open(f'./resources/data/astech/worldgen/placed_feature/ore_{ore}_placed.json', 'w') as placed_feature_file:
        placed_feature_file.write(placed_feature)

    with open(f'./resources/data/astech/forge/biome_modifier/astech/{ore}_ore.json', 'w') as modifier_file:
        modifier_file.write(biome_modifier)

    with open(f'./resources/data/astech/worldgen/configured_feature/{ore}_ore.json', 'w') as configured_feature_file:
        configured_feature_file.write(configured_feature)

datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_a')
datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_b')
datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_c')
datapack.add_item_tag('forge:genetic_material', 'astech:mutated_genetic_material')
datapack.add_fluid_tag('forge:flux_resin', 'astech:soldering_flux')
datapack.add_fluid_tag('forge:flux_resin', 'astech:ammonium_chloride')
datapack.add_fluid_tag('forge:alcohol', '#forge:ethanol')
#datapack.add_fluid_tag('forge:tier_1_photoresist', '#forge:phenolic_acid')
datapack.add_fluid_tag('forge:tier_2_photoresist', 'astech:polymethyl_methacrylate')
#datapack.add_fluid_tag('forge:photoresist', '#forge:tier_1_photoresist')
datapack.add_fluid_tag('forge:photoresist', '#forge:tier_2_photoresist')

datapack.add_fluid_tag('forge:tier_1_coolant', 'astech:methyl_chloride')
datapack.add_fluid_tag('forge:tier_2_coolant', 'astech:gelid_cryotheum')


datapack.add_item_tag('forge:chemical_protection', 'minecraft:leather_helmet')
datapack.add_item_tag('forge:chemical_protection', 'minecraft:leather_chestplate')
datapack.add_item_tag('forge:chemical_protection', 'minecraft:leather_leggings')
datapack.add_item_tag('forge:chemical_protection', 'minecraft:leather_boots')

datapack.add_item_tag('minecraft:music_discs', 'astech:help_of_disc')
datapack.add_item_tag('minecraft:music_discs', 'astech:as_an_llm_disc')
datapack.add_item_tag('minecraft:music_discs', 'astech:run_nic_run_disc')
datapack.add_item_tag('minecraft:music_discs', 'astech:stolen_identity_disc')
datapack.add_item_tag('minecraft:music_discs', 'astech:bangarang_disc')

datapack.add_simple_item_model('help_of_disc')
datapack.add_simple_item_model('bangarang_disc')
datapack.add_simple_item_model('as_an_llm_disc')
datapack.add_simple_item_model('run_nic_run_disc')
datapack.add_simple_item_model('stolen_identity_disc')
datapack.add_simple_item_model('zeolite_catalyst')
datapack.add_simple_item_model('monocrystalline_silicon')

TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java' 
SLURRY_FILE = '../java/net/astr0/astech/compat/mek/AsTechSlurries.java'
JEI_FILE = "../java/net/astr0/astech/compat/JEI/AsTechJEIPlugin.java"

for chemdef in chemicals:
    plain_text_name = chemdef['Name']
    fluid_name = chemdef['MATERIAL_ID']

    hazard_types = {
        'Asphyxiant' : 'HazardBehavior.BehaviorType.SUFFOCATE',
        'Explosive' : 'HazardBehavior.BehaviorType.EXPLOSION',
        'Radioactive' : 'HazardBehavior.BehaviorType.RADIO',
        'Freezing' : 'HazardBehavior.BehaviorType.FREEZE',
        'Hot' : 'HazardBehavior.BehaviorType.HEAT',
        'EXTREME' : 'HazardBehavior.BehaviorType.EXTREME'
    }

    hazard = hazard_types[chemdef['Hazard Type']] if chemdef.get('Hazard Type') in hazard_types else 'HazardBehavior.BehaviorType.NONE'

    datapack.add_text_to_region(FLUIDS_FILE, 'FLUID_REGION', f"""
    public static final RegistryObject<FluidType> {fluid_name.upper()}_FLUID_TYPE = registerType("{fluid_name}", "{chemdef['Form']}", "{chemdef['Color']}");
    public static final RegistryObject<FlowingFluid> SOURCE_{fluid_name.upper()} = FLUIDS.register("{fluid_name}",
            () -> new ForgeFlowingFluid.Source(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_{fluid_name.upper()} = FLUIDS.register("flowing_{fluid_name}",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties {fluid_name.upper()}_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            {fluid_name.upper()}_FLUID_TYPE, SOURCE_{fluid_name.upper()}, FLOWING_{fluid_name.upper()})
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("{fluid_name}", SOURCE_{fluid_name.upper()}))
            .bucket(ModItems.registerBucketItem("{fluid_name}", SOURCE_{fluid_name.upper()}, {hazard}));
    """)

    if chemdef['Description']:
      datapack.add_text_to_region(JEI_FILE, 'INFO_REGION', f"""registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_{fluid_name.upper()}.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("{chemdef['Description']}"));""")

    datapack.add_text_to_region(FLUIDS_FILE, 'RENDER_REGION', f"ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_{fluid_name.upper()}.get(), RenderType.translucent());")
    datapack.add_text_to_region(FLUIDS_FILE, 'RENDER_REGION', f"ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_{fluid_name.upper()}.get(), RenderType.translucent());")

    output_image_path = f'../resources/assets/astech/textures/item/{fluid_name}_bucket.png'

    datapack.add_translation(f"tooltip.{fluid_name}.fluid", f"§e{chemdef['Formula']}§r")
    datapack.add_translation(f"item.astech.{fluid_name}_bucket", f"{plain_text_name}")
    datapack.add_translation(f"fluid_type.astech.{fluid_name}", f"{'Liquid ' if chemdef['Form'] != 'gas' else ''}{plain_text_name}{' Gas' if chemdef['Form'] == 'gas' else ''}")


    datapack.add_translation(f"slurry.astech.dirty_{fluid_name}", f"Dirty {plain_text_name} Slurry")
    datapack.add_translation(f"slurry.astech.clean_{fluid_name}", f"Clean {plain_text_name} Slurry")

    datapack.add_translation(f"tooltip.{fluid_name}.material", f"§e{chemdef['Formula']}§r")

    datapack.add_simple_item_model(f'{fluid_name}_bucket')

    datapack.add_fluid_tag(f"forge:{fluid_name}", f"astech:{fluid_name}")

    layer_images(base_image_path, top_image_path, output_image_path, hex_to_rgb(chemdef["Color"]), chemdef['Form'])

    add_simple_tint_item(datapack, f"{fluid_name}_dust", f"{plain_text_name} Dust", chemdef["Color"], get_texture(fluid_name, dust_textures), fluid_name)
    datapack.add_item_tag(f"forge:dusts/{fluid_name}", f"astech:{fluid_name}_dust")

    if chemdef['Type'] == 'Chemical': continue

    add_simple_tint_item(datapack, f"{fluid_name}_screw", f"{plain_text_name} Screw", chemdef["Color"], './templates/screw.png', fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_rod", f"{plain_text_name} Rod", chemdef["Color"], get_texture(fluid_name, rod_textures), fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_ingot", f"{plain_text_name} Ingot", chemdef["Color"], get_texture(fluid_name, ingot_textures), fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_plate", f"{plain_text_name} Plate", chemdef["Color"], get_texture(fluid_name, plate_textures), fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_nugget", f"{plain_text_name} Nugget", chemdef["Color"], get_texture(fluid_name, nugget_textures), fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_gear", f"{plain_text_name} Gear", chemdef["Color"], get_texture(fluid_name, gear_textures), fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_ring", f"{plain_text_name} Ring", chemdef["Color"], './templates/ring.png', fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_curved_plate", f"{plain_text_name} Curved Plate", chemdef["Color"], './templates/curved_plate.png', fluid_name)
    add_simple_tint_item(datapack, f"{fluid_name}_wire", f"{plain_text_name} Wire", chemdef["Color"], './templates/wire.png', fluid_name)
    add_simple_tint_block(datapack, f"{fluid_name}_block", f"{plain_text_name} Block", chemdef["Color"], get_texture(fluid_name, block_textures))

    datapack.add_item_tag(f"forge:storage_blocks/{fluid_name}", f"astech:{fluid_name}_block")
    datapack.add_block_tag(f"forge:storage_blocks/{fluid_name}", f"astech:{fluid_name}_block")
    
    datapack.add_item_tag(f"forge:ingots/{fluid_name}", f"astech:{fluid_name}_ingot")
    datapack.add_item_tag(f"forge:nuggets/{fluid_name}", f"astech:{fluid_name}_nugget")
    datapack.add_item_tag(f"forge:rods/{fluid_name}", f"astech:{fluid_name}_rod")
    datapack.add_item_tag(f"forge:plates/{fluid_name}", f"astech:{fluid_name}_plate")
    datapack.add_item_tag(f"forge:wires/{fluid_name}", f"astech:{fluid_name}_wire")
    datapack.add_item_tag(f"forge:gears/{fluid_name}", f"astech:{fluid_name}_gear")
    datapack.add_smelting_recipe(f'smelting/{fluid_name}_from_{fluid_name}_dust', f"forge:dusts/{fluid_name}", f"astech:{fluid_name}_ingot")

    if chemdef['Type'] == 'Element':
        add_simple_tint_item(datapack, f"raw_{fluid_name}", f"Raw {plain_text_name}", chemdef["Color"], get_texture(fluid_name, raw_ore_textures), fluid_name)
        datapack.add_item_tag(f"forge:raw_materials/{fluid_name}", f"astech:raw_{fluid_name}")
        datapack.add_smelting_recipe(f'smelting/{fluid_name}_from_{fluid_name}_raw', f"forge:raw_materials/{fluid_name}", f"astech:{fluid_name}_ingot")
        add_simple_ore_block(datapack, f"{fluid_name}_ore", f"{plain_text_name} Ore", chemdef["Color"], get_texture(fluid_name, ore_textures))
        add_simple_ore_block(datapack, f"deepslate_{fluid_name}_ore", f"Deepslate {plain_text_name} Ore", chemdef["Color"], get_texture(fluid_name, ore_textures))
        datapack.add_block_tag("minecraft:mineable/pickaxe", f"astech:{fluid_name}_ore")
        datapack.add_block_tag("minecraft:mineable/pickaxe", f"astech:deepslate_{fluid_name}_ore")
        datapack.add_block_tag("minecraft:needs_iron_tool", f"astech:deepslate_{fluid_name}_ore")
        datapack.add_block_tag("minecraft:needs_iron_tool", f"astech:{fluid_name}_ore")
        datapack.add_ore_block_loot(f"{fluid_name}_ore", f"raw_{fluid_name}")
        datapack.add_ore_block_loot(f"deepslate_{fluid_name}_ore", f"raw_{fluid_name}")

        datapack.add_item_tag(f"forge:ores/{fluid_name}", f"astech:{fluid_name}_ore")
        datapack.add_item_tag(f"forge:ores/{fluid_name}", f"astech:deepslate_{fluid_name}_ore")
        datapack.add_block_tag(f"forge:ores/{fluid_name}", f"astech:{fluid_name}_ore")
        datapack.add_block_tag(f"forge:ores/{fluid_name}", f"astech:deepslate_{fluid_name}_ore")

        datapack.add_smelting_recipe(f'smelting/{fluid_name}_from_{fluid_name}_ore', f"forge:ores/{fluid_name}", f"astech:{fluid_name}_ingot")

        add_mek_tint_item(datapack, f"{fluid_name}_clump", f"{plain_text_name} Clump", chemdef["Color"], CLUMP_TEXTURE, CLUMP_TEXTURE_OVERLAY, fluid_name)
        datapack.add_item_tag(f"mekanism:clumps/{fluid_name}", f"astech:{fluid_name}_clump")
        add_mek_tint_item(datapack, f"{fluid_name}_dirty_dust", f"{plain_text_name} Dirty Dust", chemdef["Color"], DIRTY_DUST_TEXTURE, DIRTY_DUST_TEXTURE_OVERLAY, fluid_name)
        datapack.add_item_tag(f"mekanism:dirty_dusts/{fluid_name}", f"astech:{fluid_name}_dirty_dust")
        add_mek_tint_item(datapack, f"{fluid_name}_crystal", f"{plain_text_name} Crystal", chemdef["Color"], CRYSTAL_TEXTURE, CRYSTAL_TEXTURE_OVERLAY, fluid_name)
        datapack.add_item_tag(f"mekanism:crystals/{fluid_name}", f"astech:{fluid_name}_crystal")
        add_mek_tint_item(datapack, f"{fluid_name}_shard", f"{plain_text_name} Shard", chemdef["Color"], SHARD_TEXTURE, SHARD_TEXTURE_OVERLAY, fluid_name)
        datapack.add_item_tag(f"mekanism:shards/{fluid_name}", f"astech:{fluid_name}_shard")

        # Example usage
        template_dir = './templates/mek_processing/reference'
        dest_dir = f'../resources/data/astech/recipes/compat/mek_processing/{fluid_name}'
        substitutions = {
            '$FLUID_TYPE$': fluid_name
        }

        copy_and_substitute(template_dir, dest_dir, substitutions)

        datapack.add_text_to_region(SLURRY_FILE, 'SLURRY_REGION', f"""public static SlurryRegistryObject<Slurry, Slurry> {fluid_name.upper()}_SLURRY = SLURRIES.register("{fluid_name}", "{chemdef['Color']}", new ResourceLocation("forge","tags/items/ores/{fluid_name}"));""")


    datapack.add_generic_recipe(f"crafting/{fluid_name}_ingot_to_nugget", UNCOMPACTING_RECIPE(f"astech:{fluid_name}_ingot", f"astech:{fluid_name}_nugget"))
    datapack.add_generic_recipe(f"compat/mek/crushing/{fluid_name}_ingot_to_dust", JSON(
        {
            "type": "mekanism:crushing",
            "input": {
                "ingredient": {
                "tag": f"forge:ingots/{fluid_name}"
                }
            },
            "output": {
                "item": f"astech:{fluid_name}_dust"
            },
            "conditions": [
                {
                "type": "forge:mod_loaded",
                "modid": "mekanism"
                }
            ]
        }
    ))
    datapack.add_generic_recipe(f"crafting/{fluid_name}_block_to_ingot", UNCOMPACTING_RECIPE(f"astech:{fluid_name}_block", f"astech:{fluid_name}_ingot"))
    
    datapack.add_generic_recipe(f"compat/mek/crushing/{fluid_name}_ingot_to_plate", f"""{{
  "type": "mekanism:crushing",
  "input": {{
    "ingredient": {{
      "tag": "forge:ingots/{fluid_name}"
    }}
  }},
  "output": {{
    "count": 1,
    "item": "astech:{fluid_name}_plate"
  }}
}}
""")
    
    datapack.add_generic_recipe(f"compat/mek/sawing/{fluid_name}_plate_to_rod", f"""{{
  "type": "mekanism:sawing",
  "input": {{
    "ingredient": {{
      "item": "astech:{fluid_name}_plate"
    }}
  }},
  "mainOutput": {{
    "count": 3,
    "item": "astech:{fluid_name}_rod"
  }},
  "secondaryChance": 0.25,
  "secondaryOutput": {{
    "item": "astech:{fluid_name}_dust"
  }}
}}
""")
    
    datapack.add_generic_recipe(f"compat/mek/sawing/{fluid_name}_nugget_to_screw", f"""{{
  "type": "mekanism:sawing",
  "input": {{
    "ingredient": {{
      "item": "astech:{fluid_name}_nugget"
    }}
  }},
  "mainOutput": {{
    "count": 1,
    "item": "astech:{fluid_name}_screw"
  }}
}}
""")
    
    datapack.add_generic_recipe(f"crafting/{fluid_name}_nugget_to_ingot", f"""{{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "key": {{
    "#": {{
      "tag": "forge:nuggets/{fluid_name}"
    }},
    "P": {{
      "item": "astech:{fluid_name}_nugget"
    }}
  }},
  "pattern": [
    "###",
    "#P#",
    "###"
  ],
  "result": {{
    "count": 1,
    "item": "astech:{fluid_name}_ingot"
  }}
}}
""")
    
    datapack.add_generic_recipe(f"crafting/{fluid_name}_ingot_to_block", f"""{{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "key": {{
    "#": {{
      "item": "astech:{fluid_name}_ingot"
    }}
  }},
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "result": {{
    "count": 1,
    "item": "astech:{fluid_name}_block"
  }}
}}
""")


datapack.write_to_disk()
# Example usage
source_dir = "./resources"
destination_dir = "../resources/"

copy_and_overwrite(source_dir, destination_dir)
