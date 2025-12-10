package thaumcraft.common.entities.ai.fluid;

import java.util.ArrayList;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;

public class AILiquidEmpty extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int waterX;
   private int waterY;
   private int waterZ;
   private ForgeDirection markerOrientation;
   private World theWorld;

   public AILiquidEmpty(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      if (this.theGolem.getNavigator().noPath() && this.theGolem.fluidCarried != null && this.theGolem.fluidCarried.amount != 0 && !(this.theGolem.getDistanceSq((float)home.posX + 0.5F, (float)home.posY + 0.5F, (float)home.posZ + 0.5F) > (double)5.0F)) {
         ArrayList<FluidStack> fluids = GolemHelper.getMissingLiquids(this.theGolem);
          if (fluids != null) {
              for (FluidStack fluid : fluids) {
                  if (fluid.isFluidEqual(this.theGolem.fluidCarried)) {
                      return true;
                  }
              }

          }
      }
       return false;
   }

   public boolean continueExecuting() {
      return false;
   }

   public void startExecuting() {
      ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
      ChunkCoordinates home = this.theGolem.getHomePosition();
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = this.theWorld.getTileEntity(cX, cY, cZ);
      if (tile instanceof IFluidHandler) {
         IFluidHandler fh = (IFluidHandler)tile;
         int amt = fh.fill(ForgeDirection.getOrientation(this.theGolem.homeFacing), this.theGolem.fluidCarried, true);
         FluidStack var10000 = this.theGolem.fluidCarried;
         var10000.amount -= amt;
         if (this.theGolem.fluidCarried.amount <= 0) {
            this.theGolem.fluidCarried = null;
         }

         if (amt > 200) {
            this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", Math.min(0.2F, 0.2F * ((float)amt / (float)this.theGolem.getFluidCarryLimit())), 1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.3F);
         }

         this.theGolem.updateCarried();
         this.theWorld.markBlockForUpdate(cX, cY, cZ);
         this.theGolem.itemWatched = null;
      }

   }
}
