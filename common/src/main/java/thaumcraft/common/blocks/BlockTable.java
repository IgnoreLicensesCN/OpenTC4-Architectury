package thaumcraft.common.blocks;

//  "tile.blockTable.0.name": "台桌",
//          "tile.blockTable.1.name": "台桌",//also 6~9? in tileEntity
//          "tile.blockTable.14.name": "解構工作台",
//          "tile.blockTable.15.name": "奧術工作台",
//          "tile.blockTable.research.name": "研究桌",//2~5
public class BlockTable /*extends BlockContainer implements IWandable*/ {
//   public IIcon icon;
//   public IIcon iconQuill;

//   public BlockTable() {
//      super(Material.wood);
//      this.setHardness(2.5F);
//      this.setStepSound(soundTypeWood);
//      this.setCreativeTab(Thaumcraft.tabTC);
//   }

//   @SideOnly(Side.CLIENT)
//   public void registerBlockIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:woodplain");
//      this.iconQuill = ir.registerIcon("thaumcraft:tablequill");
//   }

//   @SideOnly(Side.CLIENT)
//   public IIcon getIcon(int side, int meta) {
//      return this.icon;
//   }

//   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, Direction side) {
//      return side == Direction.UP || super.isSideSolid(world, x, y, z, side);
//   }

//   @SideOnly(Side.CLIENT)
//   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//      par3List.add(new ItemStack(par1, 1, 0));
//      par3List.add(new ItemStack(par1, 1, 14));
//      par3List.add(new ItemStack(par1, 1, 15));
//   }

//   public TileEntity createTileEntity(World world, int metadata) {
//      if ((metadata <= 1 || metadata >= 6) && metadata < 14) {
//         return new TileTable();
//      } else if (metadata == 14) {
//         return new TileDeconstructionTable();
//      } else {
//         return metadata == 15 ? new TileArcaneWorkbench() : new TileResearchTable();
//      }
//   }
//
//   public void onBlockPlacedBy(Level par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack is) {
//      int md = par1World.getBlockMetadata(par2, par3, par4);
//      if (md < 14) {
//         int var7 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + (double)0.5F) & 3;
//         int out = var7 == 0 ? 0 : (var7 == 1 ? 1 : (var7 == 2 ? 0 : (var7 == 3 ? 1 : 0)));
//         par1World.setBlock(par2, par3, par4, this, out, 3);
//      }
//
//   }

//   public boolean isOpaqueCube() {
//      return false;
//   }

//   public boolean renderAsNormalBlock() {
//      return false;
//   }

//   public int getRenderType() {
//      return ConfigBlocks.blockTableRI;
//   }

//   public void breakBlock(Level par1World, int par2, int par3, int par4, Block par5, int par6) {
//      InventoryUtils.dropItems(par1World, par2, par3, par4);
//      super.breakBlock(par1World, par2, par3, par4, par5, par6);
//   }

//   public int getDamageValue(Level par1World, int par2, int par3, int par4) {
//      int md = par1World.getBlockMetadata(par2, par3, par4);
//      return md >= 2 && md <= 9 ? 2 : super.getDamageValue(par1World, par2, par3, par4);
//   }

//   public int damageDropped(int par1) {
//      if (par1 == 14) {
//         return 14;
//      } else {
//         return par1 == 15 ? 15 : 0;
//      }
//   }

//   public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
//      TileEntity tile = world.getTileEntity(x, y, z);
//      int md = world.getBlockMetadata(x, y, z);
//      if (tile instanceof TileResearchTable) {
//         int mm = world.getBlockMetadata(x + Direction.getOrientation(md).offsetX, y + Direction.getOrientation(md).offsetY, z + Direction.getOrientation(md).offsetZ);
//         if (mm < 6) {
//            InventoryUtils.dropItems(world, x, y, z);
//            world.setTileEntity(x, y, z, new TileTable());
//            world.setBlock(x, y, z, this, 0, 3);
//         }
//      } else if (md >= 6 && md < 14) {
//         TileEntity tile2 = world.getTileEntity(x + Direction.getOrientation(md - 4).offsetX, y + Direction.getOrientation(md - 4).offsetY, z + Direction.getOrientation(md - 4).offsetZ);
//         if (!(tile2 instanceof TileResearchTable)) {
//            world.setBlock(x, y, z, this, 0, 3);
//         }
//      }
//
//      super.onNeighborBlockChange(world, x, y, z, par5);
//   }
//
//   public boolean onBlockActivated(World world, int x, int y, int z, Player player, int idk, float what, float these, float are) {
//      TileEntity tileEntity = world.getTileEntity(x, y, z);
//      int md = world.getBlockMetadata(x, y, z);
//      if (md > 1 && tileEntity != null && !player.isSneaking()) {
//         if ((Platform.getEnvironment() == Env.CLIENT)) {
//            return true;
//         } else if (tileEntity instanceof TileArcaneWorkbench) {
//            player.openGui(Thaumcraft.instance, 13, world, x, y, z);
//            return true;
//         } else if (tileEntity instanceof TileDeconstructionTable) {
//            player.openGui(Thaumcraft.instance, 8, world, x, y, z);
//            return true;
//         } else {
//            if (tileEntity instanceof TileResearchTable) {
//               player.openGui(Thaumcraft.instance, 10, world, x, y, z);
//            } else {
//               for(int a = 2; a < 6; ++a) {
//                  TileEntity tile = world.getTileEntity(x + Direction.getOrientation(a).offsetX, y + Direction.getOrientation(a).offsetY, z + Direction.getOrientation(a).offsetZ);
//                  if (tile instanceof TileResearchTable) {
//                     player.openGui(Thaumcraft.instance, 10, world, x + Direction.getOrientation(a).offsetX, y + Direction.getOrientation(a).offsetY, z + Direction.getOrientation(a).offsetZ);
//                     break;
//                  }
//               }
//            }
//
//            return true;
//         }
//      } else {
//         return false;
//      }
//   }

//   public TileEntity createNewTileEntity(World var1, int md) {
//      return null;
//   }

//   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
//      int md = world.getBlockMetadata(x, y, z);
//      switch (md) {
//         case 2:
//         case 6:
//            return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ - (double)1.0F, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
//         case 3:
//         case 7:
//            return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ + (double)1.0F);
//         case 4:
//         case 8:
//            return AxisAlignedBB.getBoundingBox((double)x + this.minX - (double)1.0F, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
//         case 5:
//         case 9:
//            return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX + (double)1.0F, (double)y + this.maxY, (double)z + this.maxZ);
//         default:
//            return super.getSelectedBoundingBoxFromPool(world, x, y, z);
//      }
//   }

//   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
//      if (md <= 1) {
//         WandCastingItem wand = (WandCastingItem)wandstack.getItem();
//         world.setBlock(x, y, z, ConfigBlocks.blockTable, 15, 3);
//         world.setTileEntity(x, y, z, new TileArcaneWorkbench());
//         TileArcaneWorkbench tawb = (TileArcaneWorkbench)world.getTileEntity(x, y, z);
//         if (tawb != null && !wand.isStaff(wandstack)) {
//            tawb.setInventorySlotContents(10, wandstack.copy());
//            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
//         }
//
//         tawb.markDirty();
//         world.markBlockForUpdate(x, y, z);
//         world.playSoundEffect((double)x + (double)0.5F, (double)y + 0.1, (double)z + (double)0.5F, "random.click", 0.15F, 0.5F);
//         return 0;
//      } else {
//         return -1;
//      }
//   }

//   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
//      return null;
//   }
//
//   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
//   }
//
//   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
//   }
}
