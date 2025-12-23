import json
from pathlib import Path

# ===== 配置 =====
OUTPUT = Path("generated/assets/thaumcraft")

BLOCKSTATE_DIR = OUTPUT / "blockstates"
MODEL_DIR = OUTPUT / "models/block/ancient_stone/slab"

TEXTURE_MODEL_BASE = "thaumcraft:block/ancient_stone/cube/face_"

MAX_STATES = 64
# ========================

# 创建目录
MODEL_DIR.mkdir(parents=True, exist_ok=True)
BLOCKSTATE_DIR.mkdir(parents=True, exist_ok=True)

# ===== 1️⃣ 生成 slab models =====
for state in range(MAX_STATES):
    # bottom slab
    bottom = {
        "parent": "minecraft:block/slab",
        "textures": {
            "bottom": f"{TEXTURE_MODEL_BASE}{state}",
            "top": f"{TEXTURE_MODEL_BASE}{state}",
            "side": f"{TEXTURE_MODEL_BASE}{state}",
        }
    }

    # top slab
    top = {
        "parent": "minecraft:block/slab_top",
        "textures": {
            "bottom": f"{TEXTURE_MODEL_BASE}{state}",
            "top": f"{TEXTURE_MODEL_BASE}{state}",
            "side": f"{TEXTURE_MODEL_BASE}{state}",
        }
    }

    # double slab = cube
    double = {
        "parent": f"{TEXTURE_MODEL_BASE}{state}"
    }

    with open(MODEL_DIR / f"face_{state}_bottom.json", "w", encoding="utf-8") as f:
        json.dump(bottom, f, indent=2)

    with open(MODEL_DIR / f"face_{state}_top.json", "w", encoding="utf-8") as f:
        json.dump(top, f, indent=2)

    with open(MODEL_DIR / f"face_{state}_double.json", "w", encoding="utf-8") as f:
        json.dump(double, f, indent=2)

# ===== 2️⃣ 生成 blockstates =====
variants = {}

for state in range(MAX_STATES):
    variants[f"face_state={state},type=bottom"] = {
        "model": f"thaumcraft:block/ancient_stone/slab/face_{state}_bottom"
    }
    variants[f"face_state={state},type=top"] = {
        "model": f"thaumcraft:block/ancient_stone/slab/face_{state}_top"
    }
    variants[f"face_state={state},type=double"] = {
        "model": f"thaumcraft:block/ancient_stone/slab/face_{state}_double"
    }

with open(BLOCKSTATE_DIR / "ancient_stone_slab.json", "w", encoding="utf-8") as f:
    json.dump({"variants": variants}, f, indent=2)

print("Generated AncientStone slab models & blockstates.")
