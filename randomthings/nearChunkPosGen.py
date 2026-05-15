offsetVar = 'this.chunkSize'
l = [f' - {offsetVar}','',f' + {offsetVar}']

for xOff in range(3):
    for yOff in range(3):
        for zOff in range(3):
            print(f'items = get(x{l[xOff]},y{l[yOff]},z{l[zOff]});\nif (items != null){{items.forEach(action);}}')