package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

public class PacketAspectPoolS2C extends ThaumcraftBaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_pool";
   public static MessageType messageType;

   private AspectResourceLocation key;
   private int amountChanged;
   private int total;
   private static long lastSound = 0L;
   public static final long SOUND_DELAY = 100L;

   public PacketAspectPoolS2C() {}

   public PacketAspectPoolS2C(AspectResourceLocation key, int amountChanged, int total) {
      this.key = key;
      this.amountChanged = amountChanged;
      this.total = total;
   }

   // ------------------ 编码/解码 ------------------
   public static void encode(PacketAspectPoolS2C msg, FriendlyByteBuf buf) {
      buf.writeResourceLocation(msg.key);
      buf.writeInt(msg.amountChanged);
      buf.writeInt(msg.total);
   }

   public static PacketAspectPoolS2C decode(FriendlyByteBuf buf) {
      ResourceLocation key = buf.readResourceLocation();
      int amount = buf.readInt();
      int total = buf.readInt();
      return new PacketAspectPoolS2C(AspectResourceLocation.of(key), amount, total);
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
      Aspect aspect = Aspect.getAspect(this.key);
      var player = Minecraft.getInstance().player;
      if (aspect != null && player != null) {
         boolean success = Thaumcraft.playerKnowledge.setAspectPool(
                 player.getGameProfile().getName(),
                 aspect,
                 this.total
         );

         if (success && this.amountChanged > 0) {
            String text = Component.translatable("tc.addaspectpool")
                    .replace("%s", String.valueOf(this.amountChanged))
                    .replace("%n", aspect.getName());

            PlayerNotifications.addNotification(text, aspect);

            for (int i = 0; i < this.amountChanged; i++) {
               PlayerNotifications.addAspectNotification(aspect);
            }

            if (System.currentTimeMillis() > lastSound) {
               Minecraft.getInstance().player.playSound(
                       net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                       0.1F,
                       0.9F + Minecraft.getInstance().player.getRandom().nextFloat() * 0.2F
               );
               lastSound = System.currentTimeMillis() +SOUND_DELAY;
            }
         }
      }
   }
}
