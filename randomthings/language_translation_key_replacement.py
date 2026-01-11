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
]

language_file_folder = Path('../common/src/main/resources/assets/thaumcraft/lang')

for fileName in os.listdir(language_file_folder):
    if fileName.endswith('.json'):
        language_file_path = language_file_folder / fileName
        language_text: str
        with open(language_file_path, mode='r', encoding='utf-8') as f2read:
            language_text = f2read.read()

        for block_with_item_name_pair in block_with_item_names:
            replace_pattern = ''
            if isinstance(block_with_item_name_pair[1],list):
                for addName in block_with_item_name_pair[1]:
                    replace_pattern += rf'"block.thaumcraft.{addName}": "\1",\n    "item.thaumcraft.{addName}": "\1",'
                    replace_pattern += '\n    '
                    replace_pattern = replace_pattern[:len('\n    ')]
            elif isinstance(block_with_item_name_pair[1],str):
                replace_pattern += rf'"block.thaumcraft.{block_with_item_name_pair[1]}": "\1",\n    "item.thaumcraft.{block_with_item_name_pair[1]}": "\1",'
            else:
                raise Exception(str(block_with_item_name_pair))

            language_text = re.sub(rf'"{block_with_item_name_pair[0]}": "([ \S!"]+)",', replace_pattern, language_text)
            with open(language_file_path, mode='w', encoding='utf-8') as f2write:
                f2write.write(language_text)
