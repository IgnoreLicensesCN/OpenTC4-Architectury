package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCustomOre extends Block {
   public IIcon[] icon = new IIcon[5];
   private Random rand = new Random();

   public BlockCustomOre() {
      super(Material.rock);
      this.setResistance(5.0F);
      this.setHardness(1.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setTickRandomly(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:cinnibar");
      this.icon[1] = ir.registerIcon("thaumcraft:infusedorestone");
      this.icon[2] = ir.registerIcon("thaumcraft:infusedore");
      this.icon[3] = ir.registerIcon("thaumcraft:amberore");
      this.icon[4] = ir.registerIcon("thaumcraft:frostshard");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      if (par2 == 0) {
         return this.icon[0];
      } else if (par2 == 7) {
         return this.icon[3];
      } else {
         return par2 == 15 ? this.icon[4] : this.icon[1];
      }
   }

   public boolean canSilkHarvest(World world, Player player, int x, int y, int z, int metadata) {
      return true;
   }

   public int damageDropped(int par1) {
      return par1;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 6));
      par3List.add(new ItemStack(par1, 1, 7));
   }

   @SideOnly(Side.CLIENT)
   public boolean addHitEffects(World worldObj, HitResult target, EffectRenderer effectRenderer) {
      int md = worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);
      if (md != 0 && md < 6) {
         UtilsFX.infusedStoneSparkle(worldObj, target.blockX, target.blockY, target.blockZ, md);
      }

      return super.addHitEffects(worldObj, target, effectRenderer);
   }

   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int md, int fortune) {
      ArrayList<ItemStack> ret = new ArrayList<>();
      if (md == 0) {
         ret.add(new ItemStack(ConfigBlocks.blockCustomOre, 1, 0));
      } else if (md == 7) {
         ret.add(new ItemStack(ConfigItems.itemResource, 1 + world.getRandom().nextInt(fortune + 1), 6));
      } else {
         int q = 1 + world.getRandom().nextInt(2 + fortune);

         for(int a = 0; a < q; ++a) {
            ret.add(new ItemStack(ConfigItems.itemShard, 1, md - 1));
         }
      }

      return ret;
   }

   public int getExpDrop(IBlockAccess world, int md, int fortune) {
      if (md != 0 && md != 7) {
         return MathHelper.getRandomIntegerInRange(this.rand, 0, 3);
      } else {
         return md == 7 ? MathHelper.getRandomIntegerInRange(this.rand, 1, 4) : super.getExpDrop(world, md, fortune);
      }
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return true;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockCustomOreRI;
   }
}
