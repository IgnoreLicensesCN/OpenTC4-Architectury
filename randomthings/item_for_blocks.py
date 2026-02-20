import re


a = """
        public static final RegistrySupplier<Block> SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT = BLOCKS.register(
                "advanced_alchemical_construct",
                xxx::new
        );
        public static final RegistrySupplier<Block> SUPPLIER_ALCHEMICAL_CONSTRUCT = BLOCKS.register(
                "alchemical_construct",
                xxx::new
        );"""

groups = []
for b in re.findall(r"public static final RegistrySupplier<[\S]+> SUPPLIER_([\S]+) = BLOCKS\.register\(\n[ ]+\"([a-z_]+)\",\n[ ]+[\S]+\n[ ]+\);",a):
    print(b)
    groups.append(b)

for c in groups:
    print(f'''public static final RegistrySupplier<BlockItem> SUPPLIER_{c[0]} = ITEMS.register(
                "{c[1]}",
                () -> new BlockItem(ThaumcraftBlocks.{c[0]},new Item.Properties())
        );''')
for c in groups:
    print(f'''public static final BlockItem {c[0]} = Registry.SUPPLIER_{c[0]}.get();''')