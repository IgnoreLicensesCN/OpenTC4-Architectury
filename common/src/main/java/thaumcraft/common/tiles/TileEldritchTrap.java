package thaumcraft.common.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;

public class TileEldritchTrap extends TileEntity {
   int count = 20;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote && this.count-- <= 0) {
         this.count = 10 + this.worldObj.rand.nextInt(25);
         EntityPlayer p = this.worldObj.getClosestPlayer((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, 3.0F);
         if (p != null) {
            p.attackEntityFrom(DamageSource.magic, 2.0F);
            if (this.worldObj.rand.nextBoolean()) {
               Thaumcraft.addWarpToPlayer(p, 1 + this.worldObj.rand.nextInt(2), true);
            }

            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZap((float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, (float)p.posX, (float)p.boundingBox.minY + p.eyeHeight, (float)p.posZ), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0F));
         }
      }

   }
}
