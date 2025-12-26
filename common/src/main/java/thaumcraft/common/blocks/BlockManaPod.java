package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.misc.ItemManaBean;
import thaumcraft.common.tiles.TileManaPod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BlockManaPod extends Block {
   public IIcon[] icon = new IIcon[3];
   static HashMap st = new HashMap<>();

   public BlockManaPod() {
      super(Material.plants);
      this.setTickRandomly(true);
      this.blockHardness = 0.5F;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      this.icon[0] = par1IconRegister.registerIcon("thaumcraft:manapod_stem_0");
      this.icon[1] = par1IconRegister.registerIcon("thaumcraft:manapod_stem_1");
      this.icon[2] = par1IconRegister.registerIcon("thaumcraft:manapod_stem_2");
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      float md = (float)(8 - world.getBlockMetadata(x, y, z));
      return super.getBlockHardness(world, x, y, z) / md;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int meta) {
      return meta == 0 ? this.icon[0] : (meta == 1 ? this.icon[1] : this.icon[2]);
   }

   public int getRenderType() {
      return 1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
      this.setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
      return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
   }

   //   public static float W1 = 0.0625F;
   //   public static float W2 = 0.125F;
   //   public static float W3 = 0.1875F;
   //   public static float W4 = 0.25F;
   //   public static float W5 = 0.3125F;
   //   public static float W6 = 0.375F;
   //   public static float W7 = 0.4375F;
   //   public static float W8 = 0.5F;
   //   public static float W9 = 0.5625F;
   //   public static float W10 = 0.625F;
   //   public static float W11 = 0.6875F;
   //   public static float W12 = 0.75F;
   //   public static float W13 = 0.8125F;
   //   public static float W14 = 0.875F;
   //   public static float W15 = 0.9375F;
   public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
      int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
      switch (l) {
         case 0:
            this.setBlockBounds(0.25F, BlockRenderer.W12, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 1:
            this.setBlockBounds(0.25F, BlockRenderer.W10, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 2:
            this.setBlockBounds(0.25F, BlockRenderer.W8, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 3:
            this.setBlockBounds(0.25F, BlockRenderer.W6, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 4:
            this.setBlockBounds(0.25F, BlockRenderer.W5, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 5:
            this.setBlockBounds(0.25F, BlockRenderer.W4, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 6:
            this.setBlockBounds(0.25F, BlockRenderer.W3, 0.25F, 0.75F, 1.0F, 0.75F);
            break;
         case 7:
            this.setBlockBounds(0.25F, BlockRenderer.W2, 0.25F, 0.75F, 1.0F, 0.75F);
      }

   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_) {
      this.setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
      return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
   }

   public void updateTick(Level par1World, int par2, int par3, int par4, Random par5Random) {
      if (!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
      } else if (par1world.getRandom().nextInt(30) == 0) {
         TileEntity tile = par1World.getTileEntity(par2, par3, par4);
         if (tile instanceof TileManaPod) {
            ((TileManaPod)tile).checkGrowth();
         }

         st.remove(new WorldCoordinates(par2, par3, par4, par1World.dimension()));
      }

   }

   public boolean canBlockStay(Level par1World, int par2, int par3, int par4) {
      BiomeGenBase biome = par1World.getBiomeGenForCoords(par2, par4);
      boolean magicBiome = false;
      if (biome != null) {
         magicBiome = BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
      }

      Block i1 = par1World.getBlock(par2, par3 + 1, par4);
      return magicBiome && (i1 == Blocks.log || i1 == Blocks.log2 || i1 == ConfigBlocks.blockMagicalLog);
   }

   public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
      BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
      boolean magicBiome = false;
      if (biome != null) {
         magicBiome = BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
      }

      Block i1 = world.getBlock(x, y + 1, z);
      boolean b = i1 == Blocks.log || i1 == Blocks.log2 || i1 == ConfigBlocks.blockMagicalLog;
      return side == 0 && b && magicBiome;
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   public void onNeighborBlockChange(Level par1World, int par2, int par3, int par4, Block par5) {
      if (!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
      }

   }

   public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileManaPod && ((TileManaPod) tile).aspect != null) {
         st.put(new WorldCoordinates(x, y, z, world.dimension()), ((TileManaPod)tile).aspect);
      }

      super.breakBlock(world, x, y, z, block, meta);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList<ItemStack> dropped = new ArrayList<>();
       if (metadata >= 2) {
           byte b0 = 1;
           if (metadata == 7 && world.getRandom().nextFloat() > 0.33F) {
               b0 = 2;
           }

           Aspect aspect = Aspect.PLANT;
           if (st.containsKey(new WorldCoordinates(x, y, z, world.dimension()))) {
               aspect = (Aspect) st.get(new WorldCoordinates(x, y, z, world.dimension()));
           } else {
               TileEntity tile = world.getTileEntity(x, y, z);
               if (tile instanceof TileManaPod && ((TileManaPod) tile).aspect != null) {
                   aspect = ((TileManaPod) tile).aspect;
               }
           }

           for (int k1 = 0; k1 < b0; ++k1) {
               ItemStack i = new ItemStack(ConfigItems.itemManaBean);
               ((ItemManaBean) i.getItem()).setAspects(i, (new AspectList()).addAll(aspect, 1));
               dropped.add(i);
           }

           st.remove(new WorldCoordinates(x, y, z, world.dimension()));
       }
       return dropped;
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return ConfigItems.itemManaBean;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemById(0);
   }

   public boolean getBlocksMovement(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
      return true;
   }

   public boolean hasTileEntity(int metadata) {
      return true;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return new TileManaPod();
   }
}
