package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncAspectsS2C extends BaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":sync_aspects";
   public static MessageType messageType;

   public final List<AspectAmount> data;

   // ---------------- 内部类，封装 Aspect+数量 ----------------
   public static record AspectAmount(String tag, short amount) {}

   // ---------------- 构造 ----------------

   /** 服务端发送用 */
   public PacketSyncAspectsS2C(Player player) {
      AspectList aspects = Thaumcraft.playerKnowledge.getAspectsDiscovered(player.getName().getString());
      List<AspectAmount> list = new ArrayList<>();
      if (aspects != null) {
         for (Aspect a : aspects.getAspectTypes()) {
            if (a != null) list.add(new AspectAmount(a.getTag(), (short) aspects.getAmount(a)));
         }
      }
      this.data = list;
   }

   /** 解码用构造 */
   public PacketSyncAspectsS2C(List<AspectAmount> data) {
      this.data = data;
   }

   // ---------------- Architectury 必要方法 ----------------

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeShort(data.size());
      for (AspectAmount a : data) {
         buf.writeUtf(a.tag);
         buf.writeShort(a.amount);
      }
   }

   public static PacketSyncAspectsS2C decode(FriendlyByteBuf buf) {
      short size = buf.readShort();
      List<AspectAmount> list = new ArrayList<>();
      for (int i = 0; i < size; i++) {
         String tag = buf.readUtf();
         short amount = buf.readShort();
         list.add(new AspectAmount(tag, amount));
      }
      return new PacketSyncAspectsS2C(list);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (player != null && player.level().isClientSide) {
         ClientHandler.handle(this);
      }
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   // ---------------- 客户端逻辑 ----------------

   public static class ClientHandler {
      public static void handle(PacketSyncAspectsS2C msg) {
         Player player = Minecraft.getInstance().player;
         if (player == null) return;

         for (AspectAmount a : msg.data) {
            Aspect aspect = Aspect.getAspect(a.tag);
            if (aspect != null) {
               Thaumcraft.researchManager.completeAspect(player.getName().getString(), aspect, a.amount);
            }
         }
      }
   }
}
