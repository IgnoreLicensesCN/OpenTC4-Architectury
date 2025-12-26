import json
import os
import random
from consts import *
from pathlib import Path
import re

# ================= 配置 =================
MODID = "thaumcraft"
OUT_ROOT = Path("out_assets")#Path("generated/assets/thaumcraft")   # 输出根目录
TEXTURE_PREFIX = f"{MODID}:block/candle_"

# ========================================


# ---------- 模型生成 ----------
def cube(frm, to):
    return {
        "from": frm,
        "to": to,
        "faces": {
            d: {"texture": "#all", "tintindex": 0}
            for d in ["north","south","west","east","up","down"]
        }
    }

def gen_wax_elements(wax_count: int):
    rng = random.Random(wax_count * 1337)
    elements = []

    for a in range(wax_count):
        side = rng.choice([True, False])
        loc = 2 + rng.randint(0, 1)
        height = rng.randint(1, 3)

        if a % 2 == 0:
            x1 = 5 + loc
            x2 = 6 + loc
            z1 = 5 if side else 10
            z2 = 6 if side else 11
        else:
            x1 = 5 if side else 10
            x2 = 6 if side else 11
            z1 = 5 + loc
            z2 = 6 + loc

        elements.append(
            cube(
                [x1, 0, z1],
                [x2, height, z2]
            )
        )

    return elements

def gen_wax_model(color: str, wax: int):
    return {
        "parent": "block/block",
        "textures": {
            "all": f"{TEXTURE_PREFIX}{color}"
        },
        "elements": gen_wax_elements(wax)
    }


# ---------- blockstate 生成 ----------
def gen_blockstate(color: str):
    multipart = [
        { "apply": { "model": f"{MODID}:block/candle/base" } }
    ]

    for wax in range(1, 6):
        multipart.append({
            "when": { "wax": wax },
            "apply": {
                "model": f"{MODID}:block/candle/{color}/wax{wax}"
            }
        })

    return { "multipart": multipart }


# ---------- 主流程 ----------
for color in colorNames:
    # models
    model_dir = OUT_ROOT / "models" / "block" / "candle" / color
    model_dir.mkdir(parents=True, exist_ok=True)

    for wax in range(1, 6):
        model = gen_wax_model(color, wax)
        (model_dir / f"wax{wax}.json").write_text(
            json.dumps(model, indent=2)
        )

    # blockstate
    state_dir = OUT_ROOT / "blockstates"
    state_dir.mkdir(parents=True, exist_ok=True)

    blockstate = gen_blockstate(color)
    (state_dir / f"{color}_tallow_candle.json").write_text(
        json.dumps(blockstate, indent=2)
    )

    # print(f"generated candle color: {color}")

for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]
    print(f'''public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_{colorNameUpper}_TALLOW_CANDLE = BLOCKS.register(
                "{colorName}_tallow_candle",
                () -> new TallowCandleBlock({colorHex})
        );''')

for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]
    print(f'''public static final TallowCandleBlock {colorNameUpper}_TALLOW_CANDLE = Registry.SUPPLIER_{colorNameUpper}_TALLOW_CANDLE.get();''')
print("====================================")
for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]
    print(f'''public static final RegistrySupplier<BlockItem> SUPPLIER_{colorNameUpper}_TALLOW_CANDLE = ITEMS.register(
                "{colorName}_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.{colorNameUpper}_TALLOW_CANDLE,new Item.Properties())
        );''')

for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]
    print(
        f'''public static final BlockItem {colorNameUpper}_TALLOW_CANDLE = Registry.SUPPLIER_{colorNameUpper}_TALLOW_CANDLE.get();''')

for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]
    print(f"'{colorName}_tallow_candle',")


for i in range(16):
    colorName = colorNames[i]
    colorNameUpper = colorNames[i].upper()
    colorHex = colorsHex[i]

    print(f'''blockColors.register((blockState, blockAndTintGetter, blockPos, i)-> ThaumcraftBlocks.{colorNameUpper}_TALLOW_CANDLE.color,ThaumcraftBlocks.{colorNameUpper}_TALLOW_CANDLE);''')

for translationFileName in os.listdir(translationFilesPath):
    if translationFileName.endswith('.json'):

        with open(translationFilesPath/translationFileName,mode='r',encoding='utf-8') as translationFileRead:
            fileContent = translationFileRead.read()

        for i in range(16):
            colorName = colorNames[i]
            candleName = f'{colorName}_tallow_candle'
            fileContent = re.sub(
                r'"tile\.blockCandle\.'+str(i)+'\.name": "([\\S ]+)",',
                r'"block.thaumcraft.'+candleName+r'": "\1",\n    "item.thaumcraft.'+candleName+r'": "\1",',
                fileContent
            )

        with open(translationFilesPath/(translationFileName),mode='w+',encoding='utf-8') as f:
            f.write(fileContent)