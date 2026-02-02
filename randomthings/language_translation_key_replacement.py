import json
import re, os
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
    ['tile.blockEldritch.8.name', ['ancient_lock_empty','ancient_lock_inserted']],
    ['tile.blockEldritch.9.name', 'eldritch_crab_spawner'],
    ['tile.blockEldritch.10.name', 'runed_stone'],
    ['tile.blockTaint.0.name','crusted_taint'],
    ['tile.blockTaint.1.name','tainted_soil'],
    ['tile.blockTaint.2.name','block_of_flesh'],
    ["tile.blockTaintFibres.0.name", "fibrous_taint"],
    ["tile.blockTaintFibres.1.name", "tainted_grass"],
    ["tile.blockTaintFibres.2.name", "tainted_plant"],
    ["tile.blockTaintFibres.3.name", "spore_stalk"],
    ["tile.blockTaintFibres.4.name", "mature_spore_stalk"],
    ["tile.blockTable.0.name","table"],
    ["tile.blockTable.1.name","table"]
]

language_file_folder = Path('../common/src/main/resources/assets/thaumcraft/lang')

for fileName in os.listdir(language_file_folder):
    if fileName.endswith('.json'):
        language_file_path = language_file_folder / fileName
        language_text: str
        with open(language_file_path, mode='r', encoding='utf-8') as f2read:
            language_text = f2read.read()

        language_dict = json.loads(language_text)

        for block_with_item_name_pair in block_with_item_names:
            key = block_with_item_name_pair[0];
            if key not in language_dict.keys():
                continue
            value = language_dict[key]
            remappedKey = block_with_item_name_pair[1]
            if isinstance(remappedKey,str):
                language_dict.pop(key)
                language_dict[f'block.thaumcraft.{remappedKey}'] = value
                language_dict[f'item.thaumcraft.{remappedKey}'] = value
            elif isinstance(remappedKey,list):
                language_dict.pop(key)
                for remappedKeyItem in remappedKey:
                    language_dict[f'block.thaumcraft.{remappedKeyItem}'] = value
                    language_dict[f'item.thaumcraft.{remappedKeyItem}'] = value
            else:
                raise Exception(str(block_with_item_name_pair))
            language_dict = dict(sorted(language_dict.items()))
            with open(language_file_path, mode='w', encoding='utf-8') as f2write:
                json.dump(language_dict, f2write, indent=2, ensure_ascii=False)

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
