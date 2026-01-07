import os
from pathlib import Path

#(x,y,z) -> (up,down,W,E,N,S)
blockTextureIndexMap = {
    (2, 2, 2): (20, 2, 7, 3, 2, 7),
    (2, 2, 1): (11, 11, 7, 4, 7, 7),
    (2, 2, 0): (2, 20, 7, 5, 7, 0),
    (2, 1, 2): (20, 26, 6, 12, 11, 6),
    (2, 1, 1): (27, 27, 15, 13, 27, 27),
    (2, 1, 0): (2, 20, 6, 14, 6, 9),
    (2, 0, 2): (20, 20, 7, 21, 20, 7),
    (2, 0, 1): (26, 11, 7, 22, 7, 7),
    (2, 0, 0): (2, 2, 7, 23, 7, 18),

    (1, 2, 2): (19, 19, 7, 7, 1, 7),
    # (1, 2, 1): ,
    (1, 2, 0): (1, 1, 7, 7, 1, 1),
    (1, 1, 2): (19, 19, 6, 6, 10, 7),
    # (1, 1, 1): (27, 27, 13, 15, 27, 27),
    (1, 1, 0): (1, 1, 6, 6, 7, 10),
    (1, 0, 2): (25, 19, 7, 7, 19, 7),
    (1, 0, 1): (7, 10, 7, 7, 7, 7),
    (1, 0, 0): (16, 1, 7, 7, 7, 19),

    (0,2,2):(18,18,2,7,0,7),
    (0,2,1):(9,9,1,4,7,7),
    (0,2,0):(0,0,0,5,7,2),
    (0,1,2):(18,18,11,6,9,6),
    (0,1,1):(9,9,10,6,6,6),
    (0,1,0):(0,0,9,6,6,1),
    (0,0,2):(18,20,7,7,18,7),
    (0,0,1):(17,19,7,7,7,7),
    (0,0,0):(0,0,18,7,7,20),

}

blockstates_folder = Path("./generated/assets/thaumcraft/blockstates/")
models_folder = Path("./generated/assets/thaumcraft/models/infernal_furnace/")
if not blockstates_folder.exists():
    blockstates_folder.mkdir(parents=True)
if not models_folder.exists():
    models_folder.mkdir(parents=True)

corner_file = blockstates_folder / "infernal_furnace_corner.json"
with open(corner_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        counter = 0
        for x in [0,2]:
            for y in [0,2]:
                for z in [0,2]:
                    toWrite = f'''
    "corner={counter},rotation_y_axis={rot}": {{
          "model": "thaumcraft:block/infernal_furnace/corner_{counter}_rot_{rot}"
        }},'''
                    allToWrite += toWrite
                    counter += 1
    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
    }}
    ''')
for rot in range(4):
    counter = 0
    for x in [0,2]:
        for y in [0,2]:
            for z in [0,2]:
                facesKey = (x,y,z)
                if facesKey in blockTextureIndexMap.keys():
                    facesValues = blockTextureIndexMap[facesKey]
                    furnace_index_up = facesValues[0]
                    furnace_index_down = facesValues[1]
                    furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
                    furnace_index_N = furnaceNWSE[(0 + rot)%4]
                    furnace_index_W = furnaceNWSE[(1 + rot)%4]
                    furnace_index_S = furnaceNWSE[(2 + rot)%4]
                    furnace_index_E = furnaceNWSE[(3 + rot)%4]
                    modelFile = models_folder/f'corner_{counter}_rot_{rot}.json'
                    with open(modelFile,mode='w+') as f:
                        f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')
                counter += 1

bar_coord = (2,1,1)
bar_file = blockstates_folder / "infernal_furnace_bar.json"
with open(bar_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        toWrite = f'''
            "rotation_y_axis={rot}": {{
                  "model": "thaumcraft:block/infernal_furnace/bar_rot_{rot}"
                }},'''
        allToWrite += toWrite

    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
        }}
        ''')
for rot in range(4):
    facesKey = bar_coord
    if facesKey in blockTextureIndexMap.keys():
        facesValues = blockTextureIndexMap[facesKey]
        furnace_index_up = facesValues[0]
        furnace_index_down = facesValues[1]
        furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
        furnace_index_N = furnaceNWSE[(0 + rot)%4]
        furnace_index_W = furnaceNWSE[(1 + rot)%4]
        furnace_index_S = furnaceNWSE[(2 + rot)%4]
        furnace_index_E = furnaceNWSE[(3 + rot)%4]
        modelFile = models_folder/f'bar_rot_{rot}.json'
        with open(modelFile,mode='w+') as f:
            f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')


# y axis
y_axis_file = blockstates_folder / "infernal_furnace_y_axis.json"
with open(y_axis_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        counter = 0
        for x in [0,2]:
            for y in [1]:
                for z in [0,2]:
                    toWrite = f'''
    "edge_type_y_axis={counter},rotation_y_axis={rot}": {{
          "model": "thaumcraft:block/infernal_furnace/y_axis_{counter}_rot_{rot}"
        }},'''
                    allToWrite += toWrite
                    counter += 1
    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
    }}
    ''')
for rot in range(4):
    counter = 0
    for x in [0,2]:
        for y in [1]:
            for z in [0,2]:
                facesKey = (x,y,z)
                if facesKey in blockTextureIndexMap.keys():
                    facesValues = blockTextureIndexMap[facesKey]
                    furnace_index_up = facesValues[0]
                    furnace_index_down = facesValues[1]
                    furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
                    furnace_index_N = furnaceNWSE[(0 + rot)%4]
                    furnace_index_W = furnaceNWSE[(1 + rot)%4]
                    furnace_index_S = furnaceNWSE[(2 + rot)%4]
                    furnace_index_E = furnaceNWSE[(3 + rot)%4]
                    modelFile = models_folder/f'y_axis_{counter}_rot_{rot}.json'
                    with open(modelFile,mode='w+') as f:
                        f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')
                counter += 1


# x axis
x_axis_file = blockstates_folder / "infernal_furnace_x_axis.json"
with open(x_axis_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        counter = 0
        for x in [1]:
            for y in [0,2]:
                for z in [0,2]:
                    toWrite = f'''
    "edge_type_x_axis={counter},rotation_y_axis={rot}": {{
          "model": "thaumcraft:block/infernal_furnace/x_axis_{counter}_rot_{rot}"
        }},'''
                    allToWrite += toWrite
                    counter += 1
    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
    }}
    ''')
for rot in range(4):
    counter = 0
    for x in [1]:
        for y in [0,2]:
            for z in [0,2]:
                facesKey = (x,y,z)
                if facesKey in blockTextureIndexMap.keys():
                    facesValues = blockTextureIndexMap[facesKey]
                    furnace_index_up = facesValues[0]
                    furnace_index_down = facesValues[1]
                    furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
                    furnace_index_N = furnaceNWSE[(0 + rot)%4]
                    furnace_index_W = furnaceNWSE[(1 + rot)%4]
                    furnace_index_S = furnaceNWSE[(2 + rot)%4]
                    furnace_index_E = furnaceNWSE[(3 + rot)%4]
                    modelFile = models_folder/f'x_axis_{counter}_rot_{rot}.json'
                    with open(modelFile,mode='w+') as f:
                        f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')
                counter += 1



# z axis
z_axis_file = blockstates_folder / "infernal_furnace_z_axis.json"
with open(z_axis_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        counter = 0
        for x in [1]:
            for y in [0,2]:
                for z in [0,2]:
                    toWrite = f'''
    "edge_type_z_axis={counter},rotation_y_axis={rot}": {{
          "model": "thaumcraft:block/infernal_furnace/z_axis_{counter}_rot_{rot}"
        }},'''
                    allToWrite += toWrite
                    counter += 1
    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
    }}
    ''')
for rot in range(4):
    counter = 0
    for x in [1]:
        for y in [0,2]:
            for z in [0,2]:
                facesKey = (x,y,z)
                if facesKey in blockTextureIndexMap.keys():
                    facesValues = blockTextureIndexMap[facesKey]
                    furnace_index_up = facesValues[0]
                    furnace_index_down = facesValues[1]
                    furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
                    furnace_index_N = furnaceNWSE[(0 + rot)%4]
                    furnace_index_W = furnaceNWSE[(1 + rot)%4]
                    furnace_index_S = furnaceNWSE[(2 + rot)%4]
                    furnace_index_E = furnaceNWSE[(3 + rot)%4]
                    modelFile = models_folder/f'z_axis_{counter}_rot_{rot}.json'
                    with open(modelFile,mode='w+') as f:
                        f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')
                counter += 1


# side
side_file = blockstates_folder / "infernal_furnace_side.json"
coords = [
    (1,1,0),(1,1,2),(0,1,1)
]
with open(side_file,mode='w+',encoding='utf-8') as f:
    f.write('''{
  "variants": {
  ''')
    allToWrite = ''
    for rot in range(4):
        counter = 0
        for x,y,z in coords:
                    toWrite = f'''
    "side={counter},rotation_y_axis={rot}": {{
          "model": "thaumcraft:block/infernal_furnace/side_{counter}_rot_{rot}"
        }},'''
                    allToWrite += toWrite
                    counter += 1
    allToWrite = allToWrite[:-1]
    f.write(allToWrite)
    f.write('''
    }}
    ''')
for rot in range(4):
    counter = 0
    for x,y,z in coords:
                facesKey = (x,y,z)
                if facesKey in blockTextureIndexMap.keys():
                    facesValues = blockTextureIndexMap[facesKey]
                    furnace_index_up = facesValues[0]
                    furnace_index_down = facesValues[1]
                    furnaceNWSE = (facesValues[2+2],facesValues[0+2],facesValues[3+2],facesValues[1+2])
                    furnace_index_N = furnaceNWSE[(0 + rot)%4]
                    furnace_index_W = furnaceNWSE[(1 + rot)%4]
                    furnace_index_S = furnaceNWSE[(2 + rot)%4]
                    furnace_index_E = furnaceNWSE[(3 + rot)%4]
                    modelFile = models_folder/f'side_{counter}_rot_{rot}.json'
                    with open(modelFile,mode='w+') as f:
                        f.write(f'''{{
  "parent": "minecraft:block/cube",
  "textures": {{
    "down": "thaumcraft:block/furnace{furnace_index_up}",
    "up": "thaumcraft:block/furnace{furnace_index_down}",
    "north": "thaumcraft:block/furnace{furnace_index_N}",
    "south": "thaumcraft:block/furnace{furnace_index_W}",
    "west": "thaumcraft:block/furnace{furnace_index_S}",
    "east": "thaumcraft:block/furnace{furnace_index_E}"
  }}
}}''')
                counter += 1