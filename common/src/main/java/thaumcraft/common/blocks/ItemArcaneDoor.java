package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileOwned;

public class ItemArcaneDoor extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemArcaneDoor() {
      this.maxStackSize = 1;
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:arcanedoor");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      if (par7 != 1) {
         return false;
      } else {
         ++par5;
         Block var11 = ConfigBlocks.blockArcaneDoor;
         if (player.canPlayerEdit(par4, par5, par6, par7, stack) && player.canPlayerEdit(par4, par5 + 1, par6, par7, stack)) {
            if (!var11.canPlaceBlockAt(world, par4, par5, par6)) {
               return false;
            } else {
               int var12 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - (double)0.5F) & 3;
               placeDoorBlock(world, par4, par5, par6, var12, var11, player);
               --stack.stackSize;
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public static void placeDoorBlock(World world, int x, int y, int z, int par4, Block par5Block, EntityPlayer player) {
      byte var6 = 0;
      byte var7 = 0;
      if (par4 == 0) {
         var7 = 1;
      }

      if (par4 == 1) {
         var6 = -1;
      }

      if (par4 == 2) {
         var7 = -1;
      }

      if (par4 == 3) {
         var6 = 1;
      }

      int var8 = (world.isBlockNormalCubeDefault(x - var6, y, z - var7, false) ? 1 : 0) + (world.isBlockNormalCubeDefault(x - var6, y + 1, z - var7, false) ? 1 : 0);
      int var9 = (world.isBlockNormalCubeDefault(x + var6, y, z + var7, false) ? 1 : 0) + (world.isBlockNormalCubeDefault(x + var6, y + 1, z + var7, false) ? 1 : 0);
      boolean var10 = world.getBlock(x - var6, y, z - var7) == par5Block || world.getBlock(x - var6, y + 1, z - var7) == par5Block;
      boolean var11 = world.getBlock(x + var6, y, z + var7) == par5Block || world.getBlock(x + var6, y + 1, z + var7) == par5Block;
      boolean var12 = false;
      if (var10 && !var11) {
         var12 = true;
      } else if (var9 > var8) {
         var12 = true;
      }

      world.setBlock(x, y, z, par5Block, par4, 2);
      TileOwned tad = (TileOwned)world.getTileEntity(x, y, z);
      tad.owner = player.getCommandSenderName();
      world.setBlock(x, y + 1, z, par5Block, 8 | (var12 ? 1 : 0), 2);
      TileOwned tad2 = (TileOwned)world.getTileEntity(x, y + 1, z);
      tad2.owner = player.getCommandSenderName();
      world.notifyBlocksOfNeighborChange(x, y, z, par5Block);
      world.notifyBlocksOfNeighborChange(x, y + 1, z, par5Block);
   }
}
