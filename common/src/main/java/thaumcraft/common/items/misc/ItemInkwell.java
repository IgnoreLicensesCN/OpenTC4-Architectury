package thaumcraft.common.items.misc;

@Deprecated(forRemoval = true)
public class ItemInkwell /*extends Item implements IScribeTools*/ {
//   @SideOnly(Side.CLIENT)
//   public IIcon icon;
//
//   public ItemInkwell() {
//      this.maxStackSize = 1;
//      this.canRepair = true;
//      this.setMaxDamage(100);
//      this.setCreativeTab(Thaumcraft.tabTC);
//      this.setHasSubtypes(false);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:inkwell");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return this.icon;
//   }
//
//   public boolean onItemUseFirst(ItemStack stack, Player player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//      TileEntity tile = world.getTileEntity(x, y, z);
//      int md = world.getBlockMetadata(x, y, z);
//      Block bi = world.getBlock(x, y, z);
//      if (tile instanceof TileTable && md != 6) {
//         if ((Platform.getEnvironment() == Env.CLIENT)) {
//            return false;
//         }
//
//         for(int a = 2; a < 6; ++a) {
//            TileEntity tile2 = world.getTileEntity(x + Direction.getOrientation(a).offsetX, y + Direction.getOrientation(a).offsetY, z + Direction.getOrientation(a).offsetZ);
//            int md2 = world.getBlockMetadata(x + Direction.getOrientation(a).offsetX, y + Direction.getOrientation(a).offsetY, z + Direction.getOrientation(a).offsetZ);
//            if (tile2 instanceof TileTable && md2 < 6) {
//               world.setBlock(x, y, z, bi, a, 0);
//               world.setTileEntity(x, y, z, new TileResearchTable());
//               world.setBlock(x + Direction.getOrientation(a).offsetX,
//                       y + Direction.getOrientation(a).offsetY,
//                       z + Direction.getOrientation(a).offsetZ, bi,
//                       Direction.getOrientation(a).getOpposite().ordinal() + 4,
//                       0);
//               world.markBlockForUpdate(x, y, z);
//               world.markBlockForUpdate(
//                       x + Direction.getOrientation(a).offsetX,
//                       y + Direction.getOrientation(a).offsetY,
//                       z + Direction.getOrientation(a).offsetZ
//               );
//               TileEntity tile3 = world.getTileEntity(x, y, z);
//               if (tile3 instanceof TileResearchTable) {
//                  ((TileResearchTable)tile3).setInventorySlotContents(0, stack.copy());
//                  if (!player.capabilities.isCreativeMode) {
//                     player.inventory.decrStackSize(player.inventory.currentItem, 1);
//                     player.inventory.markDirty();
//                  }
//
//                  world.markBlockForUpdate(x, y, z);
//               }
//
//               return true;
//            }
//         }
//      }
//
//      return false;
//   }
}
