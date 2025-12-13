package thaumcraft.common.lib.world;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ThaumcraftItems;

import java.util.List;
import java.util.Random;

public class ComponentWizardTower extends StructureVillagePieces.Village {
   private int averageGroundLevel = -1;
   public static final WeightedRandomChestContent[] towerChestContents;

   public ComponentWizardTower() {
   }

   public ComponentWizardTower(StructureVillagePieces.Start par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
   }

   public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random, int par3, int par4, int par5, int par6, int par7) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 5, 12, 5, par6);
      return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(pieces, var8) == null ? new ComponentWizardTower(startPiece, par7, random, var8, par6) : null;
   }

   public boolean addComponentParts(Level world, Random par2Random, StructureBoundingBox bb) {
      if (this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.getAverageGroundLevel(world, bb);
         if (this.averageGroundLevel < 0) {
            return true;
         }

         this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 12 - 1, 0);
      }

      this.fillWithBlocks(world, bb, 2, 1, 2, 4, 11, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bb, 2, 0, 2, 4, 0, 4, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bb, 2, 5, 2, 4, 5, 4, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bb, 2, 10, 2, 4, 10, 4, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bb, 1, 0, 2, 1, 11, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bb, 2, 0, 1, 4, 11, 1, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bb, 5, 0, 2, 5, 11, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bb, 2, 0, 5, 4, 11, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bb, 2, 0, 5, 4, 11, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 0, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 0, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 0, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 0, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 5, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 5, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 5, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 5, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 10, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 1, 10, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 10, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 3, 5, 10, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 7, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 8, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 7, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 8, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 2, 5, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 3, 5, bb);
      int var4 = this.getMetadataWithOffset(Blocks.ladder, 4);

      for(int var5 = 1; var5 <= 9; ++var5) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, var4, 4, var5, 3, bb);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.trapdoor, var4, 4, 10, 3, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.glowstone, 0, 3, 5, 3, bb);
      ChestGenHooks cgh = new ChestGenHooks("towerChestContents", towerChestContents, 4, 9);
      this.generateStructureChestContents(world, bb, par2Random, 2, 6, 2, cgh.getItems(par2Random), cgh.getCount(par2Random));
      this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 3, 1, 1, bb);
      this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 3, 2, 1, bb);
      this.placeDoorAtCurrentPosition(world, bb, par2Random, 3, 1, 1, this.getMetadataWithOffset(Blocks.wooden_door, 1));
      if (this.getBlockAtCurrentPosition(world, 3, 0, 0, bb).isAir(world, 3, 0, 0) && !this.getBlockAtCurrentPosition(world, 3, -1, 0, bb).isAir(world, 3, -1, 0)) {
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, this.getMetadataWithOffset(Blocks.stone_stairs, 3), 3, 0, 0, bb);
      }

      for(int var5 = 0; var5 < 12; ++var5) {
         for(int var6 = 0; var6 < 5; ++var6) {
            this.clearCurrentPositionBlocksUpwards(world, var6, 12, var5, bb);
            this.func_151554_b(world, Blocks.cobblestone, 0, var6, -1, var5, bb);
         }
      }

      this.spawnVillagers(world, bb, 7, 1, 1, 1);
      return true;
   }

   protected int getVillagerType(int par1) {
      return ConfigEntities.entWizardId;
   }

   static {
      towerChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.glowstone_dust, 0, 1, 3, 3), new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 3, 5), new WeightedRandomChestContent(Items.fire_charge, 0, 1, 1, 5), new WeightedRandomChestContent(Items.skull, 0, 1, 1, 3), new WeightedRandomChestContent(ThaumcraftItems.KNOWLEDGE_FRAGMENT, 1, 3, 20), new WeightedRandomChestContent(ThaumcraftItems.ALUMENTUM,1, 1, 5), new WeightedRandomChestContent(ThaumcraftItems.NITOR,1, 1, 5), new WeightedRandomChestContent(ThaumcraftItems.THAUMIUM_INGOT,1, 2, 5), new WeightedRandomChestContent(ConfigItems.itemThaumonomicon, 0, 1, 1, 20)};
   }
}
