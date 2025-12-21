import json
import os

modid = "thaumcraft"
block = "obsidian_totem"
out_dir = "./generated_json"

os.makedirs(out_dir + "/blockstates", exist_ok=True)
os.makedirs(out_dir + "/models/block", exist_ok=True)

# ---------------------------
# blockstates
# ---------------------------
blockstates = {
    "multipart": [
        # 上方有
        {
            "when": {"up": "true"},
            "apply": {"model": f"{modid}:block/{block}_shaded"}
        },
        # 上下都没有
        {
            "when": {"up": "false", "down": "false"},
            "apply": {"model": f"{modid}:block/{block}_base"}
        },
        # 上方没有，下方有
        {
            "when": {"up": "false", "down": "true", "rand": 0},
            "apply": {"model": f"{modid}:block/{block}_rand0"}
        },
        {
            "when": {"up": "false", "down": "true", "rand": 1},
            "apply": {"model": f"{modid}:block/{block}_rand1"}
        },
        {
            "when": {"up": "false", "down": "true", "rand": 2},
            "apply": {"model": f"{modid}:block/{block}_rand2"}
        },
        {
            "when": {"up": "false", "down": "true", "rand": 3},
            "apply": {"model": f"{modid}:block/{block}_rand3"}
        }
    ]
}

with open(f"{out_dir}/blockstates/{block}.json", "w") as f:
    json.dump(blockstates, f, indent=2)

# ---------------------------
# models
# ---------------------------
# 1. shaded
shaded = {
    "parent": "minecraft:block/cube",
    "textures": {
        "up": "thaumcraft:block/obsidiantotembaseshaded",
        "down": "thaumcraft:block/obsidiantotembaseshaded",
        "north": "thaumcraft:block/obsidiantotembaseshaded",
        "south": "thaumcraft:block/obsidiantotembaseshaded",
        "west": "thaumcraft:block/obsidiantotembaseshaded",
        "east": "thaumcraft:block/obsidiantotembaseshaded"
    }
}
with open(f"{out_dir}/models/block/{block}_shaded.json", "w") as f:
    json.dump(shaded, f, indent=2)

# 2. base
base = {
    "parent": "minecraft:block/cube",
    "textures": {
        "up": "thaumcraft:block/obsidiantotembase",
        "down": "thaumcraft:block/obsidiantotembase",
        "north": "thaumcraft:block/obsidiantotembase",
        "south": "thaumcraft:block/obsidiantotembase",
        "west": "thaumcraft:block/obsidiantotembase",
        "east": "thaumcraft:block/obsidiantotembase"
    }
}
with open(f"{out_dir}/models/block/{block}_base.json", "w") as f:
    json.dump(base, f, indent=2)

# 3. rand0~rand3
rand_textures = [
    ["obsidiantotem1", "obsidiantotem2", "obsidiantotem3", "obsidiantotem4"],
    ["obsidiantotem2", "obsidiantotem3", "obsidiantotem4", "obsidiantotem1"],
    ["obsidiantotem3", "obsidiantotem4", "obsidiantotem1", "obsidiantotem2"],
    ["obsidiantotem4", "obsidiantotem1", "obsidiantotem2", "obsidiantotem3"]
]

for i in range(4):
    model = {
        "parent": "minecraft:block/cube",
        "textures": {
            "up": "thaumcraft:block/obsidiantotembase",
            "down": "thaumcraft:block/obsidiantile",
            "north": f"thaumcraft:block/{rand_textures[i][0]}",
            "south": f"thaumcraft:block/{rand_textures[i][1]}",
            "west": f"thaumcraft:block/{rand_textures[i][2]}",
            "east": f"thaumcraft:block/{rand_textures[i][3]}"
        }
    }
    with open(f"{out_dir}/models/block/{block}_rand{i}.json", "w") as f:
        json.dump(model, f, indent=2)

print("生成完成，路径：", out_dir)
