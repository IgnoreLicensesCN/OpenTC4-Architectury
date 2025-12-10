package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.Thaumcraft;

import java.util.Objects;

public class PacketAspectPlaceToServerC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_place";
   public static MessageType messageType;

   private ResourceKey<Level> dim;
   private String playerName;
   private int x, y, z;
   private Aspect aspect;
   private byte q, r;

   // ------------------ 构造 ------------------
   public PacketAspectPlaceToServerC2S() {}

   public PacketAspectPlaceToServerC2S(Player player, byte q, byte r, int x, int y, int z, Aspect aspect) {
      this.dim = player.level().dimension();
      this.playerName = player.getName().getString();
      this.x = x;
      this.y = y;
      this.z = z;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   // ------------------ 编码/解码 ------------------
   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeResourceKey(dim);
      buf.writeUtf(playerName);
      buf.writeInt(x);
      buf.writeInt(y);
      buf.writeInt(z);
      buf.writeUtf(aspect == null ? "null" : aspect.getTag());
      buf.writeByte(q);
      buf.writeByte(r);
   }

   public static PacketAspectPlaceToServerC2S decode(FriendlyByteBuf buf) {
      ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
      String playerName = buf.readUtf();
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      Aspect aspect = Aspect.getAspect(buf.readUtf());
      byte q = buf.readByte();
      byte r = buf.readByte();
      return new PacketAspectPlaceToServerC2S(playerName, dim, x, y, z, aspect, q, r);
   }

   // 私有构造，方便 decode
   private PacketAspectPlaceToServerC2S(String playerName, ResourceKey<Level> dim, int x, int y, int z, Aspect aspect, byte q, byte r) {
      this.playerName = playerName;
      this.dim = dim;
      this.x = x;
      this.y = y;
      this.z = z;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   // ------------------ 处理 ------------------
   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;

      Level world = serverPlayer.getServer().getLevel(dim);
      if (world == null) return;

      Player target = null;
      for (Player p:world.players()) {
         if (Objects.equals(p.getName().getString(),playerName)) {
            target = p;
         }
      }
      if (target == null) return;

      BlockEntity tile = world.getBlockEntity(new net.minecraft.core.BlockPos(x, y, z));
      if (tile instanceof TileResearchTable rt) {
         rt.placeAspect(q, r, aspect, target);
      }
   }

   @Override
   public MessageType getType() {
      return messageType;
   }
}
