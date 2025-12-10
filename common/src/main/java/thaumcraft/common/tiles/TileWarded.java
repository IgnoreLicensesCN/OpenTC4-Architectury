package thaumcraft.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.TileThaumcraft;

public class TileWarded extends TileThaumcraft {
   public int owner = 0;
   public Block block;
   public byte blockMd;
   public boolean safeToRemove;
   public byte light;

   public TileWarded() {
      this.block = Blocks.air;
      this.blockMd = 0;
      this.safeToRemove = false;
   }

   public boolean canUpdate() {
      return false;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.block = Block.getBlockById(nbttagcompound.getInteger("bi"));
      this.blockMd = nbttagcompound.getByte("md");
      this.light = nbttagcompound.getByte("ll");
      this.owner = nbttagcompound.getInteger("oi");
      if (this.owner == 0) {
         String s = nbttagcompound.getString("owner");
         if (s != null) {
            this.owner = s.hashCode();
         }
      }

      if (this.block == null) {
         this.block = Blocks.stone;
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("bi", Block.getIdFromBlock(this.block));
      nbttagcompound.setByte("md", this.blockMd);
      nbttagcompound.setByte("ll", this.light);
      nbttagcompound.setInteger("oi", this.owner);
   }
}
