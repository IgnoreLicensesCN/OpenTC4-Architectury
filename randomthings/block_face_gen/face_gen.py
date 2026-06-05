import random
from pathlib import Path

import json

MOD_ID = "thaumcraft"
random.seed(0xff39c5bb)

blockStatesPath = Path(f"resources/{MOD_ID}/blockstates")
blockStatesPath.mkdir(parents=True, exist_ok=True)
modelsPath = Path(f"resources/{MOD_ID}/models/block/")
modelsPath.mkdir(parents=True, exist_ok=True)


class FullBlockFaceGenProperties:
    def __init__(self, blockID: str, blockFaces: list[str]):
        self.blockID = blockID
        self.blockFaces = blockFaces

    def getModelSubFolder(self):
        return Path("random_faces") / (self.blockID)

    def getModelStoreToPath(self):
        return modelsPath.joinpath(self.getModelSubFolder())

    def writeBlockStateAndModelJson(self) -> list[int]:
        sampled = random.sample([i for i in range(len(self.blockFaces) ** 6)], len(self.blockFaces) ** 3)
        sampled.sort()
        sampledState = {
            "variants": {
                "": [
                    {"model": f"{MOD_ID}:block/random_faces/{self.blockID}/" + self.blockID + "_" + str(i)} for i in
                    sampled
                ]
            }
        }
        blockStatesPath.joinpath(self.blockID + ".json").write_text(json.dumps(sampledState))
        sampledSet: set[int] = set(sampled)
        counter: int = 0
        self.getModelStoreToPath().mkdir(parents=True, exist_ok=True)
        for n in range(len(self.blockFaces)):
            for s in range(len(self.blockFaces)):
                for w in range(len(self.blockFaces)):
                    for e in range(len(self.blockFaces)):
                        for u in range(len(self.blockFaces)):
                            for d in range(len(self.blockFaces)):
                                if (counter in sampledSet):
                                    toWrite = {
                                        "parent": "minecraft:block/cube",
                                        "textures": {
                                            "down": self.blockFaces[d],
                                            "up": self.blockFaces[u],
                                            "north": self.blockFaces[n],
                                            "south": self.blockFaces[s],
                                            "west": self.blockFaces[w],
                                            "east": self.blockFaces[e]
                                        }
                                    }
                                    self.getModelStoreToPath().joinpath(
                                        self.blockID + "_" + str(counter) + ".json").write_text(json.dumps(toWrite))

                                counter += 1
        return sampled

class SlabFaceGenProperties:
    def __init__(self, blockID: str,basicBlockID:str,sampled:list[int]):
        self.blockID = blockID
        self.basicBlockID = basicBlockID
        self.sampled = sampled

    def getModelSubFolder(self):
        return Path("random_faces") / (self.blockID)

    def getModelStoreToPath(self):
        return modelsPath.joinpath(self.getModelSubFolder())

    def writeBlockStateAndModelJson(self):
        self.sampled.sort()
        sampledState = {
            "variants": {
                f"type={stateString}": [
                    {"model": f"{MOD_ID}:block/random_faces/{self.blockID}/{self.blockID}_{str(i)}_{stateString}"} for i in
                    self.sampled
                ] for stateString in ['bottom', 'top', 'double']
            }
        }
        blockStatesPath.joinpath(self.blockID + ".json").write_text(json.dumps(sampledState))
        self.getModelStoreToPath().mkdir(parents=True, exist_ok=True)
        for i in self.sampled:
            for stateString in ['bottom', 'top', 'double']:
                if stateString == 'top':
                    toWrite = {
                        "parent": "minecraft:block/slab_top",
                        "textures": {
                            "bottom": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "top": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "side": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                        }
                    }
                elif stateString == 'bottom':
                    toWrite = {
                        "parent": "minecraft:block/slab",
                        "textures": {
                            "bottom": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "top": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "side": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                        }
                    }
                else:
                    toWrite = {
                        "parent": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                    }
                self.getModelStoreToPath().mkdir(parents=True, exist_ok=True)
                self.getModelStoreToPath().joinpath(
                     f"{self.blockID}_{str(i)}_{stateString}_.json").write_text(json.dumps(toWrite))

class StairsFaceGenProperties:
    def __init__(self, blockID: str,basicBlockID:str,sampled:list[int]):
        self.blockID = blockID
        self.basicBlockID = basicBlockID
        self.sampled = sampled

    def getModelSubFolder(self):
        return Path("random_faces") / (self.blockID)

    def getModelStoreToPath(self):
        return modelsPath.joinpath(self.getModelSubFolder())

    def writeBlockStateAndModelJson(self):
        self.sampled.sort()
        sampledState = {
            "variants": {
                f"type={stateString}": [
                    {"model": f"{MOD_ID}:block/random_faces/{self.blockID}/{self.blockID}_{str(i)}_{stateString}"} for i in
                    self.sampled
                ] for stateString in ['outer_stairs', 'inner_stairs', 'stairs']
            }
        }
        blockStatesPath.joinpath(self.blockID + ".json").write_text(json.dumps(sampledState))
        self.getModelStoreToPath().mkdir(parents=True, exist_ok=True)
        for i in self.sampled:
            for stateString in ['outer_stairs', 'inner_stairs', 'stairs']:
                if stateString == 'outer_stairs':
                    toWrite = {
                        "parent": "minecraft:block/outer_stairs",
                        "textures": {
                            "bottom": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "top": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "side": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                        }
                    }
                elif stateString == 'inner_stairs':
                    toWrite = {
                        "parent": "minecraft:block/inner_stairs",
                        "textures": {
                            "bottom": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "top": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "side": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                        }
                    }
                else:
                    toWrite = {
                        "parent": "minecraft:block/stairs",
                        "textures": {
                            "bottom": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "top": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}",
                            "side": f"{MOD_ID}:block/random_faces/{self.basicBlockID}/{self.basicBlockID}_{str(i)}"
                        }
                    }
                self.getModelStoreToPath().mkdir(parents=True, exist_ok=True)
                self.getModelStoreToPath().joinpath(
                    f"{self.blockID}_{str(i)}_{stateString}_.json").write_text(json.dumps(toWrite))

ancientStoneSampled = FullBlockFaceGenProperties("ancient_stone", ["thaumcraft:block/ancient_stone_1", "thaumcraft:block/ancient_stone_2",
                                                            "thaumcraft:block/ancient_stone_3", "thaumcraft:block/ancient_stone_4"]).writeBlockStateAndModelJson()
SlabFaceGenProperties("ancient_stone_slab","ancient_stone",ancientStoneSampled).writeBlockStateAndModelJson()
StairsFaceGenProperties("ancient_stone_stairs","ancient_stone",ancientStoneSampled).writeBlockStateAndModelJson()
for i in [
    FullBlockFaceGenProperties("ancient_rock", ["thaumcraft:block/ancient_rock_1", "thaumcraft:block/ancient_rock_2",
                                        "thaumcraft:block/ancient_rock_3", "thaumcraft:block/ancient_rock_4"]),
    FullBlockFaceGenProperties("runed_stone", ["thaumcraft:block/runed_stone_face_1", "thaumcraft:block/runed_stone_face_2",
                                                "thaumcraft:block/runed_stone_face_3", "thaumcraft:block/runed_stone_face_4"]),
]:
    i.writeBlockStateAndModelJson()
