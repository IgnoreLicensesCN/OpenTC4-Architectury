package thaumcraft.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.common.tiles.TileEssentiaReservoir;

public class BlockEssentiaReservoirItem extends ItemBlock {
   public BlockEssentiaReservoirItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public boolean placeBlockAt(ItemStack stack, Player player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (placed) {
         try {
            TileEssentiaReservoir ts = (TileEssentiaReservoir)world.getTileEntity(x, y, z);
            ts.facing = Direction.getOrientation(side).getOpposite();
            ts.markDirty();
            world.markBlockForUpdate(x, y, z);
         } catch (Exception ignored) {
         }
      }

      return placed;
   }
}
