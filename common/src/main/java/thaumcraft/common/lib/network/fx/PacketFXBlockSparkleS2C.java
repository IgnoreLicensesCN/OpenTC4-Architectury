package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;

public class PacketFXBlockSparkleS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":block_sparkle";
   public static MessageType messageType;

   private int x;
   private int y;
   private int z;
   private int color;

   public PacketFXBlockSparkleS2C() {}

   public PacketFXBlockSparkleS2C(int x, int y, int z, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
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
      buf.writeInt(color);
   }

   public static PacketFXBlockSparkleS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBlockSparkleS2C(
              buf.readInt(),
              buf.readInt(),
              buf.readInt(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      // ensure run on main thread
      context.queue(() -> {
         var world = Minecraft.getInstance().level;
         if (world != null) {
            ClientFXUtils.blockSparkle(world, x, y, z, color, 7);
         }
      });
   }
}
