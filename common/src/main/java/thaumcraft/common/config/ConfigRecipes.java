package thaumcraft.common.config;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.item.crafting.FurnaceRecipes;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.api.wands.WandTriggerRegistry;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceLavaBlock;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.armor.RecipesRobeArmorDyes;
import thaumcraft.common.items.armor.RecipesVoidRobeArmorDyes;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.crafting.ArcaneSceptreRecipe;
import thaumcraft.common.lib.crafting.ArcaneWandRecipe;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.crafting.ShapelessNBTOreRecipe;

import java.util.Arrays;

public class ConfigRecipes {
   static ItemStack basicWand;

   public static void init() {
      ((WandCastingItem)basicWand.getItem()).setCap(basicWand, ConfigItems.WAND_CAP_IRON);
      ((WandCastingItem)basicWand.getItem()).setRod(basicWand, ConfigItems.WAND_ROD_WOOD);
      initializeSmelting();
      initializeNormalRecipes();
      initializeArcaneRecipes();
      initializeInfusionRecipes();
      initializeInfusionEnchantmentRecipes();
      initializeAlchemyRecipes();
      initializeCompoundRecipes();
      RecipeSorter.register("forge:shapelessorenbt", ShapelessNBTOreRecipe.class, Category.SHAPELESS, "after:forge:shapelessore");
      RecipeSorter.register("forge:robearmordye", RecipesRobeArmorDyes.class, Category.SHAPELESS, "after:forge:shapelessorenbt");
      RecipeSorter.register("forge:voidrobearmordye", RecipesVoidRobeArmorDyes.class, Category.SHAPELESS, "after:forge:robearmordye");
   }

   private static void initializeCompoundRecipes() {
      ItemStack empty = new ItemStack(ConfigBlocks.blockHole, 1, 15);
      ConfigResearch.recipes.put("Thaumonomicon", Arrays.asList(new AspectList<>(), 1, 2, 1, Arrays.asList(basicWand, new ItemStack(Blocks.bookshelf))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 0, Blocks.bookshelf, 0, "Thaumcraft");
      ConfigResearch.recipes.put("ArcTable", Arrays.asList(new AspectList<>(), 1, 2, 1, Arrays.asList(basicWand, new ItemStack(ConfigBlocks.blockTable))));
      ConfigResearch.recipes.put("ResTable", Arrays.asList(new AspectList<>(), 1, 2, 2, Arrays.asList(null, new ItemStack(ConfigItems.itemInkwell), new ItemStack(ConfigBlocks.blockTable), new ItemStack(ConfigBlocks.blockTable))));
      ConfigResearch.recipes.put("Crucible", Arrays.asList(new AspectList<>(), 1, 2, 1, Arrays.asList(basicWand, new ItemStack(Items.cauldron))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 1, Blocks.cauldron, -1, "Thaumcraft");
      ConfigResearch.recipes.put("InfernalFurnace", Arrays.asList((new AspectList<>()).addAll(Aspects.FIRE, 50).addAll(
              Aspects.EARTH, 50), 3, 3, 3, Arrays.asList(new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), empty, new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.lava), new ItemStack(Blocks.iron_bars), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick), new ItemStack(Blocks.obsidian), new ItemStack(Blocks.nether_brick))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 2, Blocks.obsidian, -1, "Thaumcraft");
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 2, Blocks.nether_brick, -1, "Thaumcraft");
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 2, Blocks.iron_bars, -1, "Thaumcraft");
      ConfigResearch.recipes.put("InfusionAltar", Arrays.asList((new AspectList<>()).addAll(Aspects.FIRE, 25).addAll(
              Aspects.EARTH, 25).addAll(Aspects.ORDER, 25).addAll(Aspects.AIR, 25).addAll(Aspects.ENTROPY, 25).addAll(
              Aspects.WATER, 25), 3, 3, 3, Arrays.asList(empty, null, empty, null, new ItemStack(ConfigBlocks.blockStoneDevice, 1, 2), null, empty, null, empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), null, null, null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), null, new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), null, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 3, ConfigBlocks.blockStoneDevice, 2, "Thaumcraft");
      ConfigResearch.recipes.put("NodeJar", Arrays.asList((new AspectList<>()).addAll(Aspects.FIRE, 70).addAll(Aspects.EARTH, 70).addAll(
              Aspects.AIR, 70).addAll(Aspects.WATER, 70).addAll(Aspects.ORDER, 70).addAll(Aspects.ENTROPY, 70), 3, 4, 3, Arrays.asList("slabWood", "slabWood", "slabWood", "slabWood", "slabWood", "slabWood", "slabWood", "slabWood", "slabWood", new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(ConfigBlocks.blockAiry, 1, 5), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass), new ItemStack(Blocks.glass))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 4, Blocks.glass, -1, "Thaumcraft");
      ConfigResearch.recipes.put("Thaumatorium", Arrays.asList((new AspectList<>()).addAll(Aspects.FIRE, 15).addAll(
              Aspects.ORDER, 30).addAll(Aspects.WATER, 30), 1, 3, 1, Arrays.asList(new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 5, ConfigBlocks.blockMetalDevice, 9, "Thaumcraft");
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 6, ConfigBlocks.blockEldritch, 0, "Thaumcraft");
      ConfigResearch.recipes.put("AdvAlchemyFurnace", Arrays.asList((new AspectList<>()).addAll(Aspects.FIRE, 50).addAll(
              Aspects.WATER, 50).addAll(Aspects.ORDER, 50), 3, 2, 3, Arrays.asList(new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), empty, new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockStoneDevice, 1, 0), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3))));
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 7, ConfigBlocks.blockMetalDevice, 3, "Thaumcraft");
      WandTriggerRegistry.registerWandBlockTrigger(Thaumcraft.proxy.wandManager, 7, ConfigBlocks.blockMetalDevice, 9, "Thaumcraft_2");
   }

   private static void initializeAlchemyRecipes() {
      Aspect[] aspect = new Aspect[]{Aspects.AIR, Aspects.FIRE, Aspects.WATER, Aspects.EARTH, Aspects.ORDER, Aspects.ENTROPY};

      for(int a = 0; a < 6; ++a) {
         AspectList<Aspect>al = new AspectList<>();

         for(int b = 0; b < 6; ++b) {
            if (b != a) {
               al.addAll(aspect[b], 2);
            }
         }

         ConfigResearch.recipes.put("BalancedShard_" + a, CrucibleRecipe.addCrucibleRecipe("CRUCIBLE", new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, a), al));
      }

      ConfigResearch.recipes.put("Alumentum", CrucibleRecipe.addCrucibleRecipe("ALUMENTUM", new ItemStack(ThaumcraftItems.ALUMENTUM), new ItemStack(Items.coal, 1, 32767), (new AspectList<>()).mergeWithHighest(
              Aspects.ENERGY, 3).mergeWithHighest(Aspects.FIRE, 3).mergeWithHighest(Aspects.ENTROPY, 3)));
      ConfigResearch.recipes.put("Nitor", CrucibleRecipe.addCrucibleRecipe("NITOR", new ItemStack(ThaumcraftItems.NITOR), "dustGlowstone", (new AspectList<>()).mergeWithHighest(
              Aspects.ENERGY, 3).mergeWithHighest(Aspects.FIRE, 3).mergeWithHighest(Aspects.LIGHT, 3)));
      ConfigResearch.recipes.put("Thaumium", CrucibleRecipe.addCrucibleRecipe("THAUMIUM", new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(Items.iron_ingot), (new AspectList<>()).mergeWithHighest(
              Aspects.MAGIC, 4)));
      ConfigResearch.recipes.put("VoidMetal", CrucibleRecipe.addCrucibleRecipe("VOIDMETAL", new ItemStack(ThaumcraftItems.VOID_INGOT,1), new ItemStack(ThaumcraftItems.VOID_SEED, 1), (new AspectList<>()).mergeWithHighest(
              Aspects.METAL, 8)));
      ConfigResearch.recipes.put("VoidSeed", CrucibleRecipe.addCrucibleRecipe("VOIDMETAL", new ItemStack(ThaumcraftItems.VOID_SEED, 1), new ItemStack(Items.wheat_seeds), (new AspectList<>()).mergeWithHighest(
              Aspects.DARKNESS, 8).mergeWithHighest(Aspects.VOID, 8).mergeWithHighest(Aspects.ELDRITCH, 2)));
      ConfigResearch.recipes.put("Tallow", CrucibleRecipe.addCrucibleRecipe("TALLOW", new ItemStack(ThaumcraftItems.MAGIC_TALLOW), new ItemStack(Items.rotten_flesh), (new AspectList<>()).mergeWithHighest(
              Aspects.MAGIC, 2)));
      ConfigResearch.recipes.put("AltGunpowder", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALDUPLICATION", new ItemStack(Items.gunpowder, 2, 0), new ItemStack(Items.gunpowder), (new AspectList<>()).mergeWithHighest(
              Aspects.FIRE, 4).mergeWithHighest(Aspects.ENTROPY, 4)));
      ConfigResearch.recipes.put("AltSlime", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALDUPLICATION", new ItemStack(Items.slime_ball, 2, 0), new ItemStack(Items.slime_ball), (new AspectList<>()).mergeWithHighest(
              Aspects.WATER, 2).mergeWithHighest(Aspects.LIFE, 2)));
      ConfigResearch.recipes.put("AltClay", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALDUPLICATION", new ItemStack(Items.clay_ball, 2, 0), new ItemStack(Items.clay_ball), (new AspectList<>()).mergeWithHighest(
              Aspects.WATER, 1).mergeWithHighest(Aspects.EARTH, 2)));
      ConfigResearch.recipes.put("AltGlowstone", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALDUPLICATION", new ItemStack(Items.glowstone_dust, 2, 0), "dustGlowstone", (new AspectList<>()).mergeWithHighest(
              Aspects.LIGHT, 3).mergeWithHighest(Aspects.SENSES, 1)));
      ConfigResearch.recipes.put("AltInk", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALDUPLICATION", new ItemStack(Items.dye, 2, 0), new ItemStack(Items.dye, 1, 0), (new AspectList<>()).mergeWithHighest(
              Aspects.WATER, 2).mergeWithHighest(Aspects.SENSES, 2)));
      ConfigResearch.recipes.put("AltWeb", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALMANUFACTURE", new ItemStack(Blocks.web), new ItemStack(Items.string), (new AspectList<>()).mergeWithHighest(
              Aspects.TRAP, 2).mergeWithHighest(Aspects.CLOTH, 2)));
      ConfigResearch.recipes.put("AltMossyCobble", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALMANUFACTURE", new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Blocks.cobblestone), (new AspectList<>()).mergeWithHighest(
              Aspects.PLANT, 2).mergeWithHighest(
              Aspects.MAGIC, 1)));
      ConfigResearch.recipes.put("AltIce", CrucibleRecipe.addCrucibleRecipe("ALCHEMICALMANUFACTURE", new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), (new AspectList<>()).mergeWithHighest(
              Aspects.ORDER, 1).mergeWithHighest(Aspects.COLD, 1)));
      ConfigResearch.recipes.put("AltCrackedBrick", CrucibleRecipe.addCrucibleRecipe("ENTROPICPROCESSING", new ItemStack(Blocks.stonebrick, 1, 2), new ItemStack(Blocks.stonebrick), (new AspectList<>()).mergeWithHighest(
              Aspects.ENTROPY, 2)));
      ConfigResearch.recipes.put("AltBonemeal", CrucibleRecipe.addCrucibleRecipe("ENTROPICPROCESSING", new ItemStack(Items.dye, 4, 15), new ItemStack(Items.bone), (new AspectList<>()).mergeWithHighest(
              Aspects.ENTROPY, 1)));
      ConfigResearch.recipes.put("PureIron", CrucibleRecipe.addCrucibleRecipe("PUREIRON", new ItemStack(ConfigItems.itemNugget, 1, 16), "oreIron", (new AspectList<>()).mergeWithHighest(
              Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      ConfigResearch.recipes.put("PureGold", CrucibleRecipe.addCrucibleRecipe("PUREGOLD", new ItemStack(ConfigItems.itemNugget, 1, 31), "oreGold", (new AspectList<>()).mergeWithHighest(
              Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      if (Config.foundCopperIngot) {
         ConfigResearch.recipes.put("PureCopper", CrucibleRecipe.addCrucibleRecipe("PURECOPPER", new ItemStack(ConfigItems.itemNugget, 1, 17), "oreCopper", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      }

      if (Config.foundTinIngot) {
         ConfigResearch.recipes.put("PureTin", CrucibleRecipe.addCrucibleRecipe("PURETIN", new ItemStack(ConfigItems.itemNugget, 1, 18), "oreTin", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      }

      if (Config.foundSilverIngot) {
         ConfigResearch.recipes.put("PureSilver", CrucibleRecipe.addCrucibleRecipe("PURESILVER", new ItemStack(ConfigItems.itemNugget, 1, 19), "oreSilver", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      }

      if (Config.foundLeadIngot) {
         ConfigResearch.recipes.put("PureLead", CrucibleRecipe.addCrucibleRecipe("PURELEAD", new ItemStack(ConfigItems.itemNugget, 1, 20), "oreLead", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 1).mergeWithHighest(Aspects.ORDER, 1)));
      }

      ConfigResearch.recipes.put("TransIron", CrucibleRecipe.addCrucibleRecipe("TRANSIRON", new ItemStack(ConfigItems.itemNugget, 3, 0), "nuggetIron", (new AspectList<>()).mergeWithHighest(
              Aspects.METAL, 2)));
      ConfigResearch.recipes.put("TransGold", CrucibleRecipe.addCrucibleRecipe("TRANSGOLD", new ItemStack(Items.gold_nugget, 3, 0), new ItemStack(Items.gold_nugget), (new AspectList<>()).mergeWithHighest(
              Aspects.METAL, 2).mergeWithHighest(Aspects.GREED, 1)));
      if (Config.foundCopperIngot) {
         ConfigResearch.recipes.put("TransCopper", CrucibleRecipe.addCrucibleRecipe("TRANSCOPPER", new ItemStack(ConfigItems.itemNugget, 3, 1), "nuggetCopper", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 2).mergeWithHighest(Aspects.EXCHANGE, 1)));
      }

      if (Config.foundTinIngot) {
         ConfigResearch.recipes.put("TransTin", CrucibleRecipe.addCrucibleRecipe("TRANSTIN", new ItemStack(ConfigItems.itemNugget, 3, 2), "nuggetTin", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 2).mergeWithHighest(Aspects.CRYSTAL, 1)));
      }

      if (Config.foundSilverIngot) {
         ConfigResearch.recipes.put("TransSilver", CrucibleRecipe.addCrucibleRecipe("TRANSSILVER", new ItemStack(ConfigItems.itemNugget, 3, 3), "nuggetSilver", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 2).mergeWithHighest(Aspects.GREED, 1)));
      }

      if (Config.foundLeadIngot) {
         ConfigResearch.recipes.put("TransLead", CrucibleRecipe.addCrucibleRecipe("TRANSLEAD", new ItemStack(ConfigItems.itemNugget, 3, 4), "nuggetLead", (new AspectList<>()).mergeWithHighest(
                 Aspects.METAL, 2).mergeWithHighest(Aspects.ORDER, 1)));
      }

      ConfigResearch.recipes.put("EtherealBloom", CrucibleRecipe.addCrucibleRecipe("ETHEREALBLOOM", new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4), new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2), (new AspectList<>()).addAll(
              Aspects.MAGIC, 16).addAll(Aspects.PLANT, 16).addAll(Aspects.HEAL, 16).addAll(Aspects.TAINT, 8)));
      ConfigResearch.recipes.put("LiquidDeath", CrucibleRecipe.addCrucibleRecipe("LIQUIDDEATH", new ItemStack(ConfigItems.itemBucketDeath), new ItemStack(Items.bucket), (new AspectList<>()).addAll(
              Aspects.DEATH, 32).addAll(Aspects.POISON, 32).addAll(Aspects.ENTROPY, 32)));
      ItemStack bt = new ItemStack(ConfigItems.itemEssence, 1, 1);
      ((IEssentiaContainerItem) bt.getItem()).setAspects(bt, (new AspectList<>()).addAll(Aspects.TAINT, 8));
      ConfigResearch.recipes.put("BottleTaint", CrucibleRecipe.addCrucibleRecipe("BOTTLETAINT", new ItemStack(ConfigItems.itemBottleTaint), bt, (new AspectList<>()).addAll(
              Aspects.TAINT, 8).addAll(
              Aspects.MAGIC, 8)));
      ConfigResearch.recipes.put("GolemStraw", CrucibleRecipe.addCrucibleRecipe("GOLEMSTRAW", new ItemStack(ConfigItems.itemGolemPlacer, 1, 0), new ItemStack(Blocks.hay_block), (new AspectList<>()).addAll(
              Aspects.MAN, 4).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4)));
      ConfigResearch.recipes.put("GolemWood", CrucibleRecipe.addCrucibleRecipe("GOLEMWOOD", new ItemStack(ConfigItems.itemGolemPlacer, 1, 1), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0), (new AspectList<>()).addAll(
              Aspects.MAN, 4).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4)));
      ConfigResearch.recipes.put("GolemTallow", CrucibleRecipe.addCrucibleRecipe("GOLEMTALLOW", new ItemStack(ConfigItems.itemGolemPlacer, 1, 2), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 5), (new AspectList<>()).addAll(
              Aspects.MAN, 8).addAll(
              Aspects.MOTION, 8).addAll(Aspects.SOUL, 8)));
      ConfigResearch.recipes.put("GolemClay", CrucibleRecipe.addCrucibleRecipe("GOLEMCLAY", new ItemStack(ConfigItems.itemGolemPlacer, 1, 3), new ItemStack(Blocks.brick_block), (new AspectList<>()).addAll(
              Aspects.MAN, 4).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4)));
      ConfigResearch.recipes.put("GolemFlesh", CrucibleRecipe.addCrucibleRecipe("GOLEMFLESH", new ItemStack(ConfigItems.itemGolemPlacer, 1, 4), new ItemStack(ConfigBlocks.blockTaint, 1, 2), (new AspectList<>()).addAll(
              Aspects.MAN, 8).addAll(
              Aspects.MOTION, 8).addAll(Aspects.SOUL, 8)));
      ConfigResearch.recipes.put("GolemStone", CrucibleRecipe.addCrucibleRecipe("GOLEMSTONE", new ItemStack(ConfigItems.itemGolemPlacer, 1, 5), new ItemStack(Blocks.stonebrick), (new AspectList<>()).addAll(
              Aspects.MAN, 4).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4)));
      ConfigResearch.recipes.put("GolemIron", CrucibleRecipe.addCrucibleRecipe("GOLEMIRON", new ItemStack(ConfigItems.itemGolemPlacer, 1, 6), new ItemStack(Blocks.iron_block), (new AspectList<>()).addAll(
              Aspects.MAN, 4).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4)));
      ConfigResearch.recipes.put("GolemThaumium", CrucibleRecipe.addCrucibleRecipe("GOLEMTHAUMIUM", new ItemStack(ConfigItems.itemGolemPlacer, 1, 7), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 4), (new AspectList<>()).addAll(
              Aspects.MAN, 8).addAll(
              Aspects.MOTION, 8).addAll(Aspects.SOUL, 8)));
      ConfigResearch.recipes.put("CoreGather", CrucibleRecipe.addCrucibleRecipe("COREGATHER", new ItemStack(ConfigItems.itemGolemCore, 1, 2), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.GREED, 5).addAll(
              Aspects.EARTH, 5)));
      ConfigResearch.recipes.put("CoreFill", CrucibleRecipe.addCrucibleRecipe("COREFILL", new ItemStack(ConfigItems.itemGolemCore, 1, 0), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.HUNGER, 5).addAll(
              Aspects.VOID, 5)));
      ConfigResearch.recipes.put("CoreEmpty", CrucibleRecipe.addCrucibleRecipe("COREEMPTY", new ItemStack(ConfigItems.itemGolemCore, 1, 1), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.GREED, 5).addAll(
              Aspects.VOID, 5)));
      ConfigResearch.recipes.put("CoreHarvest", CrucibleRecipe.addCrucibleRecipe("COREHARVEST", new ItemStack(ConfigItems.itemGolemCore, 1, 3), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.HARVEST, 5).addAll(Aspects.CROP, 5)));
      ConfigResearch.recipes.put("CoreGuard", CrucibleRecipe.addCrucibleRecipe("COREGUARD", new ItemStack(ConfigItems.itemGolemCore, 1, 4), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.WEAPON, 5).addAll(Aspects.TRAP, 5)));
      ConfigResearch.recipes.put("CoreButcher", CrucibleRecipe.addCrucibleRecipe("COREBUTCHER", new ItemStack(ConfigItems.itemGolemCore, 1, 9), new ItemStack(ConfigItems.itemGolemCore, 1, 4), (new AspectList<>()).addAll(
              Aspects.FLESH, 5).addAll(Aspects.BEAST, 5)));
      ConfigResearch.recipes.put("CoreLiquid", CrucibleRecipe.addCrucibleRecipe("CORELIQUID", new ItemStack(ConfigItems.itemGolemCore, 1, 5), new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.VOID, 5)));
      ConfigResearch.recipes.put("BathSalts", CrucibleRecipe.addCrucibleRecipe("BATHSALTS", new ItemStack(ConfigItems.itemBathSalts), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), (new AspectList<>()).addAll(
              Aspects.MIND, 6).addAll(Aspects.AURA, 6).addAll(
              Aspects.ORDER, 6).addAll(Aspects.HEAL, 6)));
      ConfigResearch.recipes.put("SaneSoap", CrucibleRecipe.addCrucibleRecipe("SANESOAP", new ItemStack(ConfigItems.itemSanitySoap), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 5), (new AspectList<>()).addAll(
              Aspects.MIND, 16).addAll(
              Aspects.ELDRITCH, 16).addAll(Aspects.ORDER, 16).addAll(Aspects.HEAL, 16)));
   }

   private static void initializeArcaneRecipes() {
      for(int a = 0; a < 16; ++a) {
         ItemStack banner = new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 8);
         banner.setTagCompound(new NBTTagCompound());
         banner.stackTagCompound.setByte("color", (byte)a);
         ConfigResearch.recipes.put("Banner_" + a, IArcaneRecipe.addArcaneCraftingRecipe("BANNERS", banner, (new AspectList<>()).addAll(
                 Aspects.WATER, 5).addAll(Aspects.EARTH, 5), "WS", "WS", "WB", 'W', new ItemStack(Blocks.wool, 1, a), 'S', "stickWood", 'B', "slabWood"));
      }

      ConfigResearch.recipes.put("PrimalCharm", IArcaneRecipe.addArcaneCraftingRecipe("BASICARTIFACE", new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), (new AspectList<>()).addAll(
              Aspects.EARTH, 25).addAll(Aspects.FIRE, 25).addAll(Aspects.AIR, 25).addAll(Aspects.WATER, 25).addAll(
              Aspects.ORDER, 25).addAll(Aspects.ENTROPY, 25), "123", "ISI", "456", 'S', new ItemStack(ConfigItems.itemShard, 1, 6), 'I', Items.gold_ingot, '1', new ItemStack(ConfigItems.itemShard, 1, 0), '2', new ItemStack(ConfigItems.itemShard, 1, 1), '3', new ItemStack(ConfigItems.itemShard, 1, 2), '4', new ItemStack(ConfigItems.itemShard, 1, 3), '5', new ItemStack(ConfigItems.itemShard, 1, 4), '6', new ItemStack(ConfigItems.itemShard, 1, 5)));
      ConfigResearch.recipes.put("ArcaneDoor", IArcaneRecipe.addArcaneCraftingRecipe("WARDEDARCANA", new ItemStack(ConfigItems.itemArcaneDoor), (new AspectList<>()).addAll(
              Aspects.WATER, 20).addAll(Aspects.ORDER, 10).addAll(Aspects.EARTH, 10).addAll(Aspects.FIRE, 5), "TDT", "DBD", "TDT", 'T', "ingotThaumium", 'B', new ItemStack(ConfigItems.itemZombieBrain), 'D', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6)));
      ConfigResearch.recipes.put("WardedGlass", IArcaneRecipe.addArcaneCraftingRecipe("WARDEDARCANA", new ItemStack(ConfigBlocks.blockCosmeticOpaque, 8, 2), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 10).addAll(Aspects.EARTH, 5).addAll(Aspects.FIRE, 5), "GGG", "WBW", "GGG", 'B', new ItemStack(ConfigItems.itemZombieBrain), 'G', new ItemStack(Blocks.glass), 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6)));
      ConfigResearch.recipes.put("IronKey", IArcaneRecipe.addArcaneCraftingRecipe("WARDEDARCANA", new ItemStack(ConfigItems.itemKey, 2, 0), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5), "NNI", "N  ", 'I', Items.iron_ingot, 'N', "nuggetIron"));
      ConfigResearch.recipes.put("FluxScrubber", IArcaneRecipe.addArcaneCraftingRecipe("FLUXSCRUB", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 14), (new AspectList<>()).addAll(
              Aspects.WATER, 16).addAll(Aspects.ORDER, 16).addAll(Aspects.AIR, 8), " B ", "GOG", "STS", 'B', new ItemStack(ConfigBlocks.blockWoodenDevice), 'G', new ItemStack(Blocks.iron_bars), 'T', new ItemStack(ConfigBlocks.blockTube), 'O', new ItemStack(ThaumcraftItems.VIS_FILTER), 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7)));
      if (Config.wardedStone) {
         ConfigResearch.recipes.put("GoldKey", IArcaneRecipe.addArcaneCraftingRecipe("WARDEDARCANA", new ItemStack(ConfigItems.itemKey, 2, 1), (new AspectList<>()).addAll(
                 Aspects.WATER, 5).addAll(Aspects.ORDER, 5), "NNI", "N  ", 'I', Items.gold_ingot, 'N', Items.gold_nugget));
         ConfigResearch.recipes.put("ArcanePressurePlate", IArcaneRecipe.addArcaneCraftingRecipe("WARDEDARCANA", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 2), (new AspectList<>()).addAll(
                 Aspects.WATER, 20).addAll(Aspects.ORDER, 10).addAll(Aspects.FIRE, 5).addAll(Aspects.EARTH, 10), " B ", "TDT", 'T', new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), 'B', new ItemStack(ConfigItems.itemZombieBrain), 'D', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6)));
      }

      ConfigResearch.recipes.put("NodeStabilizer", IArcaneRecipe.addArcaneCraftingRecipe("NODESTABILIZER", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9), (new AspectList<>()).addAll(
              Aspects.WATER, 32).addAll(Aspects.EARTH, 32).addAll(Aspects.ORDER, 32), " G ", "QPQ", "SNS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), 'G', new ItemStack(Items.gold_ingot), 'P', new ItemStack(Blocks.piston), 'Q', new ItemStack(Blocks.quartz_block), 'N', new ItemStack(ThaumcraftItems.NITOR)));
      ConfigResearch.recipes.put("NodeTransducer", IArcaneRecipe.addArcaneCraftingRecipe("VISPOWER", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 11), (new AspectList<>()).addAll(
              Aspects.FIRE, 32).addAll(Aspects.AIR, 32).addAll(Aspects.ENTROPY, 32), "RCR", "ISI", "RAR", 'S', new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9), 'C', new ItemStack(Items.comparator), 'I', new ItemStack(Items.iron_ingot), 'R', new ItemStack(Blocks.redstone_block), 'A', new ItemStack(ThaumcraftItems.NITOR)));
      ConfigResearch.recipes.put("NodeRelay", IArcaneRecipe.addArcaneCraftingRecipe("VISPOWER", new ItemStack(ConfigBlocks.blockMetalDevice, 2, 14), (new AspectList<>()).addAll(
              Aspects.FIRE, 8).addAll(Aspects.ORDER, 8), " I ", "ISI", " I ", 'I', new ItemStack(Items.iron_ingot), 'S', new ItemStack(ConfigItems.itemShard, 1, 6)));
      ConfigResearch.recipes.put("NodeChargeRelay", IArcaneRecipe.addArcaneCraftingRecipe("VISCHARGERELAY", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 2), (new AspectList<>()).addAll(
              Aspects.FIRE, 16).addAll(Aspects.ORDER, 16).addAll(Aspects.AIR, 16), " R ", "W W", "I I", 'I', new ItemStack(Items.iron_ingot), 'R', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 14), 'W', new ItemStack(ConfigItems.itemWandRod, 1, 0)));
      ConfigResearch.recipes.put("FocalManipulator", IArcaneRecipe.addArcaneCraftingRecipe("FOCALMANIPULATION", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 13), (new AspectList<>()).addAll(
              Aspects.FIRE, 32).addAll(Aspects.AIR, 32).addAll(Aspects.ENTROPY, 32).addAll(Aspects.EARTH, 32).addAll(
              Aspects.WATER, 32).addAll(Aspects.ORDER, 32), "IQI", "SPS", "GTG", 'Q', new ItemStack(ConfigBlocks.blockSlabStone, 1, 0), 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'T', new ItemStack(ConfigBlocks.blockTable), 'I', new ItemStack(Items.iron_ingot), 'G', new ItemStack(Items.gold_ingot), 'P', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1)));
      ConfigResearch.recipes.put("GolemFetter", IArcaneRecipe.addArcaneCraftingRecipe("GOLEMFETTER", new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 9), (new AspectList<>()).addAll(
              Aspects.EARTH, 5).addAll(Aspects.ORDER, 5), "SSS", "IRI", "BBB", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'I', new ItemStack(Items.iron_ingot), 'B', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), 'R', new ItemStack(Blocks.redstone_block)));
      ConfigResearch.recipes.put("ArcaneStone1", IArcaneRecipe.addArcaneCraftingRecipe("ARCANESTONE", new ItemStack(ConfigBlocks.blockCosmeticSolid, 9, 6), (new AspectList<>()).addAll(
              Aspects.EARTH, 1).addAll(Aspects.FIRE, 1), "SSS", "SCS", "SSS", 'S', "stone", 'C', new ItemStack(ConfigItems.itemShard, 1, 32767)));
      ConfigResearch.recipes.put("ArcaneStone2", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 7), "SS", "SS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6)));
      ConfigResearch.recipes.put("ArcaneStone3", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockStairsArcaneStone, 4, 0), "K  ", "KK ", "KKK", 'K', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7)));
      ConfigResearch.recipes.put("ArcaneStone4", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockSlabStone, 6, 0), "KKK", 'K', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7)));
      ConfigResearch.recipes.put("PaveTravel", IArcaneRecipe.addArcaneCraftingRecipe("PAVETRAVEL", new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 2), (new AspectList<>()).addAll(
              Aspects.EARTH, 10).addAll(Aspects.AIR, 10), "SAS", "SBS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), 'A', new ItemStack(ConfigItems.itemShard, 1, 0), 'B', new ItemStack(ConfigItems.itemShard, 1, 3)));
      ConfigResearch.recipes.put("ArcaneLamp", IArcaneRecipe.addArcaneCraftingRecipe("ARCANELAMP", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 7), (new AspectList<>()).addAll(
              Aspects.FIRE, 8).addAll(Aspects.AIR, 8).addAll(Aspects.WATER, 4).addAll(Aspects.ENTROPY, 4), " S ", "IAI", " N ", 'A', new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 0), 'S', new ItemStack(Blocks.daylight_detector), 'N', new ItemStack(ThaumcraftItems.NITOR), 'I', new ItemStack(Items.iron_ingot)));
      ConfigResearch.recipes.put("ArcaneSpa", IArcaneRecipe.addArcaneCraftingRecipe("ARCANESPA", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 12), (new AspectList<>()).addAll(
              Aspects.WATER, 16).addAll(Aspects.ORDER, 8).addAll(Aspects.EARTH, 4), "QIQ", "SJS", "SPS", 'P', new ItemStack(Blocks.piston), 'J', new ItemStack(ConfigBlocks.blockJar), 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'Q', new ItemStack(Blocks.quartz_block), 'I', new ItemStack(Blocks.iron_bars)));
      ConfigResearch.recipes.put("PaveWard", IArcaneRecipe.addArcaneCraftingRecipe("PAVEWARD", new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 3), (new AspectList<>()).addAll(
              Aspects.FIRE, 10).addAll(Aspects.ORDER, 10), "SAS", "SBS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7), 'A', new ItemStack(ConfigItems.itemShard, 1, 1), 'B', new ItemStack(ConfigItems.itemShard, 1, 4)));
      ConfigResearch.recipes.put("Levitator", IArcaneRecipe.addArcaneCraftingRecipe("LEVITATOR", new ItemStack(ConfigBlocks.blockLifter), (new AspectList<>()).addAll(
              Aspects.AIR, 10).addAll(Aspects.EARTH, 5), "WEW", "BNB", "WAW", 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'E', new ItemStack(ConfigItems.itemShard, 1, 3), 'A', new ItemStack(ConfigItems.itemShard, 1, 0), 'N', new ItemStack(ThaumcraftItems.NITOR), 'B', Items.iron_ingot));
      ConfigResearch.recipes.put("ArcaneEar", IArcaneRecipe.addArcaneCraftingRecipe("ARCANEEAR", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 1), (new AspectList<>()).addAll(
              Aspects.AIR, 10).addAll(Aspects.ORDER, 10), "GIG", "GBG", "WRW", 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'R', Items.redstone, 'I', Items.iron_ingot, 'G', Items.gold_ingot, 'B', new ItemStack(ConfigItems.itemZombieBrain)));
      ConfigResearch.recipes.put("MirrorGlass", IArcaneRecipe.addShapelessArcaneCraftingRecipe("BASICARTIFACE", new ItemStack(ThaumcraftItems.MIRRORED_GLASS, 1), (new AspectList<>()).addAll(
              Aspects.FIRE, 10).addAll(Aspects.EARTH, 10), new ItemStack(QUICK_SILVER), Blocks.glass_pane));
      ConfigResearch.recipes.put("BoneBow",
              IArcaneRecipe.addArcaneCraftingRecipe(
                      "BONEBOW",
                      new ItemStack(ConfigItems.itemBowBone), (new AspectList<>()).addAll(Aspects.AIR, 16).addAll(Aspects.ENTROPY, 32), "SB ", "SEB", "SB ", 'E', new ItemStack(ConfigItems.itemShard, 1, 5), 'B', Items.bone, 'S', Items.string));
      Aspect[] pa = new Aspect[]{Aspects.AIR, Aspects.FIRE, Aspects.WATER, Aspects.EARTH, Aspects.ORDER, Aspects.ENTROPY};

      for(int a = 0; a < 6; ++a) {
         ConfigResearch.recipes.put("PrimalArrow_" + a, IArcaneRecipe.addArcaneCraftingRecipe("PRIMALARROW", new ItemStack(ConfigItems.itemPrimalArrow, 8, a), (new AspectList<>()).addAll(pa[a], 8), "AAA", "ASA", "AAA", 'A', Items.arrow, 'S', new ItemStack(ConfigItems.itemShard, 1, a)));
      }

      ConfigResearch.recipes.put("InfusionMatrix", IArcaneRecipe.addArcaneCraftingRecipe("INFUSION", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 2), (new AspectList<>()).addAll(
              Aspects.ORDER, 40), "SBS", "BEB", "SBS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6), 'E', Items.ender_pearl, 'B', new ItemStack(ConfigItems.itemShard, 1, 32767)));
      ConfigResearch.recipes.put("ArcanePedestal", IArcaneRecipe.addArcaneCraftingRecipe("INFUSION", new ItemStack(ConfigBlocks.blockStoneDevice, 2, 1), (new AspectList<>()).addAll(
              Aspects.AIR, 5), "SSS", " S ", "SSS", 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6)));
      ConfigResearch.recipes.put("WardedJar", IArcaneRecipe.addArcaneCraftingRecipe("DISTILESSENTIA", new ItemStack(ConfigBlocks.blockJar, 1, 0), (new AspectList<>()).addAll(
              Aspects.WATER, 1), "GWG", "G G", "GGG", 'W', "slabWood", 'G', Blocks.glass_pane));
      ConfigResearch.recipes.put("JarVoid", IArcaneRecipe.addArcaneCraftingRecipe("JARVOID", new ItemStack(ConfigBlocks.blockJar, 1, 3), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ENTROPY, 15), "O", "J", "P", 'O', Blocks.obsidian, 'P', Items.blaze_powder, 'J', new ItemStack(ConfigBlocks.blockJar, 1, 0)));
      ArcaneWandRecipe wr = new ArcaneWandRecipe();
      ThaumcraftApi.getCraftingRecipes().add(wr);
      ArcaneSceptreRecipe sr = new ArcaneSceptreRecipe();
      ThaumcraftApi.getCraftingRecipes().add(sr);
      ConfigResearch.recipes.put("WandCapGold", IArcaneRecipe.addArcaneCraftingRecipe("CAP_gold", new ItemStack(ConfigItems.itemWandCap, 1, 1), (new AspectList<>()).addAll(
              Aspects.ORDER, WandCap.caps.get("gold").getCraftCost()).addAll(Aspects.FIRE, WandCap.caps.get("gold").getCraftCost()).addAll(
              Aspects.AIR, WandCap.caps.get("gold").getCraftCost()), "NNN", "N N", 'N', Items.gold_nugget));
      if (Config.foundCopperIngot) {
         ConfigResearch.recipes.put("WandCapCopper", IArcaneRecipe.addArcaneCraftingRecipe("CAP_copper", new ItemStack(ConfigItems.itemWandCap, 1, 3), (new AspectList<>()).addAll(
                 Aspects.ORDER, WandCap.caps.get("copper").getCraftCost()).addAll(Aspects.FIRE, WandCap.caps.get("copper").getCraftCost()).addAll(
                 Aspects.AIR, WandCap.caps.get("copper").getCraftCost()), "NNN", "N N", 'N', "nuggetCopper"));
      }

      if (Config.foundSilverIngot) {
         ConfigResearch.recipes.put("WandCapSilverInert", IArcaneRecipe.addArcaneCraftingRecipe("CAP_silver", new ItemStack(ConfigItems.itemWandCap, 1, 5), (new AspectList<>()).addAll(
                 Aspects.ORDER, WandCap.caps.get("silver").getCraftCost()).addAll(Aspects.FIRE, WandCap.caps.get("silver").getCraftCost()).addAll(
                 Aspects.AIR, WandCap.caps.get("silver").getCraftCost()), "NNN", "N N", 'N', "nuggetSilver"));
      }

      ConfigResearch.recipes.put("WandCapThaumiumInert", IArcaneRecipe.addArcaneCraftingRecipe("CAP_thaumium", new ItemStack(ConfigItems.itemWandCap, 1, 6), (new AspectList<>()).addAll(
              Aspects.ORDER, WandCap.caps.get("thaumium").getCraftCost()).addAll(Aspects.FIRE, WandCap.caps.get("thaumium").getCraftCost()).addAll(
              Aspects.AIR, WandCap.caps.get("thaumium").getCraftCost()), "NNN", "N N", 'N', "nuggetThaumium"));
      ConfigResearch.recipes.put("WandCapVoidInert", IArcaneRecipe.addArcaneCraftingRecipe("CAP_void", new ItemStack(ConfigItems.itemWandCap, 1, 8), (new AspectList<>()).addAll(
              Aspects.ENTROPY, WandCap.caps.get("void").getCraftCost() * 3).addAll(Aspects.ORDER, WandCap.caps.get("void").getCraftCost() * 3).addAll(
              Aspects.FIRE, WandCap.caps.get("void").getCraftCost() * 2).addAll(Aspects.AIR, WandCap.caps.get("void").getCraftCost() * 2), "NNN", "N N", 'N', "nuggetVoid"));
      ConfigResearch.recipes.put("WandRodGreatwood", IArcaneRecipe.addArcaneCraftingRecipe("ROD_greatwood", new ItemStack(ConfigItems.itemWandRod, 1, 0), (new AspectList<>()).addAll(
              Aspects.ENTROPY, WandRod.rods.get("greatwood").getCraftCost()), " G", "G ", 'G', new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)));
      ConfigResearch.recipes.put("WandRodGreatwoodStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_greatwood_staff", new ItemStack(ConfigItems.itemWandRod, 1, 50), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("greatwood_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 0)));
      ConfigResearch.recipes.put("WandRodObsidianStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_obsidian_staff", new ItemStack(ConfigItems.itemWandRod, 1, 51), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("obsidian_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 1)));
      ConfigResearch.recipes.put("WandRodSilverwoodStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_silverwood_staff", new ItemStack(ConfigItems.itemWandRod, 1, 52), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("silverwood_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 2)));
      ConfigResearch.recipes.put("WandRodIceStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_ice_staff", new ItemStack(ConfigItems.itemWandRod, 1, 53), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("ice_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 3)));
      ConfigResearch.recipes.put("WandRodQuartzStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_quartz_staff", new ItemStack(ConfigItems.itemWandRod, 1, 54), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("quartz_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 4)));
      ConfigResearch.recipes.put("WandRodReedStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_reed_staff", new ItemStack(ConfigItems.itemWandRod, 1, 55), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("reed_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 5)));
      ConfigResearch.recipes.put("WandRodBlazeStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_blaze_staff", new ItemStack(ConfigItems.itemWandRod, 1, 56), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("blaze_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 6)));
      ConfigResearch.recipes.put("WandRodBoneStaff", IArcaneRecipe.addArcaneCraftingRecipe("ROD_bone_staff", new ItemStack(ConfigItems.itemWandRod, 1, 57), (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("bone_staff").getCraftCost()), "  S", " G ", "G  ", 'S', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'G', new ItemStack(ConfigItems.itemWandRod, 1, 7)));
      ConfigResearch.recipes.put("FocusFire", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSFIRE", new ItemStack(ConfigItems.itemFocusFire), (new AspectList<>()).addAll(
              Aspects.FIRE, 20).addAll(Aspects.ENTROPY, 10), "CQC", "Q#Q", "CQC", '#', Items.fire_charge, 'Q', Items.quartz, 'C', new ItemStack(ConfigItems.itemShard, 1, 1)));
      ConfigResearch.recipes.put("FocusFrost", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSFROST", new ItemStack(ConfigItems.itemFocusFrost), (new AspectList<>()).addAll(
              Aspects.WATER, 10).addAll(Aspects.ORDER, 10).addAll(Aspects.ENTROPY, 10), "CQC", "Q#Q", "CQC", '#', Items.diamond, 'Q', Items.quartz, 'C', new ItemStack(ConfigItems.itemShard, 1, 2)));
      ConfigResearch.recipes.put("FocusShock", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSSHOCK", new ItemStack(ConfigItems.itemFocusShock), (new AspectList<>()).addAll(
              Aspects.AIR, 10).addAll(Aspects.ORDER, 10).addAll(Aspects.ENTROPY, 10), "CQC", "Q#Q", "CQC", '#', Items.potato, 'Q', Items.quartz, 'C', new ItemStack(ConfigItems.itemShard, 1, 0)));
      ConfigResearch.recipes.put("FocusTrade", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSTRADE", new ItemStack(ConfigItems.itemFocusTrade), (new AspectList<>()).addAll(
              Aspects.ORDER, 15).addAll(Aspects.ENTROPY, 15).addAll(Aspects.EARTH, 10), "CQE", "Q#Q", "CQE", '#', new ItemStack(QUICK_SILVER), 'Q', Items.quartz, 'C', new ItemStack(ConfigItems.itemShard, 1, 6), 'E', new ItemStack(ConfigItems.itemShard, 1, 6)));
      ConfigResearch.recipes.put("FocusExcavation", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSEXCAVATION", new ItemStack(ConfigItems.itemFocusExcavation), (new AspectList<>()).addAll(
              Aspects.EARTH, 20).addAll(Aspects.ENTROPY, 5).addAll(Aspects.ORDER, 5), "CQC", "Q#Q", "CQC", '#', "gemEmerald", 'Q', Items.quartz, 'C', new ItemStack(ConfigItems.itemShard, 1, 3)));
      ConfigResearch.recipes.put("FocusPrimal", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSPRIMAL", new ItemStack(ConfigItems.itemFocusPrimal), (new AspectList<>()).addAll(
              Aspects.EARTH, 25).addAll(Aspects.ENTROPY, 25).addAll(Aspects.ORDER, 25).addAll(Aspects.AIR, 25).addAll(
              Aspects.FIRE, 25).addAll(Aspects.WATER, 25), "CQC", "Q#Q", "CQC", '#', new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), 'Q', Items.quartz, 'C', Items.diamond));
      ConfigResearch.recipes.put("FocusPouch", IArcaneRecipe.addArcaneCraftingRecipe("FOCUSPOUCH", new ItemStack(ConfigItems.itemFocusPouch), (new AspectList<>()).addAll(
              Aspects.EARTH, 10).addAll(Aspects.ORDER, 10).addAll(Aspects.ENTROPY, 10), "LGL", "LBL", "LLL", 'B', new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2), 'L', Items.leather, 'G', Items.gold_ingot));
      ConfigResearch.recipes.put("Deconstructor", IArcaneRecipe.addArcaneCraftingRecipe("DECONSTRUCTOR", new ItemStack(ConfigBlocks.blockTable, 1, 14), (new AspectList<>()).addAll(
              Aspects.ENTROPY, 20), " S ", "ATP", 'T', new ItemStack(ConfigBlocks.blockTable, 1, 0), 'S', new ItemStack(ThaumcraftItems.THAUMOMETER), 'P', new ItemStack(Items.golden_pickaxe), 'A', new ItemStack(Items.golden_axe)));
      ConfigResearch.recipes.put("ArcaneBoreBase", IArcaneRecipe.addArcaneCraftingRecipe("ARCANEBORE", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 4), (new AspectList<>()).addAll(
              Aspects.AIR, 10).addAll(Aspects.ORDER, 10), "WIW", "IDI", "WIW", 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'I', Items.iron_ingot, 'D', Blocks.dispenser));
      ConfigResearch.recipes.put("EnchantedFabric", IArcaneRecipe.addArcaneCraftingRecipe("ENCHFABRIC", new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), (new AspectList<>()).addAll(
              Aspects.AIR, 1).addAll(Aspects.EARTH, 1).addAll(Aspects.FIRE, 1).addAll(Aspects.WATER, 1).addAll(Aspects.ORDER, 1).addAll(
              Aspects.ENTROPY, 1), " S ", "SCS", " S ", 'S', new ItemStack(Items.string, 1, 32767), 'C', new ItemStack(Blocks.wool, 1, 32767)));
      ConfigResearch.recipes.put("RobeChest", IArcaneRecipe.addArcaneCraftingRecipe("ENCHFABRIC", new ItemStack(ConfigItems.itemChestRobe, 1), (new AspectList<>()).addAll(
              Aspects.AIR, 5), "I I", "III", "III", 'I', new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC)));
      ConfigResearch.recipes.put("RobeLegs", IArcaneRecipe.addArcaneCraftingRecipe("ENCHFABRIC", new ItemStack(ConfigItems.itemLegsRobe, 1), (new AspectList<>()).addAll(
              Aspects.WATER, 5), "III", "I I", "I I", 'I', new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC)));
      ConfigResearch.recipes.put("RobeBoots", IArcaneRecipe.addArcaneCraftingRecipe("ENCHFABRIC", new ItemStack(ConfigItems.itemBootsRobe, 1), (new AspectList<>()).addAll(
              Aspects.EARTH, 3), "I I", "I I", 'I', new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC)));
      GameRegistry.addRecipe(new RecipesRobeArmorDyes());
      ConfigResearch.recipes.put("Goggles", IArcaneRecipe.addArcaneCraftingRecipe("GOGGLES", new ItemStack(ConfigItems.itemGoggles), (new AspectList<>()).addAll(
              Aspects.AIR, 5).addAll(Aspects.FIRE, 5).addAll(Aspects.WATER, 5).addAll(Aspects.EARTH, 5).addAll(Aspects.ENTROPY, 3).addAll(
              Aspects.ORDER, 3), "LGL", "L L", "TGT", 'T', ThaumcraftItems.THAUMOMETER, 'G', Items.gold_ingot, 'L', Items.leather));
      ConfigResearch.recipes.put("HungryChest", IArcaneRecipe.addArcaneCraftingRecipe("HUNGRYCHEST", new ItemStack(ConfigBlocks.blockChestHungry), (new AspectList<>()).addAll(
              Aspects.AIR, 5).addAll(Aspects.ORDER, 3).addAll(Aspects.ENTROPY, 3), "WTW", "W W", "WWW", 'W', "plankWood", 'T', Blocks.trapdoor));
      ConfigResearch.recipes.put("GolemBell", IArcaneRecipe.addArcaneCraftingRecipe("GOLEMBELL", new ItemStack(ConfigItems.itemGolemBell), (new AspectList<>()).addAll(
              Aspects.ORDER, 5), " QQ", " QQ", "S  ", 'S', "stickWood", 'Q', Items.quartz));
      ConfigResearch.recipes.put("CoreBlank", IArcaneRecipe.addArcaneCraftingRecipe("COREGATHER", new ItemStack(ConfigItems.itemGolemCore, 1, 100), (new AspectList<>()).addAll(
              Aspects.ORDER, 5).addAll(Aspects.FIRE, 5), " C ", "CNC", " C ", 'C', Items.brick, 'N', new ItemStack(ThaumcraftItems.NITOR)));
      ConfigResearch.recipes.put("UpgradeAir", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEAIR", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 0), (new AspectList<>()).addAll(
              Aspects.AIR, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 0)));
      ConfigResearch.recipes.put("UpgradeEarth", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEEARTH", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 1), (new AspectList<>()).addAll(
              Aspects.EARTH, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 3)));
      ConfigResearch.recipes.put("UpgradeFire", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEFIRE", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 2), (new AspectList<>()).addAll(
              Aspects.FIRE, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 1)));
      ConfigResearch.recipes.put("UpgradeWater", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEWATER", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 3), (new AspectList<>()).addAll(
              Aspects.WATER, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 2)));
      ConfigResearch.recipes.put("UpgradeOrder", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEORDER", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 4), (new AspectList<>()).addAll(
              Aspects.ORDER, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 4)));
      ConfigResearch.recipes.put("UpgradeEntropy", IArcaneRecipe.addArcaneCraftingRecipe("UPGRADEENTROPY", new ItemStack(ConfigItems.itemGolemUpgrade, 1, 5), (new AspectList<>()).addAll(
              Aspects.ENTROPY, 10), "NNN", "NCN", "NNN", 'N', Items.gold_nugget, 'C', new ItemStack(ConfigItems.itemShard, 1, 5)));
      ConfigResearch.recipes.put("TinyHat", IArcaneRecipe.addArcaneCraftingRecipe("TINYHAT", new ItemStack(ConfigItems.itemGolemDecoration, 1, 0), (new AspectList<>()).addAll(
              Aspects.ORDER, 8).addAll(Aspects.FIRE, 8), " C ", " G ", "CCC", 'C', new ItemStack(Blocks.wool, 1, 15), 'G', Items.gold_ingot));
      ConfigResearch.recipes.put("TinyFez", IArcaneRecipe.addArcaneCraftingRecipe("TINYFEZ", new ItemStack(ConfigItems.itemGolemDecoration, 1, 3), (new AspectList<>()).addAll(
              Aspects.WATER, 4).addAll(Aspects.EARTH, 4), "CCS", "CCS", "  S", 'C', new ItemStack(Blocks.wool, 1, 14), 'S', Items.string));
      ConfigResearch.recipes.put("TinyBowtie", IArcaneRecipe.addArcaneCraftingRecipe("TINYBOWTIE", new ItemStack(ConfigItems.itemGolemDecoration, 1, 2), (new AspectList<>()).addAll(
              Aspects.AIR, 4).addAll(Aspects.ORDER, 4), "CSC", "C C", 'C', new ItemStack(Blocks.wool, 1, 15), 'S', Items.string));
      ConfigResearch.recipes.put("TinyGlasses", IArcaneRecipe.addArcaneCraftingRecipe("TINYGLASSES", new ItemStack(ConfigItems.itemGolemDecoration, 1, 1), (new AspectList<>()).addAll(
              Aspects.AIR, 4).addAll(Aspects.WATER, 4), "GIG", 'G', Blocks.glass, 'I', Items.iron_ingot));
      ConfigResearch.recipes.put("TinyDart", IArcaneRecipe.addArcaneCraftingRecipe("TINYDART", new ItemStack(ConfigItems.itemGolemDecoration, 1, 4), (new AspectList<>()).addAll(
              Aspects.AIR, 4).addAll(Aspects.FIRE, 4), "AIA", "ADA", "AIA", 'I', Items.iron_ingot, 'D', Blocks.dispenser, 'A', Items.arrow));
      ConfigResearch.recipes.put("TinyVisor", IArcaneRecipe.addArcaneCraftingRecipe("TINYVISOR", new ItemStack(ConfigItems.itemGolemDecoration, 1, 5), (new AspectList<>()).addAll(
              Aspects.EARTH, 4).addAll(Aspects.WATER, 4), "IHI", 'I', Items.iron_ingot, 'H', new ItemStack(Items.iron_helmet, 1, 32767)));
      ConfigResearch.recipes.put("TinyArmor", IArcaneRecipe.addArcaneCraftingRecipe("TINYARMOR", new ItemStack(ConfigItems.itemGolemDecoration, 1, 6), (new AspectList<>()).addAll(
              Aspects.EARTH, 8), "I I", "IAI", 'I', Items.iron_ingot, 'A', new ItemStack(Items.iron_chestplate, 1, 32767)));
      ConfigResearch.recipes.put("TinyHammer", IArcaneRecipe.addArcaneCraftingRecipe("TINYHAMMER", new ItemStack(ConfigItems.itemGolemDecoration, 1, 7), (new AspectList<>()).addAll(
              Aspects.EARTH, 4).addAll(Aspects.FIRE, 4), "III", "III", " I ", 'I', Items.iron_ingot));
      ConfigResearch.recipes.put("Filter", IArcaneRecipe.addArcaneCraftingRecipe("DISTILESSENTIA", new ItemStack(ThaumcraftItems.VIS_FILTER, 2), (new AspectList<>()).addAll(
              Aspects.ORDER, 5).addAll(Aspects.WATER, 5), "GWG", 'G', Items.gold_ingot, 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7)));
      ConfigResearch.recipes.put("AlchemyFurnace", IArcaneRecipe.addArcaneCraftingRecipe("DISTILESSENTIA", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 0), (new AspectList<>()).addAll(
              Aspects.FIRE, 5).addAll(Aspects.WATER, 5), "SCS", "SFS", "SSS", 'C', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0), 'F', Blocks.furnace, 'S', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6)));
      ConfigResearch.recipes.put("Alembic", IArcaneRecipe.addArcaneCraftingRecipe("DISTILESSENTIA", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), (new AspectList<>()).addAll(
              Aspects.AIR, 5).addAll(Aspects.WATER, 5), "FIG", "IBI", "I I", 'I', Items.iron_ingot, 'B', Items.bucket, 'G', Items.gold_ingot, 'F', new ItemStack(ThaumcraftItems.VIS_FILTER), 'L', new ItemStack(ConfigBlocks.blockMagicalLeaves, 1, 1)));
      ConfigResearch.recipes.put("Bellows", IArcaneRecipe.addArcaneCraftingRecipe("BELLOWS", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 0), (new AspectList<>()).addAll(
              Aspects.AIR, 10).addAll(Aspects.ORDER, 5), "WW ", "LCI", "WW ", 'W', "plankWood", 'C', new ItemStack(ConfigItems.itemShard, 1, 0), 'I', Items.iron_ingot, 'L', Items.leather));
      ConfigResearch.recipes.put("Tube", IArcaneRecipe.addArcaneCraftingRecipe("TUBES", new ItemStack(ConfigBlocks.blockTube, 8, 0), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5), " Q ", "IGI", " B ", 'I', Items.iron_ingot, 'B', Items.gold_nugget, 'G', Blocks.glass, 'Q', new ItemStack(ConfigItems.itemNugget, 1, 5)));
      ConfigResearch.recipes.put("Resonator", IArcaneRecipe.addArcaneCraftingRecipe("TUBES", new ItemStack(ConfigItems.itemResonator), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.AIR, 5), "I I", "INI", " S ", 'I', Items.iron_ingot, 'N', Items.quartz, 'S', "stickWood"));
      ConfigResearch.recipes.put("TubeValve", IArcaneRecipe.addShapelessArcaneCraftingRecipe("TUBES", new ItemStack(ConfigBlocks.blockTube, 1, 1), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5), new ItemStack(ConfigBlocks.blockTube, 1, 0), new ItemStack(Blocks.lever)));
      ConfigResearch.recipes.put("TubeFilter", IArcaneRecipe.addShapelessArcaneCraftingRecipe("TUBEFILTER", new ItemStack(ConfigBlocks.blockTube, 1, 3), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 16), new ItemStack(ConfigBlocks.blockTube, 1, 0), new ItemStack(ThaumcraftItems.VIS_FILTER)));
      ConfigResearch.recipes.put("TubeRestrict", IArcaneRecipe.addShapelessArcaneCraftingRecipe("TUBEFILTER", new ItemStack(ConfigBlocks.blockTube, 1, 5), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.EARTH, 16), new ItemStack(ConfigBlocks.blockTube, 1, 0), "stone"));
      ConfigResearch.recipes.put("TubeOneway", IArcaneRecipe.addShapelessArcaneCraftingRecipe("TUBEFILTER", new ItemStack(ConfigBlocks.blockTube, 1, 6), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 8).addAll(Aspects.ENTROPY, 8), new ItemStack(ConfigBlocks.blockTube, 1, 0), "dyeBlue"));
      ConfigResearch.recipes.put("TubeBuffer", IArcaneRecipe.addArcaneCraftingRecipe("CENTRIFUGE", new ItemStack(ConfigBlocks.blockTube, 1, 4), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5), "PVP", "T T", "PRP", 'T', new ItemStack(ConfigBlocks.blockTube, 1, 0), 'V', new ItemStack(ConfigBlocks.blockTube, 1, 1), 'R', new ItemStack(ConfigBlocks.blockTube, 1, 5), 'P', new ItemStack(ConfigItems.itemEssence, 1, 0)));
      ConfigResearch.recipes.put("AlchemicalConstruct", IArcaneRecipe.addArcaneCraftingRecipe("DISTILESSENTIA", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5), "VTF", "TWT", "FTV", 'W', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), 'V', new ItemStack(ConfigBlocks.blockTube, 1, 1), 'T', new ItemStack(ConfigBlocks.blockTube, 1, 0), 'F', new ItemStack(ThaumcraftItems.VIS_FILTER)));
      ConfigResearch.recipes.put("AdvAlchemyConstruct", IArcaneRecipe.addArcaneCraftingRecipe("ADVALCHEMYFURNACE", new ItemStack(ConfigBlocks.blockMetalDevice, 4, 3), (new AspectList<>()).addAll(
              Aspects.WATER, 10).addAll(Aspects.ORDER, 30).addAll(Aspects.EARTH, 10), "VAV", "APA", "VAV", 'A', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9), 'V', new ItemStack(ThaumcraftItems.VOID_INGOT,1), 'P', new ItemStack(ThaumcraftItems.PRIME_PEARL)));
      ConfigResearch.recipes.put("Centrifuge", IArcaneRecipe.addArcaneCraftingRecipe("CENTRIFUGE", new ItemStack(ConfigBlocks.blockTube, 1, 2), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.ORDER, 5).addAll(Aspects.ENTROPY, 5), " T ", "ACP", " T ", 'T', new ItemStack(ConfigBlocks.blockTube, 1, 0), 'P', new ItemStack(Blocks.piston), 'A', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1), 'C', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9)));
      ConfigResearch.recipes.put("EssentiaCrystalizer", IArcaneRecipe.addArcaneCraftingRecipe("ESSENTIACRYSTAL", new ItemStack(ConfigBlocks.blockTube, 1, 7), (new AspectList<>()).addAll(
              Aspects.WATER, 5).addAll(Aspects.EARTH, 15).addAll(Aspects.ORDER, 5), "IDI", "QCQ", "WTW", 'T', new ItemStack(ConfigBlocks.blockTube, 1, 0), 'D', new ItemStack(Blocks.dispenser), 'Q', new ItemStack(ConfigItems.itemShard, 1, 6), 'I', "ingotIron", 'W', "plankWood", 'C', new ItemStack(ConfigBlocks.blockMetalDevice, 1, 9)));
      ConfigResearch.recipes.put("MnemonicMatrix", IArcaneRecipe.addArcaneCraftingRecipe("THAUMATORIUM", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 12), (new AspectList<>()).addAll(
              Aspects.FIRE, 5).addAll(Aspects.WATER, 5).addAll(Aspects.ORDER, 5), "IAI", "ABA", "IAI", 'B', new ItemStack(ConfigItems.itemZombieBrain), 'A', new ItemStack(ThaumcraftItems.AMBER_GEM), 'I', new ItemStack(Items.iron_ingot)));
   }

   private static void initializeInfusionEnchantmentRecipes() {
      ConfigResearch.recipes.put("InfEnchRepair", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.enchantmentsList[ThaumcraftApi.enchantRepair], 4, (new AspectList<>()).addAll(
              Aspects.MAGIC, 8).addAll(Aspects.CRAFT, 10).addAll(Aspects.ORDER, 10), new ItemStack[]{new ItemStack(Blocks.anvil), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnchHaste", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.enchantmentsList[ThaumcraftApi.enchantHaste], 3, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.TRAVEL, 8).addAll(Aspects.FLIGHT, 8), new ItemStack[]{new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch0", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.protection, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.ARMOR, 8), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch1", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.fireProtection, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.ARMOR, 4).addAll(Aspects.FIRE, 4), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(Items.magma_cream), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch2", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.blastProtection, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.ARMOR, 4).addAll(Aspects.ENTROPY, 4), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(Items.gunpowder), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch3", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.projectileProtection, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.ARMOR, 4).addAll(Aspects.FLIGHT, 4), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(Items.arrow), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch4", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.featherFalling, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.AIR, 4).addAll(Aspects.FLIGHT, 4), new ItemStack[]{new ItemStack(Items.feather), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch5", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.respiration, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.AIR, 8).addAll(Aspects.WATER, 8), new ItemStack[]{new ItemStack(Items.reeds), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch6", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.aquaAffinity, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.MOTION, 8).addAll(Aspects.WATER, 8), new ItemStack[]{new ItemStack(Items.reeds), new ItemStack(Items.slime_ball), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch7", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.thorns, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 8).addAll(Aspects.PLANT, 8), new ItemStack[]{new ItemStack(Blocks.deadbush), new ItemStack(Items.quartz), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch8", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.sharpness, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 8), new ItemStack[]{new ItemStack(Items.iron_sword), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch9", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.smite, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 4).addAll(Aspects.UNDEAD, 4), new ItemStack[]{new ItemStack(Items.iron_sword), new ItemStack(Items.glowstone_dust), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch10", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.baneOfArthropods, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 4).addAll(Aspects.BEAST, 4), new ItemStack[]{new ItemStack(Items.iron_sword), new ItemStack(ThaumcraftItems.AMBER_GEM), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch11", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.knockback, 1, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 3).addAll(Aspects.MOTION, 3), new ItemStack[]{new ItemStack(Blocks.piston), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch12", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.fireAspect, 3, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 4).addAll(Aspects.FIRE, 8), new ItemStack[]{new ItemStack(Items.iron_sword), new ItemStack(Items.blaze_powder), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch13", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.looting, 3, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 4).addAll(Aspects.GREED, 8), new ItemStack[]{new ItemStack(Items.iron_sword), new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch14", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.efficiency, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.TOOL, 4).addAll(Aspects.ORDER, 4), new ItemStack[]{new ItemStack(Items.iron_pickaxe), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch15", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.silkTouch, 5, (new AspectList<>()).addAll(
              Aspects.MAGIC, 16).addAll(Aspects.TOOL, 16).addAll(Aspects.ORDER, 16).addAll(Aspects.HARVEST, 16).addAll(
              Aspects.MINE, 16), new ItemStack[]{new ItemStack(Items.iron_pickaxe), new ItemStack(Blocks.web), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch16", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.unbreaking, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.TOOL, 4).addAll(Aspects.ORDER, 8), new ItemStack[]{new ItemStack(Items.iron_pickaxe), new ItemStack(Blocks.obsidian), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch17", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.fortune, 3, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.TOOL, 4).addAll(Aspects.GREED, 8), new ItemStack[]{new ItemStack(Items.iron_pickaxe), new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch18", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.power, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 8), new ItemStack[]{new ItemStack(Items.bow), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch19", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.punch, 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 3).addAll(Aspects.MOTION, 3), new ItemStack[]{new ItemStack(Blocks.piston), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch20", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.flame, 3, (new AspectList<>()).addAll(
              Aspects.MAGIC, 4).addAll(Aspects.WEAPON, 4).addAll(Aspects.FIRE, 8), new ItemStack[]{new ItemStack(Items.bow), new ItemStack(Items.blaze_powder), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("InfEnch21", InfusionRecipe.addInfusionEnchantmentRecipe("INFUSIONENCHANTMENT", Enchantment.infinity, 5, (new AspectList<>()).addAll(
              Aspects.MAGIC, 8).addAll(Aspects.WEAPON, 16).addAll(Aspects.VOID, 16).addAll(Aspects.EXCHANGE, 16), new ItemStack[]{new ItemStack(Items.bow), new ItemStack(Items.arrow), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
   }

   private static void initializeInfusionRecipes() {
      if (Config.foundSilverIngot) {
         ConfigResearch.recipes.put("WandCapSilver", InfusionRecipe.addInfusionCraftingRecipe("CAP_silver", new ItemStack(ConfigItems.itemWandCap, 1, 4), 4, (new AspectList<>()).addAll(
                 Aspects.ENERGY, WandCap.caps.get("silver").getCraftCost() * 2).addAll(Aspects.AURA, WandCap.caps.get("silver").getCraftCost()), new ItemStack(ConfigItems.itemWandCap, 1, 5), new ItemStack[]{new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      }

      ConfigResearch.recipes.put("WandCapThaumium", InfusionRecipe.addInfusionCraftingRecipe("CAP_thaumium", new ItemStack(ConfigItems.itemWandCap, 1, 2), 5, (new AspectList<>()).addAll(
              Aspects.ENERGY, WandCap.caps.get("thaumium").getCraftCost() * 2).addAll(Aspects.AURA, WandCap.caps.get("thaumium").getCraftCost()), new ItemStack(ConfigItems.itemWandCap, 1, 6), new ItemStack[]{new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("WandCapVoid", InfusionRecipe.addInfusionCraftingRecipe("CAP_void", new ItemStack(ConfigItems.itemWandCap, 1, 7), 8, (new AspectList<>()).addAll(
              Aspects.ENERGY, WandCap.caps.get("void").getCraftCost() * 2).addAll(Aspects.VOID, WandCap.caps.get("void").getCraftCost() * 2).addAll(
              Aspects.ELDRITCH, WandCap.caps.get("void").getCraftCost() * 2).addAll(Aspects.AURA, WandCap.caps.get("void").getCraftCost() * 2), new ItemStack(ConfigItems.itemWandCap, 1, 8), new ItemStack[]{new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1)}));
      ConfigResearch.recipes.put("WandRodObsidian", InfusionRecipe.addInfusionCraftingRecipe("ROD_obsidian", new ItemStack(ConfigItems.itemWandRod, 1, 1), 3, (new AspectList<>()).addAll(
              Aspects.EARTH, WandRod.rods.get("obsidian").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("obsidian").getCraftCost()).addAll(
              Aspects.DARKNESS, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Blocks.obsidian), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 3)}));
      ConfigResearch.recipes.put("WandRodIce", InfusionRecipe.addInfusionCraftingRecipe("ROD_ice", new ItemStack(ConfigItems.itemWandRod, 1, 3), 3, (new AspectList<>()).addAll(
              Aspects.WATER, WandRod.rods.get("ice").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("ice").getCraftCost()).addAll(
              Aspects.COLD, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Blocks.ice), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 2)}));
      ConfigResearch.recipes.put("WandRodQuartz", InfusionRecipe.addInfusionCraftingRecipe("ROD_quartz", new ItemStack(ConfigItems.itemWandRod, 1, 4), 3, (new AspectList<>()).addAll(
              Aspects.ORDER, WandRod.rods.get("quartz").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("quartz").getCraftCost()).addAll(
              Aspects.CRYSTAL, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Blocks.quartz_block), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 4)}));
      ConfigResearch.recipes.put("WandRodReed", InfusionRecipe.addInfusionCraftingRecipe("ROD_reed", new ItemStack(ConfigItems.itemWandRod, 1, 5), 3, (new AspectList<>()).addAll(
              Aspects.AIR, WandRod.rods.get("reed").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("reed").getCraftCost()).addAll(
              Aspects.MOTION, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Items.reeds), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 0)}));
      ConfigResearch.recipes.put("WandRodBlaze", InfusionRecipe.addInfusionCraftingRecipe("ROD_blaze", new ItemStack(ConfigItems.itemWandRod, 1, 6), 3, (new AspectList<>()).addAll(
              Aspects.FIRE, WandRod.rods.get("blaze").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("blaze").getCraftCost()).addAll(
              Aspects.BEAST, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Items.blaze_rod), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 1)}));
      ConfigResearch.recipes.put("WandRodBone", InfusionRecipe.addInfusionCraftingRecipe("ROD_bone", new ItemStack(ConfigItems.itemWandRod, 1, 7), 3, (new AspectList<>()).addAll(
              Aspects.ENTROPY, WandRod.rods.get("bone").getCraftCost() * 2).addAll(Aspects.MAGIC, WandRod.rods.get("bone").getCraftCost()).addAll(
              Aspects.UNDEAD, WandRod.rods.get("blaze").getCraftCost()), new ItemStack(Items.bone), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      ConfigResearch.recipes.put("WandRodSilverwood", InfusionRecipe.addInfusionCraftingRecipe("ROD_silverwood", new ItemStack(ConfigItems.itemWandRod, 1, 2), 5, (new AspectList<>()).addAll(
              Aspects.AIR, WandRod.rods.get("silverwood").getCraftCost()).addAll(Aspects.FIRE, WandRod.rods.get("silverwood").getCraftCost()).addAll(
              Aspects.WATER, WandRod.rods.get("silverwood").getCraftCost()).addAll(Aspects.EARTH, WandRod.rods.get("silverwood").getCraftCost()).addAll(
              Aspects.ORDER, WandRod.rods.get("silverwood").getCraftCost()).addAll(Aspects.ENTROPY, WandRod.rods.get("silverwood").getCraftCost()).addAll(
              Aspects.MAGIC, WandRod.rods.get("silverwood").getCraftCost()), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      ConfigResearch.recipes.put("WandRodPrimalStaff", InfusionRecipe.addInfusionCraftingRecipe("ROD_primal_staff", new ItemStack(ConfigItems.itemWandRod, 1, 100), 8, (new AspectList<>()).addAll(
              Aspects.AIR, WandRod.rods.get("primal_staff").getCraftCost()).addAll(Aspects.FIRE, WandRod.rods.get("primal_staff").getCraftCost()).addAll(
              Aspects.WATER, WandRod.rods.get("primal_staff").getCraftCost()).addAll(Aspects.EARTH, WandRod.rods.get("primal_staff").getCraftCost()).addAll(
              Aspects.ORDER, WandRod.rods.get("primal_staff").getCraftCost()).addAll(Aspects.ENTROPY, WandRod.rods.get("primal_staff").getCraftCost()).addAll(
              Aspects.MAGIC, WandRod.rods.get("primal_staff").getCraftCost() * 2), new ItemStack(ConfigItems.itemWandRod, 1, 2), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigItems.itemWandRod, 1, 1), new ItemStack(ConfigItems.itemWandRod, 1, 3), new ItemStack(ConfigItems.itemWandRod, 1, 4), new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigItems.itemWandRod, 1, 5), new ItemStack(ConfigItems.itemWandRod, 1, 6), new ItemStack(ConfigItems.itemWandRod, 1, 7)}));
      ConfigResearch.recipes.put("FocusHellbat", InfusionRecipe.addInfusionCraftingRecipe("FOCUSHELLBAT", new ItemStack(ConfigItems.itemFocusHellbat), 3, (new AspectList<>()).addAll(
              Aspects.FIRE, 25).addAll(Aspects.AIR, 15).addAll(Aspects.BEAST, 15).addAll(Aspects.ENTROPY, 25), new ItemStack(Items.magma_cream), new ItemStack[]{new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      ConfigResearch.recipes.put("FocusPortableHole", InfusionRecipe.addInfusionCraftingRecipe("FOCUSPORTABLEHOLE", new ItemStack(ConfigItems.itemFocusPortableHole), 3, (new AspectList<>()).addAll(
              Aspects.TRAVEL, 25).addAll(Aspects.ELDRITCH, 10).addAll(Aspects.EXCHANGE, 10).addAll(Aspects.ENTROPY, 25), new ItemStack(Items.ender_pearl), new ItemStack[]{new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      ConfigResearch.recipes.put("FocusWarding", InfusionRecipe.addInfusionCraftingRecipe("FOCUSWARDING", new ItemStack(ConfigItems.itemFocusWarding), 4, (new AspectList<>()).addAll(
              Aspects.EARTH, 25).addAll(Aspects.ARMOR, 25).addAll(Aspects.ORDER, 25).addAll(Aspects.MIND, 10), new ItemStack(Items.nether_star), new ItemStack[]{new ItemStack(QUICK_SILVER), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(QUICK_SILVER), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.quartz), new ItemStack(ConfigItems.itemShard, 1, 4)}));
      ConfigResearch.recipes.put("WandPed", InfusionRecipe.addInfusionCraftingRecipe("WANDPED", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 5), 3, (new AspectList<>()).addAll(
              Aspects.AURA, 10).addAll(
              Aspects.MAGIC, 15).addAll(Aspects.EXCHANGE, 15), new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1), new ItemStack[]{new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(Items.diamond)}));
      ConfigResearch.recipes.put("WandPedFocus", InfusionRecipe.addInfusionCraftingRecipe("WANDPEDFOC", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 8), 4, (new AspectList<>()).addAll(
              Aspects.ORDER, 10).addAll(Aspects.MAGIC, 15).addAll(Aspects.EXCHANGE, 10), new ItemStack(Items.comparator), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ThaumcraftItems.VIS_FILTER), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ThaumcraftItems.VIS_FILTER), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ThaumcraftItems.VIS_FILTER), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ThaumcraftItems.VIS_FILTER)}));
      ConfigResearch.recipes.put("NodeStabilizerAdv", InfusionRecipe.addInfusionCraftingRecipe("NODESTABILIZERADV", new ItemStack(ConfigBlocks.blockStoneDevice, 1, 10), 10, (new AspectList<>()).addAll(
              Aspects.AURA, 32).addAll(Aspects.MAGIC, 16).addAll(
              Aspects.ORDER, 16).addAll(Aspects.ENERGY, 16), new ItemStack(ConfigBlocks.blockStoneDevice, 1, 9), new ItemStack[]{new ItemStack(ThaumcraftItems.NITOR), new ItemStack(Blocks.redstone_block), new ItemStack(ThaumcraftItems.ALUMENTUM), new ItemStack(Blocks.redstone_block), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(Blocks.redstone_block), new ItemStack(ThaumcraftItems.ALUMENTUM), new ItemStack(Blocks.redstone_block)}));
      ConfigResearch.recipes.put("JarBrain", InfusionRecipe.addInfusionCraftingRecipe("JARBRAIN", new ItemStack(ConfigBlocks.blockJar, 1, 1), 4, (new AspectList<>()).addAll(
              Aspects.MIND, 10).addAll(Aspects.SENSES, 10).addAll(Aspects.UNDEAD, 20), new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack[]{new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.spider_eye), new ItemStack(Items.water_bucket), new ItemStack(Items.spider_eye)}));
      ConfigResearch.recipes.put("AdvancedGolem", InfusionRecipe.addInfusionCraftingRecipe("ADVANCEDGOLEM", new Object[]{"advanced", new NBTTagByte((byte)1)}, 3, (new AspectList<>()).addAll(
              Aspects.MIND, 8).addAll(Aspects.SENSES, 8).addAll(
              Aspects.LIFE, 8), new ItemStack(ConfigItems.itemGolemPlacer, 1, 32767), new ItemStack[]{new ItemStack(Items.redstone), new ItemStack(Items.glowstone_dust), new ItemStack(Items.gunpowder), new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack(ConfigItems.itemZombieBrain)}));
      ConfigResearch.recipes.put("HoverHarness", InfusionRecipe.addInfusionCraftingRecipe("HOVERHARNESS", new ItemStack(ConfigItems.itemHoverHarness), 6, (new AspectList<>()).addAll(
              Aspects.FLIGHT, 32).addAll(Aspects.ENERGY, 32).addAll(Aspects.MECHANISM, 32).addAll(Aspects.TRAVEL, 16), new ItemStack(Items.leather_chestplate), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(Items.comparator), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot)}));
      ConfigResearch.recipes.put("HoverGirdle", InfusionRecipe.addInfusionCraftingRecipe("HOVERGIRDLE", new ItemStack(ConfigItems.itemGirdleHover), 8, (new AspectList<>()).addAll(
              Aspects.FLIGHT, 16).addAll(Aspects.ENERGY, 32).addAll(Aspects.AIR, 32).addAll(Aspects.TRAVEL, 16), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.feather), new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.feather), new ItemStack(Items.gold_ingot)}));
      ConfigResearch.recipes.put("VisAmulet", InfusionRecipe.addInfusionCraftingRecipe("VISAMULET", new ItemStack(ConfigItems.itemAmuletVis, 1, 1), 6, (new AspectList<>()).addAll(
              Aspects.AURA, 24).addAll(
              Aspects.ENERGY, 64).addAll(Aspects.MAGIC, 64).addAll(Aspects.VOID, 24), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 0), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new ItemStack(ConfigBlocks.blockCrystal, 1, 6)}));
      ConfigResearch.recipes.put("RunicAmulet", InfusionRecipe.addInfusionCraftingRecipe("RUNICARMOR", new ItemStack(ConfigItems.itemAmuletRunic, 1, 0), 4, (new AspectList<>()).addAll(
              Aspects.ARMOR, 20).addAll(Aspects.MAGIC, 35).addAll(
              Aspects.ENERGY, 35), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 0), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ThaumcraftItems.AMBER_GEM), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ConfigItems.itemInkwell)}));
      ConfigResearch.recipes.put("RunicAmuletEmergency", InfusionRecipe.addInfusionCraftingRecipe("RUNICEMERGENCY", new ItemStack(ConfigItems.itemAmuletRunic, 1, 1), 7, (new AspectList<>()).addAll(
              Aspects.ARMOR, 20).addAll(Aspects.MAGIC, 35).addAll(
              Aspects.EARTH, 32).addAll(Aspects.VOID, 32), new ItemStack(ConfigItems.itemAmuletRunic, 1, 0), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.potionitem, 1, 8233), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 3)}));
      ConfigResearch.recipes.put("RunicRing", InfusionRecipe.addInfusionCraftingRecipe("RUNICARMOR", new ItemStack(ConfigItems.itemRingRunic, 1, 1), 3, (new AspectList<>()).addAll(
              Aspects.ARMOR, 10).addAll(Aspects.MAGIC, 25).addAll(
              Aspects.ENERGY, 25), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 1), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ThaumcraftItems.AMBER_GEM), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ConfigItems.itemInkwell)}));
      ConfigResearch.recipes.put("RunicRingCharged", InfusionRecipe.addInfusionCraftingRecipe("RUNICCHARGED", new ItemStack(ConfigItems.itemRingRunic, 1, 2), 6, (new AspectList<>()).addAll(
              Aspects.ARMOR, 16).addAll(Aspects.MAGIC, 16).addAll(
              Aspects.ENERGY, 64), new ItemStack(ConfigItems.itemRingRunic, 1, 1), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.potionitem, 1, 8226), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 1)}));
      ConfigResearch.recipes.put("RunicRingHealing", InfusionRecipe.addInfusionCraftingRecipe("RUNICHEALING", new ItemStack(ConfigItems.itemRingRunic, 1, 3), 6, (new AspectList<>()).addAll(
              Aspects.ARMOR, 16).addAll(Aspects.MAGIC, 16).addAll(
              Aspects.WATER, 32).addAll(Aspects.HEAL, 32), new ItemStack(ConfigItems.itemRingRunic, 1, 1), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Items.potionitem, 1, 8257), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 2)}));
      ConfigResearch.recipes.put("RunicGirdle", InfusionRecipe.addInfusionCraftingRecipe("RUNICARMOR", new ItemStack(ConfigItems.itemGirdleRunic, 1, 0), 4, (new AspectList<>()).addAll(
              Aspects.ARMOR, 30).addAll(Aspects.MAGIC, 50).addAll(
              Aspects.ENERGY, 50), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ThaumcraftItems.AMBER_GEM), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ConfigItems.itemInkwell)}));
      ConfigResearch.recipes.put("RunicGirdleKinetic", InfusionRecipe.addInfusionCraftingRecipe("RUNICKINETIC", new ItemStack(ConfigItems.itemGirdleRunic, 1, 1), 7, (new AspectList<>()).addAll(
              Aspects.ARMOR, 33).addAll(Aspects.MAGIC, 55).addAll(
              Aspects.AIR, 64), new ItemStack(ConfigItems.itemGirdleRunic, 1, 0), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.potionitem, 1, 16428), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0)}));
      ConfigResearch.recipes.put("RunicGirdleKinetic_2", InfusionRecipe.addInfusionCraftingRecipe("RUNICKINETIC", new ItemStack(ConfigItems.itemGirdleRunic, 1, 1), 7, (new AspectList<>()).addAll(
              Aspects.ARMOR, 33).addAll(Aspects.MAGIC, 55).addAll(
              Aspects.AIR, 64), new ItemStack(ConfigItems.itemGirdleRunic, 1, 0), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.potionitem, 1, 24620), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0)}));
      InfusionRunicAugmentRecipe ra = new InfusionRunicAugmentRecipe();
      ThaumcraftApi.getCraftingRecipes().add(ra);
      if (Config.allowMirrors) {
         ConfigResearch.recipes.put("Mirror", InfusionRecipe.addInfusionCraftingRecipe("MIRROR", new ItemStack(ConfigBlocks.blockMirror, 1, 0), 1, (new AspectList<>()).addAll(
                 Aspects.TRAVEL, 8).addAll(Aspects.DARKNESS, 8).addAll(Aspects.EXCHANGE, 8), new ItemStack(ThaumcraftItems.MIRRORED_GLASS, 1), new ItemStack[]{new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.ender_pearl)}));
         ConfigResearch.recipes.put("MirrorHand", InfusionRecipe.addInfusionCraftingRecipe("MIRRORHAND", new ItemStack(ConfigItems.itemHandMirror), 5, (new AspectList<>()).addAll(
                 Aspects.TOOL, 16).addAll(
                 Aspects.TRAVEL, 16), new ItemStack(ConfigBlocks.blockMirror, 1, 0), new ItemStack[]{new ItemStack(Items.stick), new ItemStack(Items.compass), new ItemStack(Items.map)}));
         ConfigResearch.recipes.put("MirrorEssentia", InfusionRecipe.addInfusionCraftingRecipe("MIRRORESSENTIA", new ItemStack(ConfigBlocks.blockMirror, 1, 6), 2, (new AspectList<>()).addAll(
                 Aspects.TRAVEL, 8).addAll(Aspects.WATER, 8).addAll(Aspects.EXCHANGE, 8), new ItemStack(ThaumcraftItems.MIRRORED_GLASS, 1), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.ender_pearl)}));
      }

      ConfigResearch.recipes.put("ElementalAxe", InfusionRecipe.addInfusionCraftingRecipe("ELEMENTALAXE", new ItemStack(ConfigItems.itemAxeElemental), 1, (new AspectList<>()).addAll(
              Aspects.WATER, 16).addAll(Aspects.TREE, 8), new ItemStack(ConfigItems.itemAxeThaumium), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(Items.diamond), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)}));
      ConfigResearch.recipes.put("ElementalPick", InfusionRecipe.addInfusionCraftingRecipe("ELEMENTALPICK", new ItemStack(ConfigItems.itemPickElemental), 1, (new AspectList<>()).addAll(
              Aspects.FIRE, 8).addAll(Aspects.MINE, 8).addAll(Aspects.SENSES, 8), new ItemStack(ConfigItems.itemPickThaumium), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.diamond), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)}));
      ConfigResearch.recipes.put("ElementalSword", InfusionRecipe.addInfusionCraftingRecipe("ELEMENTALSWORD", new ItemStack(ConfigItems.itemSwordElemental), 1, (new AspectList<>()).addAll(
              Aspects.AIR, 8).addAll(Aspects.MOTION, 8).addAll(Aspects.ENERGY, 8), new ItemStack(ConfigItems.itemSwordThaumium), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(Items.diamond), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)}));
      ConfigResearch.recipes.put("ElementalShovel", InfusionRecipe.addInfusionCraftingRecipe("ELEMENTALSHOVEL", new ItemStack(ConfigItems.itemShovelElemental), 1, (new AspectList<>()).addAll(
              Aspects.EARTH, 16).addAll(Aspects.CRAFT, 8), new ItemStack(ConfigItems.itemShovelThaumium), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.diamond), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)}));
      ConfigResearch.recipes.put("ElementalHoe", InfusionRecipe.addInfusionCraftingRecipe("ELEMENTALHOE", new ItemStack(ConfigItems.itemHoeElemental), 1, (new AspectList<>()).addAll(
              Aspects.HARVEST, 8).addAll(Aspects.PLANT, 8).addAll(
              Aspects.EARTH, 8), new ItemStack(ConfigItems.itemHoeThaumium), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(ConfigItems.itemShard, 1, 5), new ItemStack(Items.diamond), new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)}));
      ConfigResearch.recipes.put("BootsTraveller", InfusionRecipe.addInfusionCraftingRecipe("BOOTSTRAVELLER", new ItemStack(ConfigItems.itemBootsTraveller), 1, (new AspectList<>()).addAll(
              Aspects.FLIGHT, 25).addAll(Aspects.TRAVEL, 25), new ItemStack(Items.leather_boots), new ItemStack[]{new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(Items.feather), new ItemStack(Items.fish, 1, 32767)}));
      ConfigResearch.recipes.put("CoreAlchemy", InfusionRecipe.addInfusionCraftingRecipe("COREALCHEMY", new ItemStack(ConfigItems.itemGolemCore, 1, 6), 2, (new AspectList<>()).addAll(
              Aspects.MAGIC, 15).addAll(
              Aspects.WATER, 15).addAll(Aspects.MOTION, 15), new ItemStack(ConfigItems.itemGolemCore, 1, 5), new ItemStack[]{new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack(Items.potionitem), new ItemStack(Items.potionitem), new ItemStack(Items.potionitem)}));
      ConfigResearch.recipes.put("CoreSorting", InfusionRecipe.addInfusionCraftingRecipe("CORESORTING", new ItemStack(ConfigItems.itemGolemCore, 1, 10), 3, (new AspectList<>()).addAll(
              Aspects.VOID, 16).addAll(Aspects.EXCHANGE, 16).addAll(Aspects.HUNGER, 16).addAll(Aspects.GREED, 16), new ItemStack(ConfigItems.itemZombieBrain), new ItemStack[]{new ItemStack(ConfigItems.itemGolemCore, 1, 0), new ItemStack(Items.comparator), new ItemStack(ConfigItems.itemGolemCore, 1, 1), new ItemStack(Items.paper)}));
      ConfigResearch.recipes.put("CoreLumber", InfusionRecipe.addInfusionCraftingRecipe("CORELUMBER", new ItemStack(ConfigItems.itemGolemCore, 1, 7), 2, (new AspectList<>()).addAll(
              Aspects.TOOL, 16).addAll(Aspects.TREE, 16).addAll(Aspects.HARVEST, 16), new ItemStack(ConfigItems.itemGolemCore, 1, 3), new ItemStack[]{new ItemStack(ConfigItems.itemAxeElemental), new ItemStack(Items.iron_axe), new ItemStack(Items.iron_axe), new ItemStack(Items.iron_axe)}));
      ConfigResearch.recipes.put("CoreFishing", InfusionRecipe.addInfusionCraftingRecipe("COREFISHING", new ItemStack(ConfigItems.itemGolemCore, 1, 11), 3, (new AspectList<>()).addAll(
              Aspects.WATER, 16).addAll(Aspects.HARVEST, 16).addAll(Aspects.BEAST, 16), new ItemStack(ConfigItems.itemGolemCore, 1, 3), new ItemStack[]{new ItemStack(Items.fishing_rod), new ItemStack(Items.fish, 1, 0), new ItemStack(Items.fish, 1, 3), new ItemStack(Items.fish, 1, 1)}));
      ConfigResearch.recipes.put("CoreUse", InfusionRecipe.addInfusionCraftingRecipe("COREUSE", new ItemStack(ConfigItems.itemGolemCore, 1, 8), 3, (new AspectList<>()).addAll(
              Aspects.TOOL, 20).addAll(Aspects.MECHANISM, 20).addAll(Aspects.MAN, 20), new ItemStack(ConfigItems.itemGolemCore, 1, 1), new ItemStack[]{new ItemStack(Items.comparator), new ItemStack(Items.flint_and_steel), new ItemStack(Items.shears), new ItemStack(Blocks.lever)}));
      ConfigResearch.recipes.put("ArcaneBore", InfusionRecipe.addInfusionCraftingRecipe("ARCANEBORE", new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 5), 4, (new AspectList<>()).addAll(
              Aspects.ENERGY, 16).addAll(Aspects.MINE, 32).addAll(Aspects.MECHANISM, 32).addAll(Aspects.VOID, 16).addAll(
              Aspects.MOTION, 16), new ItemStack(Blocks.piston), new ItemStack[]{new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond_pickaxe), new ItemStack(Items.diamond_shovel), new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 3)}));
      ConfigResearch.recipes.put("TravelTrunk", InfusionRecipe.addInfusionCraftingRecipe("TRAVELTRUNK", new ItemStack(ConfigItems.itemTrunkSpawner), 3, (new AspectList<>()).addAll(
              Aspects.MOTION, 4).addAll(Aspects.SOUL, 4).addAll(Aspects.TRAVEL, 4).addAll(Aspects.VOID, 16), new ItemStack(ConfigBlocks.blockChestHungry), new ItemStack[]{new ItemStack(Items.iron_ingot), new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6), new ItemStack(ConfigItems.itemGolemPlacer, 1, 1), new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6)}));
      ConfigResearch.recipes.put("LampGrowth", InfusionRecipe.addInfusionCraftingRecipe("LAMPGROWTH", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 8), 4, (new AspectList<>()).addAll(
              Aspects.PLANT, 16).addAll(
              Aspects.LIGHT, 8).addAll(Aspects.LIFE, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 7), new ItemStack[]{new ItemStack(Items.gold_ingot), new ItemStack(Items.dye, 1, 15), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(Items.gold_ingot), new ItemStack(Items.dye, 1, 15), new ItemStack(ConfigItems.itemShard, 1, 3)}));
      ConfigResearch.recipes.put("LampFertility", InfusionRecipe.addInfusionCraftingRecipe("LAMPFERTILITY", new ItemStack(ConfigBlocks.blockMetalDevice, 1, 13), 4, (new AspectList<>()).addAll(
              Aspects.BEAST, 16).addAll(
              Aspects.LIFE, 16).addAll(Aspects.LIGHT, 8), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 7), new ItemStack[]{new ItemStack(Items.gold_ingot), new ItemStack(Items.wheat), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(Items.gold_ingot), new ItemStack(Items.carrot), new ItemStack(ConfigItems.itemShard, 1, 1)}));
      ConfigResearch.recipes.put("ThaumiumFortressHelm", InfusionRecipe.addInfusionCraftingRecipe("ARMORFORTRESS", new ItemStack(ConfigItems.itemHelmetFortress), 3, (new AspectList<>()).addAll(
              Aspects.METAL, 24).addAll(Aspects.ARMOR, 16).addAll(Aspects.MAGIC, 16), new ItemStack(ConfigItems.itemHelmetThaumium), new ItemStack[]{new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.emerald)}));
      ConfigResearch.recipes.put("ThaumiumFortressChest", InfusionRecipe.addInfusionCraftingRecipe("ARMORFORTRESS", new ItemStack(ConfigItems.itemChestFortress), 3, (new AspectList<>()).addAll(
              Aspects.METAL, 24).addAll(Aspects.ARMOR, 24).addAll(Aspects.MAGIC, 16), new ItemStack(ConfigItems.itemChestThaumium), new ItemStack[]{new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather)}));
      ConfigResearch.recipes.put("ThaumiumFortressLegs", InfusionRecipe.addInfusionCraftingRecipe("ARMORFORTRESS", new ItemStack(ConfigItems.itemLegsFortress), 3, (new AspectList<>()).addAll(
              Aspects.METAL, 24).addAll(Aspects.ARMOR, 20).addAll(Aspects.MAGIC, 16), new ItemStack(ConfigItems.itemLegsThaumium), new ItemStack[]{new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(Items.gold_ingot), new ItemStack(Items.leather)}));
      ConfigResearch.recipes.put("VoidRobeHelm", InfusionRecipe.addInfusionCraftingRecipe("ARMORVOIDFORTRESS", new ItemStack(ConfigItems.itemHelmetVoidRobe), 6, (new AspectList<>()).addAll(
              Aspects.METAL, 16).addAll(Aspects.SENSES, 16).addAll(Aspects.ARMOR, 16).addAll(Aspects.CLOTH, 16).addAll(
              Aspects.MAGIC, 16).addAll(
              Aspects.ELDRITCH, 16).addAll(Aspects.VOID, 16), new ItemStack(ConfigItems.itemHelmetVoid), new ItemStack[]{new ItemStack(ConfigItems.itemGoggles), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC)}));
      ConfigResearch.recipes.put("VoidRobeChest", InfusionRecipe.addInfusionCraftingRecipe("ARMORVOIDFORTRESS", new ItemStack(ConfigItems.itemChestVoidRobe), 6, (new AspectList<>()).addAll(
              Aspects.METAL, 24).addAll(Aspects.ARMOR, 24).addAll(Aspects.CLOTH, 24).addAll(Aspects.MAGIC, 16).addAll(
              Aspects.ELDRITCH, 16).addAll(Aspects.VOID, 24), new ItemStack(ConfigItems.itemChestVoid), new ItemStack[]{new ItemStack(ConfigItems.itemChestRobe), new ItemStack(ThaumcraftItems.VOID_INGOT,1), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(Items.leather)}));
      ConfigResearch.recipes.put("VoidRobeLegs", InfusionRecipe.addInfusionCraftingRecipe("ARMORVOIDFORTRESS", new ItemStack(ConfigItems.itemLegsVoidRobe), 6, (new AspectList<>()).addAll(
              Aspects.METAL, 20).addAll(Aspects.ARMOR, 20).addAll(Aspects.CLOTH, 20).addAll(Aspects.MAGIC, 16).addAll(
              Aspects.ELDRITCH, 16).addAll(Aspects.VOID, 20), new ItemStack(ConfigItems.itemLegsVoid), new ItemStack[]{new ItemStack(ConfigItems.itemLegsRobe), new ItemStack(ThaumcraftItems.VOID_INGOT,1), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), new ItemStack(ThaumcraftItems.ENCHANTED_FABRIC), new ItemStack(Items.leather)}));
      GameRegistry.addRecipe(new RecipesVoidRobeArmorDyes());
      ConfigResearch.recipes.put("HelmGoggles", InfusionRecipe.addInfusionCraftingRecipe("HELMGOGGLES", new Object[]{"goggles", new NBTTagByte((byte)1)}, 5, (new AspectList<>()).addAll(
              Aspects.SENSES, 32).addAll(Aspects.AURA, 16).addAll(Aspects.ARMOR, 16), new ItemStack(ConfigItems.itemHelmetFortress, 1, 32767), new ItemStack[]{new ItemStack(Items.slime_ball), new ItemStack(ConfigItems.itemGoggles, 1, 32767)}));
      ConfigResearch.recipes.put("MaskGrinningDevil", InfusionRecipe.addInfusionCraftingRecipe("MASKGRINNINGDEVIL", new Object[]{"mask", new NBTTagInt(0)}, 8, (new AspectList<>()).addAll(
              Aspects.MIND, 64).addAll(
              Aspects.HEAL, 64).addAll(Aspects.ARMOR, 16), new ItemStack(ConfigItems.itemHelmetFortress, 1, 32767), new ItemStack[]{new ItemStack(Items.dye, 1, 0), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2), new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.iron_ingot)}));
      ConfigResearch.recipes.put("MaskAngryGhost", InfusionRecipe.addInfusionCraftingRecipe("MASKANGRYGHOST", new Object[]{"mask", new NBTTagInt(1)}, 8, (new AspectList<>()).addAll(
              Aspects.ENTROPY, 64).addAll(Aspects.DEATH, 64).addAll(Aspects.ARMOR, 16), new ItemStack(ConfigItems.itemHelmetFortress, 1, 32767), new ItemStack[]{new ItemStack(Items.dye, 1, 15), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.poisonous_potato), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.iron_ingot)}));
      ConfigResearch.recipes.put("MaskSippingFiend", InfusionRecipe.addInfusionCraftingRecipe("MASKSIPPINGFIEND", new Object[]{"mask", new NBTTagInt(2)}, 8, (new AspectList<>()).addAll(
              Aspects.UNDEAD, 64).addAll(
              Aspects.LIFE, 64).addAll(Aspects.ARMOR, 16), new ItemStack(ConfigItems.itemHelmetFortress, 1, 32767), new ItemStack[]{new ItemStack(Items.dye, 1, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather), new ItemStack(Items.ghast_tear), new ItemStack(Items.milk_bucket), new ItemStack(Items.iron_ingot)}));
      ConfigResearch.recipes.put("SanityCheck", InfusionRecipe.addInfusionCraftingRecipe("SANITYCHECK", new ItemStack(ConfigItems.itemSanityChecker), 4, (new AspectList<>()).addAll(
              Aspects.MIND, 24).addAll(Aspects.SENSES, 24).addAll(
              Aspects.ELDRITCH, 8), new ItemStack(ThaumcraftItems.THAUMOMETER), new ItemStack[]{new ItemStack(ThaumcraftItems.MIRRORED_GLASS, 1), new ItemStack(ConfigItems.itemZombieBrain), new ItemStack(Items.diamond)}));
      ConfigResearch.recipes.put("EssentiaReservoir", InfusionRecipe.addInfusionCraftingRecipe("ESSENTIARESERVOIR", new ItemStack(ConfigBlocks.blockEssentiaReservoir), 6, (new AspectList<>()).addAll(
              Aspects.WATER, 8).addAll(Aspects.VOID, 8).addAll(Aspects.MAGIC, 8).addAll(Aspects.EXCHANGE, 8), new ItemStack(ConfigBlocks.blockTube, 1, 4), new ItemStack[]{new ItemStack(ThaumcraftItems.VOID_INGOT,1), new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack(ThaumcraftItems.VOID_INGOT,1), new ItemStack(ConfigBlocks.blockJar, 1, 0), new ItemStack(ConfigBlocks.blockJar, 1, 0)}));
      ConfigResearch.recipes.put("SinStone", InfusionRecipe.addInfusionCraftingRecipe("SINSTONE", new ItemStack(ConfigItems.itemCompassStone), 5, (new AspectList<>()).addAll(
              Aspects.SENSES, 8).addAll(
              Aspects.DARKNESS, 8).addAll(Aspects.ELDRITCH, 8).addAll(Aspects.AURA, 8), new ItemStack(Items.flint), new ItemStack[]{new ItemStack(ThaumcraftItems.NITOR), new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      ConfigResearch.recipes.put("PrimalCrusher", InfusionRecipe.addInfusionCraftingRecipe("PRIMALCRUSHER", new ItemStack(ConfigItems.itemPrimalCrusher), 6, (new AspectList<>()).addAll(
              Aspects.MINE, 24).addAll(Aspects.TOOL, 24).addAll(
              Aspects.ENTROPY, 16).addAll(Aspects.VOID, 16).addAll(Aspects.WEAPON, 16).addAll(Aspects.ELDRITCH, 16).addAll(
              Aspects.GREED, 16), new ItemStack(ThaumcraftItems.PRIME_PEARL), new ItemStack[]{new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigItems.itemPickVoid, 1, 32767), new ItemStack(ConfigItems.itemShovelVoid, 1, 32767), new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1), new ItemStack(ConfigItems.itemPickElemental, 1, 32767), new ItemStack(ConfigItems.itemShovelElemental, 1, 32767)}));
      ConfigResearch.recipes.put("EldritchEye", InfusionRecipe.addInfusionCraftingRecipe("OCULUS", new ItemStack(ThaumcraftItems.ELDRITCH_EYE), 5, (new AspectList<>()).addAll(
              Aspects.ELDRITCH, 64).addAll(Aspects.VOID, 16).addAll(Aspects.DARKNESS, 16).addAll(Aspects.TRAVEL, 16), new ItemStack(Items.ender_eye), new ItemStack[]{new ItemStack(ThaumcraftItems.VOID_SEED, 1), new ItemStack(Items.gold_ingot)}));
   }

   private static void initializeNormalRecipes() {
      CraftingManager.getInstance().addRecipe(new ItemStack(ConfigItems.itemNugget, 9, 0), "#", '#', Items.iron_ingot);
      CraftingManager.getInstance().addRecipe(new ItemStack(ConfigItems.itemNugget, 9, 6), "#", '#', new ItemStack(ThaumcraftItems.THAUMIUM_INGOT));
      CraftingManager.getInstance().addRecipe(new ItemStack(ConfigItems.itemNugget, 9, 7), "#", '#', new ItemStack(ThaumcraftItems.VOID_INGOT,1));
      oreDictRecipe(new ItemStack(Items.iron_ingot), new Object[]{"###", "###", "###", '#', new ItemStack(ConfigItems.itemNugget, 1, 0)});
      oreDictRecipe(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT), new Object[]{"###", "###", "###", '#', new ItemStack(ConfigItems.itemNugget, 1, 6)});
      oreDictRecipe(new ItemStack(QUICK_SILVER), new Object[]{"###", "###", "###", '#', new ItemStack(ConfigItems.itemNugget, 1, 5)});
      CraftingManager.getInstance().addRecipe(new ItemStack(ConfigItems.itemNugget, 9, 5), "#", '#', new ItemStack(QUICK_SILVER));
      oreDictRecipe(new ItemStack(ThaumcraftItems.VOID_INGOT,1), new Object[]{"###", "###", "###", '#', new ItemStack(ConfigItems.itemNugget, 1, 7)});
      ConfigResearch.recipes.put("MundaneAmulet", oreDictRecipe(new ItemStack(ConfigItems.itemBaubleBlanks, 1, 0), new Object[]{" S ", "S S", " I ", 'S', new ItemStack(Items.string), 'I', new ItemStack(Items.gold_ingot)}));
      ConfigResearch.recipes.put("MundaneRing", oreDictRecipe(new ItemStack(ConfigItems.itemBaubleBlanks, 1, 1), new Object[]{" N ", "N N", " N ", 'N', new ItemStack(Items.gold_nugget)}));
      ConfigResearch.recipes.put("MundaneBelt", oreDictRecipe(new ItemStack(ConfigItems.itemBaubleBlanks, 1, 2), new Object[]{" L ", "L L", " I ", 'L', new ItemStack(Items.leather), 'I', new ItemStack(Items.gold_ingot)}));
      shapelessOreDictRecipe(new ItemStack(ConfigItems.itemTripleMeatTreat), new Object[]{Items.sugar, ConfigItems.itemNuggetBeef, ConfigItems.itemNuggetChicken, ConfigItems.itemNuggetPork});
      shapelessOreDictRecipe(new ItemStack(ConfigItems.itemTripleMeatTreat), new Object[]{Items.sugar, ConfigItems.itemNuggetBeef, ConfigItems.itemNuggetChicken, ConfigItems.itemNuggetFish});
      shapelessOreDictRecipe(new ItemStack(ConfigItems.itemTripleMeatTreat), new Object[]{Items.sugar, ConfigItems.itemNuggetBeef, ConfigItems.itemNuggetFish, ConfigItems.itemNuggetPork});
      shapelessOreDictRecipe(new ItemStack(ConfigItems.itemTripleMeatTreat), new Object[]{Items.sugar, ConfigItems.itemNuggetFish, ConfigItems.itemNuggetChicken, ConfigItems.itemNuggetPork});
      CraftingManager.getInstance().addRecipe(new ItemStack(QUICK_SILVER), "#", '#', new ItemStack(ConfigBlocks.blockCustomPlant, 1, 2));
      CraftingManager.getInstance().addRecipe(new ItemStack(Items.blaze_powder), "#", '#', new ItemStack(ConfigBlocks.blockCustomPlant, 1, 3));
      ConfigResearch.recipes.put("JarLabel", shapelessOreDictRecipe(new ItemStack(ThaumcraftItems.JAR_LABEL, 4), new Object[]{"dyeBlack", Items.slime_ball, Items.paper, Items.paper, Items.paper, Items.paper}));
      int count = 0;

      for(Aspect aspect : Aspects.ALL_ASPECTS.values()) {
         ItemStack essence = new ItemStack(ConfigItems.itemEssence, 1, 1);
         ((IEssentiaContainerItem) essence.getItem()).setAspects(essence, (new AspectList<>()).addAll(aspect, 8));
         ItemStack output = new ItemStack(ThaumcraftItems.JAR_LABEL, 1);
         ((IEssentiaContainerItem) output.getItem()).setAspects(output, (new AspectList<>()).addAll(aspect, 0));
         ConfigResearch.recipes.put("JarLabel" + count, shapelessNBTOreRecipe(output, new Object[]{new ItemStack(ThaumcraftItems.JAR_LABEL, 1), essence}));
         ++count;
      }

      ItemStack input = new ItemStack(ThaumcraftItems.JAR_LABEL, 1);
      ((IEssentiaContainerItem) input.getItem()).setAspects(input, (new AspectList<>()).addAll(Aspects.WATER, 1));
      ConfigResearch.recipes.put("JarLabelNull", shapelessOreDictRecipe(new ItemStack(ThaumcraftItems.JAR_LABEL, 1), new Object[]{input}));
      ConfigResearch.recipes.put("WandBasic", oreDictRecipe(basicWand, new Object[]{"  I", " S ", "I  ", 'I', new ItemStack(ConfigItems.itemWandCap, 1, 0), 'S', "stickWood"}));
      ConfigResearch.recipes.put("WandCapIron", oreDictRecipe(new ItemStack(ConfigItems.itemWandCap, 1, 0), new Object[]{"NNN", "N N", 'N', "nuggetIron"}));
      ConfigResearch.recipes.put("KnowFrag", GameRegistry.addShapedRecipe(new ItemStack(ConfigItems.itemResearchNotes, 1, 42), "KKK", "KKK", "KKK", 'K', new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
      ConfigResearch.recipes.put("PlankGreatwood", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockWoodenDevice, 4, 6), "W", 'W', new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)));
      ConfigResearch.recipes.put("PlankSilverwood", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockWoodenDevice, 4, 7), "W", 'W', new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1)));
      GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockStairsGreatwood, 4, 0), "K  ", "KK ", "KKK", 'K', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6));
      GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockStairsSilverwood, 4, 0), "K  ", "KK ", "KKK", 'K', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7));
      GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockSlabWood, 6, 0), "KKK", 'K', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 6));
      GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockSlabWood, 6, 1), "KKK", 'K', new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 7));
      ConfigResearch.recipes.put("BlockFlesh", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockTaint, 1, 2), "KKK", "KKK", "KKK", 'K', Items.rotten_flesh));
      ConfigResearch.recipes.put("BlockThaumium", oreDictRecipe(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 4), new Object[]{"KKK", "KKK", "KKK", 'K', "ingotThaumium"}));
      GameRegistry.addShapedRecipe(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT, 9), "K", 'K', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 4));
      ConfigResearch.recipes.put("BlockTallow", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 5), "KKK", "KKK", "KKK", 'K', new ItemStack(ThaumcraftItems.MAGIC_TALLOW)));
      GameRegistry.addShapedRecipe(new ItemStack(ThaumcraftItems.MAGIC_TALLOW, 9), "K", 'K', new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 5));

      for(int a = 0; a < 6; ++a) {
         ConfigResearch.recipes.put("Clusters" + a, shapelessOreDictRecipe(new ItemStack(ConfigBlocks.blockCrystal, 1, a), new Object[]{new ItemStack(ConfigItems.itemShard, 1, a), new ItemStack(ConfigItems.itemShard, 1, a), new ItemStack(ConfigItems.itemShard, 1, a), new ItemStack(ConfigItems.itemShard, 1, a), new ItemStack(ConfigItems.itemShard, 1, a), new ItemStack(ConfigItems.itemShard, 1, a)}));
      }

      ConfigResearch.recipes.put("Clusters6", shapelessOreDictRecipe(new ItemStack(ConfigBlocks.blockCrystal, 1, 6), new Object[]{new ItemStack(ConfigItems.itemShard, 1, 0), new ItemStack(ConfigItems.itemShard, 1, 1), new ItemStack(ConfigItems.itemShard, 1, 2), new ItemStack(ConfigItems.itemShard, 1, 3), new ItemStack(ConfigItems.itemShard, 1, 4), new ItemStack(ConfigItems.itemShard, 1, 5)}));
      GameRegistry.addRecipe(new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 0), "##", "##", '#', new ItemStack(ThaumcraftItems.AMBER_GEM));
      GameRegistry.addRecipe(new ItemStack(ConfigBlocks.blockCosmeticOpaque, 4, 1), "##", "##", '#', new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 0));
      GameRegistry.addRecipe(new ItemStack(ConfigBlocks.blockCosmeticSolid, 4, 1), "##", "##", '#', Blocks.obsidian);
      GameRegistry.addRecipe(new ItemStack(ThaumcraftItems.AMBER_GEM,4), "#", '#', new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 0));
      GameRegistry.addRecipe(new ItemStack(ThaumcraftItems.AMBER_GEM,4), "#", '#', new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 1));
      ConfigResearch.recipes.put("Grate", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockMetalDevice, 1, 5), "#", "T", '#', new ItemStack(Blocks.iron_bars), 'T', new ItemStack(Blocks.trapdoor)));
      ConfigResearch.recipes.put("Phial", GameRegistry.addShapedRecipe(new ItemStack(ConfigItems.itemEssence, 8, 0), " C ", "G G", " G ", 'G', Blocks.glass, 'C', Items.clay_ball));
      ConfigResearch.recipes.put("Table", oreDictRecipe(new ItemStack(ConfigBlocks.blockTable, 1, 0), new Object[]{"SSS", "W W", 'S', "slabWood", 'W', "plankWood"}));
      ConfigResearch.recipes.put("Scribe1", shapelessOreDictRecipe(new ItemStack(ConfigItems.itemInkwell), new Object[]{new ItemStack(ConfigItems.itemEssence, 1, 0), Items.feather, "dyeBlack"}));
      ConfigResearch.recipes.put("Scribe2", shapelessOreDictRecipe(new ItemStack(ConfigItems.itemInkwell), new Object[]{Items.glass_bottle, Items.feather, "dyeBlack"}));
      ConfigResearch.recipes.put("Scribe3", shapelessOreDictRecipe(new ItemStack(ConfigItems.itemInkwell), new Object[]{new ItemStack(ConfigItems.itemInkwell, 1, 32767), "dyeBlack"}));
      ConfigResearch.recipes.put("Thaumometer", oreDictRecipe(new ItemStack(ThaumcraftItems.THAUMOMETER), new Object[]{" 1 ", "IGI", " 1 ", 'I', Items.gold_ingot, 'G', Blocks.glass, '1', new ItemStack(ConfigItems.itemShard, 1, 32767)}));
      ConfigResearch.recipes.put("ThaumiumHelm", oreDictRecipe(new ItemStack(ConfigItems.itemHelmetThaumium, 1), new Object[]{"III", "I I", 'I', "ingotThaumium"}));
      ConfigResearch.recipes.put("ThaumiumChest", oreDictRecipe(new ItemStack(ConfigItems.itemChestThaumium, 1), new Object[]{"I I", "III", "III", 'I', "ingotThaumium"}));
      ConfigResearch.recipes.put("ThaumiumLegs", oreDictRecipe(new ItemStack(ConfigItems.itemLegsThaumium, 1), new Object[]{"III", "I I", "I I", 'I', "ingotThaumium"}));
      ConfigResearch.recipes.put("ThaumiumBoots", oreDictRecipe(new ItemStack(ConfigItems.itemBootsThaumium, 1), new Object[]{"I I", "I I", 'I', "ingotThaumium"}));
      ConfigResearch.recipes.put("ThaumiumShovel", oreDictRecipe(new ItemStack(ConfigItems.itemShovelThaumium, 1), new Object[]{"I", "S", "S", 'I', "ingotThaumium", 'S', "stickWood"}));
      ConfigResearch.recipes.put("ThaumiumPick", oreDictRecipe(new ItemStack(ConfigItems.itemPickThaumium, 1), new Object[]{"III", " S ", " S ", 'I', "ingotThaumium", 'S', "stickWood"}));
      ConfigResearch.recipes.put("ThaumiumAxe", oreDictRecipe(new ItemStack(ConfigItems.itemAxeThaumium, 1), new Object[]{"II", "SI", "S ", 'I', "ingotThaumium", 'S', "stickWood"}));
      ConfigResearch.recipes.put("ThaumiumHoe", oreDictRecipe(new ItemStack(ConfigItems.itemHoeThaumium, 1), new Object[]{"II", "S ", "S ", 'I', "ingotThaumium", 'S', "stickWood"}));
      ConfigResearch.recipes.put("ThaumiumSword", oreDictRecipe(new ItemStack(ConfigItems.itemSwordThaumium, 1), new Object[]{"I", "I", "S", 'I', "ingotThaumium", 'S', "stickWood"}));
      ConfigResearch.recipes.put("VoidHelm", oreDictRecipe(new ItemStack(ConfigItems.itemHelmetVoid, 1), new Object[]{"III", "I I", 'I', "ingotVoid"}));
      ConfigResearch.recipes.put("VoidChest", oreDictRecipe(new ItemStack(ConfigItems.itemChestVoid, 1), new Object[]{"I I", "III", "III", 'I', "ingotVoid"}));
      ConfigResearch.recipes.put("VoidLegs", oreDictRecipe(new ItemStack(ConfigItems.itemLegsVoid, 1), new Object[]{"III", "I I", "I I", 'I', "ingotVoid"}));
      ConfigResearch.recipes.put("VoidBoots", oreDictRecipe(new ItemStack(ConfigItems.itemBootsVoid, 1), new Object[]{"I I", "I I", 'I', "ingotVoid"}));
      ConfigResearch.recipes.put("VoidShovel", oreDictRecipe(new ItemStack(ConfigItems.itemShovelVoid, 1), new Object[]{"I", "S", "S", 'I', "ingotVoid", 'S', "stickWood"}));
      ConfigResearch.recipes.put("VoidPick", oreDictRecipe(new ItemStack(ConfigItems.itemPickVoid, 1), new Object[]{"III", " S ", " S ", 'I', "ingotVoid", 'S', "stickWood"}));
      ConfigResearch.recipes.put("VoidAxe", oreDictRecipe(new ItemStack(ConfigItems.itemAxeVoid, 1), new Object[]{"II", "SI", "S ", 'I', "ingotVoid", 'S', "stickWood"}));
      ConfigResearch.recipes.put("VoidHoe", oreDictRecipe(new ItemStack(ConfigItems.itemHoeVoid, 1), new Object[]{"II", "S ", "S ", 'I', "ingotVoid", 'S', "stickWood"}));
      ConfigResearch.recipes.put("VoidSword", oreDictRecipe(new ItemStack(ConfigItems.itemSwordVoid, 1), new Object[]{"I", "I", "S", 'I', "ingotVoid", 'S', "stickWood"}));
      ConfigResearch.recipes.put("TallowCandle", GameRegistry.addShapedRecipe(new ItemStack(ConfigBlocks.blockCandle, 3, 0), " S ", " T ", " T ", 'S', Items.string, 'T', new ItemStack(ThaumcraftItems.MAGIC_TALLOW)));

      for(int a = 1; a < 16; ++a) {
         shapelessOreDictRecipe(new ItemStack(ConfigBlocks.blockCandle, 1, a), new Object[]{ConfigAspects.dyes[15 - a], new ItemStack(ConfigBlocks.blockCandle, 1, 0)});
      }

      GameRegistry.addShapelessRecipe(new ItemStack(ConfigBlocks.blockCandle, 1, 0), new ItemStack(Items.dye, 1, 15), new ItemStack(ConfigBlocks.blockCandle, 1, 32767));
   }

   private static void initializeSmelting() {
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigBlocks.blockCustomOre, 1, 0), new ItemStack(QUICK_SILVER), 1.0F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigBlocks.blockCustomOre, 1, 7), new ItemStack(ThaumcraftItems.AMBER_GEM), 1.0F);
      GameRegistry.addSmelting(ConfigBlocks.blockMagicalLog, new ItemStack(Items.coal, 1, 1), 0.5F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigItems.itemNugget, 1, 16), new ItemStack(Items.iron_ingot, 2, 0), 1.0F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigItems.itemNugget, 1, 21), new ItemStack(ThaumcraftItems.QUICK_SILVER,2), 1.0F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigItems.itemNugget, 1, 31), new ItemStack(Items.gold_ingot, 2, 0), 1.0F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ConfigItems.itemShard, 1, 6), new ItemStack(ThaumcraftItems.SALIS_MUNDUS, 1), 1.0F);
      FurnaceRecipes.smelting().func_151394_a(new ItemStack(ThaumcraftItems.GOLD_COIN), new ItemStack(Items.gold_nugget), 0.0F);
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreGold", new ItemStack(Items.gold_nugget, 0, 0));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreIron", new ItemStack(ConfigItems.itemNugget, 0, 0));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreCinnabar", new ItemStack(ConfigItems.itemNugget, 0, 5));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreCopper", new ItemStack(ConfigItems.itemNugget, 0, 1));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreTin", new ItemStack(ConfigItems.itemNugget, 0, 2));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreSilver", new ItemStack(ConfigItems.itemNugget, 0, 3));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus("oreLead", new ItemStack(ConfigItems.itemNugget, 0, 4));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 31), new ItemStack(Items.gold_nugget, 0, 0));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 16), new ItemStack(ConfigItems.itemNugget, 0, 0));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 21), new ItemStack(ConfigItems.itemNugget, 0, 5));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 17), new ItemStack(ConfigItems.itemNugget, 0, 1));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 18), new ItemStack(ConfigItems.itemNugget, 0, 2));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 19), new ItemStack(ConfigItems.itemNugget, 0, 3));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(ConfigItems.itemNugget, 1, 20), new ItemStack(ConfigItems.itemNugget, 0, 4));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(Items.chicken), new ItemStack(ConfigItems.itemNuggetChicken));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(Items.beef), new ItemStack(ConfigItems.itemNuggetBeef));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(Items.porkchop), new ItemStack(ConfigItems.itemNuggetPork));
      InfernalFurnaceLavaBlock.addInfernalFurnaceSmeltingBonus(new ItemStack(Items.fish, 1, 32767), new ItemStack(ConfigItems.itemNuggetFish));
   }

   static IRecipe oreDictRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapedOreRecipe(res, params);
      CraftingManager.getInstance().getRecipeList().add(rec);
      return rec;
   }

   static IRecipe shapelessOreDictRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapelessOreRecipe(res, params);
      CraftingManager.getInstance().getRecipeList().add(rec);
      return rec;
   }

   static IRecipe shapelessNBTOreRecipe(ItemStack res, Object[] params) {
      IRecipe rec = new ShapelessNBTOreRecipe(res, params);
      CraftingManager.getInstance().getRecipeList().add(rec);
      return rec;
   }

   static {
      basicWand = new ItemStack(ConfigItems.WandCastingItem);
   }
}
