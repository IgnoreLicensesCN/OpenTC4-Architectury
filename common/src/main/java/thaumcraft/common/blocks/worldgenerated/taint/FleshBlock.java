package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.world.level.block.state.BlockBehaviour;

@Deprecated(forRemoval = true,since = "1.7.10 used another impl for this make it a resource block")
public class FleshBlock extends AbstractTaintBlock {
    public FleshBlock(Properties properties) {
        super(properties);
    }
    public FleshBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .randomTicks()
                        .strength(2,10)
                        .sound(TAINT_BLOCK_SOUND));
    }
}
