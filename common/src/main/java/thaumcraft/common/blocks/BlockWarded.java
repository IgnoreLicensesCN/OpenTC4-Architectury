package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileWarded;

import java.util.List;
import java.util.Random;

public class BlockWarded extends BlockContainer {
   public IIcon icon;
   public IIcon iconRune;
   int sc = 0;

   public BlockWarded() {
      super(Material.rock);
      this.setStepSound(soundTypeStone);
      this.disableStats();
      this.setResistance(999.0F);
      this.setBlockUnbreakable();
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:blank");
      this.iconRune = ir.registerIcon("thaumcraft:runeborder");
   }

   public IIcon getIcon(int i, int m) {
      return this.icon;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
      float f = (float)target.hitVec.xCoord - (float)target.blockX;
      float f1 = (float)target.hitVec.yCoord - (float)target.blockY;
      float f2 = (float)target.hitVec.zCoord - (float)target.blockZ;
      Thaumcraft.proxy.blockWard(worldObj, target.blockX, target.blockY, target.blockZ, ForgeDirection.getOrientation(target.sideHit), f, f1, f2);
      return true;
   }

   public int getRenderType() {
      return ConfigBlocks.blockWardedRI;
   }

   public Block getBlock(World world, int x, int y, int z) {
      if (this.sc > 5) {
         this.sc = 0;
         return Blocks.stone;
      } else {
         ++this.sc;
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileWarded) {
            this.sc = 0;
            return ((TileWarded)tile).block;
         } else {
            return Blocks.stone;
         }
      }
   }

   public Block getBlock(IBlockAccess world, int x, int y, int z) {
      if (this.sc > 5) {
         this.sc = 0;
         return Blocks.stone;
      } else {
         ++this.sc;
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileWarded) {
            this.sc = 0;
            return ((TileWarded)tile).block;
         } else {
            return Blocks.stone;
         }
      }
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemById(0);
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public int getMobilityFlag() {
      return 2;
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return new TileWarded();
   }

   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
      return false;
   }

   public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess ba, int x, int y, int z, int par5) {
      return this.getBlock(ba, x, y, z).getIcon(ba, x, y, z, par5);
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess ba, int x, int y, int z) {
      return this.getBlock(ba, x, y, z).colorMultiplier(ba, x, y, z);
   }

   public int getDamageValue(World world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).getDamageValue(world, x, y, z);
   }

   public int getMixedBrightnessForBlock(IBlockAccess ba, int x, int y, int z) {
      return this.getBlock(ba, x, y, z).getMixedBrightnessForBlock(ba, x, y, z);
   }

   public boolean shouldSideBeRendered(IBlockAccess ba, int x, int y, int z, int par5) {
      return this.getBlock(ba, x, y, z).shouldSideBeRendered(ba, x, y, z, par5);
   }

   public boolean isBlockSolid(IBlockAccess ba, int x, int y, int z, int par5) {
      return this.getBlock(ba, x, y, z).isBlockSolid(ba, x, y, z, par5);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World ba, int x, int y, int z) {
      return this.getBlock(ba, x, y, z).getSelectedBoundingBoxFromPool(ba, x, y, z);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World ba, int x, int y, int z) {
      return this.getBlock(ba, x, y, z).getCollisionBoundingBoxFromPool(ba, x, y, z);
   }

   public void randomDisplayTick(World ba, int x, int y, int z, Random par5Random) {
      this.getBlock(ba, x, y, z).randomDisplayTick(ba, x, y, z, par5Random);
   }

   public boolean canPlaceBlockOnSide(World ba, int x, int y, int z, int par5) {
      return this.getBlock(ba, x, y, z).canPlaceBlockOnSide(ba, x, y, z, par5);
   }

   public void onEntityWalking(World ba, int x, int y, int z, Entity par5Entity) {
      this.getBlock(ba, x, y, z).onEntityWalking(ba, x, y, z, par5Entity);
   }

   public void onBlockClicked(World ba, int x, int y, int z, EntityPlayer par5EntityPlayer) {
      this.getBlock(ba, x, y, z).onBlockClicked(ba, x, y, z, par5EntityPlayer);
   }

   public void velocityToAddToEntity(World ba, int x, int y, int z, Entity par5Entity, Vec3 par6Vec3) {
      this.getBlock(ba, x, y, z).velocityToAddToEntity(ba, x, y, z, par5Entity, par6Vec3);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess ba, int x, int y, int z) {
      this.getBlock(ba, x, y, z).setBlockBoundsBasedOnState(ba, x, y, z);
   }

   public void addCollisionBoxesToList(World ba, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
      this.getBlock(ba, x, y, z).addCollisionBoxesToList(ba, x, y, z, aabb, list, entity);
   }

   public void onEntityCollidedWithBlock(World ba, int x, int y, int z, Entity par5Entity) {
      this.getBlock(ba, x, y, z).onEntityCollidedWithBlock(ba, x, y, z, par5Entity);
   }

   public void onFallenUpon(World ba, int x, int y, int z, Entity par5Entity, float par6) {
      this.getBlock(ba, x, y, z).onFallenUpon(ba, x, y, z, par5Entity, par6);
   }

   public Item getItem(World ba, int x, int y, int z) {
      return this.getBlock(ba, x, y, z).getItem(ba, x, y, z);
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      TileEntity tile = world.getTileEntity(x, y, z);
      return tile instanceof TileWarded ? ((TileWarded)tile).light : 0;
   }

   public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
      return this.getBlock(world, x, y, z).isLadder(world, x, y, z, entity);
   }

   public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).isNormalCube(world, x, y, z);
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return this.getBlock(world, x, y, z).isSideSolid(world, x, y, z, side);
   }

   public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).canSustainLeaves(world, x, y, z);
   }

   public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).canPlaceTorchOnTop(world, x, y, z);
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).getPickBlock(target, world, x, y, z);
   }

   public boolean isFoliage(IBlockAccess world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).isFoliage(world, x, y, z);
   }

   public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
      return this.getBlock(world, x, y, z).canSustainPlant(world, x, y, z, direction, plant);
   }

   public boolean isFertile(World world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).isFertile(world, x, y, z);
   }

   public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).getLightOpacity(world, x, y, z);
   }

   public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
      return this.getBlock(world, x, y, z).isBeaconBase(world, x, y, z, beaconX, beaconY, beaconZ);
   }

   public float getEnchantPowerBonus(World world, int x, int y, int z) {
      return this.getBlock(world, x, y, z).getEnchantPowerBonus(world, x, y, z);
   }

   public boolean canHarvestBlock(EntityPlayer player, int meta) {
      return true;
   }

   public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
   }
}
