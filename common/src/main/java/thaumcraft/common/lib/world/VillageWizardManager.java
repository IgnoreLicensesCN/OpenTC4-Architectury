package thaumcraft.common.lib.world;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.world.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.world.item.ItemStack;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;

import java.util.List;
import java.util.Random;

public class VillageWizardManager implements VillagerRegistry.IVillageCreationHandler, VillagerRegistry.IVillageTradeHandler {
   public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
      if (villager.getProfession() == ConfigEntities.entWizardId) {
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 20 + random.nextInt(3)), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 3), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ThaumcraftItems.ALUMENTUM)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 6), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ThaumcraftItems.NITOR)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetChicken, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.book, 4 + random.nextInt(3), 0), new ItemStack(ThaumcraftItems.KNOWLEDGE_FRAGMENT)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetBeef, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemShard, 2 + random.nextInt(2), random.nextInt(6))));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemManaBean, 1 + random.nextInt(2), 0)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemBathSalts, 1, 0)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemRingRunic, 1, 0)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemAmuletVis, 1, 0)));
         recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 5 + random.nextInt(3)), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 3 + random.nextInt(6))));
      }

   }

   public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new StructureVillagePieces.PieceWeight(ComponentWizardTower.class, 15, MathHelper.getRandomIntegerInRange(random, i, 1 + i));
   }

   public Class getComponentClass() {
      return ComponentWizardTower.class;
   }

   public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
      return ComponentWizardTower.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
   }
}
