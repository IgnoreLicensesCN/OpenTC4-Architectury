package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileArcaneBore;

public class PacketBoreDigS2C extends BaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":bore_dig";

   public static MessageType messageType;

   private final int x;
   private final int y;
   private final int z;
   private final int digloc;

   // 构造
   public PacketBoreDigS2C(int x, int y, int z, int digloc) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.digloc = digloc;
   }

   // 解码
   public static PacketBoreDigS2C decode(FriendlyByteBuf buf) {
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      int digloc = buf.readInt();
      return new PacketBoreDigS2C(x, y, z, digloc);
   }

   // 编码
   public static void encode(PacketBoreDigS2C msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.x);
      buf.writeInt(msg.y);
      buf.writeInt(msg.z);
      buf.writeInt(msg.digloc);
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Level world = context.getPlayer().level(); // 客户端玩家世界
      BlockEntity tile = world.getBlockEntity(new net.minecraft.core.BlockPos(x, y, z));
      if (tile instanceof TileArcaneBore bore) {
         bore.getDigEvent(digloc);
      }
   }
}
