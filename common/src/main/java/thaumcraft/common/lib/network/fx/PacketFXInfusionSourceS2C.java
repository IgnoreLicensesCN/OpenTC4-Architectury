package thaumcraft.common.lib.network.fx;

import com.linearity.opentc4.mixinaccessors.clientbe.InfusionMatrixBlockEntityClientAccessor;
import com.linearity.opentc4.simpleutils.ObjectIntPair;
import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

public class PacketFXInfusionSourceS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_infusion_source";
   public static MessageType messageType;

   private final int x, y, z;
   private final byte dx, dy, dz;
   private final int color;

   public PacketFXInfusionSourceS2C(int x, int y, int z, byte dx, byte dy, byte dz, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
      this.color = color;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   public static void encode(PacketFXInfusionSourceS2C msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.x);
      buf.writeInt(msg.y);
      buf.writeInt(msg.z);
      buf.writeInt(msg.color);
      buf.writeByte(msg.dx);
      buf.writeByte(msg.dy);
      buf.writeByte(msg.dz);
   }

   public static PacketFXInfusionSourceS2C decode(FriendlyByteBuf buf) {
      return new PacketFXInfusionSourceS2C(
              buf.readInt(),
              buf.readInt(),
              buf.readInt(),
              buf.readByte(),
              buf.readByte(),
              buf.readByte(),
              buf.readInt()
      );
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      var level = Minecraft.getInstance().level;
      if (level == null) return;

      int tx = x - dx;
      int ty = y - dy;
      int tz = z - dz;

      ObjectIntPair<BlockPos> key = new ObjectIntPair<>(new BlockPos(tx,ty,tz),color);

      BlockEntity be = level.getBlockEntity(new BlockPos(x, y, z));
      if (!(be instanceof InfusionMatrixBlockEntity matrix)) return;

      int count = 15;

      BlockEntity srcTile = level.getBlockEntity(new BlockPos(tx, ty, tz));
      if (srcTile instanceof IInfusionComponentStackProvider) {
         count = 60;
      }

      // SourceFX 更新或新建
      var map = ((InfusionMatrixBlockEntityClientAccessor)matrix).opentc4$getClientTickContext().sourceFX;
      if (map.containsKey(key)) {
         InfusionMatrixBlockEntity.ClientTickContext.SourceFX fx = map.get(key);
         fx.ticks = count;
         map.put(key, fx);
      } else {
         map.put(key, new InfusionMatrixBlockEntity.ClientTickContext.SourceFX(
                 new BlockPos(tx,ty, tz),
                 count,
                 color
         ));
      }
   }
}
