package thaumcraft.common.tiles;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.function.Supplier;

public class ThaumcraftBlockEntities {


    public static final BlockEntityType<NodeBlockEntity> AURA_NODE = Registry.SUPPLIER_AURA_NODE.get();
    public static class Registry{
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Thaumcraft.MOD_ID,
                Registries.BLOCK_ENTITY_TYPE
        );

        public static final RegistrySupplier<BlockEntityType<NodeBlockEntity>> SUPPLIER_AURA_NODE = BLOCK_ENTITIES.register(
                "node",
                (Supplier<BlockEntityType<NodeBlockEntity>>) () -> BlockEntityType.Builder.of(NodeBlockEntity::new, ThaumcraftBlocks.AURA_NODE)
        );
    }

    public static void init(){
        Registry.BLOCK_ENTITIES.register();
    }
}
