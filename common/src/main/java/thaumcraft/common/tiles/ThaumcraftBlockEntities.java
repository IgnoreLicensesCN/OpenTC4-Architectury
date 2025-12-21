package thaumcraft.common.tiles;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.node.NodeBlockEntity;
import thaumcraft.common.tiles.node.ObsidianTotemNodeBlockEntity;
import thaumcraft.common.tiles.node.SilverWoodKnotNodeBlockEntity;

public class ThaumcraftBlockEntities {

    public static final BlockEntityType<NodeBlockEntity> AURA_NODE = Registry.SUPPLIER_AURA_NODE.get();
    public static final BlockEntityType<SilverWoodKnotNodeBlockEntity> SILVERWOOD_KNOT_NODE = Registry.SUPPLIER_SILVERWOOD_KNOT_NODE.get();
    public static final BlockEntityType<ObsidianTotemNodeBlockEntity> OBSIDIAN_TOTEM_NODE = Registry.SUPPLIER_OBSIDIAN_TOTEM_NODE.get();
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
                "bosidian_totem_node",
                () -> BlockEntityType.Builder.of(ObsidianTotemNodeBlockEntity::new, ThaumcraftBlocks.OBSIDIAN_TOTEM_WITH_NODE).build(null)//seems "type" not used
        );
    }

    public static void init(){
        Registry.BLOCK_ENTITIES.register();
    }
}
