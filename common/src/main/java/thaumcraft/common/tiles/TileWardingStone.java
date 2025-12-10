package thaumcraft.common.tiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import thaumcraft.common.config.ConfigBlocks;

import java.util.List;

public class TileWardingStone extends TileEntity {
   int count = 0;

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         if (this.count == 0) {
            this.count = this.worldObj.rand.nextInt(100);
         }

         if (this.count % 5 == 0 && !this.gettingPower()) {
            List<EntityLivingBase> targets = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 3, this.zCoord + 1).expand(0.1, 0.1, 0.1));
            if (!targets.isEmpty()) {
               for(EntityLivingBase e : targets) {
                  if (!e.onGround && !(e instanceof EntityPlayer)) {
                     e.addVelocity(-MathHelper.sin((e.rotationYaw + 180.0F) * (float)Math.PI / 180.0F) * 0.2F, -0.1, MathHelper.cos((e.rotationYaw + 180.0F) * (float)Math.PI / 180.0F) * 0.2F);
                  }
               }
            }
         }

         if (++this.count % 100 == 0) {
            if ((this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) != ConfigBlocks.blockAiry || this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord) != 3) && this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord).isReplaceable(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord)) {
               this.worldObj.setBlock(this.xCoord, this.yCoord + 1, this.zCoord, ConfigBlocks.blockAiry, 4, 3);
            }

            if ((this.worldObj.getBlock(this.xCoord, this.yCoord + 2, this.zCoord) != ConfigBlocks.blockAiry || this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 2, this.zCoord) != 3) && this.worldObj.getBlock(this.xCoord, this.yCoord + 2, this.zCoord).isReplaceable(this.worldObj, this.xCoord, this.yCoord + 2, this.zCoord)) {
               this.worldObj.setBlock(this.xCoord, this.yCoord + 2, this.zCoord, ConfigBlocks.blockAiry, 4, 3);
            }
         }
      }

   }
}
