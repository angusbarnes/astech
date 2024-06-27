from PIL import Image, ImageEnhance, ImageOps
from read_csv import get_chemical_defs
import numpy as np

def insert_text_in_region(file_path, region, new_text):
    # Define the anchors
    start_anchor = f"//#anchor {region}"
    end_anchor = f"//#end {region}"
    
    # Read the content of the file
    with open(file_path, 'r') as file:
        lines = file.readlines()
    
    # Find the start and end indices of the region
    start_index = None
    end_index = None
    
    for i, line in enumerate(lines):
        if start_anchor in line:
            start_index = i
        elif end_anchor in line:
            end_index = i
            break
    
    if start_index is None or end_index is None:
        print("Anchors not found in the file.")
        return
    
    # Insert the new text
    lines = lines[:start_index + 1] + [new_text + "\n"] + lines[end_index:]
    
    # Write the modified content back to the file
    with open(file_path, 'w') as file:
        file.writelines(lines)

def tint_image(image, tint_color):
    """
    Applies a color tint to the non-transparent pixels of an image.
    
    Args:
    image (PIL.Image): The image to be tinted.
    tint_color (tuple): The RGB color to apply as a tint.
    
    Returns:
    PIL.Image: The tinted image.
    """
    # Ensure the image has an alpha channel
    image = image.convert("RGBA")
    
    # Split the image into its respective channels
    r, g, b, a = image.split()
    
    # Create a new image with the tint color, but only for the RGB channels
    tinted_image = Image.new("RGBA", image.size)
    tinted_r, tinted_g, tinted_b, _ = tinted_image.split()
    
    # Blend the tint color with the grayscale value of non-transparent pixels
    for i in range(image.size[0]):
        for j in range(image.size[1]):
            if a.getpixel((i, j)) != 0:
                gray_value = r.getpixel((i, j))
                new_r = int(gray_value * (1 - 0.75) + tint_color[0] * 0.75)
                new_g = int(gray_value * (1 - 0.75) + tint_color[1] * 0.75)
                new_b = int(gray_value * (1 - 0.75) + tint_color[2] * 0.75)
                tinted_r.putpixel((i, j), new_r)
                tinted_g.putpixel((i, j), new_g)
                tinted_b.putpixel((i, j), new_b)
    
    # Combine the tinted channels back with the original alpha channel
    tinted_image = Image.merge("RGBA", (tinted_r, tinted_g, tinted_b, a))
    
    return tinted_image

def layer_images(base_image_path, top_image_path, output_image_path, tint_color, type):
    # Open the base image
    base_image = Image.open(base_image_path).convert("RGBA")
    
    # Open the top image and apply the color tint
    top_image = Image.open(top_image_path).convert("RGBA")
    tinted_top_image = tint_image(top_image, tint_color)
    
    # Layer the tinted top image on the base image
    combined_image = Image.alpha_composite(base_image, tinted_top_image)

    if type == 'gas':
        combined_image = combined_image.rotate(180)
    
    # Save the result
    combined_image.save(output_image_path, format='PNG')

def hex_to_rgb(hex_str):
    """
    Convert a hexadecimal color string to an RGB tuple.
    
    Args:
    hex_str (str): The hexadecimal color string (e.g., "#ff0000" or "ff0000" for red).
    
    Returns:
    tuple: A tuple representing the RGB values (e.g., (255, 0, 0) for red).
    """
    # Remove the hash symbol if present
    hex_str = hex_str.lstrip('#')
    
    # Convert the hex string to an integer tuple
    return tuple(int(hex_str[i:i+2], 16) for i in (0, 2, 4))


def blend_overlay(image, color):
    # Convert to RGBA to handle transparency
    image = image.convert('RGBA')
    image_data = np.array(image, dtype=np.float32) / 255

    # Separate alpha channel
    alpha = image_data[:, :, 3]

    # Convert image to grayscale and expand dimensions
    grayscale = ImageOps.grayscale(image)
    grayscale = np.array(grayscale, dtype=np.float32) / 255
    grayscale = np.expand_dims(grayscale, axis=-1) * 0.86
    # Prepare the color for blending
    color = np.array(color, dtype=np.float32) / 255
    color = color.reshape((1, 1, 3))

    # Blend the grayscale image with the color using the overlay method
    blended = np.where(grayscale < 0.57, 2 * grayscale * color, 1 - 2 * (1 - grayscale) * (1 - color))
    blended = np.clip(blended, 0, 1)

    # Combine blended color with the alpha channel
    result = np.dstack((blended, alpha))

    # Convert the result back to an image
    result = (result * 255).astype('uint8')
    return Image.fromarray(result, 'RGBA')

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
base_image_path = '../resources/assets/astech/textures/item/bucket_base_layer.png'  # Replace with your base image file path
top_image_path = '../resources/assets/astech/textures/item/bucket_fluid_layer.png'    # Replace with your top image file path

headers, chemicals = get_chemical_defs("chems.csv")

fluid_text = ""
render_text = ""
item_text = ""
tab_text = ""
tooltips = {}
for chemdef in chemicals:
    plain_text_name = chemdef['Name']
    fluid_name = chemdef['MATERIAL_ID']
    fluid_text += f"""
    public static final RegistryObject<FluidType> {fluid_name.upper()}_FLUID_TYPE = registerType("{fluid_name}_fluid", "{chemdef['Form']}", "{chemdef['Color']}");
    public static final RegistryObject<FlowingFluid> SOURCE_{fluid_name.upper()} = FLUIDS.register("{fluid_name}_fluid",
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

    tooltips[f"tooltip.{fluid_name}.fluid"] = f"§e{chemdef['Formula']}§r"
    tooltips[f"item.astech.{fluid_name}_bucket"] = f"{plain_text_name}"
    tooltips[f"fluid_type.astech.{fluid_name}_fluid"] = f"{'Liquid ' if chemdef['Form'] != 'gas' else ''}{plain_text_name}{' Gas' if chemdef['Form'] == 'gas' else ''}"

    layer_images(base_image_path, top_image_path, output_image_path, hex_to_rgb(chemdef["Color"]), chemdef['Form'])

    ingot_template = Image.open('../resources/assets/astech/textures/item/ingot_template1.png')
    dust_template = Image.open('../resources/assets/astech/textures/item/dust_template.png')

    ingot = blend_overlay(ingot_template, hex_to_rgb(chemdef["Color"]))
    dust = blend_overlay(dust_template, hex_to_rgb(chemdef["Color"]))

    ingot.save(f'../resources/assets/astech/textures/item/{fluid_name}_ingot.png', format='PNG')
    dust.save(f'../resources/assets/astech/textures/item/{fluid_name}_dust.png', format='PNG')

    tooltips[f"item.astech.{fluid_name}_ingot"] = f"{plain_text_name} Ingot"
    tooltips[f"item.astech.{fluid_name}_dust"] = f"{plain_text_name} Dust"
    tooltips[f"tooltip.{fluid_name}.material"] = f"§e{chemdef['Formula']}§r"

    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_INGOT = ITEMS.register("{fluid_name}_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""
    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_DUST = ITEMS.register("{fluid_name}_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""

    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_INGOT.get());\n"""
    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_DUST.get());\n"""

    with open(f'../resources/assets/astech/models/item/{fluid_name}_bucket.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_bucket\"}}}}")

    with open(f'../resources/assets/astech/models/item/{fluid_name}_ingot.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_ingot\"}}}}")

    with open(f'../resources/assets/astech/models/item/{fluid_name}_dust.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_dust\"}}}}")

insert_text_in_region(file_path, 'FLUID_REGION', fluid_text)
insert_text_in_region(file_path, 'RENDER_REGION', render_text)
insert_text_in_region('../java/net/astr0/astech/item/ModItems.java', 'MATERIAL_REGION', item_text)
insert_text_in_region('../java/net/astr0/astech/ModCreativeModTab.java', 'TAB_REGION', tab_text)

# Example usage
json_file_path = '../resources/assets/astech/lang/en_us.json'

merge_json_file(json_file_path, tooltips)
