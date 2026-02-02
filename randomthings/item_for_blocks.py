import re


a = """public static final RegistrySupplier<CrustedTaintBlock> SUPPLIER_CRUSTED_TAINT = BLOCKS.register(
                "crusted_taint",
                CrustedTaintBlock::new
        );
        public static final RegistrySupplier<TaintedSoilBlock> SUPPLIER_TAINTED_SOIL = BLOCKS.register(
                "tainted_soil",
                TaintedSoilBlock::new
        );
        public static final RegistrySupplier<FibrousTaintBlock> SUPPLIER_FIBROUS_TAINT = BLOCKS.register(
                "fibrous_taint",
                FibrousTaintBlock::new
        );
        public static final RegistrySupplier<TaintedGrassBlock> SUPPLIER_TAINTED_GRASS = BLOCKS.register(
                "tainted_grass",
                TaintedGrassBlock::new
        );
        public static final RegistrySupplier<TaintedPlantBlock> SUPPLIER_TAINTED_PLANT = BLOCKS.register(
                "tainted_plant",
                TaintedPlantBlock::new
        );
        public static final RegistrySupplier<SporeStalkBlock> SUPPLIER_SPORE_STALK = BLOCKS.register(
                "spore_stalk",
                SporeStalkBlock::new
        );
        public static final RegistrySupplier<MatureSporeStalkBlock> SUPPLIER_MATURE_SPORE_STALK = BLOCKS.register(
                "mature_spore_stalk",
                MatureSporeStalkBlock::new
        );
        public static final RegistrySupplier<TableBlock> SUPPLIER_TABLE = BLOCKS.register(
                "table",
                TableBlock::new
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