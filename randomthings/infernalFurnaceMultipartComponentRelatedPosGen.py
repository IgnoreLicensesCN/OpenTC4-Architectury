counter = 0

xList = [0,2]
yList = [0,2]
zList = [0,2]
for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final int CORNER_TYPE_{x}_{y}_{z} = {counter};')
            counter += 1

for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final BlockPos CENTER_POS_RELATED_FROM_{x}_{y}_{z} = new BlockPos({2-x},{1-y},{1-z});')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''if (corner == CORNER_TYPE_{x}_{y}_{z}) {{
            return CENTER_POS_RELATED_FROM_{x}_{y}_{z};
        }}''')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''public static final BlockPos SELF_POS_{x}_{y}_{z} = new BlockPos({x},{y},{z});''')
print('==========================================')
counter = 0
xList = [0,2]
yList = [1]
zList = [0,2]
for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final int EDGE_TYPE_{x}_{y}_{z} = {counter};')
            counter += 1

for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final BlockPos CENTER_POS_RELATED_FROM_{x}_{y}_{z} = new BlockPos({2-x},{1-y},{1-z});')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''if (corner == EDGE_TYPE_{x}_{y}_{z}) {{
            return CENTER_POS_RELATED_FROM_{x}_{y}_{z};
        }}''')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''public static final BlockPos SELF_POS_{x}_{y}_{z} = new BlockPos({x},{y},{z});''')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''if (corner == EDGE_TYPE_{x}_{y}_{z}) {{
            return SELF_POS_{x}_{y}_{z};
        }}''')

print('==========================================')

counter = 0
xList = [1]
yList = [0,2]
zList = [0,2]
for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final int EDGE_TYPE_{x}_{y}_{z} = {counter};')
            counter += 1

for x in xList:
    for y in yList:
        for z in zList:
            print(f'public static final BlockPos CENTER_POS_RELATED_FROM_{x}_{y}_{z} = new BlockPos({2-x},{1-y},{1-z});')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''if (edge == EDGE_TYPE_{x}_{y}_{z}) {{
            return CENTER_POS_RELATED_FROM_{x}_{y}_{z};
        }}''')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''public static final BlockPos SELF_POS_{x}_{y}_{z} = new BlockPos({x},{y},{z});''')

for x in xList:
    for y in yList:
        for z in zList:
            print(f'''if (edge == EDGE_TYPE_{x}_{y}_{z}) {{
            return SELF_POS_{x}_{y}_{z};
        }}''')

print('==========================================')
counter = 0
xyzList = [[1,1,0],[1,1,2],[0,1,1]]
for x,y,z in xyzList:
    print(f'public static final int SIDE_TYPE_{x}_{y}_{z} = {counter};')
    counter += 1

for x,y,z in xyzList:
    print(f'public static final BlockPos CENTER_POS_RELATED_FROM_{x}_{y}_{z} = new BlockPos({2-x},{1-y},{1-z});')

for x,y,z in xyzList:
    print(f'''if (side == SIDE_TYPE_{x}_{y}_{z}) {{
            return CENTER_POS_RELATED_FROM_{x}_{y}_{z};
        }}''')

for x,y,z in xyzList:
    print(f'''public static final BlockPos SELF_POS_{x}_{y}_{z} = new BlockPos({x},{y},{z});''')

for x,y,z in xyzList:
    print(f'''if (side == SIDE_TYPE_{x}_{y}_{z}) {{
            return SELF_POS_{x}_{y}_{z};
        }}''')

