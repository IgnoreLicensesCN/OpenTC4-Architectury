import re
items = '''ButcherGolemCoreItem.java
EmptyGolemCoreItem.java
EssentiaGolemCoreItem.java
FillGolemCoreItem.java
FishingGolemCoreItem.java
GatherGolemCoreItem.java
GuardGolemCoreItem.java
HarvestGolemCoreItem.java
HarvestLogGolemCoreItem.java
LiquidGolemCoreItem.java
SortGolemCoreItem.java
UseGolemCoreItem.java'''.replace('.java','').split('\n')
for clzName in items:
    clzSplit = '_'.join([s.upper() for s in re.findall('[A-Z][^A-Z]*', clzName[:-4])])
    # print(f'''
    # public static final RegistrySupplier<{clzName}> SUPPLIER_{clzSplit} = ITEMS.register(
    #         "{clzSplit.lower()}",
    #         {clzName}::new
    # );''')
    print(f'''
    public static {clzName} {clzSplit}() {{
        return ThaumcraftItemsRegistry.SUPPLIER_{clzSplit}.get();
    }}''')