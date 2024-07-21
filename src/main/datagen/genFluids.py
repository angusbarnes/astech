from PIL import Image, ImageEnhance, ImageOps
from read_csv import get_chemical_defs
import numpy as np
import hashlib
from TextureUtils import *
from context import Context
import textwrap
import json
import os

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
top_image_path = './templates/bucket_fluid_layer.png'    # Replace with your top image file path

ingot_textures = ['./templates/ingot1.png', './templates/ingot2.png', './templates/ingot3.png', './templates/ingot4.png']
dust_textures = ['./templates/dust1.png', './templates/dust2.png', './templates/dust3.png', './templates/dust4.png', './templates/dust5.png', './templates/dust6.png']
plate_textures = ['./templates/plate1.png', './templates/plate2.png']
rod_textures = ['./templates/rod1.png', './templates/rod2.png']
nugget_textures = ['./templates/nugget1.png', './templates/nugget2.png']
raw_ore_textures = ['./templates/raw_ore1.png', './templates/raw_ore2.png', './templates/raw_ore3.png', './templates/raw_ore4.png', './templates/raw_ore5.png', './templates/raw_ore6.png', './templates/raw_ore7.png', './templates/raw_ore8.png']
ore_textures = ['./templates/ore1.png', './templates/ore2.png', './templates/ore3.png', './templates/ore4.png', './templates/ore5.png', './templates/ore6.png', './templates/ore7.png', './templates/ore8.png']
block_textures = ['./templates/block1.png', './templates/block2.png', './templates/block3.png', './templates/block4.png', './templates/block5.png', './templates/block6.png', './templates/block7.png']
gear_textures = ['./templates/gear.png', './templates/gear2.png']

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
    

# def add_ore_block(ctx: Context, block_id: str, block_name, tint, template_file, material_name):

#     TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
#     ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
#     FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java'
#     BLOCK_FILE 

#     template = Image.open(template_file)
#     texture = blend_overlay(template, hex_to_rgb(tint))
#     texture.save(f'../resources/assets/astech/textures/item/{item_id}.png', format='PNG')
#     ctx.add_translation(f"item.astech.{item_id}", f"{item_name}")
#     ctx.add_text_to_region(BLOCK_FILE, 'BLOCKS_REGION', f"""public static final RegistryObject<Block> {} = registerBlock("{}",
#             () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));""")
#     ctx.add_text_to_region(TAB_FILE, 'TAB_REGION', f"output.accept(ModItems.{item_id.upper()}.get());")
#     ctx.add_simple_item_model(f'{item_id}')

headers, chemicals = get_chemical_defs("chems.csv")
datapack = Context('../resources/', 'astech')

datapack.set_base_dictionary({
    "item.astech.deez_nuts": "Deez Nuts",
    "item.astech.deez_butts": "Deez Butts",
    "item.astech.god_forged_ingot": "God Forged Ingot",
    "creativetab.astech_tab": "AsTech Items",
    "block.astech.nic_block": "§cNic Block",
    "block.astech.gem_polishing_station": "Gem Polishing Station",
    "block.astech.chemical_mixer": "Chemical Mixer"
})

static_items = [
    'backwards_ingot',
    'unrefined', 
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
    'monocrystalline_silicon', 
    'antimatter_tormentum',
    'abyssium_ingot',
    'ultradense_metal_ball', 
    'analog_circuit', 
    'genetic_material_a', 
    'portable_storage_unit', 
    'refined', 
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
    'large_heat_exchanger', 
    'resistor', 
    'interplanetary_mining_lens', 
    'not_gate', 
    'crystal_matrix_ingot', 
    'genetic_material_b', 
    'wrench', 
    'small_heat_exchanger', 
    'cosmic_meatballs', 
    'quantum_circuit_board'
]


for item in sorted(static_items):
    add_static_asset_item(datapack, item, item.replace('_', ' ').title())

datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_a')
datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_b')
datapack.add_item_tag('forge:genetic_material', 'astech:genetic_material_c')
datapack.add_item_tag('forge:genetic_material', 'astech:mutated_genetic_material')

TAB_FILE = '../java/net/astr0/astech/ModCreativeModTab.java'
ITEM_FILE = '../java/net/astr0/astech/item/ModItems.java'
FLUIDS_FILE = '../java/net/astr0/astech/Fluid/ModFluids.java' 

for chemdef in chemicals:
    plain_text_name = chemdef['Name']
    fluid_name = chemdef['MATERIAL_ID']
    datapack.add_text_to_region(FLUIDS_FILE, 'FLUID_REGION', f"""
    public static final RegistryObject<FluidType> {fluid_name.upper()}_FLUID_TYPE = registerType("{fluid_name}", "{chemdef['Form']}", "{chemdef['Color']}");
    public static final RegistryObject<FlowingFluid> SOURCE_{fluid_name.upper()} = FLUIDS.register("{fluid_name}",
            () -> new ForgeFlowingFluid.Source(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_{fluid_name.upper()} = FLUIDS.register("flowing_{fluid_name}",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties {fluid_name.upper()}_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            {fluid_name.upper()}_FLUID_TYPE, SOURCE_{fluid_name.upper()}, FLOWING_{fluid_name.upper()})
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("{fluid_name}", SOURCE_{fluid_name.upper()}))
            .bucket(ModItems.registerBucketItem("{fluid_name}", SOURCE_{fluid_name.upper()}));
    """)

    datapack.add_text_to_region(FLUIDS_FILE, 'RENDER_REGION', f"ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_{fluid_name.upper()}.get(), RenderType.translucent());")
    datapack.add_text_to_region(FLUIDS_FILE, 'RENDER_REGION', f"ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_{fluid_name.upper()}.get(), RenderType.translucent());")

    output_image_path = f'../resources/assets/astech/textures/item/{fluid_name}_bucket.png'

    datapack.add_translation(f"tooltip.{fluid_name}.fluid", f"§e{chemdef['Formula']}§r")
    datapack.add_translation(f"item.astech.{fluid_name}_bucket", f"{plain_text_name}")
    datapack.add_translation(f"fluid_type.astech.{fluid_name}", f"{'Liquid ' if chemdef['Form'] != 'gas' else ''}{plain_text_name}{' Gas' if chemdef['Form'] == 'gas' else ''}")

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
    
    datapack.add_item_tag(f"forge:ingots/{fluid_name}", f"astech:{fluid_name}_ingot")
    datapack.add_item_tag(f"forge:nuggets/{fluid_name}", f"astech:{fluid_name}_nugget")
    datapack.add_smelting_recipe(f'smelting/{fluid_name}_from_{fluid_name}_dust', f"forge:dusts/{fluid_name}", f"astech:{fluid_name}_ingot")

    if chemdef['Type'] == 'Element':
        add_simple_tint_item(datapack, f"raw_{fluid_name}", f"Raw {plain_text_name}", chemdef["Color"], get_texture(fluid_name, raw_ore_textures), fluid_name)
        datapack.add_item_tag(f"forge:raw_ores/{fluid_name}", f"astech:raw_{fluid_name}")
        datapack.add_smelting_recipe(f'smelting/{fluid_name}_from_{fluid_name}_raw', f"forge:raw_ores/{fluid_name}", f"astech:{fluid_name}_ingot")

    datapack.add_generic_recipe(f"crafting/{fluid_name}_ingot_to_nugget", f"""{{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {{
      "item": "astech:{fluid_name}_ingot"
    }}
  ],
  "result": {{
    "count": 9,
    "item": "astech:{fluid_name}_nugget"
  }}
}}
""")
    
    datapack.add_generic_recipe(f"crafting/{fluid_name}_block_to_ingot", f"""{{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {{
      "item": "astech:{fluid_name}_block"
    }}
  ],
  "result": {{
    "count": 9,
    "item": "astech:{fluid_name}_ingot"
  }}
}}
""")
    
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
