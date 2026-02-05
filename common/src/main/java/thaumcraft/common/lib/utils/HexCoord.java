package thaumcraft.common.lib.utils;

public record HexCoord(int q, int r) {

    public HexCoordUtils.CubicHex toCubicHex() {
        return new HexCoordUtils.CubicHex(this.q, this.r, -this.q - this.r);
    }

    public HexCoordUtils.Pixel toPixel(int size) {
        return new HexCoordUtils.Pixel(
                (double) size * (double) 1.5F * (double) this.q,
                (double) size * Math.sqrt(3.0F) * ((double) this.r + (double) this.q / (double) 2.0F)
        );
    }

    public HexCoord getNeighbour(int direction) {
        int[] d = HexCoordUtils.NEIGHBOURS[direction];
        return new HexCoord(this.q + d[0], this.r + d[1]);
    }

    public boolean equals(HexCoord h) {
        return h.q == this.q && h.r == this.r;
    }

    public String toString() {
        return this.q + ":" + this.r;
    }
}
