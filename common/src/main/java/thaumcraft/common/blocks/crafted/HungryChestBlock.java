package thaumcraft.common.blocks.crafted;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class HungryChestBlock extends ChestBlock {
    public HungryChestBlock(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(properties, supplier);
    }
    public HungryChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        this(BlockBehaviour.Properties.copy(Blocks.CHEST),supplier);
    }
}
