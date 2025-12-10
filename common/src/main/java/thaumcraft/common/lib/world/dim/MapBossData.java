package thaumcraft.common.lib.world.dim;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapBossData extends WorldSavedData {
    public int bossCount;

    public MapBossData(String p_i2140_1_) {
        super(p_i2140_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_76184_1_) {
        this.bossCount = p_76184_1_.getInteger("bossCount");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_76187_1_) {
        p_76187_1_.setInteger("bossCount", this.bossCount);
    }
}
