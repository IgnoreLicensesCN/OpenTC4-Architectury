package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.tiles.TileEssentiaCrystalizer;
import thaumcraft.common.tiles.TileTube;

public class BlockTubeItem extends ItemBlock {
   public BlockTubeItem(Block par1) {
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

   public boolean placeBlockAt(ItemStack stack, Player player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (ret) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileTube) {
            ((TileTube)te).facing = ForgeDirection.getOrientation(side);
         }

         if (te instanceof TileEssentiaCrystalizer) {
            ((TileEssentiaCrystalizer)te).facing = ForgeDirection.getOrientation(side).getOpposite();
         }
      }

      return ret;
   }
}
