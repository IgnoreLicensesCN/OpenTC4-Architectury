package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileEldritchNothing;

import java.util.Random;

public class BlockEldritchNothing extends Block {
   public IIcon icon;

   public BlockEldritchNothing() {
      super(Material.rock);
      this.setBlockUnbreakable();
      this.setResistance(6000000.0F);
      this.setStepSound(Block.soundTypeCloth);
      this.setLightLevel(0.2F);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setTickRandomly(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:blank");
   }

   public IIcon getIcon(int i, int m) {
      return this.icon;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
      return AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return -1;
   }

   public boolean hasTileEntity(int metadata) {
      return metadata == 1;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return metadata == 1 ? new TileEldritchNothing() : null;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemById(0);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if (BlockUtils.isBlockExposed(world, x, y, z)) {
         world.setBlockMetadataWithNotify(x, y, z, 1, 3);
      } else {
         world.setBlockMetadataWithNotify(x, y, z, 0, 3);
      }

      super.onNeighborBlockChange(world, x, y, z, block);
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if (entity.ticksExisted > 20 && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.isCreativeMode)) {
         entity.attackEntityFrom(DamageSource.outOfWorld, 8.0F);
      }

   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      float f = 0.125F;
      return AxisAlignedBB.getBoundingBox((float)x + f, (double)y + (double)f, (float)z + f, (float)(x + 1) - f, (float)(y + 1) - f, (float)(z + 1) - f);
   }
}
