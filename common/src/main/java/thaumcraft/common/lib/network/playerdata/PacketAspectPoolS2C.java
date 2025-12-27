package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import com.linearity.opentc4.utils.StatCollector;

public class PacketAspectPoolS2C extends ThaumcraftBaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_pool";
   public static MessageType messageType;

   private String key;
   private int amount;
   private int total;
   private static long lastSound = 0L;

   public PacketAspectPoolS2C() {}

   public PacketAspectPoolS2C(String key, int amount, int total) {
      this.key = key;
      this.amount = amount;
      this.total = total;
   }

   // ------------------ 编码/解码 ------------------
   public static void encode(PacketAspectPoolS2C msg, FriendlyByteBuf buf) {
      buf.writeUtf(msg.key);
      buf.writeInt(msg.amount);
      buf.writeInt(msg.total);
   }

   public static PacketAspectPoolS2C decode(FriendlyByteBuf buf) {
      String key = buf.readUtf();
      short amount = buf.readShort();
      short total = buf.readShort();
      return new PacketAspectPoolS2C(key, amount, total);
   }

   // ------------------ 客户端处理 ------------------
   public static void receive(PacketAspectPoolS2C msg, Player player) {
      ClientHandler.handle(msg);
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      if (context.getPlayer().level().isClientSide) {
         ClientHandler.handle(this);
      }
   }

   // ------------------ CLIENT LOGIC -------------------
   public static class ClientHandler {
      public static void handle(PacketAspectPoolS2C message) {
         Aspect aspect = Aspect.getAspect(message.key);
         if (aspect != null) {
            boolean success = Thaumcraft.playerKnowledge.setAspectPool(
                    Minecraft.getInstance().player.getName().getString(),
                    aspect,
                    message.total
            );

            if (success && message.amount > 0) {
               String text = StatCollector.translateToLocal("tc.addaspectpool")
                       .replace("%s", String.valueOf(message.amount))
                       .replace("%n", aspect.getName());

               PlayerNotifications.addNotification(text, aspect);

               for (int i = 0; i < message.amount; i++) {
                  PlayerNotifications.addAspectNotification(aspect);
               }

               if (System.currentTimeMillis() > lastSound) {
                  Minecraft.getInstance().player.playSound(
                          net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP, // 可换成原来的 random.orb
                          0.1F,
                          0.9F + Minecraft.getInstance().player.getRandom().nextFloat() * 0.2F
                  );
                  lastSound = System.currentTimeMillis() + 100L;
               }
            }
         }
      }
   }
}
