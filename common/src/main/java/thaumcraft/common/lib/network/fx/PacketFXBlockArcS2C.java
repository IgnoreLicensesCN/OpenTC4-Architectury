package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.boss.EntityCultistPortal;

public class PacketFXBlockArcS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_block_arc";
   public static MessageType messageType;

   private int x;
   private int y;
   private int z;
   private int sourceId;

   public PacketFXBlockArcS2C() {}

   public PacketFXBlockArcS2C(int x, int y, int z, int sourceId) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.sourceId = sourceId;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeInt(x);
      buf.writeInt(y);
      buf.writeInt(z);
      buf.writeInt(sourceId);
   }

   public static PacketFXBlockArcS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBlockArcS2C(
              buf.readInt(),
              buf.readInt(),
              buf.readInt(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel level = mc.level;
      if (level == null) return;

      Entity source = level.getEntity(sourceId);
      if (source == null) return;

      // arc 颜色
      float r = 0.3F - level.random.nextFloat() * 0.1F;
      float g = 0.0F;
      float b = 0.5F + level.random.nextFloat() * 0.2F;

      if (source instanceof EntityCultistPortal) {
         r = 0.5F + level.random.nextFloat() * 0.2F;
         g = 0.0F;
         b = 0.0F;
      }

      double height = source.getBbHeight();

      ClientFXUtils.arcLightning(
              level,
              source.getX(),
              source.getBoundingBox().minY + (height / 2.0),
              source.getZ(),
              x + 0.5,
              y + 1,
              z + 0.5,
              r, g, b,
              0.5F
      );
   }
}
