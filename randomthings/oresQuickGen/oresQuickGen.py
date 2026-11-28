from pathlib import Path

fromWord = "iron"
fromWord2 = "ore"
toWord2 = "infused_stone"
for inPathItem in Path("./in").rglob("*"):
    pathIn = Path("./") / str(inPathItem)
    if not pathIn.is_file():
        continue
    for toWord in ["air","earth","entropy","order","fire","water"]:
        pathOut = (Path("out") / (str(inPathItem).replace(fromWord,toWord).replace(fromWord2,toWord2)))
        if not pathOut.parent.exists():
            pathOut.parent.mkdir(exist_ok=True, parents=True)
        with open(pathIn,mode="r",encoding="utf-8") as inFile:
            text = inFile.read().replace(fromWord,toWord).replace(fromWord2,toWord2)
            with open(pathOut,mode="w+",encoding="utf-8") as outFile:
                outFile.write(text)
