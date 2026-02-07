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
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

public class PacketAspectCombinationC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_combination";
   public static MessageType messageType;

   private ResourceKey<Level> dim;
   private String playerName;
   private BlockPos tablePos;
   private Aspect aspect1;
   private Aspect aspect2;
   private boolean ab1;
   private boolean ab2;

   public PacketAspectCombinationC2S() {}

   @Override
   public MessageType getType() {
      return messageType;
   }

   public PacketAspectCombinationC2S(ServerPlayer player, BlockPos pos,
                                     Aspect aspect1, Aspect aspect2,
                                     boolean ab1, boolean ab2) {
      this.dim = player.level().dimension();
      this.playerName = player.getGameProfile().getName();
      this.tablePos = pos;
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
      this.ab1 = ab1;
      this.ab2 = ab2;
   }
   public PacketAspectCombinationC2S(ResourceKey<Level> dim,String playerName, BlockPos pos,
                                     Aspect aspect1, Aspect aspect2,
                                     boolean ab1, boolean ab2) {
      this.dim = dim;
      this.playerName = playerName;
      this.tablePos = pos;
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
      this.ab1 = ab1;
      this.ab2 = ab2;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeResourceKey(dim);
      buf.writeUtf(playerName);
      buf.writeBlockPos(tablePos);
      buf.writeResourceLocation(aspect1.getAspectKey());
      buf.writeResourceLocation(aspect2.getAspectKey());
      buf.writeBoolean(ab1);
      buf.writeBoolean(ab2);
   }

   public static PacketAspectCombinationC2S decode(FriendlyByteBuf buf) {
      ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
      String playerId = buf.readUtf();
      var tablePos = buf.readBlockPos();
      Aspect aspect1 = Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation()));
      Aspect aspect2 = Aspect.getAspect(AspectResourceLocation.of(buf.readResourceLocation()));
      boolean ab1 = buf.readBoolean();
      boolean ab2 = buf.readBoolean();
      return new PacketAspectCombinationC2S(dim,playerId,tablePos,aspect1,aspect2,ab1,ab2);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;

      // 获取世界
      Level world = serverPlayer.getServer().getLevel(dim);
      if (world == null) return;

      // 获取研究台 TileEntity
      BlockEntity te = world.getBlockEntity(tablePos);
      if (!(te instanceof ResearchTableBlockEntity table)) return;

      // Sanity check：研究台和玩家都有该两个原始 aspect
      if (!sanityCheckAspectCombination0(this, serverPlayer, table)) return;

      // 获取组合结果
      Aspect combo = ResearchManager.getCombinationResult(aspect1, aspect2);

      String playerName = serverPlayer.getGameProfile().getName();

      // 处理第一个 aspect
      if (Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect1) <= 0 && ab1) {
         table.bonusAspects.reduceAndRemoveIfNotPositive(aspect1, 1);
         world.sendBlockUpdated(tablePos, te.getBlockState(), te.getBlockState(), 3);
         te.setChanged();
      } else {
         Thaumcraft.playerKnowledge.addAspectPool(playerName, aspect1, (short) -1);
         new PacketAspectPoolS2C(aspect1.getAspectKey(), (short) 0,
                 Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect1)).sendTo(serverPlayer);
      }

      // 处理第二个 aspect
      if (Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect2) <= 0 && ab2) {
         table.bonusAspects.reduceAndRemoveIfNotPositive(aspect2, 1);
         world.sendBlockUpdated(tablePos, te.getBlockState(), te.getBlockState(), 3);
         te.setChanged();
      } else {
         Thaumcraft.playerKnowledge.addAspectPool(playerName, aspect2, (short) -1);
         new PacketAspectPoolS2C(aspect2.getAspectKey(), (short) 0,
                 Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect2)).sendTo(serverPlayer);
      }

      // 完成组合，添加新的 aspect 知识
      if (combo != null) {
         ScanManager.checkAndSyncAspectKnowledge(serverPlayer, combo, 1);
      }

      // 保存玩家知识
      ResearchManager.scheduleSave(serverPlayer.getGameProfile().getName());
   }

   /**
    * Sanity check: 研究台有 lhs/rhs aspect，玩家存在
    */
   private static boolean sanityCheckAspectCombination0(PacketAspectCombinationC2S packet,
                                                        ServerPlayer player,
                                                        ResearchTableBlockEntity table) {
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

   private static boolean hasAspect(ResearchTableBlockEntity table, ServerPlayer player, Aspect aspect) {
      return hasAspect(player, aspect, 0) || table.bonusAspects.getAmount(aspect) > 0;
   }
   public static boolean hasAspect(ServerPlayer player, Aspect aspect, int threshold) {
      return Thaumcraft.playerKnowledge.getAspectPoolFor(player.getGameProfile().getName(), aspect) >= threshold;
   }

}



