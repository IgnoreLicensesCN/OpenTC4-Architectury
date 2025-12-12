package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import thaumcraft.client.fx.migrated.particles.FXVisSparkle;
import thaumcraft.common.Thaumcraft;

import java.awt.*;
import java.util.Random;

public class PacketFXVisDrainS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_vis_drain";
   public static MessageType messageType;

   private int x, y, z;
   private int dx, dy, dz;
   private int color;

   public PacketFXVisDrainS2C() {}

   public PacketFXVisDrainS2C(int x, int y, int z, int xd, int yd, int zd, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = xd;
      this.dy = yd;
      this.dz = zd;
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
      buf.writeInt(dx);
      buf.writeInt(dy);
      buf.writeInt(dz);
      buf.writeInt(color);
   }

   public static PacketFXVisDrainS2C decode(FriendlyByteBuf buf) {
      return new PacketFXVisDrainS2C(
              buf.readInt(), buf.readInt(), buf.readInt(),
              buf.readInt(), buf.readInt(), buf.readInt(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world == null) return;

      Random rand = new Random();
      FXVisSparkle fb = new FXVisSparkle(
              world,
              dx + 0.4 + rand.nextFloat() * 0.2f,
              dy + 0.4 + rand.nextFloat() * 0.2f,
              dz + 0.4 + rand.nextFloat() * 0.2f,
              x + rand.nextFloat(),
              y + rand.nextFloat(),
              z + rand.nextFloat()
      );

      Color c = new Color(color);
      fb.setRBGColorF(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);

      mc.particleEngine.add(fb);
   }
}
