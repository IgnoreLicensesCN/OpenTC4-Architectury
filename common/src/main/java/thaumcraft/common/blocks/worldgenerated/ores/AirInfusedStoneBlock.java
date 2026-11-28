package thaumcraft.common.blocks.worldgenerated.ores;

import thaumcraft.api.aspects.Aspects;

public class AirInfusedStoneBlock extends AbstractInfusedStoneBlock {
    public AirInfusedStoneBlock() {
        super(Aspects.AIR.color,1);
    }
    public AirInfusedStoneBlock(Properties properties) {
        super(properties,Aspects.AIR.color,1);
    }
}
