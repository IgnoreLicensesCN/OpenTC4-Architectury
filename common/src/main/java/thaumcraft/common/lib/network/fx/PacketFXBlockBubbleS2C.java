package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;

import java.awt.*;

public class PacketFXBlockBubbleS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":block_bubble";
   public static MessageType messageType;

   private int x;
   private int y;
   private int z;
   private int color;

   public PacketFXBlockBubbleS2C() {}

   public PacketFXBlockBubbleS2C(int x, int y, int z, int color) {
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

   public static PacketFXBlockBubbleS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBlockBubbleS2C(
              buf.readInt(),
              buf.readInt(),
              buf.readInt(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      context.queue(() -> {
         var world = Minecraft.getInstance().level;
         if (world == null) return;

         Color c = new Color(color);
         float r = c.getRed() / 255f;
         float g = c.getGreen() / 255f;
         float b = c.getBlue() / 255f;

         for (int a = 0; a < ClientFXUtils.particleCount(1); ++a) {

            ClientFXUtils.crucibleBubble(world,
                    x,
                    y + world.random.nextFloat(),
                    z + world.random.nextFloat(),
                    r, g, b);

            ClientFXUtils.crucibleBubble(world,
                    x + 1,
                    y + world.random.nextFloat(),
                    z + world.random.nextFloat(),
                    r, g, b);

            ClientFXUtils.crucibleBubble(world,
                    x + world.random.nextFloat(),
                    y + world.random.nextFloat(),
                    z,
                    r, g, b);

            ClientFXUtils.crucibleBubble(world,
                    x + world.random.nextFloat(),
                    y + world.random.nextFloat(),
                    z + 1,
                    r, g, b);
         }
      });
   }
}
