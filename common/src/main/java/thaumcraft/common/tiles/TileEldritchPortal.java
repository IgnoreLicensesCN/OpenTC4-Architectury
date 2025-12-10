package thaumcraft.common.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.world.dim.TeleporterThaumcraft;

import java.util.List;

public class TileEldritchPortal extends TileEntity {
   public int opencount = -1;
   private int count = 0;

   @Override
   public boolean canUpdate() {
       return super.canUpdate();
   }

   @Override
   public double getMaxRenderDistanceSquared() {
      return 9216.0F;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
   }

   @Override
   public void updateEntity() {
      ++this.count;
      if ((Platform.getEnvironment() == Env.CLIENT) && (this.count % 250 == 0 || this.count == 0)) {
         this.level().playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:evilportal", 1.0F, 1.0F, false);
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && this.opencount < 30) {
         ++this.opencount;
      }

      if (Platform.getEnvironment() != Env.CLIENT && this.count % 5 == 0) {
         List ents = this.level().getEntitiesWithinAABB(ServerPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(0.5F, 1.0F, 0.5F));
         if (!ents.isEmpty()) {
            for(Object e : ents) {
               ServerPlayer player = (ServerPlayer)e;
               if (player.ridingEntity == null && player.riddenByEntity == null) {
                  MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                  if (player.timeUntilPortal > 0) {
                     player.timeUntilPortal = 100;
                  } else if (player.dimension != Config.dimensionOuterId) {
                     player.timeUntilPortal = 100;
                     player.mcServer.getConfigurationManager().transferPlayerToDimension(player, Config.dimensionOuterId, new TeleporterThaumcraft(mServer.worldServerForDimension(Config.dimensionOuterId)));
                     if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ENTEROUTER")) {
                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ENTEROUTER"), player);
                        Thaumcraft.proxy.getResearchManager().completeResearch(player, "ENTEROUTER");
                     }
                  } else {
                     player.timeUntilPortal = 100;
                     player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterThaumcraft(mServer.worldServerForDimension(0)));
                  }
               }
            }
         }
      }

   }
}
