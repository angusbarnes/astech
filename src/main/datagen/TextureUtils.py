from PIL import Image, ImageEnhance, ImageOps
from read_csv import get_chemical_defs
import numpy as np
import hashlib

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