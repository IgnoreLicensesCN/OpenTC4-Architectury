# use this to generate IShapeConnectNearBlock models and needed code
import json
from pathlib import Path

mainPath = Path('./shape_connected_generated/')
if not mainPath.exists():
    mainPath.mkdir()
shape_connect_block_names = ['glowing_crusted_stone','glyphed_stone']

default_shape = (2,2,2,14,14,14)
offsets = {
    'UP': (0,0,0,0,2,0),
    'DOWN': (0,-2,0,0,0,0),
    'NORTH': (0,0,-2,0,0,0),
    'SOUTH': (0,0,0,0,0,2),
    'WEST': (-2,0,0,0,0,0),
    'EAST': (0,0,0,+2,0,0),
}

result_near_blocks_to_offsets_map_put = ''
voxel_shape_consts = ''
variants = '''{
  "variants": {
'''

for N in [False,True]:
    for S in [False,True]:
        for W in [False,True]:
            for E in [False,True]:
                for U in [False,True]:
                    for D in [False,True]:
                        shape:list[int,int,int,int,int,int] = list(default_shape)
                        flagPatterns = ['!northConnected','!southConnected','!westConnected','!eastConnected',
                                        '!upConnected','!downConnected']
                        blockStatePatterns = {
                            'north_connected':'false',
                            'south_connected':'false',
                            'west_connected':'false',
                            'east_connected':'false',
                            'up_connected':'false',
                            'down_connected':'false',
                        }
                        varName = ''
                        intKey = 0
                        if N:
                            shape = [shape[i] + offsets['NORTH'][i] for i in range(len(shape))]
                            varName += 'NORTH_'
                            flagPatterns[0] = 'northConnected'
                            blockStatePatterns['north_connected'] = 'true'
                            intKey += 1
                        if S:
                            shape = [shape[i] + offsets['SOUTH'][i] for i in range(len(shape))]
                            varName += 'SOUTH_'
                            flagPatterns[1] = 'southConnected'
                            blockStatePatterns['south_connected'] = 'true'
                            intKey += 2
                        if W:
                            shape = [shape[i] + offsets['WEST'][i] for i in range(len(shape))]
                            varName += 'WEST_'
                            flagPatterns[2] = 'westConnected'
                            blockStatePatterns['west_connected'] = 'true'
                            intKey += 2**2
                        if E:
                            shape = [shape[i] + offsets['EAST'][i] for i in range(len(shape))]
                            varName += 'EAST_'
                            flagPatterns[3] = 'eastConnected'
                            blockStatePatterns['east_connected'] = 'true'
                            intKey += 2**3
                        if U:
                            shape = [shape[i] + offsets['UP'][i] for i in range(len(shape))]
                            varName += 'UP_'
                            flagPatterns[4] = 'upConnected'
                            blockStatePatterns['up_connected'] = 'true'
                            intKey += 2**4
                        if D:
                            shape = [shape[i] + offsets['DOWN'][i] for i in range(len(shape))]
                            varName += 'DOWN_'
                            flagPatterns[5] = 'downConnected'
                            blockStatePatterns['down_connected'] = 'true'
                            intKey += 2**5

                        if varName == '':
                            varName = 'DEFAULT_'
                        varName += 'SHAPE'

                        flagPatternFinal = ''
                        for s in flagPatterns:
                            if s.startswith('!'):
                                flagPatternFinal += s + ' && '
                        if flagPatternFinal.endswith(' && '):
                            flagPatternFinal = flagPatternFinal[:-4]

                        blockStatePatternFinal = ''
                        for key in blockStatePatterns.keys():
                            blockStatePatternFinal += f'{key}={blockStatePatterns[key]},'
                        blockStatePatternFinal = blockStatePatternFinal[:-1]

                        voxel_shape_consts += f'VoxelShape {varName} = Block.box({shape[0]},{shape[1]},{shape[2]},{shape[3]},{shape[4]},{shape[5]});\n'
                        result_near_blocks_to_offsets_map_put += f'put({intKey},{varName});\n'
                        variants += f'    "{blockStatePatternFinal}": {{ "model": "thaumcraft:block/shape_connect_default/%bName%_{varName.lower()}" }},\n'

                        model_path = mainPath / 'models/block/shape_connect_default'
                        if not model_path.exists():
                            model_path.mkdir(parents=True)
                        for bName in shape_connect_block_names:
                            with open(model_path/f'{bName}_{varName.lower()}.json',mode='w+',encoding='utf-8') as f:
                                fx, fy, fz, tx, ty, tz = shape
                                model = {
                                    "parent": "block/block",
                                    "textures": {
                                        "all": f"thaumcraft:block/{bName}"
                                    },
                                    "elements": [
                                        {
                                            "from": [fx, fy, fz],
                                            "to":   [tx, ty, tz],
                                            "faces": {
                                                "north": {"texture": "#all", "uv": [fx, fy, tx, ty]},
                                                "south": {"texture": "#all", "uv": [fx, fy, tx, ty]},
                                                "west":  {"texture": "#all", "uv": [fz, fy, tz, ty]},
                                                "east":  {"texture": "#all", "uv": [fz, fy, tz, ty]},
                                                "up":    {"texture": "#all", "uv": [fx, fz, tx, tz]},
                                                "down":  {"texture": "#all", "uv": [fx, fz, tx, tz]},
                                            }
                                        }
                                    ]
                                }

                                json.dump(model, f, indent=2)

variants = variants[:-2] + '''
  }
}'''

print(voxel_shape_consts)
print(result_near_blocks_to_offsets_map_put)
for bName in shape_connect_block_names:
    blockStatePath = mainPath/'blockstates'
    if not blockStatePath.exists():
        blockStatePath.mkdir()
    with open(blockStatePath/f'{bName}.json',mode='w+',encoding='utf-8') as f:
        f.write(variants.replace('%bName%',f'{bName}'))