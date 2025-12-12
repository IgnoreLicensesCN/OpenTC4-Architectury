package thaumcraft.common.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.damagesource.DamageSource;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockZapS2C;

public class TileEldritchTrap extends TileEntity {
   int count = 20;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (Platform.getEnvironment() != Env.CLIENT && this.count-- <= 0) {
         this.count = 10 + this.level().rand.nextInt(25);
         Player p = this.level().getClosestPlayer((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, 3.0F);
         if (p != null) {
            p.attackEntityFrom(DamageSource.magic, 2.0F);
            if (this.level().rand.nextBoolean()) {
               Thaumcraft.addWarpToPlayer(p, 1 + this.level().rand.nextInt(2), true);
            }

            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockZapS2C((float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, (float)p.posX, (float)p.boundingBox.minY + p.eyeHeight, (float)p.posZ), new NetworkRegistry.TargetPoint(this.level().dimension(), this.xCoord, this.yCoord, this.zCoord, 32.0F));
         }
      }

   }
}
