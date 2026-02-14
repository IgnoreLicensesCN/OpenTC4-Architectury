package thaumcraft.common.lib.network.fx;

import com.linearity.opentc4.Color;
import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.beams.FXBeam;
import thaumcraft.common.Thaumcraft;

public class PacketFXBeamPulseS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_beam_pulse";
   public static MessageType messageType;

   private int sourceId;
   private int targetId;
   private int color;

   public PacketFXBeamPulseS2C() {}

   public PacketFXBeamPulseS2C(int sourceId, int targetId, int color) {
      this.sourceId = sourceId;
      this.targetId = targetId;
      this.color = color;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeInt(sourceId);
      buf.writeInt(targetId);
      buf.writeInt(color);
   }

   public static PacketFXBeamPulseS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBeamPulseS2C(
              buf.readInt(),
              buf.readInt(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world == null) return;

      Entity src = world.getEntity(sourceId);
      Entity dst = world.getEntity(targetId);
      if (src == null || dst == null) return;

      Color c = new Color(color);

      FXBeam beam = new FXBeam(
              world,
              src.getX(),
              src.getY() + src.getEyeHeight(),
              src.getZ(),
              dst,
              c.getRed() / 255f,
              c.getGreen() / 255f,
              c.getBlue() / 255f,
              20
      );

      beam.blendmode = 771;
      beam.width = 2.5F;
      beam.setType(1);
      beam.setReverse(true);
      beam.setPulse(true);

      mc.particleEngine.add(beam);
   }
}
