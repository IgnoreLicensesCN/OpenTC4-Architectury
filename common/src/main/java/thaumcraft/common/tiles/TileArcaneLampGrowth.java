package thaumcraft.common.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.lib.utils.CropUtils;

import java.util.ArrayList;
import java.util.Collections;

public class TileArcaneLampGrowth extends TileThaumcraft implements IEssentiaTransport {
   public ForgeDirection facing = ForgeDirection.getOrientation(0);
   private boolean reserve = false;
   public int charges = -1;
   int lx = 0;
   int ly = 0;
   int lz = 0;
   Block lid;
   int lmd;
   ArrayList checklist;
   int drawDelay;

   public TileArcaneLampGrowth() {
      this.lid = Blocks.air;
      this.lmd = 0;
      this.checklist = new ArrayList<>();
      this.drawDelay = 0;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.worldObj != null && this.worldObj.isRemote) {
         this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         if (this.charges <= 0) {
            if (this.reserve) {
               this.charges = 100;
               this.reserve = false;
               this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            } else if (this.drawEssentia()) {
               this.charges = 100;
               this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
         }

         if (!this.reserve && this.drawEssentia()) {
            this.reserve = true;
         }

         if (this.charges == 0) {
            this.charges = -1;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }

         if (this.charges > 0) {
            this.updatePlant();
         }
      }

   }

   boolean isPlant(int x, int y, int z) {
      boolean flag = this.worldObj.getBlock(x, y, z) instanceof IGrowable;
      Material mat = this.worldObj.getBlock(x, y, z).getMaterial();
      return (flag || mat == Material.cactus || mat == Material.plants) && mat != Material.grass;
   }

   private void updatePlant() {
      if (this.lid != this.worldObj.getBlock(this.lx, this.ly, this.lz) || this.lmd != this.worldObj.getBlockMetadata(this.lx, this.ly, this.lz)) {
         EntityPlayer p = this.worldObj.getClosestPlayer(this.lx, this.ly, this.lz, 32.0F);
         if (p != null) {
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(this.lx, this.ly, this.lz, 4259648), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.lx, this.ly, this.lz, 32.0F));
         }

         this.lid = this.worldObj.getBlock(this.lx, this.ly, this.lz);
         this.lmd = this.worldObj.getBlockMetadata(this.lx, this.ly, this.lz);
      }

      int distance = 6;
      if (this.checklist.isEmpty()) {
         for(int a = -distance; a <= distance; ++a) {
            for(int b = -distance; b <= distance; ++b) {
               this.checklist.add(new BlockCoordinates(this.xCoord + a, this.yCoord + distance, this.zCoord + b));
            }
         }

         Collections.shuffle(this.checklist, this.worldObj.rand);
      }

      int x = ((BlockCoordinates)this.checklist.get(0)).x;
      int y = ((BlockCoordinates)this.checklist.get(0)).y;
      int z = ((BlockCoordinates)this.checklist.get(0)).z;
      this.checklist.remove(0);

      while(y >= this.yCoord - distance) {
         if (!this.worldObj.isAirBlock(x, y, z) && this.isPlant(x, y, z) && this.getDistanceFrom((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F) < (double)(distance * distance) && !CropUtils.isGrownCrop(this.worldObj, x, y, z) && CropUtils.doesLampGrow(this.worldObj, x, y, z)) {
            --this.charges;
            this.lx = x;
            this.ly = y;
            this.lz = z;
            this.lid = this.worldObj.getBlock(x, y, z);
            this.lmd = this.worldObj.getBlockMetadata(x, y, z);
            this.worldObj.scheduleBlockUpdate(x, y, z, this.worldObj.getBlock(x, y, z), 1);
            return;
         }

         --y;
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getInteger("orientation"));
      this.reserve = nbttagcompound.getBoolean("reserve");
      this.charges = nbttagcompound.getInteger("charges");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.facing.ordinal());
      nbttagcompound.setBoolean("reserve", this.reserve);
      nbttagcompound.setInteger("charges", this.charges);
   }

   boolean drawEssentia() {
      if (++this.drawDelay % 5 != 0) {
         return false;
      } else {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.facing);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(this.facing.getOpposite())) {
               return false;
            }

             return ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && ic.takeEssentia(Aspect.PLANT, 1, this.facing.getOpposite()) == 1;
         }

         return false;
      }
   }

   public boolean isConnectable(ForgeDirection face) {
      return face == this.facing;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return face == this.facing;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(ForgeDirection face) {
      return Aspect.PLANT;
   }

   public int getSuctionAmount(ForgeDirection face) {
      return face != this.facing || this.reserve && this.charges > 0 ? 0 : 128;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return null;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection loc) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection loc) {
      return 0;
   }
}
