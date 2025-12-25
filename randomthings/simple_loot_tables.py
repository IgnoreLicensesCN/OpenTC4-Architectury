import json
import os

# 这里填你的方块名字列表
block_names = [
    "cinnabar_ore",
    "greatwood_log",
    "silverwood_log",
    "greatwood_planks",
    "silverwood_planks",
    "greatwood_sapling",
    "silverwood_sapling",
    "obsidian_totem",
    "obsidian_tile",
    "paving_stone_travel",
    "paving_stone_warding",
    "thaumium_block",
    "tallow_block",
    "arcane_stone_block",
    "arcane_stone_bricks",
    "golem_fetter",
    "ancient_stone",
    "ancient_rock",
    "crusted_stone",
    "ancient_stone_pedestal",
    "ancient_stone_stairs",
    "arcane_stone_brick_stairs",
    "greatwood_planks_stairs",
    "silverwood_planks_stairs",
    "hungry_chest",
    "cinnabar_ore",
]

slab_names = [
    "ancient_stone_slab",
    "arcane_stone_brick_slab",
    "greatwood_planks_slab",
    "silverwood_planks_slab"
]

# 输出目录
output_dir = "loot_tables/blocks"

# 确保目录存在
os.makedirs(output_dir, exist_ok=True)

# 遍历生成 JSON
for s in block_names:
    loot_table = {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [
                    {"type": "minecraft:item", "name": f"thaumcraft:{s}"}
                ]
            }
        ]
    }

    # 写文件
    file_path = os.path.join(output_dir, f"{s}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(loot_table, f, indent=4)

    print(f"Generated {file_path}")

for s in slab_names:
    loot_table = {
        "type": "minecraft:block",
        "pools": [
            {
                "bonus_rolls": 0.0,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "functions": [
                            {
                                "add": False,
                                "conditions": [
                                    {
                                        "block": "minecraft:"+s,
                                        "condition": "minecraft:block_state_property",
                                        "properties": {
                                            "type": "double"
                                        }
                                    }
                                ],
                                "count": 2.0,
                                "function": "minecraft:set_count"
                            },
                            {
                                "function": "minecraft:explosion_decay"
                            }
                        ],
                        "name": "minecraft:"+s
                    }
                ],
                "rolls": 1.0
            }
        ],
        "random_sequence": "minecraft:blocks/"+s
    }


    file_path = os.path.join(output_dir, f"{s}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(loot_table, f, indent=4)

    print(f"Generated {file_path}")
