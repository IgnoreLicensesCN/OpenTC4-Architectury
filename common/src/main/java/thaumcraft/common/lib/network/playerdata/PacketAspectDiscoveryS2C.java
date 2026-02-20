package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.api.aspects.Aspect;


public class PacketAspectDiscoveryS2C extends ThaumcraftBaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_discovery";
   public static MessageType messageType;

   private ResourceLocation key;

   // 构造
   public PacketAspectDiscoveryS2C() {}
   public PacketAspectDiscoveryS2C(ResourceLocation key) { this.key = key; }

   // 编码
   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeResourceLocation(key);
   }

   // 解码
   public static PacketAspectDiscoveryS2C decode(FriendlyByteBuf buf) {
      return new PacketAspectDiscoveryS2C(buf.readResourceLocation());
   }

   // 客户端处理逻辑
   @Override
   public void handle(NetworkManager.PacketContext context) {
      if (!context.getPlayer().level().isClientSide) return;

      Minecraft mc = Minecraft.getInstance();
      Aspect aspect = Aspect.getAspect(key);
      if (aspect == null) return;

      Thaumcraft.playerKnowledge.addDiscoveredAspect(mc.player.getGameProfile().getName(), aspect);

      String text = Component.translatable("tc.addaspectdiscovery").replace("%n", aspect.getName());
      PlayerNotifications.addNotification("§6" + text, aspect);

      mc.player.playSound(
              net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP, // 原 "random.orb" 替换为 Minecraft 1.20+
              0.2F,
              0.5F + mc.level.random.nextFloat() * 0.2F
      );

      GuiResearchBrowser.highlightedResearch.add("ASPECTS");
      GuiResearchBrowser.highlightedResearch.add("BASICS");
   }

   @Override
   public MessageType getType() {
      return messageType;
   }
}
