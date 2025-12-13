# -*- coding: utf-8 -*-

# 老版 Potion meta 示例列表
old_metas = [0, 16, 8194, 8201, 8226]

# 对应 base potion 映射（示例，自己补全完整映射表）
base_potions = {
    0: "minecraft:water",            # water
    1: "minecraft:moveSpeed",        # moveSpeed
    2: "minecraft:moveSlowdown",     # moveSlowdown
    3: "minecraft:digSpeed",         # digSpeed
    4: "minecraft:digSlowdown",      # digSlowdown
    5: "minecraft:damageBoost",      # damageBoost
    6: "minecraft:heal",             # heal
    7: "minecraft:harm",             # harm
    8: "minecraft:jump",             # jump
    9: "minecraft:confusion",        # confusion
    10: "minecraft:regeneration",    # regeneration
    11: "minecraft:resistance",      # resistance
    12: "minecraft:fireResistance",  # fireResistance
    13: "minecraft:waterBreathing",  # waterBreathing
    14: "minecraft:invisibility",    # invisibility
    15: "minecraft:blindness",       # blindness
    16: "minecraft:nightVision",     # nightVision
    17: "minecraft:hunger",          # hunger
    18: "minecraft:weakness",        # weakness
    19: "minecraft:poison",          # poison
    20: "minecraft:wither",          # wither
    21: "minecraft:healthBoost",     # healthBoost
    22: "minecraft:absorption",      # absorption
    23: "minecraft:saturation",      # saturation
    24: "minecraft:unused_24",       # null placeholder
    25: "minecraft:unused_25",       # null placeholder
    26: "minecraft:unused_26",       # null placeholder
    27: "minecraft:unused_27",       # null placeholder
    28: "minecraft:unused_28",       # null placeholder
    29: "minecraft:unused_29",       # null placeholder
    30: "minecraft:unused_30",       # null placeholder
    31: "minecraft:unused_31",       # null placeholder
}



def decode_meta(meta):
    is_splash = bool(meta & 0x4000)
    is_long = bool(meta & 0x40)
    is_strong = bool(meta & 0x20)
    base_index = meta & 0xFF
    base_name = base_potions.get(base_index, f"unknown({base_index})")

    # 生成现代形式描述
    potion_variant = base_name
    if is_strong:
        potion_variant = f"strong_{potion_variant}"
    if is_long:
        potion_variant = f"long_{potion_variant}"

    item_type = "splash" if is_splash else "potion"

    return {
        "meta": meta,
        "item_type": item_type,
        "potion": potion_variant
    }


# 输出结果
# for meta in old_metas:
#     decoded = decode_meta(meta)
#     print(f"Meta {decoded['meta']:>5}: {decoded['item_type']:>6} | {decoded['potion']}")
while True:
    try:
        decoded = decode_meta(int(input("input meta:")))
        print(f"Meta {decoded['meta']:>5}: {decoded['item_type']:>6} | {decoded['potion']}")
    except:
        print("inputError?")