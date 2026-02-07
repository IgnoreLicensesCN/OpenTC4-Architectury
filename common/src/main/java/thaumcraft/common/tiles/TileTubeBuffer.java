package thaumcraft.common.tiles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Vec3;
import net.minecraft.core.Direction;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.Cuboid6;

import java.util.List;

public class TileTubeBuffer extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, IWandable {
   public AspectList<Aspect>aspects = new AspectList<>();
   public final int MAXAMOUNT = 8;
   public boolean[] openSides = new boolean[]{true, true, true, true, true, true};
   public byte[] chokedSides = new byte[]{0, 0, 0, 0, 0, 0};
   int count = 0;
   int bellows = -1;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspects.readFromNBT(nbttagcompound);
      byte[] sides = nbttagcompound.getByteArray("open");
      if (sides != null && sides.length == 6) {
         for(int a = 0; a < 6; ++a) {
            this.openSides[a] = sides[a] == 1;
         }
      }

      this.chokedSides = nbttagcompound.getByteArray("choke");
      if (this.chokedSides == null || this.chokedSides.length < 6) {
         this.chokedSides = new byte[]{0, 0, 0, 0, 0, 0};
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspects.writeToNBT(nbttagcompound);
      byte[] sides = new byte[6];

      for(int a = 0; a < 6; ++a) {
         sides[a] = (byte)(this.openSides[a] ? 1 : 0);
      }

      nbttagcompound.setByteArray("open", sides);
      nbttagcompound.setByteArray("choke", this.chokedSides);
   }

   public AspectList<Aspect>getAspects() {
      return this.aspects;
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
      if (am != 1) {
         return am;
      } else if (this.aspects.visSize() < 8) {
         this.aspects.addAll(tt, am);
         this.markDirty();
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return 0;
      } else {
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.aspects.getAmount(tt) >= am) {
         this.aspects.reduceAndRemoveIfNotPositive(tt, am);
         this.markDirty();
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return this.aspects.getAmount(tag) >= amt;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return this.aspects.getAmount(tag);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(Direction face) {
      return this.openSides[face.ordinal()];
   }

   public boolean canInputFrom(Direction face) {
      return this.openSides[face.ordinal()];
   }

   public boolean canOutputTo(Direction face) {
      return this.openSides[face.ordinal()];
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(Direction loc) {
      return null;
   }

   public int getSuctionAmount(Direction loc) {
      return this.chokedSides[loc.ordinal()] == 2 ? 0 : (this.bellows > 0 && this.chokedSides[loc.ordinal()] != 1 ? this.bellows * 32 : 1);
   }

   public Aspect getEssentiaType(Direction loc) {
      return this.aspects.size() > 0 ? this.aspects.getAspects()[this.level().rand.nextInt(this.aspects.getAspects().length)] : null;
   }

   public int getEssentiaAmount(Direction loc) {
      return this.aspects.visSize();
   }

   public int takeEssentia(Aspect aspect, int amount, Direction face) {
      if (!this.canOutputTo(face)) {
         return 0;
      } else {
         TileEntity te = null;
         IEssentiaTransport ic = null;
         int suction = 0;
         te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, face);
         if (te != null) {
            ic = (IEssentiaTransport)te;
            suction = ic.getSuctionAmount(face.getOpposite());
         }

         for(Direction dir : Direction.VALID_DIRECTIONS) {
            if (this.canOutputTo(dir) && dir != face) {
               te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, dir);
               if (te != null) {
                  ic = (IEssentiaTransport)te;
                  int sa = ic.getSuctionAmount(dir.getOpposite());
                  Aspect su = ic.getSuctionType(dir.getOpposite());
                  if ((su == aspect || su == null) && suction < sa && this.getSuctionAmount(dir) < sa) {
                     return 0;
                  }
               }
            }
         }

         if (amount > this.aspects.getAmount(aspect)) {
            amount = this.aspects.getAmount(aspect);
         }

         return this.takeFromContainer(aspect, amount) ? amount : 0;
      }
   }

   public int addEssentia(Aspect aspect, int amount, Direction face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      ++this.count;
      if (this.bellows < 0 || this.count % 20 == 0) {
         this.getBellows();
      }

      if (Platform.getEnvironment() != Env.CLIENT && this.count % 5 == 0) {
         int var10000 = this.aspects.visSize();
         this.getClass();
         if (var10000 < 8) {
            this.fillBuffer();
         }
      }

   }

   void fillBuffer() {
      TileEntity te = null;
      IEssentiaTransport ic = null;

      for(Direction dir : Direction.VALID_DIRECTIONS) {
         te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, dir);
         if (te != null) {
            ic = (IEssentiaTransport)te;
            if (ic.getEssentiaAmount(dir.getOpposite()) > 0 && ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount(dir) && this.getSuctionAmount(dir) >= ic.getMinimumSuction()) {
               Aspect ta = ic.getEssentiaType(dir.getOpposite());
               this.addToContainer(ta, ic.takeEssentia(ta, 1, dir.getOpposite()));
               return;
            }
         }
      }

   }

   public void getBellows() {
      this.bellows = TileBellows.getBellows(this.level(), this.xCoord, this.yCoord, this.zCoord, Direction.VALID_DIRECTIONS);
   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      HitResult hit = RayTracer.retraceBlock(world, player, x, y, z);
       if (hit != null) {
           if (hit.subHit >= 0 && hit.subHit < 6) {
               player.swingItem();
               if (player.isSneaking()) {
                   player.level().playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:squeek", 0.6F, 1.1F + world.getRandom().nextFloat() * 0.2F, false);
                   if (Platform.getEnvironment() != Env.CLIENT) {
                       ++this.chokedSides[hit.subHit];
                       if (this.chokedSides[hit.subHit] > 2) {
                           this.chokedSides[hit.subHit] = 0;
                       }

                       this.markDirty();
                       world.markBlockForUpdate(x, y, z);
                   }
               } else {
                   player.level().playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.level().rand.nextFloat() * 0.2F, false);
                   this.openSides[hit.subHit] = !this.openSides[hit.subHit];
                   Direction dir = Direction.getOrientation(hit.subHit);
                   TileEntity tile = this.level().getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                   if (tile instanceof TileTube) {
                       ((TileTube) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                       world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                       tile.markDirty();
                   }

                   if (tile instanceof TileTubeBuffer) {
                       ((TileTubeBuffer) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                       world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                       tile.markDirty();
                   }

                   this.markDirty();
                   world.markBlockForUpdate(x, y, z);
               }
           }

       }
       return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
   }

   public HitResult rayTrace(World world, Vec3 vec3d, Vec3 vec3d1, HitResult fullblock) {
      return fullblock;
   }

   private boolean canConnectSide(int side) {
      Direction dir = Direction.getOrientation(side);
      TileEntity tile = this.level().getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
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

      cuboids.add(new IndexedCuboid6(6, new Cuboid6((float)this.xCoord + 0.25F, (float)this.yCoord + 0.25F, (float)this.zCoord + 0.25F, (float)this.xCoord + 0.75F, (float)this.yCoord + 0.75F, (float)this.zCoord + 0.75F)));
   }
}
