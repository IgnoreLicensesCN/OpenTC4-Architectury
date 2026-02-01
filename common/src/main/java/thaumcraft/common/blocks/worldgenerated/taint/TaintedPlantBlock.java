package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

//blocktaintfibres:2
public class TaintedPlantBlock extends AbstractTaintFibreBlock{
    public TaintedPlantBlock(Properties properties) {
        super(properties);
    }
    public TaintedPlantBlock() {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .randomTicks()
                .noCollission()
                .requiresCorrectToolForDrops()
                .sound(TAINT_FIBRE_SOUND)
                .strength(1,5)
                .mapColor(MapColor.COLOR_PURPLE)
                .lightLevel(s -> 8)
        );
    }
}
