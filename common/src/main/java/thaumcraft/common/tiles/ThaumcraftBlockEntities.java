package thaumcraft.common.tiles;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.crafted.*;
import thaumcraft.common.tiles.eldritch.EldritchAltarBlockEntity;
import thaumcraft.common.tiles.eldritch.EldritchVoidBlockEntity;
import thaumcraft.common.tiles.node.NodeBlockEntity;
import thaumcraft.common.tiles.node.ObsidianTotemNodeBlockEntity;
import thaumcraft.common.tiles.node.SilverWoodKnotNodeBlockEntity;

public class ThaumcraftBlockEntities {

    public static final BlockEntityType<NodeBlockEntity> AURA_NODE = Registry.SUPPLIER_AURA_NODE.get();
    public static final BlockEntityType<SilverWoodKnotNodeBlockEntity> SILVERWOOD_KNOT_NODE = Registry.SUPPLIER_SILVERWOOD_KNOT_NODE.get();
    public static final BlockEntityType<ObsidianTotemNodeBlockEntity> OBSIDIAN_TOTEM_NODE = Registry.SUPPLIER_OBSIDIAN_TOTEM_NODE.get();
    public static final BlockEntityType<WardingStoneBlockEntity> WARDING_STONE = Registry.SUPPLIER_WARDING_STONE.get();
    public static final BlockEntityType<HungryChestBlockEntity> HUNGRY_CHEST = Registry.SUPPLIER_HUNGRY_CHEST.get();
    public static final BlockEntityType<EldritchVoidBlockEntity> ELDRITCH_VOID = Registry.SUPPLIER_ELDRITCH_VOID.get();
    public static final BlockEntityType<OwnedBlockEntity> OWNED = Registry.SUPPLIER_OWNED.get();
    public static final BlockEntityType<EtherealBloomBlockEntity> ETHEREAL_BLOOM = Registry.SUPPLIER_ETHEREAL_BLOOM.get();
    public static final BlockEntityType<InfernalFurnaceBlockEntity> INFERNAL_FURNACE = Registry.SUPPLIER_INFERNAL_FURNACE.get();
    public static final BlockEntityType<EldritchAltarBlockEntity> ELDRITCH_ALTAR = Registry.SUPPLIER_ELDRITCH_ALTAR.get();
    public static class Registry{
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Thaumcraft.MOD_ID,
                Registries.BLOCK_ENTITY_TYPE
        );

        public static final RegistrySupplier<BlockEntityType<NodeBlockEntity>> SUPPLIER_AURA_NODE = BLOCK_ENTITIES.register(
                "node",
                () -> BlockEntityType.Builder.of(NodeBlockEntity::new, ThaumcraftBlocks.AURA_NODE).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<SilverWoodKnotNodeBlockEntity>> SUPPLIER_SILVERWOOD_KNOT_NODE = BLOCK_ENTITIES.register(
                "silverwood_knot_node",
                () -> BlockEntityType.Builder.of(SilverWoodKnotNodeBlockEntity::new, ThaumcraftBlocks.SILVERWOOD_KNOT).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<ObsidianTotemNodeBlockEntity>> SUPPLIER_OBSIDIAN_TOTEM_NODE = BLOCK_ENTITIES.register(
                "obsidian_totem_node",
                () -> BlockEntityType.Builder.of(ObsidianTotemNodeBlockEntity::new, ThaumcraftBlocks.OBSIDIAN_TOTEM_WITH_NODE).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<WardingStoneBlockEntity>> SUPPLIER_WARDING_STONE = BLOCK_ENTITIES.register(
                "warding_stone",
                () -> BlockEntityType.Builder.of(WardingStoneBlockEntity::new, ThaumcraftBlocks.PAVING_STONE_WARDING).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<HungryChestBlockEntity>> SUPPLIER_HUNGRY_CHEST = BLOCK_ENTITIES.register(
                "hungry_chest",
                () -> BlockEntityType.Builder.of(HungryChestBlockEntity::new, ThaumcraftBlocks.HUNGRY_CHEST).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<EldritchVoidBlockEntity>> SUPPLIER_ELDRITCH_VOID = BLOCK_ENTITIES.register(
                "eldritch_void",
                () -> BlockEntityType.Builder.of(EldritchVoidBlockEntity::new, ThaumcraftBlocks.ELDRITCH_VOID).build(null)//seems "type" not used
        );

        public static final RegistrySupplier<BlockEntityType<OwnedBlockEntity>> SUPPLIER_OWNED = BLOCK_ENTITIES.register(
                "owned",
                () -> BlockEntityType.Builder.of(
                        OwnedBlockEntity::new,
                        ThaumcraftBlocks.WARDED_GLASS,
                        ThaumcraftBlocks.ARCANE_DOOR
                ).build(null)
        );

        public static final RegistrySupplier<BlockEntityType<EtherealBloomBlockEntity>> SUPPLIER_ETHEREAL_BLOOM = BLOCK_ENTITIES.register(
                "ethereal_bloom",
                () -> BlockEntityType.Builder.of(
                        EtherealBloomBlockEntity::new,
                        ThaumcraftBlocks.ETHEREAL_BLOOM
                ).build(null)
        );

        public static final RegistrySupplier<BlockEntityType<InfernalFurnaceBlockEntity>> SUPPLIER_INFERNAL_FURNACE = BLOCK_ENTITIES.register(
                "infernal_furnace",
                () -> BlockEntityType.Builder.of(
                        InfernalFurnaceBlockEntity::new,
                        ThaumcraftBlocks.INFERNAL_FURNACE_LAVA
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchAltarBlockEntity>> SUPPLIER_ELDRITCH_ALTAR = BLOCK_ENTITIES.register(
                "eldritch_altar",
                () -> BlockEntityType.Builder.of(
                        EldritchAltarBlockEntity::new,
                        ThaumcraftBlocks.ELDRITCH_ALTAR
                ).build(null)
        );
    }

    public static void init(){
        Registry.BLOCK_ENTITIES.register();
    }
}
