package thaumcraft.api.listeners.worldgen.node;

import net.minecraft.core.BlockPos;

public class PickNodeCoordinateContext {
    public PickNodeCoordinateContext(BlockPos pos, boolean silverwood, boolean eerie, boolean small) {
        this.pos = pos;
        this.silverwood = silverwood;
        this.eerie = eerie;
        this.small = small;
    }

    public BlockPos pos;
    public boolean silverwood;
    public boolean eerie;
    public boolean small;

}
