package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.ChunkPos;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TilePedestal;

public class PacketFXInfusionSourceS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = Thaumcraft.MOD_ID + ":fx_infusion_source";
   public static MessageType messageType;

   private int x, y, z;
   private byte dx, dy, dz;
   private int color;

   public PacketFXInfusionSourceS2C() {}

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

      String key = tx + ":" + ty + ":" + tz + ":" + color;

      BlockEntity be = level.getBlockEntity(new BlockPos(x, y, z));
      if (!(be instanceof TileInfusionMatrix matrix)) return;

      int count = 15;

      // 判断源头是否是 pedestal
      BlockEntity srcTile = level.getBlockEntity(new BlockPos(tx, ty, tz));
      if (srcTile instanceof TilePedestal) {
         count = 60;
      }

      // SourceFX 更新或新建
      var map = matrix.sourceFX;
      if (map.containsKey(key)) {
         TileInfusionMatrix.SourceFX fx = (TileInfusionMatrix.SourceFX) map.get(key);
         fx.ticks = count;
         map.put(key, fx);
      } else {
         map.put(key, new TileInfusionMatrix.SourceFX(
                 new BlockPos(tx,ty, tz),   // 1.20.1 ChunkPos(x,z) only
                 count,
                 color
         ));
      }
   }
}
