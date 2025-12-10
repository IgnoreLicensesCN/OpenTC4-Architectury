package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.*;

public class BlockMetalDeviceItem extends ItemBlock {
   public BlockMetalDeviceItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      if (stack.getItemDamage() != 0 && stack.getItemDamage() != 1 && stack.getItemDamage() != 2 && stack.getItemDamage() != 3 && stack.getItemDamage() != 5 && stack.getItemDamage() != 6 && stack.getItemDamage() != 7 && stack.getItemDamage() != 8 && stack.getItemDamage() != 9 && stack.getItemDamage() != 13 && stack.getItemDamage() != 14) {
         Block bi = world.getBlock(x, y, z);
         int md = world.getBlockMetadata(x, y, z);
         if (stack.getItemDamage() == 12) {
            return bi == ConfigBlocks.blockMetalDevice && (md == 10 || md == 11) && super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
         } else {
            if (bi == ConfigBlocks.blockMetalDevice && md == 0) {
               if (side == 0 || side == 1) {
                  return false;
               }

               if (side == 2) {
                  --z;
               }

               if (side == 3) {
                  ++z;
               }

               if (side == 4) {
                  --x;
               }

               if (side == 5) {
                  ++x;
               }
            }

            if (stack.stackSize == 0) {
               return false;
            } else if (!player.canPlayerEdit(x, y, z, side, stack)) {
               return false;
            } else if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
               return false;
            } else {
               Block var11 = world.getBlock(x, y, z);
               if (world.isAirBlock(x, y, z) || var11.isReplaceable(world, x, y, z) || var11 == Blocks.vine || var11 == Blocks.tallgrass || var11 == Blocks.deadbush || var11 == Blocks.snow_layer) {
                  for(int a = 2; a < 6; ++a) {
                     ForgeDirection dir = ForgeDirection.getOrientation(a);
                     int xx = x + dir.offsetX;
                     int yy = y + dir.offsetY;
                     int zz = z + dir.offsetZ;
                     Block bid = world.getBlock(xx, yy, zz);
                     int meta = world.getBlockMetadata(xx, yy, zz);
                     if (bid == ConfigBlocks.blockMetalDevice && meta == 0 && this.placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, stack.getItemDamage())) {
                        world.playSoundEffect((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.field_150939_a.stepSound.getStepResourcePath(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                        --stack.stackSize;
                        world.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, dir.getOpposite().ordinal() - 1, 3);
                        return true;
                     }
                  }
               }

               return false;
            }
         }
      } else {
         return super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
      }
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (metadata == 7) {
         TileArcaneLamp tile = (TileArcaneLamp)world.getTileEntity(x, y, z);
         if (tile instanceof TileArcaneLamp) {
            tile.facing = ForgeDirection.getOrientation(side).getOpposite();
            world.markBlockForUpdate(x, y, x);
         }
      } else if (metadata == 8) {
         TileArcaneLampGrowth tile = (TileArcaneLampGrowth)world.getTileEntity(x, y, z);
         if (tile instanceof TileArcaneLampGrowth) {
            tile.facing = ForgeDirection.getOrientation(side).getOpposite();
            world.markBlockForUpdate(x, y, x);
         }
      } else if (metadata == 12) {
         TileBrainbox tile = (TileBrainbox)world.getTileEntity(x, y, z);
         if (tile instanceof TileBrainbox) {
            tile.facing = ForgeDirection.getOrientation(side).getOpposite();
            world.markBlockForUpdate(x, y, x);
         }
      } else if (metadata == 13) {
         TileArcaneLampFertility tile = (TileArcaneLampFertility)world.getTileEntity(x, y, z);
         if (tile instanceof TileArcaneLampFertility) {
            tile.facing = ForgeDirection.getOrientation(side).getOpposite();
            world.markBlockForUpdate(x, y, x);
         }
      } else if (metadata == 14) {
         TileVisRelay tile = (TileVisRelay)world.getTileEntity(x, y, z);
         if (tile instanceof TileVisRelay) {
            tile.orientation = (short)side;
            world.markBlockForUpdate(x, y, x);
         }
      }

      return ret;
   }
}
