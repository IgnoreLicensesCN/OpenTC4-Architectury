#ai generated,seems useful
import itertools
import json

from PIL import Image

def blend_pixel_overlay(pixels, x, y, overlay_rgba):
    orig_r, orig_g, orig_b, orig_a = pixels[x, y]
    if orig_a == 0:
        return
    over_r, over_g, over_b,alpha = overlay_rgba
    alpha /= 255.
    new_r = int(orig_r * (1 - alpha) + over_r * alpha)
    new_g = int(orig_g * (1 - alpha) + over_g * alpha)
    new_b = int(orig_b * (1 - alpha) + over_b * alpha)
    pixels[x, y] = (new_r, new_g, new_b, orig_a)

all_situations = [list(p) for p in itertools.product(range(9), repeat=4) if sum(p) == 8]
all_situations.sort()
counter = 0
fromX = 5
toX = 7
fromY = 11
colors = [(127,0,127,127),(192,0,192,127),(255,127,255,127)]
print(all_situations)
for situation in all_situations:
    situationCopy = situation.copy()
    img = Image.open("sanitychecker.png")
    img = img.convert("RGBA")
    pixels = img.load()
    for yOffset in range(8):
        pickColorIndex = -1
        if (situationCopy[0] != 0):
            pickColorIndex = 0
            situationCopy[0] -= 1
        elif (situationCopy[1] != 0):
            pickColorIndex = 1
            situationCopy[1] -= 1
        elif (situationCopy[2] != 0):
            pickColorIndex = 2
            situationCopy[2] -= 1
        if (pickColorIndex < 0):
            break
        color = colors[pickColorIndex]
        for xOffset in range(3):
            blend_pixel_overlay(pixels,fromX+xOffset,fromY-yOffset,color)
    img.save(f"sanity_checker/sanity_checker_{counter}.png")
    counter += 1


gen_json = json.loads(
    str(
        {
            "parent": "item/generated",
            "textures": {
                "layer0": "item/sanity_checker"
            },
            "overrides": [
                {
                    "predicate": {
                        "warp_index": f"{warp_index}"
                    },
                    "model": f"item/sanity_checker_states/sanity_checker_{warp_index}"
                } for warp_index in range(165)
            ]
        }
    ).replace('\'','"')
)
with open("sanity_checker/jsons/sanity_checker.json", "w+") as outfile:
    json.dump(gen_json, outfile)

for i in range(165):
    with open(f"sanity_checker/jsons/sanity_checker_states/sanity_checker_{i}.json", "w+") as outFile:
        outFile.write("""{
    "parent": "item/sanity_checker",
    "textures": {
        "layer0": f"item/sanity_checker_states/sanity_checker_{i}"
    }
}
""")