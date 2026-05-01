package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.tiles.abstracts.IResearchAspectProviderBlockEntity;

public class PacketAspectCombinationC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_combination";
   public static MessageType messageType;

   private final BlockPos tablePos;
   private final Aspect aspect1;
   private final Aspect aspect2;
   private final boolean canUseProviderAspect1;
   private final boolean canUseProviderAspect2;

   @Override
   public MessageType getType() {
      return messageType;
   }

   public PacketAspectCombinationC2S(BlockPos pos,
                                     Aspect aspect1, Aspect aspect2,
                                     boolean canUseProviderAspect1, boolean canUseProviderAspect2
   ) {
      this.tablePos = pos;
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
      this.canUseProviderAspect1 = canUseProviderAspect1;
      this.canUseProviderAspect2 = canUseProviderAspect2;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeBlockPos(tablePos);
      buf.writeResourceLocation(aspect1.getAspectKey());
      buf.writeResourceLocation(aspect2.getAspectKey());
      buf.writeBoolean(canUseProviderAspect1);
      buf.writeBoolean(canUseProviderAspect2);
   }

   public static PacketAspectCombinationC2S decode(FriendlyByteBuf buf) {
      var tablePos = buf.readBlockPos();
      Aspect aspect1 = Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation()));
      Aspect aspect2 = Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation()));
      boolean ab1 = buf.readBoolean();
      boolean ab2 = buf.readBoolean();
      return new PacketAspectCombinationC2S(tablePos,aspect1,aspect2,ab1,ab2);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;
      var server = serverPlayer.getServer();
      if (server == null) return;
      Level world = serverPlayer.level();
      BlockEntity be = world.getBlockEntity(tablePos);
      if (!(be instanceof IResearchAspectProviderBlockEntity aspectProviderBE)) return;
      if (!sanityCheckAspectCombination0(this, serverPlayer, aspectProviderBE)) return;
      Aspect combinationResult = ResearchManager.getCombinationResult(aspect1, aspect2);

      costAspect(aspectProviderBE,serverPlayer,aspect1, canUseProviderAspect1);
      costAspect(aspectProviderBE,serverPlayer,aspect2, canUseProviderAspect2);

      // 完成组合，添加新的 aspect 知识
      if (!combinationResult.isEmpty()) {
         ScanManager.checkAndSyncAspectKnowledge(serverPlayer, combinationResult, 1);
      }

      // 保存玩家知识
      ResearchManager.scheduleSave(serverPlayer);
   }

   private static boolean sanityCheckAspectCombination0(PacketAspectCombinationC2S packet,
                                                        ServerPlayer player,
                                                        IResearchAspectProviderBlockEntity table) {
      return packet.lhs() != null &&
              packet.rhs() != null &&
              hasAspect(table, player, packet.lhs()) &&
              hasAspect(table, player, packet.rhs());
   }

   public Aspect lhs(){
      return aspect1;
   };
   public Aspect rhs(){
      return aspect2;
   };

   private static boolean hasAspect(IResearchAspectProviderBlockEntity aspectProviderBE, ServerPlayer player, Aspect aspect) {
      return playerHasAspect(player, aspect, 0)
              || aspectProviderBE.getAspectOwning(aspect) > 0
              ;
   }

   public static boolean playerHasAspect(ServerPlayer player, Aspect aspect, int threshold) {
      return Thaumcraft.playerKnowledge.getAspectPoolFor(player, aspect) >= threshold;
   }

   private void costAspect(IResearchAspectProviderBlockEntity aspectProviderBE,ServerPlayer player,Aspect aspect,boolean canUseProviderAspect) {
      if (Thaumcraft.playerKnowledge.getAspectPoolFor(player, aspect) <= 0 && canUseProviderAspect) {
         aspectProviderBE.costAspect(aspect,1);
      } else {
         Thaumcraft.playerKnowledge.addAspectPool(player, aspect, (short) -1);
         new PacketAspectPoolS2C(aspect, (short) 0,
                 Thaumcraft.playerKnowledge.getAspectPoolFor(player, aspect)).sendTo(player);
      }
   }

}



