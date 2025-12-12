package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import thaumcraft.common.ClientFXUtils;

import static thaumcraft.common.ThaumcraftSounds.ZAP;

public class PacketFXBlockZapS2C extends BaseS2CMessage {

   public static final String ID = "thaumcraft:fx_block_zap";
   public static MessageType messageType;

   private float x;
   private float y;
   private float z;
   private float dx;
   private float dy;
   private float dz;

   public PacketFXBlockZapS2C() {}

   public PacketFXBlockZapS2C(float x, float y, float z, float dx, float dy, float dz) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   public static void encode(PacketFXBlockZapS2C msg, FriendlyByteBuf buf) {
      buf.writeFloat(msg.x);
      buf.writeFloat(msg.y);
      buf.writeFloat(msg.z);
      buf.writeFloat(msg.dx);
      buf.writeFloat(msg.dy);
      buf.writeFloat(msg.dz);
   }

   public static PacketFXBlockZapS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBlockZapS2C(
              buf.readFloat(),
              buf.readFloat(),
              buf.readFloat(),
              buf.readFloat(),
              buf.readFloat(),
              buf.readFloat()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      var world = Minecraft.getInstance().level;
      if (world == null) return;

      // bolt effect
      ClientFXUtils.nodeBolt(world, x, y, z, dx, dy, dz);

      // play sound
      world.playLocalSound(
              x, y, z,
              ZAP,      // 推荐提前做 SoundEvent
              SoundSource.BLOCKS,
              0.1F,
              1.0F + world.random.nextFloat() * 0.2F,
              false
      );
   }
}
