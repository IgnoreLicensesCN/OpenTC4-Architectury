package thaumcraft.common.config;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration;
import thaumcraft.common.items.ThaumcraftItems;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigAspects {
   private static final int[] ALLMETA = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
   public static String[] dyes = new String[]{"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};

   public static void init() {
      registerItemAspects();
      registerEntityAspects();
   }

   private static void registerEntityAspects() {
      ThaumcraftApi.registerEntityTag("Zombie", (
              new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 2).addAll(Aspects.MAN, 1).addAll(
              Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Giant", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 4).addAll(Aspects.MAN, 3).addAll(
              Aspects.EARTH, 3));
      ThaumcraftApi.registerEntityTag("Skeleton", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 3).addAll(Aspects.MAN, 1).addAll(
              Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Skeleton", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 4).addAll(Aspects.MAN, 1).addAll(
              Aspects.FIRE, 2), new ThaumcraftApi.EntityTagsNBT("SkeletonType", (byte)1));
      ThaumcraftApi.registerEntityTag("Creeper", (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 2).addAll(Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("Creeper", (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 3).addAll(Aspects.FIRE, 3).addAll(
              Aspects.ENERGY, 3), new ThaumcraftApi.EntityTagsNBT("powered", (byte)1));
      ThaumcraftApi.registerEntityTag("EntityHorse", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 4).addAll(Aspects.EARTH, 1).addAll(
              Aspects.AIR, 1));
      ThaumcraftApi.registerEntityTag("Pig", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("XPOrb", (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 5));
      ThaumcraftApi.registerEntityTag("Sheep", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("Cow", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 3).addAll(Aspects.EARTH, 3));
      ThaumcraftApi.registerEntityTag("MushroomCow", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 3).addAll(Aspects.PLANT, 1).addAll(
              Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("SnowMan", (new LinkedHashAspectList<>()).addAll(Aspects.COLD, 3).addAll(Aspects.WATER, 1));
      ThaumcraftApi.registerEntityTag("Ozelot", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 3).addAll(Aspects.ENTROPY, 3));
      ThaumcraftApi.registerEntityTag("Chicken", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.FLIGHT, 2).addAll(
              Aspects.AIR, 1));
      ThaumcraftApi.registerEntityTag("Squid", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.WATER, 2));
      ThaumcraftApi.registerEntityTag("Wolf", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 3).addAll(Aspects.EARTH, 3));
      ThaumcraftApi.registerEntityTag("Bat", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 1).addAll(Aspects.FLIGHT, 1).addAll(
              Aspects.AIR, 1));
      ThaumcraftApi.registerEntityTag("Boat", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 2).addAll(Aspects.WATER, 2));
      ThaumcraftApi.registerEntityTag("Spider", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 3).addAll(Aspects.ENTROPY, 2));
      ThaumcraftApi.registerEntityTag("Slime", (new LinkedHashAspectList<>()).addAll(Aspects.SLIME, 2).addAll(Aspects.WATER, 2));
      ThaumcraftApi.registerEntityTag("Ghast", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 3).addAll(Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("PigZombie", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 4).addAll(Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("Enderman", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 4).addAll(Aspects.TRAVEL, 2).addAll(
              Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("CaveSpider", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.POISON, 2).addAll(
              Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Silverfish", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 1).addAll(Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Blaze", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 4).addAll(Aspects.FIRE, 1));
      ThaumcraftApi.registerEntityTag("LavaSlime", (new LinkedHashAspectList<>()).addAll(Aspects.SLIME, 3).addAll(Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("EnderDragon", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 20).addAll(Aspects.BEAST, 20).addAll(
              Aspects.ENTROPY, 20));
      ThaumcraftApi.registerEntityTag("WitherBoss", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 20).addAll(Aspects.ENTROPY, 20).addAll(
              Aspects.FIRE, 15));
      ThaumcraftApi.registerEntityTag("Witch", (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 3).addAll(Aspects.MAGIC, 2).addAll(
              Aspects.FIRE, 1));
      ThaumcraftApi.registerEntityTag("Villager", (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 3).addAll(Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("VillagerGolem", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 4).addAll(Aspects.EARTH, 3));
      ThaumcraftApi.registerEntityTag("MinecartRideable", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("MinecartChest", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 1).addAll(
              Aspects.VOID, 1));
      ThaumcraftApi.registerEntityTag("MinecartFurnace", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 1).addAll(
              Aspects.FIRE, 1));
      ThaumcraftApi.registerEntityTag("MinecartTNT", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 1).addAll(
              Aspects.FIRE, 1));
      ThaumcraftApi.registerEntityTag("MinecartHopper", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 1).addAll(
              Aspects.EXCHANGE, 1));
      ThaumcraftApi.registerEntityTag("MinecartSpawner", (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 3).addAll(Aspects.AIR, 1).addAll(
              Aspects.MAGIC, 1));
      ThaumcraftApi.registerEntityTag("EnderCrystal", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 3).addAll(Aspects.MAGIC, 3).addAll(
              Aspects.HEAL, 3));
      ThaumcraftApi.registerEntityTag("ItemFrame", (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 3).addAll(Aspects.CLOTH, 1));
      ThaumcraftApi.registerEntityTag("Painting", (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 5).addAll(Aspects.CLOTH, 3));
      ThaumcraftApi.registerEntityTag("Thaumcraft.PrimalOrb", (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 5).addAll(Aspects.ENTROPY, 10).addAll(
              Aspects.MAGIC, 10).addAll(Aspects.ENERGY, 10));
      ThaumcraftApi.registerEntityTag("Thaumcraft.Firebat", (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(Aspects.FLIGHT, 1).addAll(
              Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 2).addAll(Aspects.MAGIC, 2).addAll(
              Aspects.EXCHANGE, 2).addAll(Aspects.GREED, 2), new ThaumcraftApi.EntityTagsNBT("PechType", (byte)0));
      ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 2).addAll(Aspects.MAGIC, 2).addAll(
              Aspects.EXCHANGE, 2).addAll(Aspects.WEAPON, 2), new ThaumcraftApi.EntityTagsNBT("PechType", (byte)1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 2).addAll(Aspects.MAGIC, 4).addAll(
              Aspects.EXCHANGE, 2), new ThaumcraftApi.EntityTagsNBT("PechType", (byte)2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.ThaumSlime", (new LinkedHashAspectList<>()).addAll(Aspects.SLIME, 2).addAll(
              Aspects.MAGIC, 1).addAll(Aspects.WATER, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.BrainyZombie", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 3).addAll(
              Aspects.MAN, 1).addAll(Aspects.MIND, 1).addAll(
              Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.GiantBrainyZombie", (new LinkedHashAspectList<>()).addAll(Aspects.UNDEAD, 4).addAll(
              Aspects.MAN, 2).addAll(Aspects.MIND, 1).addAll(
              Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.Taintacle", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 3).addAll(Aspects.WATER, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintacleTiny", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 1).addAll(
              Aspects.WATER, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSpider", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 1).addAll(
              Aspects.EARTH, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSpore", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSwarmer", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSwarm", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 3).addAll(
              Aspects.AIR, 3));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedPig", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedSheep", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.EARTH, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedCow", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 3).addAll(
              Aspects.EARTH, 3));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedChicken", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.FLIGHT, 2).addAll(Aspects.AIR, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedVillager", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 3).addAll(
              Aspects.AIR, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.TaintedCreeper", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.MindSpider", (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.FIRE, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchGuardian", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 4).addAll(
              Aspects.DEATH, 2).addAll(Aspects.UNDEAD, 4));
      ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchOrb", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 2).addAll(
              Aspects.DEATH, 2));
      ThaumcraftApi.registerEntityTag("Thaumcraft.CultistKnight", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 1).addAll(
              Aspects.MAN, 2).addAll(
              Aspects.ENTROPY, 1));
      ThaumcraftApi.registerEntityTag("Thaumcraft.CultistCleric", (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 1).addAll(
              Aspects.MAN, 2).addAll(
              Aspects.ENTROPY, 1));

      for(Aspect tag : Aspects.ALL_ASPECTS.values()) {
         ThaumcraftApi.registerEntityTag("Thaumcraft.Wisp", (new LinkedHashAspectList<>()).addAll(tag, 2).addAll(Aspects.MAGIC, 1).addAll(
                 Aspects.AIR, 1), new ThaumcraftApi.EntityTagsNBT("Type", tag.getAspectKey()));
      }

      ThaumcraftApi.registerEntityTag("Thaumcraft.Golem", (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 2).addAll(Aspects.EARTH, 2).addAll(
              Aspects.MAGIC, 2));
   }

   private static void registerItemAspects() {
      ThaumcraftApi.registerObjectTag("stone", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2));
      ThaumcraftApi.registerObjectTag("cobblestone", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.ENTROPY, 1));
      ThaumcraftApi.registerObjectTag("logWood", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 4));
      ThaumcraftApi.registerObjectTag("plankWood", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1));
      ThaumcraftApi.registerObjectTag("slabWood", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1));
      ThaumcraftApi.registerObjectTag("stairWood", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1));
      ThaumcraftApi.registerObjectTag("stickWood", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1));
      ThaumcraftApi.registerObjectTag("treeSapling", (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1).addAll(Aspects.PLANT, 2));
      ThaumcraftApi.registerObjectTag("treeLeaves", (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1));

      for(int i = 0; i < 16; ++i) {
         ThaumcraftApi.registerObjectTag(dyes[i], (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 1));
      }

      ThaumcraftApi.registerObjectTag("oreLapis", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.SENSES, 3));
      ThaumcraftApi.registerObjectTag("oreDiamond", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.GREED, 3).addAll(
              Aspects.CRYSTAL, 3));
      ThaumcraftApi.registerObjectTag("gemDiamond", (new LinkedHashAspectList<>()).addAll(Aspects.CRYSTAL, 4).addAll(Aspects.GREED, 4));
      ThaumcraftApi.registerObjectTag("oreRedstone", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.ENERGY, 2).addAll(
              Aspects.MECHANISM, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.lit_redstone_ore), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.ENERGY, 3).addAll(Aspects.MECHANISM, 2));
      ThaumcraftApi.registerObjectTag("oreEmerald", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.GREED, 4).addAll(
              Aspects.CRYSTAL, 3));
      ThaumcraftApi.registerObjectTag("gemEmerald", (new LinkedHashAspectList<>()).addAll(Aspects.CRYSTAL, 4).addAll(Aspects.GREED, 5));
      ThaumcraftApi.registerObjectTag("oreQuartz", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.CRYSTAL, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.quartz), (new LinkedHashAspectList<>()).addAll(Aspects.CRYSTAL, 1).addAll(
              Aspects.ENERGY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.gold_nugget), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
      ThaumcraftApi.registerObjectTag("nuggetIron", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
      ThaumcraftApi.registerObjectTag("oreIron", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.METAL, 3));
      ThaumcraftApi.registerObjectTag("dustIron", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.ENTROPY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.iron_ingot), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 4));
      ThaumcraftApi.registerObjectTag("oreGold", (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(Aspects.METAL, 2).addAll(
              Aspects.GREED, 1));
      ThaumcraftApi.registerObjectTag("dustGold", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.ENTROPY, 1).addAll(
              Aspects.GREED, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.gold_ingot), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(
              Aspects.GREED, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.coal_ore), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.ENERGY, 2).addAll(Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.coal, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.ENERGY, 2).addAll(
              Aspects.FIRE, 2));
      ThaumcraftApi.registerObjectTag("dustRedstone", (new LinkedHashAspectList<>()).addAll(Aspects.ENERGY, 2).addAll(Aspects.MECHANISM, 1));
      ThaumcraftApi.registerObjectTag("dustGlowstone", (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 1).addAll(Aspects.LIGHT, 2));
      ThaumcraftApi.registerObjectTag("glowstone", (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 3).addAll(Aspects.LIGHT, 10));
      if (Config.foundCopperIngot) {
         ThaumcraftApi.registerObjectTag("nuggetCopper", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
         ThaumcraftApi.registerObjectTag("ingotCopper", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.EXCHANGE, 1));
         ThaumcraftApi.registerObjectTag("dustCopper", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.EXCHANGE, 1));
         ThaumcraftApi.registerObjectTag("oreCopper", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.EARTH, 1).addAll(
                 Aspects.EXCHANGE, 1));
         ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 17), (new LinkedHashAspectList<>()).addAll(
                 Aspects.ORDER, 1).addAll(Aspects.METAL, 5).addAll(Aspects.EARTH, 1).addAll(Aspects.EXCHANGE, 2));
      }

      if (Config.foundTinIngot) {
         ThaumcraftApi.registerObjectTag("nuggetTin", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
         ThaumcraftApi.registerObjectTag("ingotTin", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.CRYSTAL, 1));
         ThaumcraftApi.registerObjectTag("dustTin", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.CRYSTAL, 1));
         ThaumcraftApi.registerObjectTag("oreTin", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.CRYSTAL, 1));
         ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 18), (new LinkedHashAspectList<>()).addAll(
                 Aspects.ORDER, 1).addAll(Aspects.METAL, 5).addAll(Aspects.EARTH, 1).addAll(Aspects.CRYSTAL, 2));
      }

      if (Config.foundSilverIngot) {
         ThaumcraftApi.registerObjectTag("nuggetSilver", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
         ThaumcraftApi.registerObjectTag("ingotSilver", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.GREED, 1));
         ThaumcraftApi.registerObjectTag("dustSilver", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.GREED, 1));
         ThaumcraftApi.registerObjectTag("oreSilver", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.GREED, 1));
         ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 19), (new LinkedHashAspectList<>()).addAll(
                 Aspects.ORDER, 1).addAll(Aspects.METAL, 5).addAll(Aspects.EARTH, 1).addAll(Aspects.GREED, 2));
      }

      if (Config.foundLeadIngot) {
         ThaumcraftApi.registerObjectTag("nuggetLead", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
         ThaumcraftApi.registerObjectTag("ingotLead", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.ORDER, 1));
         ThaumcraftApi.registerObjectTag("dustLead", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 2).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.ORDER, 1));
         ThaumcraftApi.registerObjectTag("oreLead", (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(Aspects.ENTROPY, 1).addAll(
                 Aspects.ORDER, 1));
         ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 20), (new LinkedHashAspectList<>()).addAll(
                 Aspects.ORDER, 1).addAll(Aspects.METAL, 5).addAll(Aspects.EARTH, 1).addAll(Aspects.ORDER, 2));
      }

      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.bedrock), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 16).addAll(
              Aspects.ENTROPY, 16).addAll(Aspects.EARTH, 16).addAll(Aspects.DARKNESS, 16));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.dirt, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.dirt, 1, 2), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.farmland, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.HARVEST, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.sand, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.ENTROPY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.grass), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.clay_ball, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 1).addAll(
              Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.end_stone), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.DARKNESS, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.gravel), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.mycelium), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.clay, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 3).addAll(
              Aspects.WATER, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.hardened_clay, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 4).addAll(
              Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stained_hardened_clay, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 3).addAll(Aspects.FIRE, 1).addAll(Aspects.SENSES, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.brick, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.netherbrick, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.soul_sand, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.TRAP, 1).addAll(
              Aspects.SOUL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.netherrack, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.EARTH, 1).addAll(Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.nether_brick), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2).addAll(
              Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.glass, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stained_glass, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.mossy_cobblestone, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.PLANT, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.obsidian, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2).addAll(
              Aspects.FIRE, 2).addAll(Aspects.DARKNESS, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.monster_egg, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2).addAll(
              Aspects.BEAST, 1).addAll(Aspects.TRAP, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stonebrick, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stonebrick, 1, 1), (new AspectList(new ItemStack(Blocks.stonebrick))).reduceAndRemoveIfNotPositive(
              Aspects.EARTH, 1).addAll(Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stonebrick, 1, 2), (new AspectList(new ItemStack(Blocks.stonebrick))).reduceAndRemoveIfNotPositive(
              Aspects.EARTH, 1).addAll(Aspects.ENTROPY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.stonebrick, 1, 3), (new AspectList(new ItemStack(Blocks.stonebrick))).reduceAndRemoveIfNotPositive(
              Aspects.EARTH, 1).addAll(Aspects.ORDER, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.sandstone, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).reduceAndRemoveIfNotPositive(Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.sandstone, 1, 1), (new AspectList(new ItemStack(Blocks.sandstone))).reduceAndRemoveIfNotPositive(
              Aspects.EARTH, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.sandstone, 1, 2), (new AspectList(new ItemStack(Blocks.sandstone))).reduceAndRemoveIfNotPositive(
              Aspects.EARTH, 1).addAll(Aspects.ORDER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.tallgrass, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.AIR, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.double_plant, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.AIR, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.waterlily, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 2).addAll(
              Aspects.WATER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.deadbush, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.ENTROPY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.vine, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.wheat_seeds, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.melon_seeds, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.pumpkin_seeds, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.melon, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.nether_wart), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.red_flower, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.LIFE, 1).addAll(Aspects.SENSES, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.yellow_flower, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.LIFE, 1).addAll(Aspects.SENSES, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.cactus), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 3).addAll(
              Aspects.WATER, 1).addAll(Aspects.ENTROPY, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.brown_mushroom), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.DARKNESS, 1).addAll(Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.red_mushroom), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.DARKNESS, 1).addAll(Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.brown_mushroom_block, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(
              Aspects.DARKNESS, 1).addAll(Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.red_mushroom_block, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(
              Aspects.DARKNESS, 1).addAll(Aspects.FIRE, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.reeds), (new LinkedHashAspectList<>()).addAll(Aspects.PLANT, 1).addAll(
              Aspects.WATER, 1).addAll(Aspects.AIR, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.wheat), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 2).addAll(
              Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.apple), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 2).addAll(
              Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.carrot), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 1).addAll(
              Aspects.HUNGER, 1).addAll(Aspects.SENSES, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.potato), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 1).addAll(
              Aspects.HUNGER, 1).addAll(
              Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.baked_potato), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 1).addAll(
              Aspects.HUNGER, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.poisonous_potato), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 1).addAll(
              Aspects.POISON, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.pumpkin, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CROP, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.melon_block, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.CROP, 2).reduceAndRemoveIfNotPositive(Aspects.HUNGER, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.name_tag, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 2).addAll(
              Aspects.BEAST, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.iron_horse_armor, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.ARMOR, 2).addAll(Aspects.BEAST, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.golden_horse_armor, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.ARMOR, 4).addAll(Aspects.BEAST, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.diamond_horse_armor, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.ARMOR, 6).addAll(Aspects.BEAST, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.fire, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.water, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.lava, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 3).addAll(
              Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.ice, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.COLD, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.packed_ice, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.COLD, 3).addAll(
              Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.snowball, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.COLD, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cookie, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.potionitem), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 1).addAll(
              Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.torch, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.LIGHT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.web, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.TRAP, 2).addAll(
              Aspects.CLOTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.flint, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.TOOL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.string, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 1).addAll(
              Aspects.CLOTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.slime_ball), (new LinkedHashAspectList<>()).addAll(Aspects.SLIME, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.leather, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CLOTH, 2).addAll(
              Aspects.BEAST, 1).addAll(Aspects.ARMOR, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.rotten_flesh, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.MAN, 1).addAll(
              Aspects.FLESH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.feather, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FLIGHT, 2).addAll(
              Aspects.AIR, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.bone, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 2).addAll(
              Aspects.FLESH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.egg, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.SLIME, 1).addAll(
              Aspects.LIFE, 1).addAll(Aspects.BEAST, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.spider_eye, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 2).addAll(
              Aspects.BEAST, 2).addAll(
              Aspects.POISON, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.gunpowder, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 4).addAll(
              Aspects.ENTROPY, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.wool, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CLOTH, 4).addAll(
              Aspects.CRAFT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.fish, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FLESH, 3).addAll(
              Aspects.LIFE, 1).addAll(Aspects.WATER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cooked_fished, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 1).addAll(
              Aspects.FLESH, 4).addAll(Aspects.HUNGER, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.chicken, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FLESH, 3).addAll(
              Aspects.LIFE, 2).addAll(Aspects.BEAST, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cooked_chicken, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 1).addAll(
              Aspects.FLESH, 4).addAll(Aspects.HUNGER, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.porkchop, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FLESH, 3).addAll(
              Aspects.LIFE, 1).addAll(Aspects.BEAST, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cooked_porkchop, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 1).addAll(
              Aspects.FLESH, 3).addAll(Aspects.HUNGER, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.beef, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FLESH, 4).addAll(
              Aspects.LIFE, 2).addAll(Aspects.BEAST, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cooked_beef, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 1).addAll(
              Aspects.FLESH, 4).addAll(Aspects.HUNGER, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.blaze_rod, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 4).addAll(
              Aspects.MAGIC, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.saddle, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 2).addAll(
              Aspects.CLOTH, 3).addAll(
              Aspects.TRAVEL, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.ender_pearl, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 4).addAll(
              Aspects.MAGIC, 2).addAll(Aspects.TRAVEL, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.ghast_tear, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 1).addAll(
              Aspects.UNDEAD, 4).addAll(
              Aspects.SOUL, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.skull, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 4).addAll(
              Aspects.SOUL, 4).addAll(Aspects.UNDEAD, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.skull, 1, 1), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 4).addAll(
              Aspects.SOUL, 4).addAll(Aspects.UNDEAD, 4).addAll(Aspects.POISON, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.skull, 1, 2), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 4).addAll(
              Aspects.SOUL, 4).addAll(Aspects.FLESH, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.skull, 1, 3), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 4).addAll(
              Aspects.SOUL, 4).addAll(Aspects.MAN, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.skull, 1, 4), (new LinkedHashAspectList<>()).addAll(Aspects.DEATH, 4).addAll(
              Aspects.SOUL, 4).addAll(Aspects.ENTROPY, 2).addAll(Aspects.FIRE, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_11), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_13), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.WATER, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_cat), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.BEAST, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_chirp), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.EARTH, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_far), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.ELDRITCH, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_mall), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.MAN, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_mellohi), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.CRAFT, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_stal), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.DARKNESS, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_strad), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.MECHANISM, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_ward), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.MAGIC, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_blocks), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.TOOL, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.record_wait), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.AIR, 4).addAll(Aspects.TRAP, 4).addAll(Aspects.GREED, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.nether_star), (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 8).addAll(
              Aspects.MAGIC, 8).addAll(Aspects.ORDER, 8).addAll(Aspects.LIGHT, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.chainmail_helmet, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.chainmail_chestplate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 12));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.chainmail_leggings, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 11));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.chainmail_boots, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 7));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.paper), (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.book), (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 3));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.enchanted_book), new AspectList(new ItemStack(Items.book)));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.bookshelf), (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.dragon_egg), (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 8).addAll(
              Aspects.BEAST, 8).addAll(
              Aspects.MAGIC, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.portal, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 4).addAll(
              Aspects.TRAVEL, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.end_portal, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 4).addAll(
              Aspects.TRAVEL, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.end_portal_frame, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.ELDRITCH, 4).addAll(Aspects.MECHANISM, 4).addAll(Aspects.TRAVEL, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.mob_spawner, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.BEAST, 4).addAll(
              Aspects.TRAVEL, 4).addAll(Aspects.UNDEAD, 4).addAll(Aspects.MAGIC, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.ender_eye), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.arrow), (new LinkedHashAspectList<>()).addAll(Aspects.WEAPON, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.glass_bottle), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.chest, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.golden_apple, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 2).addAll(
              Aspects.HEAL, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.golden_apple, 1, 1), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 4).addAll(
              Aspects.HEAL, 8));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.bowl), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.mushroom_stew), (new LinkedHashAspectList<>()).addAll(Aspects.HUNGER, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.minecart), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 2).addAll(
              Aspects.TRAVEL, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.iron_door), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 2).addAll(
              Aspects.MOTION, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.wooden_door), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 1).addAll(
              Aspects.MOTION, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.boat), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 4).addAll(
              Aspects.TRAVEL, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.flint_and_steel, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.FIRE, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.fishing_rod, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.WATER, 1).addAll(Aspects.TOOL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.bucket), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 8).addAll(
              Aspects.VOID, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.water_bucket), (new AspectList(new ItemStack(Items.bucket))).addAll(
              Aspects.WATER, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.lava_bucket), (new AspectList(new ItemStack(Items.bucket))).addAll(
              Aspects.FIRE, 4).addAll(Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.milk_bucket), (new AspectList(new ItemStack(Items.bucket))).addAll(
              Aspects.HUNGER, 2).addAll(
              Aspects.HEAL, 2).addAll(Aspects.WATER, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.cake, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.WATER, 4).addAll(
              Aspects.HUNGER, 4).addAll(
              Aspects.LIFE, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.brewing_stand), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 2).addAll(
              Aspects.WATER, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.stone_button), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.rail, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1).addAll(
              Aspects.TRAVEL, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.detector_rail, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.SENSES, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.golden_rail, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(
              Aspects.ENERGY, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.activator_rail, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.fence_gate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(
              Aspects.TRAVEL, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.wooden_pressure_plate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.SENSES, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.stone_pressure_plate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.SENSES, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.SENSES, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.SENSES, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.lever, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.redstone_torch, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.piston, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 2).addAll(
              Aspects.MOTION, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.sticky_piston, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 2).addAll(
              Aspects.MOTION, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.jukebox), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.MECHANISM, 2).addAll(
              Aspects.AIR, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.noteblock), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 4).addAll(
              Aspects.MECHANISM, 1).addAll(
              Aspects.AIR, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.trapdoor, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MOTION, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.lit_furnace, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.FIRE, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.furnace, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.enchanting_table), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 8).addAll(
              Aspects.CRAFT, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.crafting_table), (new LinkedHashAspectList<>()).addAll(Aspects.CRAFT, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.clock), (new LinkedHashAspectList<>()).addAll(Aspects.MECHANISM, 2));
      ThaumcraftApi.registerObjectTag(new ItemStack(Blocks.anvil), new int[]{0, 1, 2}, (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 64).addAll(Aspects.CRAFT, 2).addAll(Aspects.TOOL, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.beacon), (new LinkedHashAspectList<>()).addAll(Aspects.AURA, 2).addAll(
              Aspects.MAGIC, 2).addAll(Aspects.EXCHANGE, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.wooden_button, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MECHANISM, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.carrot_on_a_stick, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.TRAVEL, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.flower_pot), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 1).addAll(
              Aspects.PLANT, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Items.golden_carrot), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 2));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.ender_chest, 1, 32767), (new LinkedHashAspectList<>()).mergeWithHighest(
              Aspects.EXCHANGE, 2).mergeWithHighest(Aspects.TRAVEL, 2).mergeWithHighest(Aspects.VOID, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.hopper, 1, 32767), (new LinkedHashAspectList<>()).mergeWithHighest(
              Aspects.MECHANISM, 1).mergeWithHighest(
              Aspects.EXCHANGE, 1).mergeWithHighest(Aspects.VOID, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.dropper, 1, 32767), (new LinkedHashAspectList<>()).mergeWithHighest(
              Aspects.MECHANISM, 1).mergeWithHighest(
              Aspects.EXCHANGE, 1).mergeWithHighest(Aspects.VOID, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.dispenser, 1, 32767), (new LinkedHashAspectList<>()).mergeWithHighest(
              Aspects.MECHANISM, 1).mergeWithHighest(
              Aspects.EXCHANGE, 1).mergeWithHighest(Aspects.VOID, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Blocks.tripwire_hook, 1, 32767), (new LinkedHashAspectList<>()).addAll(Aspects.SENSES, 1).addAll(
              Aspects.MECHANISM, 1).addAll(Aspects.TRAP, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(Blocks.daylight_detector, 1, 32767), (new LinkedHashAspectList<>()).mergeWithHighest(
              Aspects.SENSES, 2).mergeWithHighest(
              Aspects.LIGHT, 3).mergeWithHighest(Aspects.MECHANISM, 3));
      Map<List<PotionEffect>,Integer> lhm = new LinkedHashMap<>();

      for(int var4 = 1; var4 <= 32767; ++var4) {
         List<PotionEffect> var5 = (List<PotionEffect>)PotionHelper.getPotionEffects(var4, false);
         if (var5 != null && !var5.isEmpty()) {
            lhm.put(var5, var4);
         }
      }

      for(int var7 : lhm.values()) {
         ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(Items.potionitem, 1, var7), new AspectList(new ItemStack(Items.potionitem)));
      }

      ThaumcraftApi.registerComplexObjectTag(new ItemStack(ConfigBlocks.blockTable), (new LinkedHashAspectList<>()).addAll(Aspects.TOOL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTable, 1, 15), (new AspectList(new ItemStack(ConfigBlocks.blockTable))).addAll(
              Aspects.CRAFT, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTable, 1, 2), (new AspectList(new ItemStack(ConfigBlocks.blockTable))).addAll(
              Aspects.MIND, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 16), (new LinkedHashAspectList<>()).addAll(Aspects.ORDER, 1).addAll(
              Aspects.METAL, 6).addAll(Aspects.EARTH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 31), (new LinkedHashAspectList<>()).addAll(Aspects.ORDER, 1).addAll(
              Aspects.METAL, 4).addAll(Aspects.EARTH, 1).addAll(Aspects.GREED, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 21), (new LinkedHashAspectList<>()).addAll(Aspects.ORDER, 1).addAll(
              Aspects.METAL, 4).addAll(Aspects.EARTH, 1).addAll(Aspects.EXCHANGE, 4).addAll(Aspects.POISON, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 5), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNugget, 1, 6), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.METAL, 2).addAll(Aspects.EXCHANGE, 2).addAll(Aspects.POISON, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 7), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.TRAP, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.AIR, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 2), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.FIRE, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 3), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.WATER, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 4), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.EARTH, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 5), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.ORDER, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomOre, 1, 6), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.ENTROPY, 3).addAll(Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaint, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.TREE, 1).addAll(
              Aspects.TAINT, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaint, 1, 1), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 1).addAll(
              Aspects.TAINT, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaintFibres, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.LIFE, 1).addAll(Aspects.TAINT, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaintFibres, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 1).addAll(Aspects.TAINT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaintFibres, 1, 2), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 1).addAll(Aspects.TAINT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaintFibres, 1, 3), (new LinkedHashAspectList<>()).addAll(
              Aspects.BEAST, 1).addAll(Aspects.PLANT, 1).addAll(Aspects.TAINT, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockTaintFibres, 1, 4), (new LinkedHashAspectList<>()).addAll(
              Aspects.BEAST, 1).addAll(Aspects.PLANT, 1).addAll(Aspects.TAINT, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCosmeticSolid), (new LinkedHashAspectList<>()).addAll(Aspects.EARTH, 4).addAll(
              Aspects.DARKNESS, 2).addAll(Aspects.ELDRITCH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.TREE, 3).addAll(
              Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.TREE, 3).addAll(
              Aspects.MAGIC, 1).addAll(Aspects.ORDER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockMagicalLeaves, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockMagicalLeaves, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(Aspects.TREE, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(Aspects.TREE, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(
              Aspects.EXCHANGE, 2).addAll(Aspects.MAGIC, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 3), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(
              Aspects.FIRE, 2).addAll(Aspects.MAGIC, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5), (new LinkedHashAspectList<>()).addAll(
              Aspects.PLANT, 2).addAll(
              Aspects.POISON, 1).addAll(Aspects.MAGIC, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(QUICK_SILVER), (new LinkedHashAspectList<>()).addAll(Aspects.METAL, 3).addAll(
              Aspects.POISON, 1).addAll(Aspects.EXCHANGE, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemZombieBrain), (new LinkedHashAspectList<>()).addAll(Aspects.FLESH, 2).addAll(
              Aspects.MIND, 4).addAll(Aspects.UNDEAD, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.AMBER_GEM), (new LinkedHashAspectList<>()).addAll(Aspects.TRAP, 2).addAll(
              Aspects.CRYSTAL, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.KNOWLEDGE_FRAGMENT), (new LinkedHashAspectList<>()).addAll(
              Aspects.MIND, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.TAINTED_GOO,1), (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 3).addAll(
              Aspects.SLIME, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.TAINT_TENDRIL,1), (new LinkedHashAspectList<>()).addAll(Aspects.TAINT, 2).addAll(
              Aspects.GREED, 1).addAll(Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.GOLD_COIN), (new LinkedHashAspectList<>()).addAll(Aspects.GREED, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLootbag, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.GREED, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLootbag, 1, 1), (new LinkedHashAspectList<>()).addAll(Aspects.GREED, 16));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLootbag, 1, 2), (new LinkedHashAspectList<>()).addAll(Aspects.GREED, 32));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNuggetBeef, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNuggetChicken, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNuggetPork, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemNuggetFish, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.HUNGER, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(ConfigItems.itemTripleMeatTreat, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.HEAL, 1).reduceAndRemoveIfNotPositive(Aspects.HUNGER, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.AIR, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 1), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.FIRE, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 2), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.WATER, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 3), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.EARTH, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 4), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.ORDER, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemShard, 1, 5), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 1).addAll(
              Aspects.ENTROPY, 2).addAll(Aspects.CRYSTAL, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.SALIS_MUNDUS, 1), (new AspectList(new ItemStack(ConfigItems.itemShard, 1, 6))).addAll(
              Aspects.MAGIC, 2).remove(
              Aspects.CRYSTAL));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockMetalDevice), (new AspectList(new ItemStack(Items.cauldron, 1, 32767))).addAll(
              Aspects.CRAFT, 4).addAll(Aspects.MAGIC, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCandle), (new LinkedHashAspectList<>()).addAll(Aspects.LIGHT, 2).addAll(
              Aspects.FLESH, 1).addAll(Aspects.MAGIC, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockAiry, 1, 2), (new LinkedHashAspectList<>()).addAll(Aspects.LIGHT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockAiry, 1, 3), (new LinkedHashAspectList<>()).addAll(Aspects.LIGHT, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemThaumonomicon, 1, 32767), (new AspectList(new ItemStack(Blocks.bookshelf))).addAll(
              Aspects.MAGIC, 2).mergeWithHighest(Aspects.MIND, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockAlchemyFurnace, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.MAGIC, 8).addAll(
              Aspects.WATER, 8).addAll(Aspects.CRAFT, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemEssence, 1, 0), (new LinkedHashAspectList<>()).addAll(Aspects.VOID, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemEssence, 1, 1), new LinkedHashAspectList<>());
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemWispEssence, 1, 0), (new LinkedHashAspectList<>()).addAll(
              Aspects.AURA, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemCrystalEssence, 1, 0), new LinkedHashAspectList<>());
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(ConfigItems.itemPrimalArrow), (new LinkedHashAspectList<>()).addAll(
              Aspects.WEAPON, 1));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(ConfigItems.itemGoggles, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.SENSES, 4));
      ThaumcraftApi.registerComplexObjectTag(new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 1), (new LinkedHashAspectList<>()).addAll(
              Aspects.SENSES, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemBaubleBlanks, 1, 3), (new LinkedHashAspectList<>()).addAll(
              Aspects.MAGIC, 5));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemFocusPech), (new LinkedHashAspectList<>()).addAll(Aspects.MAGIC, 5).addAll(
              Aspects.POISON, 5).addAll(Aspects.ENTROPY, 5).addAll(Aspects.ELDRITCH, 5).addAll(Aspects.WEAPON, 5));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemChestCultistPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemChestCultistRobe, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 3).addAll(Aspects.CLOTH, 2).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemChestCultistLeaderPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLegsCultistPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLegsCultistLeaderPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemLegsCultistRobe, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 3).addAll(Aspects.CLOTH, 2).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemHelmetCultistPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemHelmetCultistLeaderPlate, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 5).addAll(Aspects.ELDRITCH, 2));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemHelmetCultistRobe, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 3).addAll(Aspects.CLOTH, 2).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigItems.itemBootsCultist, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 4).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 8), (new LinkedHashAspectList<>()).addAll(
              Aspects.ELDRITCH, 1).addAll(Aspects.TREE, 2).addAll(Aspects.CLOTH, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.ELDRITCH_EYE), (new LinkedHashAspectList<>()).addAll(Aspects.ELDRITCH, 5).addAll(
              Aspects.AURA, 3).addAll(Aspects.MAGIC, 3).addAll(Aspects.SENSES, 3).addAll(
              Aspects.SOUL, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.CRIMSON_RITES), (new LinkedHashAspectList<>()).addAll(Aspects.MIND, 5).addAll(
              Aspects.MAGIC, 3).addAll(
              Aspects.ELDRITCH, 3).addAll(Aspects.SOUL, 3));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.RUNED_TABLET), (new LinkedHashAspectList<>()).addAll(Aspects.TRAP, 4).addAll(
              Aspects.MIND, 4).addAll(Aspects.MECHANISM, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ThaumcraftItems.ThaumcraftItemInstances.PRIME_PEARL), (new LinkedHashAspectList<>()).addAll(Aspects.AIR, 16).addAll(
              Aspects.EARTH, 16).addAll(Aspects.FIRE, 16).addAll(Aspects.WATER, 16).addAll(Aspects.ORDER, 16).addAll(
              Aspects.ENTROPY, 16));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritch, 1, 32767), (new LinkedHashAspectList<>()).addAll(
              Aspects.VOID, 8).addAll(Aspects.ELDRITCH, 8).addAll(Aspects.SENSES, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritchPortal), (new LinkedHashAspectList<>()).addAll(
              Aspects.VOID, 8).addAll(Aspects.ELDRITCH, 8).addAll(Aspects.TRAVEL, 8));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritch, 1, 3), (new LinkedHashAspectList<>()).addAll(
              Aspects.VOID, 4).addAll(Aspects.ELDRITCH, 4));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritch, 1, 4), (new LinkedHashAspectList<>()).addAll(
              Aspects.LIGHT, 1).addAll(Aspects.EARTH, 1).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritch, 1, 5), (new LinkedHashAspectList<>()).addAll(
              Aspects.MIND, 2).addAll(
              Aspects.EARTH, 1).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockEldritch, 1, 6), (new LinkedHashAspectList<>()).addAll(
              Aspects.METAL, 2).addAll(Aspects.MECHANISM, 2).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 11), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.ELDRITCH, 1));
      ItemBasicAspectRegistration.registerItemBasicAspects(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 12), (new LinkedHashAspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.ELDRITCH, 1));
   }
}
