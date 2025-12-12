package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.client.fx.migrated.beams.FXBeamGolemBoss;
import thaumcraft.common.Thaumcraft;

public class PacketFXBeamPulseGolemBossS2C extends BaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_beam_pulse";
   public static MessageType messageType;

   private int source;
   private int target;

   public PacketFXBeamPulseGolemBossS2C() {}

   public PacketFXBeamPulseGolemBossS2C(int source, int target) {
      this.source = source;
      this.target = target;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeInt(source);
      buf.writeInt(target);
   }

   public static PacketFXBeamPulseGolemBossS2C decode(FriendlyByteBuf buf) {
      return new PacketFXBeamPulseGolemBossS2C(buf.readInt(), buf.readInt());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world == null) return;

      Entity src = world.getEntity(source);
      Entity dst = world.getEntity(target);

      if (!(src instanceof LivingEntity s)) return;
      if (!(dst instanceof LivingEntity t)) return;

      // Beam 1
      FXBeamGolemBoss beam1 = new FXBeamGolemBoss(world, s, t,
              0.07F, 0.376F, 0.325F, 20);
      beam1.blendmode = 1;
      beam1.width = 3.0F;
      beam1.setType(2);
      beam1.setReverse(false);
      beam1.setPulse(true);
      mc.particleEngine.add(beam1);

      // Beam 2
      FXBeamGolemBoss beam2 = new FXBeamGolemBoss(world, s, t,
              1.0F, 0.5F, 0.5F, 20);
      beam2.blendmode = 1;
      beam2.width = 1.5F;
      beam2.setType(1);
      beam2.setReverse(false);
      beam2.setPulse(true);
      mc.particleEngine.add(beam2);
   }
}
