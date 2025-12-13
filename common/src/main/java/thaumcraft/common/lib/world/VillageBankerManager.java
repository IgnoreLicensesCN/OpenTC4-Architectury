package thaumcraft.common.lib.world;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.world.entity.passive.EntityVillager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ThaumcraftItems;

import java.util.List;
import java.util.Random;

public class VillageBankerManager implements VillagerRegistry.IVillageCreationHandler, VillagerRegistry.IVillageTradeHandler {
   public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
      if (villager.getProfession() == ConfigEntities.entBankerId) {
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 20 + random.nextInt(3)), new ItemStack(Items.emerald)));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 2 + random.nextInt(2)), Items.arrow));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 6 + random.nextInt(3)), Item.getItemFromBlock(Blocks.wool)));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 3 + random.nextInt(2)), Items.paper));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 7 + random.nextInt(3)), Items.book));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 16 + random.nextInt(5)), Items.experience_bottle));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 9 + random.nextInt(4)), Item.getItemFromBlock(Blocks.glowstone)));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 2 + random.nextInt(2)), Items.coal));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 22 + random.nextInt(3)), Items.diamond));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 6 + random.nextInt(3)), Items.iron_ingot));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 10 + random.nextInt(3)), new ItemStack(ThaumcraftItems.THAUMIUM_INGOT)));
         recipeList.add(new MerchantRecipe(new ItemStack(ThaumcraftItems.GOLD_COIN, 25 + random.nextInt(8)), Items.saddle));
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
