import re,os
from pathlib import Path

block_with_item_names = [
    ['tile.blockWoodenDevice.0.name','arcane_bellow'],
    ['tile.blockWoodenDevice.6.name','greatwood_planks'],
    ['tile.blockWoodenDevice.7.name','silverwood_planks'],
]

language_file_folder = Path('../common/src/main/resources/assets/thaumcraft/lang')

for fileName in os.listdir(language_file_folder):
    if fileName.endswith('.json'):
        language_file_path = language_file_folder/fileName
        language_text:str
        with open(language_file_path,mode='r',encoding='utf-8') as f2read:
            language_text = f2read.read()

        for block_with_item_name_pair in block_with_item_names:
            language_text = re.sub(rf'"{block_with_item_name_pair[0]}": "([ \S!"]+)",',rf'"block.thaumcraft.{block_with_item_name_pair[1]}": "\1",\n    "item.thaumcraft.{block_with_item_name_pair[1]}": "\1",',language_text)
            with open(language_file_path,mode='w',encoding='utf-8') as f2write:
                f2write.write(language_text)