package thaumcraft.api.expands.worldgen.node;

public class PickNodeCoordinateContext {
    public PickNodeCoordinateContext(int x, int y, int z, boolean silverwood, boolean eerie, boolean small) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.silverwood = silverwood;
        this.eerie = eerie;
        this.small = small;
    }

    public int x;
    public int y;
    public int z;
    public boolean silverwood;
    public boolean eerie;
    public boolean small;

}
