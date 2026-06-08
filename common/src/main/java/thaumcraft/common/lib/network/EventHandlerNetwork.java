package thaumcraft.common.lib.network;

@Deprecated(forRemoval = true)
public class EventHandlerNetwork {
//   @SubscribeEvent
//   public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
//      Side side = FMLCommonHandler.instance().getEffectiveSide();
//      if (side == Side.SERVER) {
//         Player p = event.player;
//         PacketHandler.INSTANCE.sendTo(new PacketSyncWipe(), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(p), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedItems(p), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedEntities(p), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedPhenomena(p), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(p), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)0), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)1), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)2), (ServerPlayer)p);
//         PacketHandler.INSTANCE.sendTo(new PacketConfig(), (ServerPlayer)p);
//      }
//
//   }
//
//   @SubscribeEvent
//   public void clientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
//      if (Thaumcraft.getClientWorld() != null && Minecraft.getMinecraft().thePlayer != null) {
//         GuiResearchBrowser.completedResearch.put(Minecraft.getMinecraft().thePlayer.getGameProfile().getName(), new ArrayList());
//         Thaumcraft.log.info("Resetting research to defaults.");
//      }
//
//   }
//
//   @SubscribeEvent
//   public void clientLogsOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
//      if (Thaumcraft.getClientWorld() != null) {
//         Config.allowCheatSheet = Config.CallowCheatSheet;
//         Config.wardedStone = Config.CwardedStone;
//         Config.allowMirrors = Config.CallowMirrors;
//         Config.hardNode = Config.ChardNode;
//         Config.wuss = Config.Cwuss;
//         Config.researchDifficulty = Config.CresearchDifficulty;
//         Config.aspectTotalCap = Config.CaspectTotalCap;
//         Thaumcraft.log.info("Restoring client configs.");
//      }
//
//   }
}
