import itertools
import json
import random
from pathlib import Path

# ========= 配置 =========
OUTPUT = Path("generated/assets/thaumcraft")

BLOCKSTATE_DIR = OUTPUT / "blockstates"
MODEL_DIR = OUTPUT / "models/block/runed_stone"

TEXTURE_BASE = "thaumcraft:block/runed_stone_face_"
CUBE_MODEL_BASE = "thaumcraft:block/runed_stone/cube/face_"
# STAIRS_MODEL_BASE = "thaumcraft:block/runed_stone/stairs/face_"

FACE_NAMES = ["down", "up", "north", "south", "west", "east"]

MAX_STATES = 64
RANDOM_SEED = 0x39c5bb
# ========================

random.seed(RANDOM_SEED)

# 创建目录
for d in [
    BLOCKSTATE_DIR,
    MODEL_DIR / "cube",
]:
    d.mkdir(parents=True, exist_ok=True)

# 1️⃣ 枚举并洗牌 4^6
all_faces = list(itertools.product(range(4), repeat=6))
random.shuffle(all_faces)
selected = all_faces[:MAX_STATES]

# 2️⃣ cube models
for idx, faces in enumerate(selected):
    textures = {
        face: f"{TEXTURE_BASE}{faces[i] + 1}"
        for i, face in enumerate(FACE_NAMES)
    }

    model = {
        "parent": "minecraft:block/cube",
        "textures": textures
    }

    with open(MODEL_DIR / "cube" / f"face_{idx}.json", "w", encoding="utf-8") as f:
        json.dump(model, f, indent=2)

# # 3️⃣ stairs models（straight / inner / outer）
# STAIR_PARENTS = {
#     "": "minecraft:block/stairs",
#     "_inner": "minecraft:block/inner_stairs",
#     "_outer": "minecraft:block/outer_stairs",
# }

# for idx, faces in enumerate(selected):
#     textures = {
#         face: f"{TEXTURE_BASE}{faces[i] + 1}"
#         for i, face in enumerate(FACE_NAMES)
#     }
#
#     for suffix, parent in STAIR_PARENTS.items():
#         model = {
#             "parent": parent,
#             "textures": {
#                 "bottom": textures["down"],
#                 "top": textures["up"],
#                 "side": textures["north"]
#             }
#         }
#
#         with open(
#             MODEL_DIR / "stairs" / f"face_{idx}{suffix}.json",
#             "w",
#             encoding="utf-8"
#         ) as f:
#             json.dump(model, f, indent=2)

# 4️⃣ runed_stone blockstate
block_variants = {
    f"face_state={i}": {
        "model": f"{CUBE_MODEL_BASE}{i}"
    }
    for i in range(MAX_STATES)
}

with open(BLOCKSTATE_DIR / "runed_stone.json", "w", encoding="utf-8") as f:
    json.dump({"variants": block_variants}, f, indent=2)

# 5️⃣ runed_stone_stairs blockstate
# stairs_variants = {}
#
# def stair_key(facing, half, shape):
#     return f"facing={facing},half={half},shape={shape}"
#
# ROT = {
#     "north": 270,
#     "south": 90,
#     "west": 180,
#     "east": 0,
# }
#
# for i in range(MAX_STATES):
#     for facing in ["north", "south", "west", "east"]:
#         for half in ["bottom", "top"]:
#             for shape in ["straight", "inner_left", "inner_right", "outer_left", "outer_right"]:
#                 if "inner" in shape:
#                     suffix = "_inner"
#                 elif "outer" in shape:
#                     suffix = "_outer"
#                 else:
#                     suffix = ""
#
#                 model = {
#                     "model": f"{STAIRS_MODEL_BASE}{i}{suffix}",
#                     "y": ROT[facing]
#                 }
#
#                 if half == "top":
#                     model["x"] = 180
#
#                 stairs_variants[
#                     f"face_state={i}," + stair_key(facing, half, shape)
#                 ] = model
#
# with open(BLOCKSTATE_DIR / "runed_stone_stairs.json", "w", encoding="utf-8") as f:
#     json.dump({"variants": stairs_variants}, f, indent=2)

# print(f"Generated {MAX_STATES} face_state variants for cube + stairs.")
