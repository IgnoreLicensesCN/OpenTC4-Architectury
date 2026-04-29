package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.abstracts.IResearchAspectPlaceableBlockEntity;

//ResearchTableBlockEntity only packet
public class PacketAspectPlaceC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_place";
   public static MessageType messageType;

   private final BlockPos placeAspectToPos;
   private final Aspect aspect;
   private final byte q;
   private final byte r;

   public PacketAspectPlaceC2S(byte q, byte r, BlockPos placeAspectToPos, Aspect aspect) {
      this.placeAspectToPos = placeAspectToPos;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeBlockPos(placeAspectToPos);
      buf.writeResourceLocation(aspect.aspectKey);
      buf.writeByte(q);
      buf.writeByte(r);
   }

   public static PacketAspectPlaceC2S decode(FriendlyByteBuf buf) {
      var blockPos = buf.readBlockPos();
      Aspect aspect = Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation()));
      byte q = buf.readByte();
      byte r = buf.readByte();
      return new PacketAspectPlaceC2S(blockPos, aspect, q, r);
   }

   private PacketAspectPlaceC2S(BlockPos placeAspectToPos, Aspect aspect, byte q, byte r) {
      this.placeAspectToPos = placeAspectToPos;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;
      var server = serverPlayer.getServer();
      if (server == null) return;
      var world = serverPlayer.level();

      BlockEntity tile = world.getBlockEntity(placeAspectToPos);
      if (tile instanceof IResearchAspectPlaceableBlockEntity researchAspectPlaceable) {
         researchAspectPlaceable.placeAspect(new HexCoord(q, r), aspect, serverPlayer);
      }
   }

   @Override
   public MessageType getType() {
      return messageType;
   }
}
