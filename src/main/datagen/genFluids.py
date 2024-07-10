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
rod_textures = ['./templates/rod1.png']

def get_texture(filename, textures):
    # Step 1: Hash the filename using a hash function (e.g., SHA-256)
    hash_object = hashlib.sha256(filename.encode())
    hash_value = int(hash_object.hexdigest(), 16)
    
    # Step 2: Map the hash value to one of three textures
    texture_index = hash_value % len(textures)
    
    return textures[texture_index]

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

fluid_text = ""
render_text = ""
item_text = ""
tab_text = ""
tooltips = {}
for chemdef in chemicals:
    plain_text_name = chemdef['Name']
    fluid_name = chemdef['MATERIAL_ID']
    fluid_text += f"""
    public static final RegistryObject<FluidType> {fluid_name.upper()}_FLUID_TYPE = registerType("{fluid_name}", "{chemdef['Form']}", "{chemdef['Color']}");
    public static final RegistryObject<FlowingFluid> SOURCE_{fluid_name.upper()} = FLUIDS.register("{fluid_name}",
            () -> new ForgeFlowingFluid.Source(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_{fluid_name.upper()} = FLUIDS.register("flowing_{fluid_name}",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.{fluid_name.upper()}_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties {fluid_name.upper()}_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            {fluid_name.upper()}_FLUID_TYPE, SOURCE_{fluid_name.upper()}, FLOWING_{fluid_name.upper()})
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.registerFluidBlock("{fluid_name}", SOURCE_{fluid_name.upper()}))
            .bucket(ModItems.registerBucketItem("{fluid_name}", SOURCE_{fluid_name.upper()}));\n
    """

    render_text += f"ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_{fluid_name.upper()}.get(), RenderType.translucent());\n"
    render_text += f"ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_{fluid_name.upper()}.get(), RenderType.translucent());\n"

    output_image_path = f'../resources/assets/astech/textures/item/{fluid_name}_bucket.png'

    datapack.add_translation(f"tooltip.{fluid_name}.fluid", f"§e{chemdef['Formula']}§r")
    datapack.add_translation(f"item.astech.{fluid_name}_bucket", f"{plain_text_name}")
    datapack.add_translation(f"fluid_type.astech.{fluid_name}", f"{'Liquid ' if chemdef['Form'] != 'gas' else ''}{plain_text_name}{' Gas' if chemdef['Form'] == 'gas' else ''}")

    layer_images(base_image_path, top_image_path, output_image_path, hex_to_rgb(chemdef["Color"]), chemdef['Form'])

    ingot_template = Image.open(get_texture(fluid_name, ingot_textures))
    dust_template = Image.open(get_texture(fluid_name, dust_textures))
    plate_template = Image.open(get_texture(fluid_name, plate_textures))
    rod_template = Image.open(get_texture(fluid_name, rod_textures))

    ingot = blend_overlay(ingot_template, hex_to_rgb(chemdef["Color"]))
    dust = blend_overlay(dust_template, hex_to_rgb(chemdef["Color"]))
    plate = blend_overlay(plate_template, hex_to_rgb(chemdef["Color"]))

    ingot.save(f'../resources/assets/astech/textures/item/{fluid_name}_ingot.png', format='PNG')
    dust.save(f'../resources/assets/astech/textures/item/{fluid_name}_dust.png', format='PNG')
    plate.save(f'../resources/assets/astech/textures/item/{fluid_name}_plate.png', format='PNG')

    datapack.add_translation(f"item.astech.{fluid_name}_ingot", f"{plain_text_name} Ingot")
    datapack.add_translation(f"item.astech.{fluid_name}_dust", f"{plain_text_name} Dust")
    datapack.add_translation(f"item.astech.{fluid_name}_plate", f"{plain_text_name} Plate")
    datapack.add_translation(f"tooltip.{fluid_name}.material", f"§e{chemdef['Formula']}§r")

    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_INGOT = ITEMS.register("{fluid_name}_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""
    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_DUST = ITEMS.register("{fluid_name}_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""
    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_PLATE = ITEMS.register("{fluid_name}_plate", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""

    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_INGOT.get());\n"""
    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_DUST.get());\n"""
    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_PLATE.get());\n"""

    datapack.add_simple_item_model(f'{fluid_name}_bucket')
    datapack.add_simple_item_model(f'{fluid_name}_dust')
    datapack.add_simple_item_model(f'{fluid_name}_ingot')
    datapack.add_simple_item_model(f'{fluid_name}_plate')

        
    datapack.add_fluid_tag(f"forge:{fluid_name}", f"astech:{fluid_name}")
    datapack.add_item_tag(f"forge:dusts/{fluid_name}", f"astech:{fluid_name}_dust")
    datapack.add_item_tag(f"forge:ingots/{fluid_name}", f"astech:{fluid_name}_ingot")
        
    datapack.add_smelting_recipe(fluid_name, f"{fluid_name}_ingot")


Context._insert_text_in_region(file_path, 'FLUID_REGION', fluid_text)
Context._insert_text_in_region(file_path, 'RENDER_REGION', render_text)
Context._insert_text_in_region('../java/net/astr0/astech/item/ModItems.java', 'MATERIAL_REGION', item_text)
Context._insert_text_in_region('../java/net/astr0/astech/ModCreativeModTab.java', 'TAB_REGION', tab_text)
datapack.write_to_disk()
# Example usage
json_file_path = '../resources/assets/astech/lang/en_us.json'

merge_json_file(json_file_path, tooltips)
