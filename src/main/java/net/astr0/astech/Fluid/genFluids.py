from PIL import Image, ImageEnhance, ImageOps
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



file_path = 'ModFluids.java'  # Replace with your file path
base_image_path = '../../../../../resources/assets/astech/textures/item/bucket_base_layer.png'  # Replace with your base image file path
top_image_path = '../../../../../resources/assets/astech/textures/item/bucket_fluid_layer.png'    # Replace with your top image file path

fluids = [
    ['Polytetrafluoroethylene', 'liquid', '#c6fff8', 'C2F4', 'A polymer commonly used in non-stick coatings for cookware.'],
    ['Dimethyl Ether', 'gas', '#fcfffe', 'C2H6O', 'Used as a propellant in aerosol products.'],
    ['Hydrocarbonic Broth', 'liquid', '#1e1e1e', 'C12H26', 'A mixture of hydrocarbons, often used as a solvent.'],
    ['Ethane', 'gas', '#ebba34', 'C2H6', 'A component of natural gas used as a feedstock for ethylene production.'],
    ['Piss Water', 'liquid', '#ebba34', 'Urine', 'A slang term for urine, which is primarily water with dissolved waste products.'],
    ['Alumunium Hydroxide', 'gas', '#a8e5eb', 'Al(OH)3', 'Used as an antacid and in the manufacture of aluminum compounds.'],
    ['Sulfuric Acid', 'liquid', '#ffcc00', 'H2SO4', 'A highly corrosive acid used in industrial processes and battery acid.'],
    ['Ammonia', 'gas', '#b2dfdb', 'NH3', 'Used in fertilizers and as a refrigerant gas.'],
    ['Benzene', 'liquid', '#f28e1c', 'C6H6', 'An important industrial solvent and precursor in the production of various chemicals.'],
    ['Chlorine', 'gas', '#d4ff00', 'Cl2', 'Used as a disinfectant and in the production of PVC.'],
    ['Acetone', 'liquid', '#e7e4e4', 'C3H6O', 'A common solvent used in nail polish remover.'],
    ['Methanol', 'liquid', '#80d4ff', 'CH3OH', 'Used as a solvent, antifreeze, and fuel.'],
    ['Hydrogen', 'gas', '#fff4e6', 'H2', 'The lightest and most abundant chemical element in the universe.'],
    ['Nitrogen', 'gas', '#8a8dff', 'N2', 'Makes up 78% of the Earth\'s atmosphere and is used in the production of ammonia.'],
    ['Toluene', 'liquid', '#ff6600', 'C7H8', 'A solvent used in paint thinners and adhesives.'],
    ['Propane', 'gas', '#ffe6cc', 'C3H8', 'Used as a fuel for heating, cooking, and in engines.'],
    ['Ethanol', 'liquid', '#ff9999', 'C2H5OH', 'Commonly known as alcohol, used in beverages and as a fuel.'],
    ['Formaldehyde', 'gas', '#ccffcc', 'CH2O', 'Used in the production of resins and as a preservative.'],
    ['Hexane', 'liquid', '#ffd700', 'C6H14', 'Used as a solvent in the extraction of vegetable oils.'],
    ['Butane', 'gas', '#ccffff', 'C4H10', 'Used as a fuel in lighters and portable stoves.'],
    ['Carbon Tetrachloride', 'liquid', '#cccccc', 'CCl4', 'Once used in fire extinguishers and as a cleaning agent.'],
    ['Ethylene Glycol', 'liquid', '#99ccff', 'C2H6O2', 'Used as antifreeze in cooling and heating systems.'],
    ['Acetic Acid', 'liquid', '#ff6666', 'C2H4O2', 'The main component of vinegar apart from water.'],
    ['Methyl Chloride', 'gas', '#ffcc99', 'CH3Cl', 'Used as a refrigerant and in the production of silicone polymers.'],
    ['Phosgene', 'gas', '#999966', 'COCl2', 'A highly toxic gas used in the production of plastics and pesticides.'],
    ['Isopropanol', 'liquid', '#cc99ff', 'C3H8O', 'Commonly known as rubbing alcohol, used as a disinfectant.'],
    ['Aniline', 'liquid', '#b2b2b2', 'C6H5NH2', 'Used in the production of dyes, rubber, and chemicals.'],
    ['Sodium Hypochlorite', 'liquid', '#99ff99', 'NaClO', 'Used as a bleaching agent and disinfectant.'],
    ['Hydrogen Sulfide', 'gas', '#ffff99', 'H2S', 'A toxic gas with a characteristic smell of rotten eggs.'],
    ['Vinyl Chloride', 'gas', '#b3ffb3', 'C2H3Cl', 'Used in the production of polyvinyl chloride (PVC).'],
    ['Xylene', 'liquid', '#ff9966', 'C8H10', 'Used as a solvent in the printing, rubber, and leather industries.'],
    ['Hydrochloric Acid', 'liquid', '#ff6666', 'HCl', 'A strong acid used in many industrial processes, including metal refining.'],
    ['Nitric Acid', 'liquid', '#ffcc00', 'HNO3', 'Used in the production of fertilizers and explosives.'],
    ['Sodium Hydroxide', 'liquid', '#99ccff', 'NaOH', 'A strong base used in soap making and chemical manufacturing.'],
    ['Dichloromethane', 'liquid', '#c6c6a7', 'CH2Cl2', 'A solvent used in paint removers and degreasing agents.'],
    ['Trichloroethylene', 'liquid', '#cc9999', 'C2HCl3', 'Used as a solvent and in the manufacturing of hydrofluorocarbon refrigerants.'],
    ['Perchloroethylene', 'liquid', '#9999cc', 'C2Cl4', 'Commonly used in dry cleaning.'],
    ['Bromine', 'liquid', '#ff3300', 'Br2', 'Used in flame retardants and some types of medication.'],
    ['Phosphoric Acid', 'liquid', '#ccff99', 'H3PO4', 'Used in fertilizers, food flavoring, and rust removal.'],
    ['Sodium Bicarbonate', 'liquid', '#ffffff', 'NaHCO3', 'Commonly known as baking soda, used in cooking and cleaning.'],
    ['Dimethyl Sulfoxide', 'liquid', '#99ccff', 'C2H6OS', 'A solvent with the ability to penetrate biological membranes.'],
    ['Hydrazine', 'liquid', '#ccffcc', 'N2H4', 'Used as a rocket propellant and in fuel cells.'],
    ['Hexafluoropropylene', 'gas', '#99ffff', 'C3F6', 'Used in the production of fluoropolymers.'],
    ['Tetrahydrofuran', 'liquid', '#e6e6e6', 'C4H8O', 'A solvent used in the production of polymers.'],
    ['Styrene', 'liquid', '#ff9999', 'C8H8', 'Used in the production of polystyrene plastics and resins.'],
    ['Propylene', 'gas', '#ffcc99', 'C3H6', 'A key raw material in the production of polypropylene plastics.'],
    ['Acrolein', 'liquid', '#cc9966', 'C3H4O', 'Used in the production of acrylic acid and its esters.'],
    ['Tetrachloroethylene', 'liquid', '#999999', 'C2Cl4', 'Used as a solvent in dry cleaning and metal degreasing.'],
    ['Aqua Regia', 'liquid', '#ffcc00', 'HNO3+HCl', 'A mixture of nitric acid and hydrochloric acid, capable of dissolving gold.'],
    ['Cyanogen', 'gas', '#ccffff', 'C2N2', 'A highly toxic gas used in chemical synthesis and fumigation.'],
    ['Fluorosilicic Acid', 'liquid', '#99cc99', 'H2SiF6', 'Used in water fluoridation and in the production of ceramics.'],
    ['Titanium Tetrachloride', 'liquid', '#cccccc', 'TiCl4', 'Used in the production of titanium metal and in smoke screens.'],
    ['Methyl Ethyl Ketone', 'liquid', '#e6ccff', 'C4H8O', 'A solvent used in the production of plastics and textiles.'],
    ['Thionyl Chloride', 'liquid', '#ccff99', 'SOCl2', 'Used in the synthesis of acyl chlorides and in lithium batteries.'],
    ['Azoth', 'gas', '#b3ffff', 'N4', 'A fictional chemical often referenced in alchemical texts and stories.'],
    ['Unobtanium', 'liquid', '#ff66cc', 'Ubt', 'A rare and highly valuable fictional material with exceptional properties.'],
    ['Dilithium', 'liquid', '#99ccff', 'Li2', 'A fictional element used as a power source in the Star Trek universe.'],
    ['Adamantium', 'liquid', '#cccccc', 'Ad', 'A fictional, virtually indestructible metal alloy from the Marvel universe.'],
    ['Carbonadium', 'liquid', '#666666', 'Cbd', 'A malleable and resilient fictional alloy used in various sci-fi contexts.'],
    ['Radon', 'gas', '#ffd700', 'Rn', 'A radioactive noble gas used in some types of cancer treatment.'],
    ['Neon', 'gas', '#ff5ccd', 'Ne', 'A noble gas used in neon signs and high-voltage indicators.'],
    ['Argon', 'gas', '#80dfff', 'Ar', 'A noble gas used in welding and as a protective atmosphere for growing silicon and germanium crystals.'],
    ['Xenon', 'gas', '#3a79ff', 'Xe', 'A noble gas used in high-intensity lamps and ion propulsion systems in spacecraft.'],
    ['Cryptic Acid', 'liquid','#DD33DD', '§kJKSD', 'Hmm, what is this strange substrance']
]

fluid_text = ""
render_text = ""
item_text = ""
tab_text = ""
tooltips = {}
for fluid_name, type, tint, formula, desc in fluids:
    plain_text_name = fluid_name
    fluid_name = fluid_name.lower().replace(' ', '_')
    fluid_text += f"""
    public static final RegistryObject<FluidType> {fluid_name.upper()}_FLUID_TYPE = registerType("{fluid_name}_fluid", "{type}", "{tint}");
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

    output_image_path = f'../../../../../resources/assets/astech/textures/item/{fluid_name}_bucket.png'

    tooltips[f"tooltip.{fluid_name}.fluid"] = f"§e{formula}§r"
    tooltips[f"item.astech.{fluid_name}_bucket"] = f"{plain_text_name}"
    tooltips[f"fluid_type.astech.{fluid_name}_fluid"] = f"{'Liquid ' if type != 'gas' else ''}{plain_text_name}{' Gas' if type == 'gas' else ''}"

    layer_images(base_image_path, top_image_path, output_image_path, hex_to_rgb(tint), type)

    ingot_template = Image.open('../../../../../resources/assets/astech/textures/item/ingot_template1.png')
    dust_template = Image.open('../../../../../resources/assets/astech/textures/item/dust_template.png')

    ingot = blend_overlay(ingot_template, hex_to_rgb(tint))
    dust = blend_overlay(dust_template, hex_to_rgb(tint))

    ingot.save(f'../../../../../resources/assets/astech/textures/item/{fluid_name}_ingot.png', format='PNG')
    dust.save(f'../../../../../resources/assets/astech/textures/item/{fluid_name}_dust.png', format='PNG')

    tooltips[f"item.astech.{fluid_name}_ingot"] = f"{plain_text_name} Ingot"
    tooltips[f"item.astech.{fluid_name}_dust"] = f"{plain_text_name} Dust"
    tooltips[f"tooltip.{fluid_name}.material"] = f"§e{formula}§r"

    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_INGOT = ITEMS.register("{fluid_name}_ingot", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""
    item_text += f"""public static final RegistryObject<AsTechMaterialItem> {fluid_name.upper()}_DUST = ITEMS.register("{fluid_name}_dust", () -> new AsTechMaterialItem(new Item.Properties().stacksTo(64), "{fluid_name}"));\n"""

    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_INGOT.get());\n"""
    tab_text += f"""output.accept(ModItems.{fluid_name.upper()}_DUST.get());\n"""

    with open(f'../../../../../resources/assets/astech/models/item/{fluid_name}_bucket.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_bucket\"}}}}")

    with open(f'../../../../../resources/assets/astech/models/item/{fluid_name}_ingot.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_ingot\"}}}}")

    with open(f'../../../../../resources/assets/astech/models/item/{fluid_name}_dust.json', 'w') as model_file:
        model_file.write(f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"astech:item/{fluid_name}_dust\"}}}}")

insert_text_in_region(file_path, 'FLUID_REGION', fluid_text)
insert_text_in_region(file_path, 'RENDER_REGION', render_text)
insert_text_in_region('../item/ModItems.java', 'MATERIAL_REGION', item_text)
insert_text_in_region('../ModCreativeModTab.java', 'TAB_REGION', tab_text)

# Example usage
json_file_path = '../../../../../resources/assets/astech/lang/en_us.json'

merge_json_file(json_file_path, tooltips)
