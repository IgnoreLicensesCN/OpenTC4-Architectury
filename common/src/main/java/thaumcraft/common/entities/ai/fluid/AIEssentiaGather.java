package thaumcraft.common.entities.ai.fluid;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileJarFillable;

public class AIEssentiaGather extends EntityAIBase {
   private EntityGolemBase theGolem;
   private double crucX;
   private double crucY;
   private double crucZ;
   private World theWorld;
   private long delay = 0L;
   int start = 0;

   public AIEssentiaGather(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.getNavigator().noPath() && this.delay <= System.currentTimeMillis()) {
         ChunkCoordinates home = this.theGolem.getHomePosition();
         ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;
         if (this.theGolem.getDistanceSq((float)cX + 0.5F, (float)cY + 0.5F, (float)cZ + 0.5F) > (double)6.0F) {
            return false;
         } else {
            this.start = 0;
            TileEntity te = this.theWorld.getTileEntity(cX, cY, cZ);
            if (te != null) {
               if (te instanceof IEssentiaTransport) {
                  IEssentiaTransport etrans = (IEssentiaTransport)te;
                  if ((te instanceof TileJarFillable || te instanceof TileEssentiaReservoir || etrans.canOutputTo(facing)) && etrans.getEssentiaAmount(facing) > 0 && (this.theGolem.essentiaAmount == 0 || (this.theGolem.essentia == null || this.theGolem.essentia.equals(etrans.getEssentiaType(facing)) || this.theGolem.essentia.equals(etrans.getEssentiaType(ForgeDirection.UNKNOWN))) && this.theGolem.essentiaAmount < this.theGolem.getCarryLimit())) {
                     this.delay = System.currentTimeMillis() + 1000L;
                     this.start = 0;
                     return true;
                  }
               } else {
                  int a = 5;
                  this.start = -1;

                  for(int prevTot = -1; a >= 0; --a) {
                     te = this.theWorld.getTileEntity(cX, cY + a, cZ);
                     if (te instanceof TileAlembic) {
                        TileAlembic ta = (TileAlembic)te;
                        if ((this.theGolem.essentiaAmount == 0 || (this.theGolem.essentia == null || this.theGolem.essentia.equals(ta.aspect)) && this.theGolem.essentiaAmount < this.theGolem.getCarryLimit()) && ta.amount > prevTot) {
                           this.delay = System.currentTimeMillis() + 1000L;
                           this.start = a;
                           prevTot = ta.amount;
                        }
                     }
                  }

                   return this.start >= 0;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity te = this.theWorld.getTileEntity(cX, cY + this.start, cZ);
      if (te instanceof IEssentiaTransport) {
         if (te instanceof TileAlembic || te instanceof TileJarFillable) {
            facing = ForgeDirection.UP;
         }

         if (te instanceof TileEssentiaReservoir) {
            facing = ((TileEssentiaReservoir)te).facing;
         }

         IEssentiaTransport ta = (IEssentiaTransport)te;
         if (ta.getEssentiaAmount(facing) == 0) {
            return;
         }

         if (ta.canOutputTo(facing) && ta.getEssentiaAmount(facing) > 0 && (this.theGolem.essentiaAmount == 0 || (this.theGolem.essentia == null || this.theGolem.essentia.equals(ta.getEssentiaType(facing)) || this.theGolem.essentia.equals(ta.getEssentiaType(ForgeDirection.UNKNOWN))) && this.theGolem.essentiaAmount < this.theGolem.getCarryLimit())) {
            Aspect a = ta.getEssentiaType(facing);
            if (a == null) {
               a = ta.getEssentiaType(ForgeDirection.UNKNOWN);
            }

            int qq = ta.getEssentiaAmount(facing);
            if (te instanceof TileEssentiaReservoir) {
               qq = ((TileEssentiaReservoir)te).containerContains(a);
            }

            int am = Math.min(qq, this.theGolem.getCarryLimit() - this.theGolem.essentiaAmount);
            this.theGolem.essentia = a;
            int taken = ta.takeEssentia(a, am, facing);
            if (taken > 0) {
               EntityGolemBase var10000 = this.theGolem;
               var10000.essentiaAmount += taken;
               this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.05F, 1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.3F);
               this.theGolem.updateCarried();
            } else {
               this.theGolem.essentia = null;
            }

            this.delay = System.currentTimeMillis() + 100L;
         }
      }

   }
}
