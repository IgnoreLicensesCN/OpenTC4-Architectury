package thaumcraft.common.entities.ai.fluid;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;

public class AIEssentiaGoto extends EntityAIBase {
   private EntityGolemBase theGolem;
   private double jarX;
   private double jarY;
   private double jarZ;
   private World theWorld;
   int count = 0;
   int prevX = 0;
   int prevY = 0;
   int prevZ = 0;

   public AIEssentiaGoto(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.ticksExisted % Config.golemDelay <= 0 && this.theGolem.essentia != null && this.theGolem.essentiaAmount != 0) {
         ChunkCoordinates jarloc = GolemHelper.findJarWithRoom(this.theGolem);
         if (jarloc == null) {
            return false;
         } else {
            this.jarX = jarloc.posX;
            this.jarY = jarloc.posY;
            this.jarZ = jarloc.posZ;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.count > 0 && !this.theGolem.getNavigator().noPath();
   }

   public void resetTask() {
      this.count = 0;
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

   public void startExecuting() {
      this.count = 200;
      this.prevX = MathHelper.floor_double(this.theGolem.posX);
      this.prevY = MathHelper.floor_double(this.theGolem.posY);
      this.prevZ = MathHelper.floor_double(this.theGolem.posZ);
      this.theGolem.getNavigator().tryMoveToXYZ(this.jarX, this.jarY, this.jarZ, this.theGolem.getAIMoveSpeed());
   }
}
