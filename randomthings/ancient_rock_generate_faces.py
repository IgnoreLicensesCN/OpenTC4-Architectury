import json
from pathlib import Path

# ===== 配置 =====
OUTPUT = Path("generated/assets/thaumcraft")

MODEL_DIR = OUTPUT / "models/block/ancient_rock"
BLOCKSTATE_DIR = OUTPUT / "blockstates"

TEXTURE_BASE = "thaumcraft:block/ancient_rock_"
BLOCK_MODEL_BASE = "thaumcraft:block/ancient_rock/face_"

FACE_NAMES = ["down", "up", "north", "south", "west", "east"]

FACE_STATE_MAX = 8  # 0~7
# =================

MODEL_DIR.mkdir(parents=True, exist_ok=True)
BLOCKSTATE_DIR.mkdir(parents=True, exist_ok=True)

# ===== 生成 models =====
for state in range(FACE_STATE_MAX):
    x_bit = state & 1          # x%2
    y_bit = (state >> 1) & 1   # y%2
    z_bit = (state >> 2) & 1   # z%2

    textures = {}
    for i, face in enumerate(FACE_NAMES):
        if i in (0, 1):        # down / up
            tex_idx = x_bit + z_bit * 2
        elif i in (2, 3):      # north / south
            tex_idx = x_bit + y_bit * 2
        elif i in (4, 5):      # west / east
            tex_idx = z_bit + y_bit * 2
        else:
            tex_idx = 0

        textures[face] = f"{TEXTURE_BASE}{tex_idx + 1}"

    model_json = {
        "parent": "minecraft:block/cube",
        "textures": textures
    }

    with open(MODEL_DIR / f"face_{state}.json", "w", encoding="utf-8") as f:
        json.dump(model_json, f, indent=2)

# ===== 生成 blockstates =====
variants = {
    f"face_state={state}": {
        "model": f"{BLOCK_MODEL_BASE}{state}"
    }
    for state in range(FACE_STATE_MAX)
}

with open(BLOCKSTATE_DIR / "ancient_rock.json", "w", encoding="utf-8") as f:
    json.dump({"variants": variants}, f, indent=2)

print(f"Generated {FACE_STATE_MAX} face states for AncientRockBlock")
