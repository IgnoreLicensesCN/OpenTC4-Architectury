package thaumcraft.api.listeners.warp.consts;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;

import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;
import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBaubleAndArmor;
import static thaumcraft.api.listeners.warp.WarpEventManager.getFinalWarp;
import static thaumcraft.api.listeners.warp.WarpEventManager.tryTriggerRandomWarpEvent;

public class WarpEvents {

   public static void checkWarpEvent(ServerPlayer player) {
      tryTriggerRandomWarpEvent(player);

   }

   public static void spawnMist(ServerPlayer player, int warp, int guardian) {
      new PacketMiscEventS2C((short)1).sendTo(player);
      if (guardian > 0) {
         guardian = Math.min(8, guardian);

         for(int a = 0; a < guardian; ++a) {
            spawnGuardian(player);
         }
      }

      player.displayClientMessage(Component.literal("§5§o" + Component.translatable("warp.text.6")),true);
   }

   public static void grantResearchAspect(ServerPlayer player, int times) {
      int amt = 1 + player.getRandom().nextInt(times);
      var info = ResearchAndScannedInfo.getFromPlayer(player);
      var aspectTypes = new ArrayList<>(Aspects.getPrimalAspects());
      for(int a = 0; a < amt; ++a) {
         var aspect = aspectTypes.get(player.getRandom().nextInt(aspectTypes.size()));
         info.addResearchAspectAndSyncToPlayer(aspect,1,player);
      }
   }

   public static void spawnGuardian(Player player) {
      EntityEldritchGuardian eg = new EntityEldritchGuardian(player.level());
      int i = MathHelper.floor_double(player.posX);
      int j = MathHelper.floor_double(player.posY);
      int k = MathHelper.floor_double(player.posZ);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         int j1 = j + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         int k1 = k + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         if (World.doesBlockHaveSolidTopSurface(player.level(), i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (player.level().checkNoEntityCollision(eg.boundingBox) && player.level().getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !player.level().isAnyLiquid(eg.boundingBox)) {
               eg.setTarget(player);
               eg.setAttackTarget(player);
               player.level().spawnEntityInWorld(eg);
               break;
            }
         }
      }

   }

   public static void suddenlySpiders(Player player, int warp, boolean real) {
      int spawns = Math.min(50, warp);

      for(int a = 0; a < spawns; ++a) {
         EntityMindSpider spider = new EntityMindSpider(player.level());
         int i = MathHelper.floor_double(player.posX);
         int j = MathHelper.floor_double(player.posY);
         int k = MathHelper.floor_double(player.posZ);
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            int j1 = j + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            int k1 = k + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            if (World.doesBlockHaveSolidTopSurface(player.level(), i1, j1 - 1, k1)) {
               spider.setPosition(i1, j1, k1);
               if (player.level().checkNoEntityCollision(spider.boundingBox) && player.level().getCollidingBoundingBoxes(spider, spider.boundingBox).isEmpty() && !player.level().isAnyLiquid(spider.boundingBox)) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.setTarget(player);
            spider.setAttackTarget(player);
            if (!real) {
               spider.setViewer(player.getGameProfile().getName());
               spider.setHarmless(true);
            }

            player.level().spawnEntityInWorld(spider);
         }
      }

      player.displayClientMessage(Component.literal("§5§o" + Component.translatable("warp.text.7")));
   }

   public static int getWarpFromGear(Player player) {
      AtomicInteger w = new AtomicInteger(
              getFinalWarp(player.getMainHandItem(), player)
                      + getFinalWarp(player.getOffhandItem(), player)//time changes now
      );
      forEachBaubleAndArmor(player,armorInSlot -> w.addAndGet(getFinalWarp(armorInSlot, player)));
      return w.addAndGet(getFinalWarp(player.getOffhandItem(), player));
   }
}
