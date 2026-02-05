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
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.lib.research.ScanManager;

import static tc4tweak.PacketCheck.hasAspect;

public class PacketAspectCombinationC2S extends BaseC2SMessage {
   public static final String ID = Thaumcraft.MOD_ID + ":aspect_combination";
   public static MessageType messageType;

   private ResourceKey<Level> dim;
   private String playerName;
   private int x, y, z;
   private Aspect aspect1;
   private Aspect aspect2;
   private boolean ab1;
   private boolean ab2;

   public PacketAspectCombinationC2S() {}

   @Override
   public MessageType getType() {
      return messageType;
   }

   public PacketAspectCombinationC2S(ServerPlayer player, int x, int y, int z,
                                     Aspect aspect1, Aspect aspect2,
                                     boolean ab1, boolean ab2) {
      this.dim = player.level().dimension();
      this.playerName = player.getGameProfile().getName();
      this.x = x;
      this.y = y;
      this.z = z;
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
      this.ab1 = ab1;
      this.ab2 = ab2;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      buf.writeResourceKey(dim);
      buf.writeUtf(playerName);
      buf.writeInt(x);
      buf.writeInt(y);
      buf.writeInt(z);
      buf.writeUtf(aspect1.getAspectKey());
      buf.writeUtf(aspect2.getAspectKey());
      buf.writeBoolean(ab1);
      buf.writeBoolean(ab2);
   }

   public static PacketAspectCombinationC2S decode(FriendlyByteBuf buf) {
      ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
      String playerId = buf.readUtf();
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      Aspect aspect1 = Aspect.getAspect(buf.readUtf());
      Aspect aspect2 = Aspect.getAspect(buf.readUtf());
      boolean ab1 = buf.readBoolean();
      boolean ab2 = buf.readBoolean();
      PacketAspectCombinationC2S pkt = new PacketAspectCombinationC2S();
      pkt.dim = dim;
      pkt.playerName = playerId;
      pkt.x = x;
      pkt.y = y;
      pkt.z = z;
      pkt.aspect1 = aspect1;
      pkt.aspect2 = aspect2;
      pkt.ab1 = ab1;
      pkt.ab2 = ab2;
      return pkt;
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      Player player = context.getPlayer();
      if (!(player instanceof ServerPlayer serverPlayer)) return;

      // 获取世界
      Level world = serverPlayer.getServer().getLevel(dim);
      if (world == null) return;

      // 获取研究台 TileEntity
      BlockEntity te = world.getBlockEntity(new BlockPos(x, y, z));
      if (!(te instanceof TileResearchTable table)) return;

      // Sanity check：研究台和玩家都有该两个原始 aspect
      if (!sanityCheckAspectCombination0(this, serverPlayer, table)) return;

      // 获取组合结果
      Aspect combo = ResearchManager.getCombinationResult(aspect1, aspect2);

      String playerName = serverPlayer.getGameProfile().getName();

      // 处理第一个 aspect
      if (Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect1) <= 0 && ab1) {
         table.bonusAspects.reduceAndRemoveIfNegative(aspect1, 1);
         world.sendBlockUpdated(new BlockPos(x, y, z), te.getBlockState(), te.getBlockState(), 3);
         te.setChanged();
      } else {
         Thaumcraft.playerKnowledge.addAspectPool(playerName, aspect1, (short) -1);
         new PacketAspectPoolS2C(aspect1.getAspectKey(), (short) 0,
                 Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect1)).sendTo(serverPlayer);
      }

      // 处理第二个 aspect
      if (Thaumcraft.playerKnowledge.getAspectPoolFor(playerName, aspect2) <= 0 && ab2) {
         table.bonusAspects.reduceAndRemoveIfNegative(aspect2, 1);
         world.sendBlockUpdated(new BlockPos(x, y, z), te.getBlockState(), te.getBlockState(), 3);
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
                                                        TileResearchTable table) {
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

}



