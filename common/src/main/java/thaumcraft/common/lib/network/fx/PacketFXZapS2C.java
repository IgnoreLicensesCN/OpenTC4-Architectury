package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.bolt.FXLightningBolt;
import thaumcraft.common.Thaumcraft;

public class PacketFXZapS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_zap";
   public static MessageType messageType;

   private int sourceId;
   private int targetId;

   public PacketFXZapS2C() {} // 必须有无参构造

   public PacketFXZapS2C(int sourceId, int targetId) {
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

   public static PacketFXZapS2C decode(FriendlyByteBuf buf) {
      return new PacketFXZapS2C(buf.readInt(), buf.readInt());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      // 只在客户端执行
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world == null) return;

      Entity source = world.getEntity(sourceId);
      Entity target = world.getEntity(targetId);
      if (source != null && target != null) {
         double y1 = source.getY() + source.getBbHeight() / 2.0;
         double y2 = target.getY() + target.getBbHeight() / 2.0;

         FXLightningBolt bolt = new FXLightningBolt(world,
                 source.getX(), y1, source.getZ(),
                 target.getX(), y2, target.getZ(),
                 world.getRandom().nextLong(),
                 6, 0.5f, 8);
         bolt.defaultFractal();
         bolt.setType(2);
         bolt.setWidth(0.125f);
         bolt.finalizeBolt();
      }
   }
}
