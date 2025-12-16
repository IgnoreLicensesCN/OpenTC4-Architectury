package thaumcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import static com.linearity.opentc4.Consts.WorldCoordsCompoundTagAccessors.*;

public class WorldCoordinates implements Comparable<WorldCoordinates>
{
    public int x;

    /** the y coordinate */
    public int y;

    /** the z coordinate */
    public int z;
    
    public String dim;

    public WorldCoordinates() {}

    public WorldCoordinates(int par1, int par2, int par3, String d)
    {
        this.x = par1;
        this.y = par2;
        this.z = par3;
        this.dim = d;
    }
    
    public WorldCoordinates(BlockEntity tile)
    {
        BlockPos pos = tile.getBlockPos();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.dim = tile.getLevel().dimension().registry().toString();
    }

    public WorldCoordinates(WorldCoordinates par1ChunkPos)
    {
        this.x = par1ChunkPos.x;
        this.y = par1ChunkPos.y;
        this.z = par1ChunkPos.z;
        this.dim = par1ChunkPos.dim;
    }

    public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof WorldCoordinates))
        {
            return false;
        }
        else
        {
        	WorldCoordinates coordinates = (WorldCoordinates)par1Obj;
            return this.x == coordinates.x && this.y == coordinates.y && this.z == coordinates.z && this.dim == coordinates.dim ;
        }
    }

    @Override
    public int hashCode()
    {
        return this.y * 31 + this.x * 91 + this.z * 29303 + this.dim.hashCode() * 39916801;
//        return this.x + this.y << 8 + this.z << 16 + this.dim << 24;
    }

    /**
     * Compare the coordinate with another coordinate
     */
    public int compareWorldCoordinate(WorldCoordinates par1)
    {
        return this.dim == par1.dim ? (
        		this.y == par1.y ? (this.z == par1.z ? this.x - par1.x : this.z - par1.z) : this.y - par1.y) : -1;
    }

    public void set(int x, int y, int z, String dim)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    /**
     * Returns the squared distance between this coordinates and the coordinates given as argument.
     */
    @Deprecated
    public float getDistanceSquared(int x, int y, int z)//lengthSquared
    {
        return getLengthSquared(x, y, z);
    }

    //yeah i mean just use minecraft naming style since we have that obfuscation map
    public float getLengthSquared(int x, int y, int z){
        float f = (float)(this.x - x);
        float f1 = (float)(this.y - y);
        float f2 = (float)(this.z - z);
        return f * f + f1 * f1 + f2 * f2;
    }

    /**
     * Return the squared distance between this coordinates and the ChunkPos given as argument.
     */
    public float getDistanceSquaredToWorldCoordinates(WorldCoordinates par1ChunkPos)
    {
        return this.getDistanceSquared(par1ChunkPos.x, par1ChunkPos.y, par1ChunkPos.z);
    }

    @Override
    public int compareTo(@NotNull WorldCoordinates par1Obj)
    {
        return this.compareWorldCoordinate(par1Obj);
    }
    
    public void readNBT(CompoundTag tag) {

    	this.x = WORLD_X_ACCESSOR.readFromCompoundTag(tag);
    	this.y = WORLD_Y_ACCESSOR.readFromCompoundTag(tag);
    	this.z = WORLD_Z_ACCESSOR.readFromCompoundTag(tag);
    	this.dim = WORLD_DIM_ACCESSOR.readFromCompoundTag(tag);
    }
    
    public void writeNBT(CompoundTag nbt) {
        WORLD_X_ACCESSOR.writeToCompoundTag(nbt,x);
        WORLD_Y_ACCESSOR.writeToCompoundTag(nbt,y);
        WORLD_Z_ACCESSOR.writeToCompoundTag(nbt,z);
        WORLD_DIM_ACCESSOR.writeToCompoundTag(nbt,dim);
    }

    
}
