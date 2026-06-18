import json
import os
from pathlib import Path

block_with_item_names = [
    ['tile.blockWoodenDevice.0.name', ['arcane_bellow']],
    ['tile.blockWoodenDevice.6.name', ['greatwood_planks']],
    ['tile.blockWoodenDevice.7.name', ['silverwood_planks']],
    ['tile.blockArcaneDoor.name', ['arcane_door']],
    ['tile.blockEldritch.0.name', ['eldritch_altar']],
    ['tile.blockEldritch.1.name', 'eldritch_obelisk_with_ticker'],
    ['tile.blockEldritch.2.name', 'eldritch_obelisk'],
    ['tile.blockEldritch.3.name', 'eldritch_capstone'],
    ['tile.blockEldritch.4.name', 'glowing_crusted_stone'],
    ['tile.blockEldritch.5.name', 'glyphed_stone'],
    ['tile.blockEldritch.7.name', 'ancient_gateway'],
    ['tile.blockEldritch.8.name', ['ancient_lock_empty', 'ancient_lock_inserted']],
    ['tile.blockEldritch.9.name', 'eldritch_crab_spawner'],
    ['tile.blockEldritch.10.name', 'runed_stone'],
    ['tile.blockTaint.0.name', 'crusted_taint'],
    ['tile.blockTaint.1.name', 'tainted_soil'],
    ['tile.blockTaint.2.name', 'block_of_flesh'],
    ["tile.blockTaintFibres.0.name", "fibrous_taint"],
    ["tile.blockTaintFibres.1.name", "tainted_grass"],
    ["tile.blockTaintFibres.2.name", "tainted_plant"],
    ["tile.blockTaintFibres.3.name", "spore_stalk"],
    ["tile.blockTaintFibres.4.name", "mature_spore_stalk"],
    ["tile.blockTable.0.name", "table"],
    ["tile.blockTable.1.name", "table"],
    ['tile.blockTable.15.name', 'arcane_workbench'],
    ['tile.blockTable.14.name', 'deconstruction_table'],
    ['tile.blockTable.research.name', ['research_table_left_part', 'research_table_right_part']],
    ['block.thaumcraft.research_table', ['research_table_left_part', 'research_table_right_part']],
    ['item.thaumcraft.research_table', ['research_table_left_part', 'research_table_right_part']],
    ['item.researchnotes.name', 'research_note'],
    ['item.ItemInkwell.name', 'ink_well'],
    ['tile.blockAiry.1.name', 'nitor'],
    ['tile.blockAiry.2.name', 'glimmer_of_light'],
    ['tile.blockAiry.3.name', 'glimmer_of_light'],
    ['tile.blockAiry.5.name', 'energized_aura_node'],
    ['tile.blockMetalDevice.14.name', 'vis_relay'],
    ['tile.blockMetalDevice.2.name', 'vis_charge_relay'],
    ['tile.blockStoneDevice.9.name', 'node_stabilizer'],
    ['tile.blockStoneDevice.10.name', 'advanced_node_stabilizer'],
    ['tile.blockStoneDevice.11.name', 'node_transducer'],
    ['tile.blockStoneDevice.0.name', 'alchemical_furnace'],
    ['tile.blockMetalDevice.3.name', 'advanced_alchemical_construct'],
    ['tile.blockMetalDevice.9.name', 'alchemical_construct'],
    ['tile.blockMetalDevice.0.name', 'crucible'],
    ['tile.blockMetalDevice.1.name', 'arcane_alembic'],
    ['tile.blockMetalDevice.5.name', 'item_crate'],
    ['tile.blockMetalDevice.7.name', 'arcane_lamp'],
    ['tile.blockMetalDevice.8.name', 'growth_arcane_lamp'],
    ['tile.blockMetalDevice.11.name', ['thaumatorium_bottom', 'thaumatorium_top', 'thaumatorium']],
    ['tile.blockMetalDevice.12.name', 'mnemonic_matrix'],
    ['tile.blockMetalDevice.13.name', 'fertility_arcane_lamp'],
    ["tile.blockAlchemyFurnace.name", ['advanced_alchemical_furnace_alembic', 'advanced_alchemical_furnace_upper_fence',
                                       'advanced_alchemical_furnace_nozzle', 'advanced_alchemical_furnace_base_corner',
                                       'advanced_alchemical_furnace_base']],
    ['tile.blockJar.0.name', 'essentia_jar'],
    ['tile.blockJar.1.name', 'brain_jar'],
    ['tile.blockJar.2.name', 'node_jar'],
    ['tile.blockJar.3.name', 'void_jar'],
    ['tile.blockLifter.name', 'arcane_levitator'],
    ['tile.blockEssentiaReservoir.name', 'essentia_reservoir'],
    ["item.ItemManaBean.name", 'mana_bean'],
    ['tile.blockTube.0.name', 'essentia_tube'],
    ['tile.blockTube.1.name', 'essentia_tube_valve'],
    ['tile.blockTube.3.name', 'essentia_tube_filter'],
    ['tile.blockTube.4.name', 'essentia_buffer'],
    ['tile.blockTube.5.name', 'essentia_tube_restrict'],
    ['tile.blockTube.6.name', 'essentia_tube_oneway'],
    ['tile.blockTube.2.name', 'essentia_centrifuge'],
    ['tile.blockTube.7.name', 'essentia_crystallizer'],
    ['tile.blockWoodenDevice.2.name', 'arcane_pressure_plate'],
    ['tile.blockWoodenDevice.4.name', 'arcane_bore_base'],
    ['tile.blockWoodenDevice.5.name', ['arcane_bore_drill','arcane_bore']],
    ['tile.blockWoodenDevice.1.name', 'arcane_ear'],
    ['tile.blockFluidDeath.name', 'death_fluid'],
    ['tile.blockHole.name','hole'],
    ['tile.blockLootUrn.name','urn_loot'],
    ['tile.blockLootCrate.name','crate_loot'],
    ['tile.blockMirror.0.name','mirror'],
    ['tile.blockMirror.6.name','essentia_mirror'],
    ['tile.blockStoneDevice.1.name','arcane_pedestal'],
    ['tile.blockStoneDevice.3.name','infusion_pillar'],
    ['tile.blockStoneDevice.4.name','infusion_pillar'],
    ['tile.blockStoneDevice.6.name','infusion_pillar'],
    ['tile.blockStoneDevice.7.name','infusion_pillar'],
    ['tile.blockStoneDevice.2.name','infusion_matrix'],
    ['tile.blockStoneDevice.5.name','wand_recharge_pedestal'],
    ['tile.blockStoneDevice.8.name','compound_recharge_focus'],
    ['tile.blockStoneDevice.12.name','arcane_spa'],
    ['tile.blockStoneDevice.13.name','focal_manipulator'],
    ['tile.blockStoneDevice.14.name','flux_scrubber'],
    ['item.ItemPrimalCrusher.name','primal_crusher'],
    ['item.ItemSwordVoid.name','void_sword'],
    ['item.ItemShovelVoid.name','void_shovel'],
    ['item.ItemPickVoid.name','void_pickaxe'],
    ['item.ItemAxeVoid.name','void_axe'],
    ['item.ItemHoeVoid.name','void_hoe'],
    ['item.ItemSwordThaumium.name','thaumium_sword'],
    ['item.ItemShovelThaumium.name','thaumium_shovel'],
    ['item.ItemPickThaumium.name','thaumium_pickaxe'],
    ['item.ItemAxeThaumium.name','thaumium_axe'],
    ['item.ItemHoeThaumium.name','thaumium_hoe'],
    ['item.ItemHoeThaumium.name','thaumium_hoe'],
    ['item.ItemHelmetVoid.name','void_helmet'],
    ['item.ItemChestplateVoid.name','void_chestplate'],
    ['item.ItemLeggingsVoid.name','void_leggings'],
    ['item.ItemBootsVoid.name','void_boots'],
    ['item.ItemHelmetThaumium.name','thaumium_helmet'],
    ['item.ItemChestplateThaumium.name','thaumium_chestplate'],
    ['item.ItemLeggingsThaumium.name','thaumium_leggings'],
    ['item.ItemBootsThaumium.name','thaumium_boots'],
    ['item.ItemBucketDeath.name','death_fluid_bucket'],
    ['item.ItemBucketPure.name','pure_fluid_bucket'],
    ['item.ItemBathSalts.name','bath_salts'],
    ['item.ItemNugget.5.name','quicksilver_drop'],
    ['item.ItemNugget.6.name','thaumium_nugget'],
    ['item.ItemNugget.7.name','void_nugget'],
    ['item.ItemNugget.1.name','copper_nugget'],
    ['tile.blockFluxGas.name','flux_gas'],
    ['tile.blockFluxGoo.name','flux_goo'],
    ['tile.blockFluidPure.name','pure_fluid'],
    ['tile.blockMirror.name','mirror'],
    ['tile.blockPortalEldritch.name','eldritch_portal'],
    ['item.ItemShovelElemental.name','elemental_shovel'],
    ['item.ItemPickaxeElemental.name','elemental_pickaxe'],
    ['item.ItemSwordElemental.name','elemental_sword'],
    ['item.ItemAxeElemental.name','elemental_axe'],
    ['item.ItemHoeElemental.name','elemental_hoe'],
    ['item.ItemBowBone.name','bone_bow'],
    ['item.ItemSwordCrimson.name','crimson_sword'],
    ['item.ItemPrimalArrow.0.name','air_arrow'],
    ['item.ItemPrimalArrow.1.name','fire_arrow'],
    ['item.ItemPrimalArrow.2.name','water_arrow'],
    ['item.ItemPrimalArrow.3.name','earth_arrow'],
    ['item.ItemPrimalArrow.4.name','order_arrow'],
    ['item.ItemPrimalArrow.5.name','entropy_arrow'],
    ['item.ItemChestplateRobe.name','robe_chestplate'],
    ['item.ItemBootsRobe.name','robe_boots'],
    ['item.ItemLeggingsRobe.name','robe_leggings'],
    ['item.HoverHarness.name','thaumostatic_harness'],
    ['item.BootsTraveller.name','traveller_boots'],
    ['item.ItemBootsCultist.name','cultist_boots'],
    ['item.ItemChestplateCultistPlate.name','cultist_plate_chestplate'],
    ['item.ItemLeggingsCultistPlate.name','cultist_plate_leggings'],
    ['item.ItemHelmetCultistPlate.name','cultist_plate_helmet'],
    ['item.ItemChestplateCultistRobe.name','cultist_robe_chestplate'],
    ['item.ItemLeggingsCultistRobe.name','cultist_robe_leggings'],
    ['item.ItemHelmetCultistRobe.name','cultist_robe_helmet'],
    ['item.ItemChestplateCultistLeaderPlate.name','cultist_leader_plate_chestplate'],
    ['item.ItemLeggingsCultistLeaderPlate.name','cultist_leader_plate_leggings'],
    ['item.ItemHelmetCultistLeaderPlate.name','cultist_leader_plate_helmet'],
    ['item.ItemLeggingsVoidRobe.name','void_robe_leggings'],
    ['item.ItemChestplateVoidRobe.name','void_robe_chestplate'],
    ['item.ItemHelmetVoidRobe.name','void_robe_helmet'],

    ['item.ItemHelmetFortress.name','thaumium_fortress_helmet'],
    ['item.ItemChestplateFortress.name','thaumium_fortress_chestplate'],
    ['item.ItemLeggingsFortress.name','thaumium_fortress_leggings'],

    ['item.HelmetFortress.mask.0','grinning_devil_mask'],
    ['item.HelmetFortress.mask.1','angry_ghost_mask'],
    ['item.HelmetFortress.mask.2','sipping_fiend_mask'],
    ['item.HelmetFortress.mask.2','sipping_fiend_mask'],
    ['item.ItemEssence.1.name','essentia_phial'],
    ['item.ItemCompassStone.name','compass_stone'],

    ['item.ItemNuggetChicken.name','chicken_nugget'],
    ['item.ItemNuggetBeef.name','beef_nugget'],
    ['item.ItemNuggetPork.name','pork_nugget'],
    ['item.ItemNuggetFish.name','cod_nugget'],
    ['item.TripleMeatTreat.name','triple_meat'],
    ['item.HandMirror.name','hand_mirror'],
    ['item.ItemNugget.16.name','iron_cluster'],
    ['item.ItemNugget.17.name','copper_cluster'],
    ['item.ItemNugget.18.name','tin_cluster'],
    ['item.ItemNugget.19.name','silver_cluster'],
    ['item.ItemNugget.20.name','lead_cluster'],
    ['item.ItemNugget.21.name','cinnabar_cluster'],
    ['item.ItemNugget.31.name','gold_cluster'],
    ['tile.blockCosmeticSolid.9.name','golem_fetter'],
    ['tile.blockCosmeticSolid.13.name','ancient_stone'],
    ['item.ItemSanityChecker.name','sanity_checker'],
    ['item.ItemSanitySoap.name','sanity_soap'],
    ['item.ArcaneDoorKey.0.name','iron_key'],
    ['item.ArcaneDoorKey.1.name','gold_key'],

    ['item.ItemAmuletVis.0.name','vis_amulet'],
    ['item.ItemAmuletVis.1.name','reinforced_vis_amulet'],

    ['item.ItemBaubleBlanks.0.name',"mundane_amulet"],
    ['item.ItemBaubleBlanks.1.name',"mundane_ring"],
    ['item.ItemBaubleBlanks.2.name',"mundane_belt"],
    ['item.ItemBaubleBlanks.3.name',[
        ["air_apprentices_ring",["%TYPE","Aer"]],
        ["water_apprentices_ring",["%TYPE","Aqua"]],
        ["fire_apprentices_ring",["%TYPE","Ignis"]],
        ["earth_apprentices_ring",["%TYPE","Terra"]],
        ["order_apprentices_ring",["%TYPE","Ordo"]],
        ["entropy_apprentices_ring",["%TYPE","Perditio"]]
    ]],

    ['item.ItemRingRunic.0.name','protection_ring'],
    ['item.ItemRingRunic.1.name','runic_shield_ring'],
    ['item.ItemRingRunic.2.name','charged_runic_shield_ring'],
    ['item.ItemRingRunic.3.name','revitalizing_runic_shield_ring'],

    ['item.ItemLootBag.0.name','common_loot_bag'],
    ['item.ItemLootBag.1.name','uncommon_loot_bag'],
    ['item.ItemLootBag.2.name','rare_loot_bag'],

    ['item.ItemGirdleHover.name','hover_girdle'],
    
    ['item.ItemAmuletRunic.0.name','runic_amulet'],
    ['item.ItemAmuletRunic.1.name','emergency_runic_amulet'],

    ['item.ItemGirdleRunic.0.name','runic_girdle'],
    ['item.ItemGirdleRunic.1.name','kinetic_runic_girdle'],
    ['item.FocusPouch.name','focus_pouch'],

    ['item.ItemResonator.name','essentia_resonator'],
    ['item.ItemBottleTaint.name','taint_bottle'],
    ['item.FocusExcavation.name','excavation_focus'],
    ['item.FocusFire.name','fire_focus'],
    ['item.FocusShock.name','shock_focus'],

    # ['tc.research_name.RUNICARMOR',['tc.research_name.RUNICARMOR','runic_shield.thaumcraft.runic_armor']],
    # ['tc.research_name.RUNICCHARGED',['tc.research_name.RUNICCHARGED','runic_shield.thaumcraft.runic_charged']],
    # ['tc.research_name.RUNICEMERGENCY',['tc.research_name.RUNICEMERGENCY','runic_shield.thaumcraft.runic_emergency']],
    # ['tc.research_name.RUNICHEALING',['tc.research_name.RUNICHEALING','runic_shield.thaumcraft.runic_healing']],
    # ['tc.research_name.RUNICKINETIC',['tc.research_name.RUNICKINETIC','runic_shield.thaumcraft.runic_kinetic']],
]

force_add_keys = {
    "thaumcraft.use_block.arcane_pressure_plate_setting.0": "It will now trigger on everything.",
    "thaumcraft.use_block.arcane_pressure_plate_setting.1": "It will now trigger on everything except you.",
    "thaumcraft.use_block.arcane_pressure_plate_setting.2": "It will now trigger on just you.",
}

language_file_folder = Path('../common/src/main/resources/assets/thaumcraft/lang')

def special_key_sort_weight(parts:list[str]):
    if parts[-1].startswith("mundane_"):
        return 7

    if parts[-1].endswith("_arrow"):
        return 1
    if parts[-1].endswith("_apprentices_ring"):
        return 2
    if parts[-1].endswith("_candle"):
        return 3
    if parts[-1].endswith("_girdle"):
        return 4
    if parts[-1].endswith("_amulet"):
        return 5
    if parts[-1].endswith("_ring"):
        return 6
    if parts[-1].endswith("_focus") and len(parts[-1].split("_")) == 2:
        return 8
    return 0

def key_sorter(key_string):
    parts = key_string.split('.')

    part_count = len(parts)
    partsCopy = parts.copy()
    for i in range(len(partsCopy)):
        if partsCopy[i].isdigit():
            partsCopy[i] = "%04d" % int(partsCopy[i])

    return (parts[0],part_count, parts[1],special_key_sort_weight(parts),partsCopy)

for fileName in os.listdir(language_file_folder):
    if fileName.endswith('.json'):
        language_file_path = language_file_folder / fileName
        language_text: str
        with open(language_file_path, mode='r', encoding='utf-8') as f2read:
            language_text = f2read.read()

        language_dict = json.loads(language_text)

        for force_add_key in force_add_keys.keys():
            language_dict[force_add_key] = force_add_keys[force_add_key]
        for block_with_item_name_pair in block_with_item_names:
            key = block_with_item_name_pair[0]
            if key not in language_dict.keys():
                continue
            value = language_dict[key]
            remappedKey = block_with_item_name_pair[1]
            if isinstance(remappedKey, str):
                # if key is not None:
                language_dict.pop(key)
                language_dict[f'block.thaumcraft.{remappedKey}'] = value
                language_dict[f'item.thaumcraft.{remappedKey}'] = value
            elif isinstance(remappedKey, list):
                # if key is not None:
                language_dict.pop(key)
                for remappedKeyItem in remappedKey:
                    if (isinstance(remappedKeyItem,list)
                        and len(remappedKeyItem) == 2
                        and isinstance(remappedKeyItem[0],str)
                        and isinstance(remappedKeyItem[1],list)
                        and len(remappedKeyItem[1]) == 2
                        and isinstance(remappedKeyItem[1][0],str)
                        and isinstance(remappedKeyItem[1][1],str)
                        ):
                        replacedValue = value.replace(remappedKeyItem[1][0], remappedKeyItem[1][1])
                        language_dict[f'block.thaumcraft.{remappedKeyItem[0]}'] = replacedValue
                        language_dict[f'item.thaumcraft.{remappedKeyItem[0]}'] = replacedValue
                    else:
                        language_dict[f'block.thaumcraft.{remappedKeyItem}'] = value
                        language_dict[f'item.thaumcraft.{remappedKeyItem}'] = value
            else:
                raise Exception(str(block_with_item_name_pair))
        sorted_keys = (sorted(list(language_dict.keys()), key=key_sorter))
        language_dict_final = {k: language_dict[k] for k in sorted_keys}
        with open(language_file_path, mode='w', encoding='utf-8') as f2write:
            json.dump(language_dict_final, f2write, indent=2, ensure_ascii=False)

            # replace_pattern = ''
            # if isinstance(block_with_item_name_pair[1],list):
            #     for addName in block_with_item_name_pair[1]:
            #         replace_pattern += rf'"block.thaumcraft.{addName}": "\1",\n    "item.thaumcraft.{addName}": "\1",'
            #         replace_pattern += '\n    '
            #         replace_pattern = replace_pattern[:len('\n    ')]
            # elif isinstance(block_with_item_name_pair[1],str):
            #     replace_pattern += rf'"block.thaumcraft.{block_with_item_name_pair[1]}": "\1",\n    "item.thaumcraft.{block_with_item_name_pair[1]}": "\1",'
            # else:
            #     raise Exception(str(block_with_item_name_pair))
            #
            # language_text = re.sub(rf'"{block_with_item_name_pair[0]}": "([ \S!"]+)",', replace_pattern, language_text)
            # with open(language_file_path, mode='w', encoding='utf-8') as f2write:
            #     f2write.write(language_text)
