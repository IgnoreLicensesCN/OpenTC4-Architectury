package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.EntityCultistKnight;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;

import java.util.List;

public class TileEldritchAltar extends TileThaumcraft {
    private boolean spawner = false;
    private boolean open = false;
    private boolean spawnedClerics = false;
    private byte spawnType = 0;
    private byte eyes = 0;
    private int counter = 0;

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        this.setEyes(nbttagcompound.getByte("eyes"));
        this.setOpen(nbttagcompound.getBoolean("open"));
    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("eyes", this.getEyes());
        nbttagcompound.setBoolean("open", this.isOpen());
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.spawnedClerics = nbttagcompound.getBoolean("spawnedClerics");
        this.spawner = nbttagcompound.getBoolean("spawner");
        this.spawnType = nbttagcompound.getByte("spawntype");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("spawnedClerics", this.spawnedClerics);
        nbttagcompound.setBoolean("spawner", this.spawner);
        nbttagcompound.setByte("spawntype", this.spawnType);
    }

    public double getMaxRenderDistanceSquared() {
        return 9216.0F;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    public boolean isSpawner() {
        return this.spawner;
    }

    public void setSpawner(boolean spawner) {
        this.spawner = spawner;
    }

    public boolean canUpdate() {
        return super.canUpdate();
    }

    public void updateEntity() {
        if (Platform.getEnvironment() != Env.CLIENT && this.isSpawner() && this.counter++ >= 80 && this.counter % 40 == 0) {
            switch (this.spawnType) {
                case 0:
                    if (!this.spawnedClerics) {
                        this.spawnClerics();
                    } else {
                        this.spawnGuards();
                    }
                    break;
                case 1:
                    this.spawnGuardian();
            }
        }

    }

    private void spawnGuards() {
        List ents = this.level().getEntitiesWithinAABB(EntityCultistCleric.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(24.0F, 16.0F, 24.0F));
        if (ents.isEmpty()) {
            this.setSpawner(false);
        } else {
            ents = this.level().getEntitiesWithinAABB(EntityCultist.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(24.0F, 16.0F, 24.0F));
            if (ents.size() < 8) {
                EntityCultistKnight eg = new EntityCultistKnight(this.level());
                int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
                if (World.doesBlockHaveSolidTopSurface(this.level(), i1, j1 - 1, k1)) {
                    eg.setPosition(i1, j1, k1);
                    if (this.level().checkNoEntityCollision(eg.boundingBox) && this.level().getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !this.level().isAnyLiquid(eg.boundingBox)) {
                        eg.onSpawnWithEgg(null);
                        eg.spawnExplosionParticle();
                        eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
                        this.level().spawnEntityInWorld(eg);
                    }
                }
            }

        }
    }

    private void spawnGuardian() {
        EntityEldritchGuardian eg = new EntityEldritchGuardian(this.level());
        int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.level().rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.level().rand, -1, 1);
        if (World.doesBlockHaveSolidTopSurface(this.level(), i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (eg.getCanSpawnHere()) {
                eg.onSpawnWithEgg(null);
                eg.spawnExplosionParticle();
                eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
                this.level().spawnEntityInWorld(eg);
            }
        }

    }

    private void spawnClerics() {
        int success = 0;

        for (int a = 0; a < 4; ++a) {
            int xx = 0;
            int zz = 0;
            switch (a) {
                case 0:
                    xx = -2;
                    zz = -2;
                    break;
                case 1:
                    xx = -2;
                    zz = 2;
                    break;
                case 2:
                    xx = 2;
                    zz = -2;
                    break;
                case 3:
                    xx = 2;
                    zz = 2;
            }

            EntityCultistCleric cleric = new EntityCultistCleric(this.level());
            if (World.doesBlockHaveSolidTopSurface(this.level(), this.xCoord + xx, this.yCoord - 1, this.zCoord + zz)) {
                cleric.setPosition((double) this.xCoord + (double) 0.5F + (double) xx, this.yCoord, (double) this.zCoord + (double) 0.5F + (double) zz);
                if (this.level().checkNoEntityCollision(cleric.boundingBox) && this.level().getCollidingBoundingBoxes(cleric, cleric.boundingBox).isEmpty() && !this.level().isAnyLiquid(cleric.boundingBox)) {
                    cleric.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 8);
                    cleric.onSpawnWithEgg(null);
                    cleric.spawnExplosionParticle();
                    if (this.level().spawnEntityInWorld(cleric)) {
                        ++success;
                        cleric.setIsRitualist(true);
                    }
                }
            }
        }

        if (success > 2) {
            this.spawnedClerics = true;
            this.markDirty();
        }

    }

    public byte getSpawnType() {
        return this.spawnType;
    }

    public void setSpawnType(byte spawnType) {
        this.spawnType = spawnType;
    }

    public byte getEyes() {
        return this.eyes;
    }

    public void setEyes(byte eyes) {
        this.eyes = eyes;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean checkForMaze() {
        int w = 15 + this.level().rand.nextInt(8) * 2;
        int h = 15 + this.level().rand.nextInt(8) * 2;
        if (!MazeHandler.mazesInRange(this.xCoord >> 4, this.zCoord >> 4, w, h)) {
            Thread t = new Thread(new MazeThread(this.xCoord >> 4, this.zCoord >> 4, w, h, this.level().rand.nextLong()));
            t.start();
            return false;
        } else {
            return true;
        }
    }
}
