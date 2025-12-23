import json
from pathlib import Path

# ===== 配置 =====
OUTPUT = Path("generated/assets/thaumcraft")
BLOCKS = [
    {
        "name": "greatwood_planks",
        "texture": "thaumcraft:block/greatwood_planks"
    },
    {
        "name": "silverwood_planks",
        "texture": "thaumcraft:block/silverwood_planks"
    },
    {
        "name": "arcane_stone_bricks",
        "texture": "thaumcraft:block/arcane_stone_bricks"
    }
]

# ===== 路径 =====
BLOCKSTATE_DIR = OUTPUT / "blockstates"
MODEL_DIR = OUTPUT / "models/block"
BLOCKSTATE_DIR.mkdir(parents=True, exist_ok=True)
MODEL_DIR.mkdir(parents=True, exist_ok=True)

# ===== 生成函数 =====
def write_json(path: Path, data: dict):
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2)

# ===== 生成 Slab 和 Stair 模型 =====
for block in BLOCKS:
    name = block["name"]
    tex = block["texture"]

    # ---- slab ----
    slab_dir = MODEL_DIR / f"{name}_slab"
    # bottom
    bottom = {
        "parent": "minecraft:block/slab",
        "textures": {"bottom": tex, "top": tex, "side": tex}
    }
    write_json(slab_dir / f"{name}_slab_bottom.json", bottom)
    # top
    top = {
        "parent": "minecraft:block/slab_top",
        "textures": {"bottom": tex, "top": tex, "side": tex}
    }
    write_json(slab_dir / f"{name}_slab_top.json", top)
    # double
    double = {"parent": "minecraft:block/cube_all", "textures": {"all": tex}}
    write_json(slab_dir / f"{name}_slab_double.json", double)

    # blockstates slab
    slab_variants = {
        "type=bottom": {"model": f"thaumcraft:block/{name}_slab/{name}_slab_bottom"},
        "type=top":    {"model": f"thaumcraft:block/{name}_slab/{name}_slab_top"},
        "type=double": {"model": f"thaumcraft:block/{name}_slab/{name}_slab_double"}
    }
    write_json(BLOCKSTATE_DIR / f"{name}_slab.json", {"variants": slab_variants})

    # ---- stairs ----
    stair_dir = MODEL_DIR / f"{name}_stairs"
    stairs_json = {
        "parent": "minecraft:block/stairs",
        "textures": {"bottom": tex, "top": tex, "side": tex}
    }
    inner_json = {
        "parent": "minecraft:block/inner_stairs",
        "textures": {"bottom": tex, "top": tex, "side": tex}
    }
    outer_json = {
        "parent": "minecraft:block/outer_stairs",
        "textures": {"bottom": tex, "top": tex, "side": tex}
    }
    write_json(stair_dir / f"{name}_stairs.json", stairs_json)
    write_json(stair_dir / f"{name}_stairs_inner.json", inner_json)
    write_json(stair_dir / f"{name}_stairs_outer.json", outer_json)

    # blockstates stairs
    stair_variants = {}
    facings = ["north", "south", "west", "east"]
    types = ["straight", "inner_left", "inner_right", "outer_left", "outer_right"]
    halves = ["bottom", "top"]
    for facing in facings:
        for type_ in types:
            for half in halves:
                key = f"facing={facing},half={half},shape={type_}"
                if type_ == "straight":
                    model = f"thaumcraft:block/{name}_stairs/{name}_stairs"
                elif type_.startswith("inner"):
                    model = f"thaumcraft:block/{name}_stairs/{name}_stairs_inner"
                else:
                    model = f"thaumcraft:block/{name}_stairs/{name}_stairs_outer"
                stair_variants[key] = {"model": model}
    write_json(BLOCKSTATE_DIR / f"{name}_stairs.json", {"variants": stair_variants})

print("Generated slabs and stairs for Greatwood Planks, Silverwood Planks, Arcane Stone Bricks.")
