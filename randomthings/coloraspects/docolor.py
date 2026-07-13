import os
from pathlib import Path

from PIL import Image

from aspect_color_mapping import ASPECTS_TO_COLORS
pathIn = Path("./notcolored")
pathOut = Path("./colored")

#all xxx.png
def color_tint_image(image_path, color:tuple[int,int,int]):
    r_tint = color[0]
    g_tint = color[1]
    b_tint = color[2]

    img = Image.open(image_path).convert("RGBA")
    pixels = img.load()

    for x in range(img.width):
        for y in range(img.height):
            r, g, b, a = pixels[x, y]
            if a == 0: continue
            new_r = int(r_tint * (r / 255.0))
            new_g = int(g_tint * (g / 255.0))
            new_b = int(b_tint * (b / 255.0))
            pixels[x, y] = (new_r, new_g, new_b, a)
    return img
for imgName in os.listdir(pathIn):
    aspectName = imgName.split(".")[0]
    aspectColor = ASPECTS_TO_COLORS[aspectName]
    tinted_img = color_tint_image(pathIn / imgName,aspectColor)
    tinted_img.save(pathOut / imgName, "PNG")