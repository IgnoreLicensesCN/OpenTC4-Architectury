import itertools
import json
import random
from pathlib import Path

# ====== 配置 ======
OUTPUT = Path("generated")
MODEL_DIR = OUTPUT / "models/block"
BLOCKSTATE_DIR = OUTPUT / "blockstates"

TEXTURE_BASE = "thaumcraft:block/ancient_stone_"
BLOCK_MODEL_BASE = "thaumcraft:block/ancient_stone_face_"

FACE_NAMES = ["down", "up", "north", "south", "west", "east"]

MAX_STATES = 64          # 你要用的 FACE_STATE 数量
RANDOM_SEED = 114514     # 固定种子，方便复现
# ==================

random.seed(RANDOM_SEED)

MODEL_DIR.mkdir(parents=True, exist_ok=True)
BLOCKSTATE_DIR.mkdir(parents=True, exist_ok=True)

# 1️⃣ 枚举所有 4^6 组合
all_faces = list(itertools.product(range(4), repeat=6))
random.shuffle(all_faces)

selected = all_faces[:MAX_STATES]

# 2️⃣ 生成 models
for idx, faces in enumerate(selected):
    textures = {
        face: f"{TEXTURE_BASE}{faces[i] + 1}"
        for i, face in enumerate(FACE_NAMES)
    }

    model = {
        "parent": "minecraft:block/cube",
        "textures": textures
    }

    with open(MODEL_DIR / f"ancient_stone_face_{idx}.json", "w", encoding="utf-8") as f:
        json.dump(model, f, indent=2)

# 3️⃣ 生成 blockstates
variants = {}
for i in range(MAX_STATES):
    variants[f"face_state={i}"] = {
        "model": f"{BLOCK_MODEL_BASE}{i}"
    }

blockstate = {
    "variants": variants
}

with open(BLOCKSTATE_DIR / "ancient_stone.json", "w", encoding="utf-8") as f:
    json.dump(blockstate, f, indent=2)

print(f"Generated {MAX_STATES} states.")
