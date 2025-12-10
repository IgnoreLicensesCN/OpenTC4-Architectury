package thaumcraft.common.entities.ai.inventory;

import java.util.ArrayList;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;

public class AIEmptyGoto extends EntityAIBase {
   private EntityGolemBase theGolem;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private ChunkCoordinates dest = null;
   int count = 0;
   int prevX = 0;
   int prevY = 0;
   int prevZ = 0;

   public AIEmptyGoto(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.itemCarried != null && this.theGolem.ticksExisted % Config.golemDelay <= 0) {
         ArrayList<Byte> matchingColors = this.theGolem.getColorsMatching(this.theGolem.itemCarried);

         for(byte color : matchingColors) {
            ArrayList<IInventory> results = GolemHelper.getContainersWithRoom(this.theGolem.worldObj, this.theGolem, color);
            if (!results.isEmpty()) {
               ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
               ChunkCoordinates home = this.theGolem.getHomePosition();
               int cX = home.posX - facing.offsetX;
               int cY = home.posY - facing.offsetY;
               int cZ = home.posZ - facing.offsetZ;
               int tX = 0;
               int tY = 0;
               int tZ = 0;
               double range = Double.MAX_VALUE;
               float dmod = this.theGolem.getRange();

               for(IInventory te : results) {
                  double distance = this.theGolem.getDistanceSq((double)((TileEntity)te).xCoord + (double)0.5F, (double)((TileEntity)te).yCoord + (double)0.5F, (double)((TileEntity)te).zCoord + (double)0.5F);
                  if (distance < range && distance <= (double)(dmod * dmod) && (((TileEntity)te).xCoord != cX || ((TileEntity)te).yCoord != cY || ((TileEntity)te).zCoord != cZ)) {
                     range = distance;
                     tX = ((TileEntity)te).xCoord;
                     tY = ((TileEntity)te).yCoord;
                     tZ = ((TileEntity)te).zCoord;
                     this.dest = new ChunkCoordinates(tX, tY, tZ);
                  }
               }

               if (this.dest != null) {
                  this.movePosX = tX;
                  this.movePosY = tY;
                  this.movePosZ = tZ;
                  return true;
               }
            }
         }

         for(byte color : matchingColors) {
            for(Marker marker : this.theGolem.getMarkers()) {
               if ((marker.color == color || color == -1) && (this.theGolem.worldObj.getTileEntity(marker.x, marker.y, marker.z) == null || !(this.theGolem.worldObj.getTileEntity(marker.x, marker.y, marker.z) instanceof IInventory))) {
                  this.movePosX = marker.x;
                  this.movePosY = marker.y;
                  this.movePosZ = marker.z;
                  return true;
               }
            }
         }

      }
       return false;
   }

   public boolean continueExecuting() {
      return this.count > 0 && !this.theGolem.getNavigator().noPath();
   }

   public void updateTask() {
      --this.count;
      if (this.count == 0 && this.prevX == MathHelper.floor_double(this.theGolem.posX) && this.prevY == MathHelper.floor_double(this.theGolem.posY) && this.prevZ == MathHelper.floor_double(this.theGolem.posZ)) {
         Vec3 var2 = RandomPositionGenerator.findRandomTarget(this.theGolem, 2, 1);
         if (var2 != null) {
            this.count = 20;
            this.theGolem.getNavigator().tryMoveToXYZ(var2.xCoord, var2.yCoord, var2.zCoord, this.theGolem.getAIMoveSpeed());
         }
      }

      super.updateTask();
   }

   public void resetTask() {
      this.count = 0;
      this.dest = null;
   }

   public void startExecuting() {
      this.count = 200;
      this.prevX = MathHelper.floor_double(this.theGolem.posX);
      this.prevY = MathHelper.floor_double(this.theGolem.posY);
      this.prevZ = MathHelper.floor_double(this.theGolem.posZ);
      this.theGolem.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.theGolem.getAIMoveSpeed());
   }
}
