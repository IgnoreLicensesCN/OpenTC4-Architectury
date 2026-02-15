package thaumcraft.common.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import thaumcraft.api.nodes.INodeLockBlock;

public abstract class AbstractNodeLockBlock extends Block implements INodeLockBlock {
    public AbstractNodeLockBlock(Properties properties) {
        super(properties);
    }
    public AbstractNodeLockBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(3.f,25.f)
        );
    }
}
