package thaumcraft.api.listeners.warp.consts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;

import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.entities.monster.MindSpiderEntity;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateAspectS2C;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ENTITY_TEST;
import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;
import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBaubleAndArmor;
import static thaumcraft.api.listeners.warp.WarpEventManager.*;

public class WarpEvents {

   public static void checkWarpEvent(LivingEntity living) {

      if (!Config.wuss && living.tickCount > 0 && living.tickCount % getWarpEventDelayForPlayer(living) == 0) {
         tryTriggerRandomWarpEvent(living);
      }

   }

   public static void spawnMist(LivingEntity living, int warp, int guardian) {
      if (living instanceof ServerPlayer serverPlayer) {
         new PacketMiscEventS2C((short)1).sendTo(serverPlayer);
      }
      if (guardian > 0) {
         guardian = Math.min(8, guardian);

         for(int a = 0; a < guardian; ++a) {
            spawnGuardian(living);
         }
      }

      living.sendSystemMessage(Component.translatable("warp.text.6").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
   }

   public static void grantResearchAspect(LivingEntity living, int times) {
      int amt = 1 + living.getRandom().nextInt(times);
      var info = ResearchAndScannedInfo.getFromLiving(living);
      if (info == null) {
         return;
      }
      var serverPlayer = living instanceof ServerPlayer sp ? sp : null;
      var aspectTypes = new ArrayList<>(Aspects.getPrimalAspects());
      for(int a = 0; a < amt; ++a) {
         var aspect = aspectTypes.get(living.getRandom().nextInt(aspectTypes.size()));
         info.addResearchAspect(aspect, 1);
         if (serverPlayer != null) {
            new PacketUpdateAspectS2C(aspect, 1, info.getResearchAspect(aspect)).sendTo(serverPlayer);
         }
      }
   }

   public static void spawnGuardian(LivingEntity living) {
      var level = living.level();
      var random = living.getRandom();
      var eg = new EntityEldritchGuardian(level);
      int i = living.getBlockX();
      int j = living.getBlockY();
      int k = living.getBlockZ();
      var livingAABB = living.getBoundingBox();

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.getRandomIntegerInRange(random, 7, 24) * MathHelper.getRandomIntegerInRange(random, -1, 1);
         int j1 = j + MathHelper.getRandomIntegerInRange(random, 7, 24) * MathHelper.getRandomIntegerInRange(random, -1, 1);
         int k1 = k + MathHelper.getRandomIntegerInRange(random, 7, 24) * MathHelper.getRandomIntegerInRange(random, -1, 1);
         var pos = new BlockPos(i1, j1 - 1, k1);
         if (level.getBlockState(pos).isFaceSturdy(level,pos, Direction.UP)) {
            eg.setPos(i1, j1, k1);
            if (level.getEntities(ENTITY_TEST,livingAABB,e -> true).isEmpty()
                    && level.noCollision(living,livingAABB)
                    && !level.containsAnyLiquid(livingAABB)) {
               eg.setTarget(living);
//               eg.setAttackTarget(living);
               level.addFreshEntity(eg);
               break;
            }
         }
      }

   }

   public static void suddenlySpiders(LivingEntity living, int warp, boolean real) {
      int spawns = Math.min(50, warp);
      var random = living.getRandom();
      var level = living.level();
      int i = living.getBlockX();
      int j = living.getBlockY();
      int k = living.getBlockZ();

      for(int a = 0; a < spawns; ++a) {
         var spider = new MindSpiderEntity(living.level());
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.getRandomIntegerInRange(random, 7, 24)
                    * MathHelper.getRandomIntegerInRange(random, -1, 1);
            int j1 = j + MathHelper.getRandomIntegerInRange(random, 7, 24)
                    * MathHelper.getRandomIntegerInRange(random, -1, 1);
            int k1 = k + MathHelper.getRandomIntegerInRange(random, 7, 24)
                    * MathHelper.getRandomIntegerInRange(random, -1, 1);
            var pos = new BlockPos(i1, j1 - 1, k1);
            if (level.getBlockState(pos).isFaceSturdy(level,pos, Direction.UP)) {
               spider.setPos(i1, j1, k1);
               var spiderAABB = spider.getBoundingBox();
               if (level.getEntities(ENTITY_TEST,spiderAABB,e -> true).isEmpty()
                       && level.noCollision(spider,spiderAABB)
                       && !level.containsAnyLiquid(spiderAABB)
               ) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.setTarget(living);
//            spider.setAttackTarget(living);
            if (!real) {
               spider.setVisibleTo(living);
               spider.setHarmless(true);
            }else {
               spider.setHarmless(false);
            }

            level.addFreshEntity(spider);
         }
      }

      living.sendSystemMessage(Component.translatable("warp.text.7").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
   }

   public static int getWarpFromGear(LivingEntity living) {
      AtomicInteger w = new AtomicInteger(
              getFinalWarp(living.getMainHandItem(), living)
                      + getFinalWarp(living.getOffhandItem(), living)//time changes now
      );
      forEachBaubleAndArmor(living,armorInSlot -> w.addAndGet(getFinalWarp(armorInSlot, living)));
      return w.addAndGet(getFinalWarp(living.getOffhandItem(), living));
   }
}
