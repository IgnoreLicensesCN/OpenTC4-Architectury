package thaumcraft.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.NotNull;

import static com.linearity.opentc4.Consts.BlockPosCompoundTagKeys.*;

@Deprecated(forRemoval = true,since = "BlockPos")
public class BlockCoordinates implements Comparable<BlockCoordinates>
{
    /** the x coordinate */
    public int x;

    /** the y coordinate */
    public int y;

    /** the z coordinate */
    public int z;

    public BlockCoordinates() {}

    public BlockCoordinates(int par1, int par2, int par3)
    {
        this.x = par1;
        this.y = par2;
        this.z = par3;
    }

    public BlockCoordinates(BlockEntity tile)
    {
        this.x = tile.getBlockPos().getX();
        this.y = tile.getBlockPos().getY();
        this.z = tile.getBlockPos().getZ();
    }

    public BlockCoordinates(BlockCoordinates par1ChunkPos)
    {
        this.x = par1ChunkPos.x;
        this.y = par1ChunkPos.y;
        this.z = par1ChunkPos.z;
    }

    public boolean equals(Object par1Obj)
    {
        if (par1Obj instanceof BlockCoordinates coordinates) {
            return this.x == coordinates.x && this.y == coordinates.y && this.z == coordinates.z ;
        } else {
            return false;
        }
    }

    public int hashCode()
    {
        return this.y * 31 + this.x * 91 + this.z * 29303;
    }

    /**
     * Compare the coordinate with another coordinate
     */
    public int compareWorldCoordinate(@NotNull BlockCoordinates par1)
    {
        return this.y == par1.y ? (this.z == par1.z ? this.x - par1.x : this.z - par1.z) : this.y - par1.y;
    }

    public void set(int par1, int par2, int par3, int d)
    {
        this.x = par1;
        this.y = par2;
        this.z = par3;
    }

    /**
     * Returns the squared distance between this coordinates and the coordinates given as argument.
     */
    public float getDistanceSquared(int par1, int par2, int par3)
    {
        float f = (float)(this.x - par1);
        float f1 = (float)(this.y - par2);
        float f2 = (float)(this.z - par3);
        return f * f + f1 * f1 + f2 * f2;
    }

    /**
     * Return the squared distance between this coordinates and the ChunkPos given as argument.
     */
    public float getDistanceSquaredToWorldCoordinates(@NotNull BlockCoordinates par1ChunkPos)
    {
        return this.getDistanceSquared(par1ChunkPos.x, par1ChunkPos.y, par1ChunkPos.z);
    }

    @Override
    public int compareTo(@NotNull BlockCoordinates par1Obj)
    {
        return this.compareWorldCoordinate(par1Obj);
    }

    public void readNBT(@NotNull CompoundTag nbt) {
        this.x = BLOCK_X_ACCESSOR.readFromCompoundTag(nbt);
        this.y = BLOCK_Y_ACCESSOR.readFromCompoundTag(nbt);
        this.z = BLOCK_Z_ACCESSOR.readFromCompoundTag(nbt);
    }

    public void writeNBT(@NotNull CompoundTag nbt) {
        BLOCK_X_ACCESSOR.writeToCompoundTag(nbt, x);
        BLOCK_Y_ACCESSOR.writeToCompoundTag(nbt, y);
        BLOCK_Z_ACCESSOR.writeToCompoundTag(nbt, z);
    }
}
