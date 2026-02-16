import re


a = """
        public static final RegistrySupplier<VisNetChargeRelayBlock> SUPPLIER_VIS_CHARGE_RELAY = BLOCKS.register(
                "vis_charge_relay",
                VisNetChargeRelayBlock::new
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