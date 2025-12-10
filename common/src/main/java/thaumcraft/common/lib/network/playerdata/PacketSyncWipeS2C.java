package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;

import thaumcraft.common.Thaumcraft;

public class PacketSyncWipeS2C extends BaseS2CMessage {
   public static final ResourceLocation ID = new ResourceLocation(Thaumcraft.MOD_ID, "sync_wipe");
   public static MessageType TYPE;

   public PacketSyncWipeS2C() {}

   // ------------- BaseS2CMessage 必须实现的 3 个方法 -------------

   /** 空包 → 不写任何东西 */
   @Override
   public void write(FriendlyByteBuf buf) {}

   /** 收到消息时执行 */
   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (player != null && player.level().isClientSide) {
         ClientHandler.handle();
      }
   }

   /** 返回消息类型 */
   @Override
   public MessageType getType() {
      return TYPE;
   }

   // ---------------- CLIENT LOGIC -------------------
   public static class ClientHandler {
      public static void handle() {
         Player player = Minecraft.getInstance().player;
         if (player == null) return;

         String name = player.getName().getString();
         Thaumcraft.playerKnowledge.wipePlayerKnowledge(name);
      }
   }
}
