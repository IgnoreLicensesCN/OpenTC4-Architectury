package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileArcaneBore;
import thaumcraft.common.tiles.TileArcaneBoreBase;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileBellows;

public class BlockWoodenDeviceItem extends ItemBlock {
   public BlockWoodenDeviceItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return stack.hasTagCompound() && stack.stackTagCompound.hasKey("color") ? super.getUnlocalizedName() + "." + stack.getItemDamage() + "." + stack.stackTagCompound.getByte("color") : super.getUnlocalizedName() + "." + stack.getItemDamage();
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (ret) {
         if (metadata == 0) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileBellows) {
               ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
               ((TileBellows)tile).orientation = (byte)dir.ordinal();
               int xx = x + dir.offsetX;
               int yy = y + dir.offsetY;
               int zz = z + dir.offsetZ;
               Block bi = world.getBlock(xx, yy, zz);
               if (bi == Blocks.furnace || bi == Blocks.lit_furnace) {
                  ((TileBellows)tile).onVanillaFurnace = true;
               }

               tile.markDirty();
               world.markBlockForUpdate(x, y, x);
            }
         }

         if (metadata == 4) {
            TileArcaneBoreBase tile = (TileArcaneBoreBase)world.getTileEntity(x, y, z);
            if (tile instanceof TileArcaneBoreBase) {
               int var6 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + (double)0.5F) & 3;
               switch (var6) {
                  case 0:
                     tile.orientation = ForgeDirection.getOrientation(2);
                     break;
                  case 1:
                     tile.orientation = ForgeDirection.getOrientation(5);
                     break;
                  case 2:
                     tile.orientation = ForgeDirection.getOrientation(3);
                     break;
                  case 3:
                     tile.orientation = ForgeDirection.getOrientation(4);
               }

               tile.markDirty();
               world.markBlockForUpdate(x, y, x);
            }
         }

         if (metadata == 5) {
            TileArcaneBore tile = (TileArcaneBore)world.getTileEntity(x, y, z);
            if (tile instanceof TileArcaneBore) {
               tile.baseOrientation = ForgeDirection.getOrientation(side);
               int var6 = BlockPistonBase.determineOrientation(world, x, y, z, player);
               tile.orientation = ForgeDirection.getOrientation(var6);
               world.markBlockForUpdate(x, y, x);
               tile.markDirty();
            }
         }

         if (metadata == 8) {
            TileBanner tile = (TileBanner)world.getTileEntity(x, y, z);
            if (tile != null) {
               if (side <= 1) {
                  int i = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + (double)0.5F) & 15;
                  tile.setFacing((byte)i);
               } else {
                  tile.setWall(true);
                  int i = 0;
                  if (side == 2) {
                     i = 8;
                  }

                  if (side == 4) {
                     i = 4;
                  }

                  if (side == 5) {
                     i = 12;
                  }

                  tile.setFacing((byte)i);
               }

               if (stack.hasTagCompound()) {
                  if (stack.stackTagCompound.getString("aspect") != null) {
                     tile.setAspect(Aspect.getAspect(stack.stackTagCompound.getString("aspect")));
                  }

                  if (stack.stackTagCompound.hasKey("color")) {
                     tile.setColor(stack.stackTagCompound.getByte("color"));
                  }
               }

               tile.markDirty();
               world.markBlockForUpdate(x, y, z);
            }
         }
      }

      return ret;
   }

   public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
      if (par7ItemStack.getItemDamage() == 5) {
         if (side > 1) {
            return false;
         }

         if (world.getBlock(x, y, z) != ConfigBlocks.blockWoodenDevice && world.getBlockMetadata(x, y, z) != 4) {
            return false;
         }
      }

      return super.func_150936_a(world, x, y, z, side, par6EntityPlayer, par7ItemStack);
   }
}
