package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.other.FXShieldRunes;
import thaumcraft.common.Thaumcraft;

public class PacketFXShieldS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_shield";
   public static MessageType messageType;

   private int source;
   private int target;

   public PacketFXShieldS2C() {}

   public PacketFXShieldS2C(int source, int target) {
      this.source = source;
      this.target = target;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   public static void encode(PacketFXShieldS2C msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.source);
      buf.writeInt(msg.target);
   }

   public static PacketFXShieldS2C decode(FriendlyByteBuf buf) {
      return new PacketFXShieldS2C(buf.readInt(), buf.readInt());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;

      Entity p = mc.level.getEntity(this.source);
      if (p == null) return;

      float pitch = 0.0F;
      float yaw = 0.0F;

      // ---- TARGET >= 0: 有目标实体，计算方向 ----
      if (this.target >= 0) {
         Entity t = mc.level.getEntity(this.target);

         if (t != null) {
            double dx = p.getX() - t.getX();
            double dy = (p.getBoundingBox().minY + p.getBoundingBox().maxY) * 0.5
                    - (t.getBoundingBox().minY + t.getBoundingBox().maxY) * 0.5;
            double dz = p.getZ() - t.getZ();
            double dh = Math.sqrt(dx * dx + dz * dz);

            yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
            pitch = (float)(-Math.toDegrees(Math.atan2(dy, dh)));
         } else {
            pitch = 90.0F;
            yaw = 0.0F;
         }

         FXShieldRunes fx = new FXShieldRunes(mc.level, p.getX(), p.getY(), p.getZ(), p, 8, yaw, pitch);
         mc.particleEngine.add(fx);
      }

      // ---- target == -1: 两个方向 ----
      else if (this.target == -1) {
         FXShieldRunes fx1 = new FXShieldRunes(mc.level, p.getX(), p.getY(), p.getZ(), p, 8, 0.0F, 90.0F);
         mc.particleEngine.add(fx1);

         FXShieldRunes fx2 = new FXShieldRunes(mc.level, p.getX(), p.getY(), p.getZ(), p, 8, 0.0F, 270.0F);
         mc.particleEngine.add(fx2);
      }

      // ---- target == -2: 单方向 270° ----
      else if (this.target == -2) {
         FXShieldRunes fx = new FXShieldRunes(mc.level, p.getX(), p.getY(), p.getZ(), p, 8, 0.0F, 270.0F);
         mc.particleEngine.add(fx);
      }

      // ---- target == -3: 单方向 90° ----
      else if (this.target == -3) {
         FXShieldRunes fx = new FXShieldRunes(mc.level, p.getX(), p.getY(), p.getZ(), p, 8, 0.0F, 90.0F);
         mc.particleEngine.add(fx);
      }
   }
}
