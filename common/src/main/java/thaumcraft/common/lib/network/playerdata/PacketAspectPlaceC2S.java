package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

import java.util.Objects;

public class PacketAspectPlaceC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_place";
   public static MessageType messageType;

   private ResourceKey<Level> dim;
   private String playerName;
   private BlockPos pos;
   private Aspect aspect;
   private byte q, r;

   // ------------------ 构造 ------------------
   public PacketAspectPlaceC2S() {}

   public PacketAspectPlaceC2S(Player player, byte q, byte r, BlockPos pos, Aspect aspect) {
      this.dim = player.level().dimension();
      this.playerName = player.getGameProfile().getName();
      this.pos = pos;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   // ------------------ 编码/解码 ------------------
   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeResourceKey(dim);
      buf.writeUtf(playerName);
      buf.writeBlockPos(pos);
      buf.writeResourceLocation(aspect.aspectKey);
      buf.writeByte(q);
      buf.writeByte(r);
   }

   public static PacketAspectPlaceC2S decode(FriendlyByteBuf buf) {
      ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
      String playerName = buf.readUtf();
      var blockPos = buf.readBlockPos();
      Aspect aspect = Aspect.getAspect(new AspectResourceLocation(buf.readResourceLocation()));
      byte q = buf.readByte();
      byte r = buf.readByte();
      return new PacketAspectPlaceC2S(playerName, dim, blockPos, aspect, q, r);
   }

   // 私有构造，方便 decode
   private PacketAspectPlaceC2S(String playerName, ResourceKey<Level> dim, BlockPos pos, Aspect aspect, byte q, byte r) {
      this.playerName = playerName;
      this.dim = dim;
      this.pos = pos;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   // ------------------ 处理 ------------------
   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;

      var world = serverPlayer.getServer().getLevel(dim);
      if (world == null) return;

      ServerPlayer target = null;
      for (ServerPlayer p:world.players()) {
         if (Objects.equals(p.getName().getString(),playerName)) {
            target = p;
         }
      }
      if (target == null) return;

      BlockEntity tile = world.getBlockEntity(pos);
      if (tile instanceof ResearchTableBlockEntity researchTable) {
         researchTable.placeAspect(new HexCoord(q, r), aspect, target);
      }
   }

   @Override
   public MessageType getType() {
      return messageType;
   }
}
