package thaumcraft.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import tc4tweak.network.TileHoleSyncPacket;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.foci.ItemFocusPortableHole;

public class TileHole extends TileMemory {
   public short countdown = 0;
   public short countdownmax = 120;
   public byte count = 0;
   public byte direction = 0;

   public TileHole() {
   }

   public TileHole(Block bi, int md, short max, byte count, byte direction, TileEntity te) {
      super(bi, md, te);
      this.count = count;
      this.countdownmax = max;
      this.direction = direction;
   }

   public TileHole(byte count) {
      this.count = count;
   }

   public boolean canUpdate() {
      return true;
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.worldObj.isRemote) {
         this.surroundwithsparkles();
      }

      if (this.countdown == 0 && this.count > 1 && this.direction != -1) {
         int ii = this.xCoord;
         int jj = this.yCoord;
         int kk = this.zCoord;
         switch (this.direction) {
            case 0:
            case 1:
               int a = 0;

               for(; a < 9; ++a) {
                  if (a / 3 != 1 || a % 3 != 1) {
                     ItemFocusPortableHole.createHole(this.worldObj, ii - 1 + a / 3, jj, kk - 1 + a % 3, -1, (byte)1, this.countdownmax);
                  }
               }
               break;
            case 2:
            case 3:
               for(int i = 0; i < 9; ++i) {
                  if (i / 3 != 1 || i % 3 != 1) {
                     ItemFocusPortableHole.createHole(this.worldObj, ii - 1 + i / 3, jj - 1 + i % 3, kk, -1, (byte)1, this.countdownmax);
                  }
               }
               break;
            case 4:
            case 5:
               for(int i = 0; i < 9; ++i) {
                  if (i / 3 != 1 || i % 3 != 1) {
                     ItemFocusPortableHole.createHole(this.worldObj, ii, jj - 1 + i / 3, kk - 1 + i % 3, -1, (byte)1, this.countdownmax);
                  }
               }
         }

         switch (this.direction) {
            case 0:
               ++jj;
               break;
            case 1:
               --jj;
               break;
            case 2:
               ++kk;
               break;
            case 3:
               --kk;
               break;
            case 4:
               ++ii;
               break;
            case 5:
               --ii;
         }

         if (!ItemFocusPortableHole.createHole(this.worldObj, ii, jj, kk, this.direction, (byte)(this.count - 1), this.countdownmax)) {
            this.count = 0;
         }
      }

      ++this.countdown;
      if (this.countdown >= this.countdownmax) {
         if (this.worldObj.isRemote) {
            Thaumcraft.proxy.blockSparkle(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 4194368, 1);
         } else {
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.oldblock, this.oldmeta, 0);
            this.recreateTileEntity();
         }

         this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.oldblock, 2);
      }

   }

   private void surroundwithsparkles() {
      boolean yp = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord).isOpaqueCube();
      boolean xp = this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord).isOpaqueCube();
      boolean zp = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1).isOpaqueCube();
      boolean yn = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord).isOpaqueCube();
      boolean xn = this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord).isOpaqueCube();
      boolean zn = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1).isOpaqueCube();
      boolean b1 = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) != ConfigBlocks.blockHole;
      boolean b2 = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) != ConfigBlocks.blockHole;
      boolean b3 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1) != ConfigBlocks.blockHole;
      boolean b4 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1) != ConfigBlocks.blockHole;
      boolean b5 = this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord) != ConfigBlocks.blockHole;
      boolean b6 = this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord) != ConfigBlocks.blockHole;
      if (!xp && yp && b6) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)(this.yCoord + 1), (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!xn && yp && b5) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)(this.yCoord + 1), (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!zp && yp && b4) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)(this.yCoord + 1), (float)(this.zCoord + 1), 2);
      }

      if (!zn && yp && b3) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)(this.yCoord + 1), (float)this.zCoord, 2);
      }

      if (!xp && yn && b6) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord, (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!xn && yn && b5) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord, (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!zp && yn && b4) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)this.yCoord, (float)(this.zCoord + 1), 2);
      }

      if (!zn && yn && b3) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)this.yCoord, (float)this.zCoord, 2);
      }

      if (!yp && xp && b1) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)(this.yCoord + 1), (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!yn && xp && b2) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord, (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!zp && xp && b4) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)(this.zCoord + 1), 2);
      }

      if (!zn && xp && b3) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)this.zCoord, 2);
      }

      if (!yp && xn && b1) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)(this.yCoord + 1), (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!yn && xn && b2) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord, (float)this.zCoord + this.worldObj.rand.nextFloat(), 2);
      }

      if (!zp && xn && b4) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)(this.zCoord + 1), 2);
      }

      if (!zn && xn && b3) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)this.zCoord, 2);
      }

      if (!xp && zp && b6) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)(this.zCoord + 1), 2);
      }

      if (!xn && zp && b5) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)(this.zCoord + 1), 2);
      }

      if (!yp && zp && b1) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)(this.yCoord + 1), (float)(this.zCoord + 1), 2);
      }

      if (!yn && zp && b2) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)this.yCoord, (float)(this.zCoord + 1), 2);
      }

      if (!xp && zn && b6) {
         Thaumcraft.proxy.sparkle((float)(this.xCoord + 1), (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)this.zCoord, 2);
      }

      if (!xn && zn && b5) {
         Thaumcraft.proxy.sparkle((float)this.xCoord, (float)this.yCoord + this.worldObj.rand.nextFloat(), (float)this.zCoord, 2);
      }

      if (!yp && zn && b1) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)(this.yCoord + 1), (float)this.zCoord, 2);
      }

      if (!yn && zn && b2) {
         Thaumcraft.proxy.sparkle((float)this.xCoord + this.worldObj.rand.nextFloat(), (float)this.yCoord, (float)this.zCoord, 2);
      }

   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.readCustomNBT(nbttagcompound);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.countdown = nbttagcompound.getShort("countdown");
      this.countdownmax = nbttagcompound.getShort("countdownmax");
      this.count = nbttagcompound.getByte("count");
      this.direction = nbttagcompound.getByte("direction");
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      this.writeCustomNBT(nbttagcompound);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setShort("countdown", this.countdown);
      nbttagcompound.setShort("countdownmax", this.countdownmax);
      nbttagcompound.setByte("count", this.count);
      nbttagcompound.setByte("direction", this.direction);
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.writeCustomNBT(nbttagcompound);
      S35PacketUpdateTileEntity origin =
              new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, nbttagcompound);
      try {
         return Thaumcraft.instance.CHANNEL.getPacketFrom(new TileHoleSyncPacket(origin));
      } catch (Exception ex) {
         // fallback to original packet if anything goes wrong
         return origin;
      }
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.readCustomNBT(pkt.func_148857_g());
   }
}
