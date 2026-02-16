package thaumcraft.common.blocks.junkbox;


@Deprecated(forRemoval = true)
public class BlockCandle /*extends Block implements IInfusionStabiliser*/ {
//   public IIcon icon;
//   public IIcon iconStub;
//
//   public BlockCandle() {
//      super(Material.circuits);
//      this.setHardness(0.1F);
//      this.setStepSound(soundTypeCloth);
//      this.setCreativeTab(Thaumcraft.tabTC);
//      this.setLightLevel(0.95F);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//      for(int var4 = 0; var4 < 16; ++var4) {
//         par3List.add(new ItemStack(par1, 1, var4));
//      }
//
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerBlockIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:candle");
//      this.iconStub = ir.registerIcon("thaumcraft:candlestub");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIcon(int side, int meta) {
//      return this.icon;
//   }
//
//   public int getRenderColor(int par1) {
//      return Utils.colors[par1];
//   }
//
//   public boolean canPlaceBlockAt(Level par1World, int par2, int par3, int par4) {
//      return World.doesBlockHaveSolidTopSurface(par1World, par2, par3, par4);
//   }
//
//   public void onNeighborBlockChange(Level par1World, int par2, int par3, int par4, Block par5) {
//      int var6 = par1World.getBlockMetadata(par2, par3, par4);
//      boolean var7 = this.canPlaceBlockAt(par1World, par2, par3 - 1, par4);
//      if (!var7) {
//         this.dropBlockAsItem(par1World, par2, par3, par4, var6, 0);
//         par1World.setBlockToAir(par2, par3, par4);
//      }
//
//      super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
//   }
//
//   public boolean canPlaceBlockOnSide(Level par1World, int par2, int par3, int par4, int par5) {
//      return this.canPlaceBlockAt(par1World, par2, par3 - 1, par4);
//   }//torch
//
//   public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
//      int md = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
//      return Utils.colors[md];
//   }
//
//   public int damageDropped(int par1) {
//      return par1;
//   }
//
//   public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
//      this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.5F, 0.625F);
//      super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
//   }//torch
//
//   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, Direction side) {
//      return false;
//   }
//
//   public AxisAlignedBB getCollisionBoundingBoxFromPool(Level par1World, int par2, int par3, int par4) {
//      return null;
//   }
//
//   public int getRenderType() {
//      return ConfigBlocks.blockCandleRI;
//   }
//
//   public boolean renderAsNormalBlock() {
//      return false;
//   }
//
//   public boolean isOpaqueCube() {
//      return false;
//   }
//
//   public void randomDisplayTick(Level par1World, int par2, int par3, int par4, Random par5Random) {
//      double var7 = (float)par2 + 0.5F;
//      double var9 = (float)par3 + 0.7F;
//      double var11 = (float)par4 + 0.5F;
//      par1World.spawnParticle("smoke", var7, var9, var11, 0.0F, 0.0F, 0.0F);
//      par1World.spawnParticle("flame", var7, var9, var11, 0.0F, 0.0F, 0.0F);
//   }//torch
//
//   public boolean canStabaliseInfusion(Level world, BlockPos pos) {
//      return true;
//   }
}
