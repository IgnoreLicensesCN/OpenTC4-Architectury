package thaumcraft.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.tiles.TileCrystal;

public class BlockCrystalItem extends ItemBlock {
   public BlockCrystalItem(Block par1) {
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
      boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (placed && metadata <= 7) {
         try {
            TileCrystal ts = (TileCrystal)world.getTileEntity(x, y, z);
            ts.orientation = (short)side;
         } catch (Exception ignored) {
         }
      }

      return placed;
   }
}
