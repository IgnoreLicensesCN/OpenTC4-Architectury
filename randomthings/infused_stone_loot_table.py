import json
import os

aspectTypeNames = [
    'air','fire','water','earth','order','entropy'
]

output_dir = "loot_tables/blocks"

for aspectName in aspectTypeNames:

    blockName = aspectName + '_infused_stone'
    shardName = aspectName + '_shard'
    loot_table = {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [
                    {
                        "type": "minecraft:alternatives",
                        "children": [
                            # Silk touch 掉本体
                            {
                                "type": "minecraft:item",
                                "name": "thaumcraft:" + blockName,
                                "conditions": [
                                    {
                                        "condition": "minecraft:match_tool",
                                        "predicate": {
                                            "enchantments": [
                                                {
                                                    "enchantment": "minecraft:silk_touch",
                                                    "levels": {"min": 1}
                                                }
                                            ]
                                        }
                                    }
                                ]
                            },
                            # 非 silk touch 掉 shard
                            {
                                "type": "minecraft:item",
                                "name": "thaumcraft:" + shardName,
                                "functions": [
                                    # count = 1 + random.nextInt(2 + fortune)
                                    {
                                        "function": "minecraft:set_count",
                                        "count": {
                                            "type": "minecraft:uniform",
                                            "min": 1,
                                            "max": 3
                                        }
                                    },
                                    # fortune 叠加
                                    {
                                        "function": "minecraft:apply_bonus",
                                        "enchantment": "minecraft:fortune",
                                        "formula": "minecraft:ore_drops"
                                    },
                                    # 爆炸衰减
                                    {
                                        "function": "minecraft:explosion_decay"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    }


    file_path = os.path.join(output_dir, f"{blockName}.json")
    with open(file_path, "w", encoding="utf-8") as f:
        json.dump(loot_table, f, indent=4)

    print(f"Generated {file_path}")