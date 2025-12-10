package thaumcraft.common.entities.ai.fluid;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;

public class AILiquidGoto extends EntityAIBase {
   private EntityGolemBase theGolem;
   private double waterX;
   private double waterY;
   private double waterZ;
   private World theWorld;
   int count = 0;
   int prevX = 0;
   int prevY = 0;
   int prevZ = 0;

   public AILiquidGoto(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.ticksExisted % Config.golemDelay <= 0 && (this.theGolem.fluidCarried == null || this.theGolem.fluidCarried.amount <= this.theGolem.getFluidCarryLimit() - 1000)) {
         for(FluidStack fluid : GolemHelper.getMissingLiquids(this.theGolem)) {
            Vec3 var1 = GolemHelper.findPossibleLiquid(fluid, this.theGolem);
            if (var1 != null) {
               this.theGolem.itemWatched = new ItemStack(Item.getItemById(fluid.getFluidID()), 1, fluid.amount);
               this.waterX = var1.xCoord;
               this.waterY = var1.yCoord;
               this.waterZ = var1.zCoord;
               double dd = this.theGolem.getDistance(this.waterX, this.waterY, this.waterZ);

               for(int xx = -1; xx <= 1; ++xx) {
                  for(int zz = -1; zz <= 1; ++zz) {
                     double dd2 = this.theGolem.getDistance(var1.xCoord + (double)xx, this.waterY, var1.zCoord + (double)zz);
                     if (dd2 < dd && this.theGolem.worldObj.isBlockNormalCubeDefault((int)var1.xCoord + xx, (int)this.waterY, (int)var1.zCoord + zz, true)) {
                        this.waterX = var1.xCoord + (double)xx;
                        this.waterZ = var1.zCoord + (double)zz;
                        dd = dd2;
                     }
                  }
               }

               return true;
            }
         }

         return false;
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
      this.theGolem.getNavigator().tryMoveToXYZ(this.waterX, this.waterY, this.waterZ, this.theGolem.getAIMoveSpeed());
   }
}
