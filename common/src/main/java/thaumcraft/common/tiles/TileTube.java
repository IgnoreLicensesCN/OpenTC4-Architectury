package thaumcraft.common.tiles;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

public class TileTube extends TileThaumcraft implements IEssentiaTransport, IWandable {
   public ForgeDirection facing;
   public boolean[] openSides;
   Aspect essentiaType;
   int essentiaAmount;
   Aspect suctionType;
   int suction;
   int venting;
   int count;
   static final int freq = 5;
   int ventColor;

   public TileTube() {
      this.facing = ForgeDirection.NORTH;
      this.openSides = new boolean[]{true, true, true, true, true, true};
      this.essentiaType = null;
      this.essentiaAmount = 0;
      this.suctionType = null;
      this.suction = 0;
      this.venting = 0;
      this.count = 0;
      this.ventColor = 0;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.essentiaType = Aspect.getAspect(nbttagcompound.getString("type"));
      this.essentiaAmount = nbttagcompound.getInteger("amount");
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getInteger("side"));
      byte[] sides = nbttagcompound.getByteArray("open");
      if (sides != null && sides.length == 6) {
         for(int a = 0; a < 6; ++a) {
            this.openSides[a] = sides[a] == 1;
         }
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.essentiaType != null) {
         nbttagcompound.setString("type", this.essentiaType.getTag());
      }

      nbttagcompound.setInteger("amount", this.essentiaAmount);
      byte[] sides = new byte[6];

      for(int a = 0; a < 6; ++a) {
         sides[a] = (byte)(this.openSides[a] ? 1 : 0);
      }

      nbttagcompound.setInteger("side", this.facing.ordinal());
      nbttagcompound.setByteArray("open", sides);
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.suctionType = Aspect.getAspect(nbttagcompound.getString("stype"));
      this.suction = nbttagcompound.getInteger("samount");
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      if (this.suctionType != null) {
         nbttagcompound.setString("stype", this.suctionType.getTag());
      }

      nbttagcompound.setInteger("samount", this.suction);
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (this.venting > 0) {
         --this.venting;
      }

      if (this.count == 0) {
         this.count = this.worldObj.rand.nextInt(10);
      }

      if (!this.worldObj.isRemote) {
         if (this.venting <= 0) {
            if (++this.count % 2 == 0) {
               this.calculateSuction(null, false, false);
               this.checkVenting();
               if (this.essentiaType != null && this.essentiaAmount == 0) {
                  this.essentiaType = null;
               }
            }

            if (this.count % 5 == 0 && this.suction > 0) {
               this.equalizeWithNeighbours(false);
            }
         }
      } else if (this.venting > 0) {
         Random r = new Random(this.hashCode() * 4L);
         float rp = r.nextFloat() * 360.0F;
         float ry = r.nextFloat() * 360.0F;
         double fx = -MathHelper.sin(ry / 180.0F * (float)Math.PI) * MathHelper.cos(rp / 180.0F * (float)Math.PI);
         double fz = MathHelper.cos(ry / 180.0F * (float)Math.PI) * MathHelper.cos(rp / 180.0F * (float)Math.PI);
         double fy = -MathHelper.sin(rp / 180.0F * (float)Math.PI);
         Thaumcraft.proxy.drawVentParticles(this.worldObj, (double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, fx / (double)5.0F, fy / (double)5.0F, fx / (double)5.0F, this.ventColor);
      }

   }

   void calculateSuction(Aspect filter, boolean restrict, boolean directional) {
      this.suction = 0;
      this.suctionType = null;
      ForgeDirection loc = null;

      for(int dir = 0; dir < 6; ++dir) {
         try {
            loc = ForgeDirection.getOrientation(dir);
            if ((!directional || this.facing == loc.getOpposite()) && this.isConnectable(loc)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, loc);
               if (te != null) {
                  IEssentiaTransport ic = (IEssentiaTransport)te;
                  if ((filter == null || ic.getSuctionType(loc.getOpposite()) == null || ic.getSuctionType(loc.getOpposite()) == filter) && (filter != null || this.getEssentiaAmount(loc) <= 0 || ic.getSuctionType(loc.getOpposite()) == null || this.getEssentiaType(loc) == ic.getSuctionType(loc.getOpposite())) && (filter == null || this.getEssentiaAmount(loc) <= 0 || this.getEssentiaType(loc) == null || ic.getSuctionType(loc.getOpposite()) == null || this.getEssentiaType(loc) == ic.getSuctionType(loc.getOpposite()))) {
                     int suck = ic.getSuctionAmount(loc.getOpposite());
                     if (suck > 0 && suck > this.suction + 1) {
                        Aspect st = ic.getSuctionType(loc.getOpposite());
                        if (st == null) {
                           st = filter;
                        }

                        this.setSuction(st, restrict ? suck / 2 : suck - 1);
                     }
                  }
               }
            }
         } catch (Exception ignored) {
         }
      }

   }

   void checkVenting() {
      ForgeDirection loc = null;

      for(int dir = 0; dir < 6; ++dir) {
         try {
            loc = ForgeDirection.getOrientation(dir);
            if (this.isConnectable(loc)) {
               TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, loc);
               if (te != null) {
                  IEssentiaTransport ic = (IEssentiaTransport)te;
                  int suck = ic.getSuctionAmount(loc.getOpposite());
                  if (this.suction > 0 && (suck == this.suction || suck == this.suction - 1) && this.suctionType != ic.getSuctionType(loc.getOpposite())) {
                     int c = -1;
                     if (this.suctionType != null) {
                        c = Config.aspectOrder.indexOf(this.suctionType);
                     }

                     this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockTube, 1, c);
                     this.venting = 40;
                  }
               }
            }
         } catch (Exception ignored) {
         }
      }

   }

   void equalizeWithNeighbours(boolean directional) {
      ForgeDirection loc = null;
      if (this.essentiaAmount <= 0) {
         for(int dir = 0; dir < 6; ++dir) {
            try {
               loc = ForgeDirection.getOrientation(dir);
               if ((!directional || this.facing != loc.getOpposite()) && this.isConnectable(loc)) {
                  TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, loc);
                  if (te != null) {
                     IEssentiaTransport ic = (IEssentiaTransport)te;
                     if (ic.canOutputTo(loc.getOpposite()) && (this.getSuctionType(null) == null || this.getSuctionType(null) == ic.getEssentiaType(loc.getOpposite()) || ic.getEssentiaType(loc.getOpposite()) == null) && this.getSuctionAmount(null) > ic.getSuctionAmount(loc.getOpposite()) && this.getSuctionAmount(null) >= ic.getMinimumSuction()) {
                        Aspect a = this.getSuctionType(null);
                        if (a == null) {
                           a = ic.getEssentiaType(loc.getOpposite());
                           if (a == null) {
                              a = ic.getEssentiaType(ForgeDirection.UNKNOWN);
                           }
                        }

                        int am = this.addEssentia(a, ic.takeEssentia(a, 1, loc.getOpposite()), loc);
                        if (am > 0) {
                           if (this.worldObj.rand.nextInt(100) == 0) {
                              this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockTube, 0, 0);
                           }

                           return;
                        }
                     }
                  }
               }
            } catch (Exception ignored) {
            }
         }

      }
   }

   public boolean isConnectable(ForgeDirection face) {
      return face != ForgeDirection.UNKNOWN && this.openSides[face.ordinal()];
   }

   public boolean canInputFrom(ForgeDirection face) {
      return face != ForgeDirection.UNKNOWN && this.openSides[face.ordinal()];
   }

   public boolean canOutputTo(ForgeDirection face) {
      return face != ForgeDirection.UNKNOWN && this.openSides[face.ordinal()];
   }

   public void setSuction(Aspect aspect, int amount) {
      this.suctionType = aspect;
      this.suction = amount;
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return this.suctionType;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return this.suction;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.essentiaType;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.essentiaAmount;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      if (this.canOutputTo(face) && this.essentiaType == aspect && this.essentiaAmount > 0 && amount > 0) {
         --this.essentiaAmount;
         if (this.essentiaAmount <= 0) {
            this.essentiaType = null;
         }

         this.markDirty();
         return 1;
      } else {
         return 0;
      }
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      if (this.canInputFrom(face) && this.essentiaAmount == 0 && amount > 0) {
         this.essentiaType = aspect;
         ++this.essentiaAmount;
         this.markDirty();
         return 1;
      } else {
         return 0;
      }
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i == 0) {
         if (this.worldObj.isRemote) {
            this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:creak", 1.0F, 1.3F + this.worldObj.rand.nextFloat() * 0.2F, false);
         }

         return true;
      } else if (i != 1) {
         return super.receiveClientEvent(i, j);
      } else {
         if (this.worldObj.isRemote) {
            if (this.venting <= 0) {
               this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "random.fizz", 0.1F, 1.0F + this.worldObj.rand.nextFloat() * 0.1F, false);
            }

            this.venting = 50;
            if (j != -1 && j < Config.aspectOrder.size()) {
               this.ventColor = ((Aspect)Config.aspectOrder.get(j)).getColor();
            } else {
               this.ventColor = 11184810;
            }
         }

         return true;
      }
   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
      MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
       if (hit != null) {
           if (hit.subHit >= 0 && hit.subHit < 6) {
               player.worldObj.playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
               player.swingItem();
               this.markDirty();
               world.markBlockForUpdate(x, y, z);
               this.openSides[hit.subHit] = !this.openSides[hit.subHit];
               ForgeDirection dir = ForgeDirection.getOrientation(hit.subHit);
               TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
               if (tile instanceof TileTube) {
                   ((TileTube) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                   world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                   tile.markDirty();
               }
           }

           if (hit.subHit == 6) {
               player.worldObj.playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
               player.swingItem();
               int a = this.facing.ordinal();
               this.markDirty();

               while (true) {
                   ++a;
                   if (a >= 20) {
                       break;
                   }

                   if (this.canConnectSide(ForgeDirection.getOrientation(a % 6).getOpposite().ordinal()) && this.isConnectable(ForgeDirection.getOrientation(a % 6).getOpposite())) {
                       a %= 6;
                       this.facing = ForgeDirection.getOrientation(a);
                       world.markBlockForUpdate(x, y, z);
                       break;
                   }
               }
           }

       }
       return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {
   }

   public MovingObjectPosition rayTrace(World world, Vec3 vec3d, Vec3 vec3d1, MovingObjectPosition fullblock) {
      return fullblock;
   }

   protected boolean canConnectSide(int side) {
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
      return tile instanceof IEssentiaTransport;
   }

   public void addTraceableCuboids(List cuboids) {
      float min = 0.42F;
      float max = 0.58F;
      if (this.canConnectSide(0)) {
         cuboids.add(new IndexedCuboid6(0, new Cuboid6((float)this.xCoord + min, this.yCoord, (float)this.zCoord + min, (float)this.xCoord + max, (double)this.yCoord + (double)0.5F, (float)this.zCoord + max)));
      }

      if (this.canConnectSide(1)) {
         cuboids.add(new IndexedCuboid6(1, new Cuboid6((float)this.xCoord + min, (double)this.yCoord + (double)0.5F, (float)this.zCoord + min, (float)this.xCoord + max, this.yCoord + 1, (float)this.zCoord + max)));
      }

      if (this.canConnectSide(2)) {
         cuboids.add(new IndexedCuboid6(2, new Cuboid6((float)this.xCoord + min, (float)this.yCoord + min, this.zCoord, (float)this.xCoord + max, (float)this.yCoord + max, (double)this.zCoord + (double)0.5F)));
      }

      if (this.canConnectSide(3)) {
         cuboids.add(new IndexedCuboid6(3, new Cuboid6((float)this.xCoord + min, (float)this.yCoord + min, (double)this.zCoord + (double)0.5F, (float)this.xCoord + max, (float)this.yCoord + max, this.zCoord + 1)));
      }

      if (this.canConnectSide(4)) {
         cuboids.add(new IndexedCuboid6(4, new Cuboid6(this.xCoord, (float)this.yCoord + min, (float)this.zCoord + min, (double)this.xCoord + (double)0.5F, (float)this.yCoord + max, (float)this.zCoord + max)));
      }

      if (this.canConnectSide(5)) {
         cuboids.add(new IndexedCuboid6(5, new Cuboid6((double)this.xCoord + (double)0.5F, (float)this.yCoord + min, (float)this.zCoord + min, this.xCoord + 1, (float)this.yCoord + max, (float)this.zCoord + max)));
      }

      cuboids.add(new IndexedCuboid6(6, new Cuboid6((double)this.xCoord + (double)0.34375F, (double)this.yCoord + (double)0.34375F, (double)this.zCoord + (double)0.34375F, (double)this.xCoord + (double)0.65625F, (double)this.yCoord + (double)0.65625F, (double)this.zCoord + (double)0.65625F)));
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
   }
}
