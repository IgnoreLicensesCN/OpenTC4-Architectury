package thaumcraft.common.tiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.core.Direction;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.IEssentiaTransportBlockEntity;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileArcaneLampFertility extends TileThaumcraft implements IEssentiaTransportBlockEntity {
   public Direction facing = Direction.getOrientation(0);
   public int charges = 0;
   int count = 0;
   int drawDelay = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.level() != null && (Platform.getEnvironment() == Env.CLIENT)) {
         this.level().updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.charges < 4 && this.drawEssentia()) {
            ++this.charges;
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }

         if (this.charges > 1 && this.count++ % 300 == 0) {
            this.updateAnimals();
         }
      }

   }

   private void updateAnimals() {
      int distance = 7;
      List<EntityAnimal> var5 = this.level().getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(distance, distance, distance));

      for(EntityAnimal var3 : var5) {
         EntityLivingBase var4 = var3;
         if (var3.getGrowingAge() == 0 && !var3.isInLove()) {
            ArrayList<EntityAnimal> sa = new ArrayList<>();

            for(EntityAnimal var7 : var5) {
               if (var7.getClass().equals(var4.getClass())) {
                  sa.add(var7);
               }
            }

            if (sa == null || sa.size() <= 7) {
               Iterator var22 = sa.iterator();
               EntityAnimal partner = null;

               while(var22.hasNext()) {
                  EntityAnimal var33 = (EntityAnimal)var22.next();
                  if (var33.getGrowingAge() == 0 && !var33.isInLove()) {
                     if (partner != null) {
                        this.charges -= 2;
                        var33.func_146082_f(null);
                        partner.func_146082_f(null);
                        return;
                     }

                     partner = var33;
                  }
               }
            }
         }
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = Direction.getOrientation(nbttagcompound.getInteger("orientation"));
      this.charges = nbttagcompound.getInteger("charges");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.facing.ordinal());
      nbttagcompound.setInteger("charges", this.charges);
   }

   boolean drawEssentia() {
      if (++this.drawDelay % 5 != 0) {
         return false;
      } else {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, this.facing);
         if (te != null) {
            IEssentiaTransportBlockEntity ic = (IEssentiaTransportBlockEntity)te;
            if (!ic.canOutputTo(this.facing.getOpposite())) {
               return false;
            }

             return ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && ic.takeEssentia(
                     Aspects.LIFE, 1, this.facing.getOpposite()) == 1;
         }

         return false;
      }
   }

   public boolean isConnectable(Direction face) {
      return face == this.facing;
   }

   public boolean canInputFrom(Direction face) {
      return face == this.facing;
   }

   public boolean canOutputTo(Direction face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuctionToDrainOut() {
      return 0;
   }

   public Aspect getSuctionType(Direction face) {
      return Aspects.LIFE;
   }

   public int getSuctionAmount(Direction face) {
      return face == this.facing ? 128 - this.charges * 10 : 0;
   }

   public Aspect getEssentiaType(Direction loc) {
      return null;
   }

   public int getEssentiaAmount(Direction loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, Direction facing) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction facing) {
      return 0;
   }
}
