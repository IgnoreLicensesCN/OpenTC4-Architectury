package thaumcraft.common.lib.world;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;

import java.util.List;
import java.util.Random;

public class VillageBankerManager implements VillagerRegistry.IVillageCreationHandler, VillagerRegistry.IVillageTradeHandler {
   public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
      if (villager.getProfession() == ConfigEntities.entBankerId) {
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 20 + random.nextInt(3), 18), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 2 + random.nextInt(2), 18), Items.arrow));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 6 + random.nextInt(3), 18), Item.getItemFromBlock(Blocks.wool)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 3 + random.nextInt(2), 18), Items.paper));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 7 + random.nextInt(3), 18), Items.book));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 16 + random.nextInt(5), 18), Items.experience_bottle));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 9 + random.nextInt(4), 18), Item.getItemFromBlock(Blocks.glowstone)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 2 + random.nextInt(2), 18), Items.coal));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 22 + random.nextInt(3), 18), Items.diamond));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 6 + random.nextInt(3), 18), Items.iron_ingot));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 10 + random.nextInt(3), 18), new ItemStack(ConfigItems.itemResource, 1, 2)));
         recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 25 + random.nextInt(8), 18), Items.saddle));
      }

   }

   public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
      return new StructureVillagePieces.PieceWeight(ComponentBankerHome.class, 25, MathHelper.getRandomIntegerInRange(random, i, 1 + i));
   }

   public Class getComponentClass() {
      return ComponentBankerHome.class;
   }

   public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
      return ComponentBankerHome.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
   }
}
