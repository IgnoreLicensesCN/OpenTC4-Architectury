package thaumcraft.api.listeners.warp.consts;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;

import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateAspectS2C;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
      EntityEldritchGuardian eg = new EntityEldritchGuardian(living.level());
      int i = MathHelper.floor_double(living.posX);
      int j = MathHelper.floor_double(living.posY);
      int k = MathHelper.floor_double(living.posZ);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
         int j1 = j + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
         int k1 = k + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
         if (World.doesBlockHaveSolidTopSurface(living.level(), i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (living.level().checkNoEntityCollision(eg.boundingBox) && living.level().getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !living.level().isAnyLiquid(eg.boundingBox)) {
               eg.setTarget(living);
               eg.setAttackTarget(living);
               living.level().spawnEntityInWorld(eg);
               break;
            }
         }
      }

   }

   public static void suddenlySpiders(LivingEntity living, int warp, boolean real) {
      int spawns = Math.min(50, warp);

      for(int a = 0; a < spawns; ++a) {
         EntityMindSpider spider = new EntityMindSpider(living.level());
         int i = MathHelper.floor_double(living.posX);
         int j = MathHelper.floor_double(living.posY);
         int k = MathHelper.floor_double(living.posZ);
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
            int j1 = j + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
            int k1 = k + MathHelper.getRandomIntegerInRange(living.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(living.getRandom(), -1, 1);
            if (World.doesBlockHaveSolidTopSurface(living.level(), i1, j1 - 1, k1)) {
               spider.setPosition(i1, j1, k1);
               if (living.level().checkNoEntityCollision(spider.boundingBox) && living.level().getCollidingBoundingBoxes(spider, spider.boundingBox).isEmpty() && !living.level().isAnyLiquid(spider.boundingBox)) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.setTarget(living);
            spider.setAttackTarget(living);
            if (!real) {
               spider.setViewer(living.getGameProfile().getName());
               spider.setHarmless(true);
            }

            living.level().spawnEntityInWorld(spider);
         }
      }

      living.sendSystemMessage(Component.translatable("warp.text.7").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC), true);
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
