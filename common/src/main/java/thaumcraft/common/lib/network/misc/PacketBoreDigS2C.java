package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

public class PacketBoreDigS2C extends ThaumcraftBaseS2CMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":bore_dig";

   public static MessageType messageType;

   private final BlockPos selfPos;
   private final BlockPos digPos;

   public PacketBoreDigS2C(BlockPos selfPos, BlockPos digPos) {
      this.selfPos = selfPos;
      this.digPos = digPos;
   }

   public static PacketBoreDigS2C decode(FriendlyByteBuf buf) {
      var selfPos = buf.readBlockPos();
      var digPos = buf.readBlockPos();
      return new PacketBoreDigS2C(selfPos,digPos);
   }

   public static void encode(PacketBoreDigS2C msg, FriendlyByteBuf buf) {
      buf.writeBlockPos(msg.selfPos);
      buf.writeBlockPos(msg.digPos);
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
      if (world.getBlockEntity(selfPos) instanceof ArcaneBoreBlockEntity bore) {
         bore.getDigEvent(digPos);
      }
   }
}
