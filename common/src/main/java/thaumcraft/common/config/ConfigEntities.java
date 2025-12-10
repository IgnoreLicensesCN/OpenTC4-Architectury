package thaumcraft.common.config;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.EntityRegistry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.EntityFollowingItem;
import thaumcraft.common.entities.EntityItemGrate;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.ItemSpawnerEgg;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EntityGolemBobber;
import thaumcraft.common.entities.golems.EntityTravelingTrunk;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.EntityCultistKnight;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityFireBat;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityTaintChicken;
import thaumcraft.common.entities.monster.EntityTaintCow;
import thaumcraft.common.entities.monster.EntityTaintCreeper;
import thaumcraft.common.entities.monster.EntityTaintPig;
import thaumcraft.common.entities.monster.EntityTaintSheep;
import thaumcraft.common.entities.monster.EntityTaintSpider;
import thaumcraft.common.entities.monster.EntityTaintSpore;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;
import thaumcraft.common.entities.monster.EntityTaintSwarm;
import thaumcraft.common.entities.monster.EntityTaintVillager;
import thaumcraft.common.entities.monster.EntityTaintacle;
import thaumcraft.common.entities.monster.EntityTaintacleSmall;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.entities.monster.EntityWatcher;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;
import thaumcraft.common.entities.monster.boss.EntityCultistPortal;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.projectile.EntityAlumentum;
import thaumcraft.common.entities.projectile.EntityBottleTaint;
import thaumcraft.common.entities.projectile.EntityDart;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.entities.projectile.EntityEmber;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;
import thaumcraft.common.entities.projectile.EntityFrostShard;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.entities.projectile.EntityPechBlast;
import thaumcraft.common.entities.projectile.EntityPrimalArrow;
import thaumcraft.common.entities.projectile.EntityPrimalOrb;
import thaumcraft.common.entities.projectile.EntityShockOrb;

public class ConfigEntities {
   public static int entWizardId = 190;
   public static int entBankerId = 191;
   public static HashMap<Class<?>,Integer> championModWhitelist = new HashMap<>();

   public static void init() {
      int id = 0;
      EntityRegistry.registerModEntity(EntitySpecialItem.class, "SpecialItem", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityPermanentItem.class, "PermanentItem", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityFollowingItem.class, "FollowItem", id++, Thaumcraft.instance, 64, 20, false);
      EntityRegistry.registerModEntity(EntityAspectOrb.class, "AspectOrb", id++, Thaumcraft.instance, 120, 20, true);
      EntityRegistry.registerModEntity(EntityFallingTaint.class, "FallingTaint", id++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityAlumentum.class, "Alumentum", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityPrimalOrb.class, "PrimalOrb", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityFrostShard.class, "FrostShard", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityDart.class, "Dart", id++, Thaumcraft.instance, 64, 20, false);
      EntityRegistry.registerModEntity(EntityPrimalArrow.class, "PrimalArrow", id++, Thaumcraft.instance, 64, 20, false);
      EntityRegistry.registerModEntity(EntityPechBlast.class, "PechBlast", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityEldritchOrb.class, "EldritchOrb", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityBottleTaint.class, "BottleTaint", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGolemOrb.class, "GolemOrb", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityShockOrb.class, "ShockOrb", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityExplosiveOrb.class, "ExplosiveOrb", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityEmber.class, "Ember", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGolemBase.class, "Golem", id++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityTravelingTrunk.class, "TravelingTrunk", id++, Thaumcraft.instance, 64, 3, true);
      EntityRegistry.registerModEntity(EntityBrainyZombie.class, "BrainyZombie", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("BrainyZombie", 16761087, 32768);
      EntityRegistry.registerModEntity(EntityGiantBrainyZombie.class, "GiantBrainyZombie", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("GiantBrainyZombie", 16761087, 16384);
      EntityRegistry.registerModEntity(EntityWisp.class, "Wisp", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("Wisp", 16761087, 16777215);
      EntityRegistry.registerModEntity(EntityFireBat.class, "Firebat", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("Firebat", 16761087, 12582912);
      EntityRegistry.registerModEntity(EntityPech.class, "Pech", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("Pech", 16761087, 4194368);
      EntityRegistry.registerModEntity(EntityMindSpider.class, "MindSpider", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("MindSpider", 11184810, 4210752);
      EntityRegistry.registerModEntity(EntityEldritchGuardian.class, "EldritchGuardian", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("EldritchGuardian", 2236962, 4210752);
      EntityRegistry.registerModEntity(EntityEldritchWarden.class, "EldritchWarden", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("EldritchWarden", 5579298, 4210752);
      EntityRegistry.registerModEntity(EntityCultistKnight.class, "CultistKnight", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("CultistKnight", 16732245, 128);
      EntityRegistry.registerModEntity(EntityCultistCleric.class, "CultistCleric", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("CultistCleric", 16732245, 8388608);
      EntityRegistry.registerModEntity(EntityCultistLeader.class, "CultistLeader", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("CultistLeader", 16732245, 5263440);
      EntityRegistry.registerModEntity(EntityCultistPortal.class, "CultistPortal", id++, Thaumcraft.instance, 64, 20, false);
      ItemSpawnerEgg.addMapping("CultistPortal", 16732245, 16732415);
      EntityRegistry.registerModEntity(EntityEldritchGolem.class, "EldritchGolem", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("EldritchGolem", 5592405, 4210752);
      EntityRegistry.registerModEntity(EntityEldritchCrab.class, "EldritchCrab", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("EldritchCrab", 5592405, 5570560);
      EntityRegistry.registerModEntity(EntityInhabitedZombie.class, "InhabitedZombie", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("InhabitedZombie", 5601109, 5570560);
      EntityRegistry.registerModEntity(EntityThaumicSlime.class, "ThaumSlime", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("ThaumSlime", 16761087, 16744703);
      EntityRegistry.registerModEntity(EntityTaintSpider.class, "TaintSpider", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintSpider", 16761087, 4210752);
      EntityRegistry.registerModEntity(EntityTaintacle.class, "Taintacle", id++, Thaumcraft.instance, 64, 3, false);
      ItemSpawnerEgg.addMapping("Taintacle", 16761087, 8388736);
      EntityRegistry.registerModEntity(EntityTaintacleSmall.class, "TaintacleTiny", id++, Thaumcraft.instance, 64, 3, false);
      ItemSpawnerEgg.addMapping("TaintacleTiny", 16761087, 8388752);
      EntityRegistry.registerModEntity(EntityTaintSpore.class, "TaintSpore", id++, Thaumcraft.instance, 64, 20, false);
      ItemSpawnerEgg.addMapping("TaintSpore", 16761087, 8388720);
      EntityRegistry.registerModEntity(EntityTaintSporeSwarmer.class, "TaintSwarmer", id++, Thaumcraft.instance, 64, 20, false);
      ItemSpawnerEgg.addMapping("TaintSwarmer", 16761087, 8388704);
      EntityRegistry.registerModEntity(EntityTaintSwarm.class, "TaintSwarm", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintSwarm", 16761087, 8388688);
      EntityRegistry.registerModEntity(EntityTaintChicken.class, "TaintedChicken", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedChicken", 16761087, 12632256);
      EntityRegistry.registerModEntity(EntityTaintCow.class, "TaintedCow", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedCow", 16761087, 8272443);
      EntityRegistry.registerModEntity(EntityTaintCreeper.class, "TaintedCreeper", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedCreeper", 16761087, 65280);
      EntityRegistry.registerModEntity(EntityTaintPig.class, "TaintedPig", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedPig", 16761087, 15702511);
      EntityRegistry.registerModEntity(EntityTaintSheep.class, "TaintedSheep", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedSheep", 16761087, 8421504);
      EntityRegistry.registerModEntity(EntityTaintVillager.class, "TaintedVillager", id++, Thaumcraft.instance, 64, 3, true);
      ItemSpawnerEgg.addMapping("TaintedVillager", 16761087, 65535);
      EntityRegistry.registerModEntity(EntityTaintacleGiant.class, "TaintacleGiant", id++, Thaumcraft.instance, 64, 3, false);
      ItemSpawnerEgg.addMapping("TaintacleGiant", 16761087, 8421504);
      EntityRegistry.registerModEntity(EntityItemGrate.class, "SpecialItemGrate", id++, Thaumcraft.instance, 64, 20, true);
      EntityRegistry.registerModEntity(EntityGolemBobber.class, "GolemBobber", id++, Thaumcraft.instance, 64, 64, false);
   }

   public static void initEntitySpawns() {
      List<BiomeGenBase> biomes = WorldChunkManager.allowedBiomes;
      BiomeGenBase[] allBiomes = biomes.toArray(new BiomeGenBase[]{null});
      if (Config.spawnAngryZombie) {
         for(BiomeGenBase bgb : biomes) {
            if (bgb.getSpawnableList(EnumCreatureType.monster) != null & !bgb.getSpawnableList(EnumCreatureType.monster).isEmpty()) {
               EntityRegistry.addSpawn(EntityBrainyZombie.class, 10, 1, 1, EnumCreatureType.monster, bgb);
            }
         }
      }

      if (Config.spawnPech) {
         for(BiomeGenBase bgb : BiomeDictionary.getBiomesForType(Type.MAGICAL)) {
            if (bgb.getSpawnableList(EnumCreatureType.monster) != null & !bgb.getSpawnableList(EnumCreatureType.monster).isEmpty()) {
               EntityRegistry.addSpawn(EntityPech.class, 10, 1, 1, EnumCreatureType.monster, bgb);
            }
         }
      }

      if (Config.spawnFireBat) {
         EntityRegistry.addSpawn(EntityFireBat.class, 10, 1, 2, EnumCreatureType.monster, BiomeDictionary.getBiomesForType(Type.NETHER));
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(System.currentTimeMillis());
         if (calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) == 31) {
            EntityRegistry.addSpawn(EntityFireBat.class, 5, 1, 2, EnumCreatureType.monster, biomes.toArray(allBiomes));
         }
      }

      if (Config.spawnWisp) {
         EntityRegistry.addSpawn(EntityWisp.class, 5, 1, 1, EnumCreatureType.monster, BiomeDictionary.getBiomesForType(Type.NETHER));
      }

      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Zombie:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Spider:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Blaze:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Enderman:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Skeleton:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Witch:1");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.EldritchCrab:0");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.Taintacle:2");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.Wisp:1");
      FMLInterModComms.sendMessage("Thaumcraft", "championWhiteList", "Thaumcraft.InhabitedZombie:3");
      championModWhitelist.put(EntityCultist.class, 1);
      championModWhitelist.put(EntityWatcher.class, 2);
      championModWhitelist.put(EntityPech.class, 1);
      championModWhitelist.put(EntityThaumcraftBoss.class, 200);
   }
}
