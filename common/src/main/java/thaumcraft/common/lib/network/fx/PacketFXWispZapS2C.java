package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;

public class PacketFXWispZapS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_wisp_zap";
   public static MessageType messageType;

   private int sourceId;
   private int targetId;

   public PacketFXWispZapS2C() {} // 无参构造必须有

   public PacketFXWispZapS2C(int sourceId, int targetId) {
      this.sourceId = sourceId;
      this.targetId = targetId;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeInt(sourceId);
      buf.writeInt(targetId);
   }

   public static PacketFXWispZapS2C decode(FriendlyByteBuf buf) {
      return new PacketFXWispZapS2C(buf.readInt(), buf.readInt());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world == null) return;

      Entity source = world.getEntity(sourceId);
      Entity target = world.getEntity(targetId);
      if (source != null && target != null) {
         ClientFXUtils.bolt(world, source, target);
      }
   }
}
