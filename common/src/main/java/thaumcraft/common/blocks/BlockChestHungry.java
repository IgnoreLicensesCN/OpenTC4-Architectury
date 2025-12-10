package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileChestHungry;

import java.util.Random;

public class BlockChestHungry extends BlockContainer {
   private Random random = new Random();
   public IIcon icon;

   public BlockChestHungry() {
      super(Material.wood);
      this.setHardness(2.5F);
      this.setStepSound(soundTypeWood);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:woodplain");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return this.icon;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockChestHungryRI;
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof IInventory ? Container.calcRedstoneFromInventory((IInventory)te) : 0;
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack is) {
      int var6 = par1World.getBlockMetadata(par2, par3, par4) & 3;
      int var7 = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
      par1World.setBlock(par2, par3, par4, this, var7, 3);
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      TileChestHungry var7 = (TileChestHungry)par1World.getTileEntity(par2, par3, par4);
      if (var7 != null) {
         for(int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
            ItemStack var9 = var7.getStackInSlot(var8);
            if (var9 != null) {
               float var10 = this.random.nextFloat() * 0.8F + 0.1F;
               float var11 = this.random.nextFloat() * 0.8F + 0.1F;

               EntityItem var14;
               for(float var12 = this.random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14)) {
                  int var13 = this.random.nextInt(21) + 10;
                  if (var13 > var9.stackSize) {
                     var13 = var9.stackSize;
                  }

                  var9.stackSize -= var13;
                  var14 = new EntityItem(par1World, (float)par2 + var10, (float)par3 + var11, (float)par4 + var12, new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
                  float var15 = 0.05F;
                  var14.motionX = (float)this.random.nextGaussian() * var15;
                  var14.motionY = (float)this.random.nextGaussian() * var15 + 0.2F;
                  var14.motionZ = (float)this.random.nextGaussian() * var15;
                  if (var9.hasTagCompound()) {
                     var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                  }
               }
            }
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      float var5 = 0.0625F;
      return AxisAlignedBB.getBoundingBox((float)par2 + var5, par3, (float)par4 + var5, (float)(par2 + 1) - var5, (float)(par3 + 1) - var5, (float)(par4 + 1) - var5);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      Object var10 = world.getTileEntity(x, y, z);
      if (var10 != null) {
         if (!world.isRemote) {
            if (entity instanceof EntityItem && !entity.isDead) {
               ItemStack leftovers = InventoryUtils.placeItemStackIntoInventory(((EntityItem)entity).getEntityItem(), (IInventory)var10, 1, true);
               if (leftovers == null || leftovers.stackSize != ((EntityItem)entity).getEntityItem().stackSize) {
                  world.playSoundAtEntity(entity, "random.eat", 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
                  world.addBlockEvent(x, y, z, ConfigBlocks.blockChestHungry, 2, 2);
               }

               if (leftovers != null) {
                  ((EntityItem)entity).setEntityItemStack(leftovers);
               } else {
                  entity.setDead();
               }

               ((TileChestHungry)var10).markDirty();
            }

         }
      }
   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      Object var10 = par1World.getTileEntity(par2, par3, par4);
      if (var10 == null) {
         return true;
      } else if (par1World.isRemote) {
         return true;
      } else {
         par5EntityPlayer.displayGUIChest((IInventory)var10);
         return true;
      }
   }

   public TileEntity createNewTileEntity(World par1World, int m) {
      return new TileChestHungry();
   }
}
