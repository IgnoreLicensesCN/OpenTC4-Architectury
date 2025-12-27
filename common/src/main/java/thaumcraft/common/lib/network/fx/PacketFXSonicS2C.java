package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.other.FXSonic;
import thaumcraft.common.Thaumcraft;

public class PacketFXSonicS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_sonic";
   public static MessageType messageType;

   private int source;

   public PacketFXSonicS2C() {}

   public PacketFXSonicS2C(int source) {
      this.source = source;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   public static void encode(PacketFXSonicS2C msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.source);
   }

   public static PacketFXSonicS2C decode(FriendlyByteBuf buf) {
      return new PacketFXSonicS2C(buf.readInt());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      Entity e = mc.level.getEntity(this.source);
      if (e != null) {
         FXSonic fx = new FXSonic(mc.level,
                 e.getX(), e.getY(), e.getZ(),
                 e, 10);
         mc.particleEngine.add(fx);
      }
   }
}
