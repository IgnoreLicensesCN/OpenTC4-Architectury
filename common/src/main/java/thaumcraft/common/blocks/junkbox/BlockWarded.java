package thaumcraft.common.blocks.junkbox;
@Deprecated(forRemoval = true)
public class BlockWarded /*extends BlockContainer*/ {
//   public IIcon icon;
//   public IIcon iconRune;
//   int sc = 0;
//
//   public BlockWarded() {
//      super(Material.rock);
//      this.setStepSound(soundTypeStone);
//      this.disableStats();
//      this.setResistance(999.0F);
//      this.setBlockUnbreakable();
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerBlockIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:blank");
//      this.iconRune = ir.registerIcon("thaumcraft:runeborder");
//   }
//
//   public IIcon getIcon(int i, int m) {
//      return this.icon;
//   }
//
//   public boolean isOpaqueCube() {
//      return false;
//   }
//
//   public boolean renderAsNormalBlock() {
//      return false;
//   }
//
//   public boolean addHitEffects(World worldObj, HitResult target, EffectRenderer effectRenderer) {
//      float f = (float)target.hitVec.xCoord - (float)target.blockX;
//      float f1 = (float)target.hitVec.yCoord - (float)target.blockY;
//      float f2 = (float)target.hitVec.zCoord - (float)target.blockZ;
//      Thaumcraft.proxy.blockWard(worldObj, target.blockX, target.blockY, target.blockZ, Direction.getOrientation(target.sideHit), f, f1, f2);
//      return true;
//   }
//
//   public int getRenderType() {
//      return ConfigBlocks.blockWardedRI;
//   }
//
//   public Block getBlock(World world, int x, int y, int z) {
//      if (this.sc > 5) {
//         this.sc = 0;
//         return Blocks.stone;
//      } else {
//         ++this.sc;
//         TileEntity tile = world.getTileEntity(x, y, z);
//         if (tile instanceof TileWarded) {
//            this.sc = 0;
//            return ((TileWarded)tile).block;
//         } else {
//            return Blocks.stone;
//         }
//      }
//   }
//
//   public Block getBlock(IBlockAccess world, int x, int y, int z) {
//      if (this.sc > 5) {
//         this.sc = 0;
//         return Blocks.stone;
//      } else {
//         ++this.sc;
//         TileEntity tile = world.getTileEntity(x, y, z);
//         if (tile instanceof TileWarded) {
//            this.sc = 0;
//            return ((TileWarded)tile).block;
//         } else {
//            return Blocks.stone;
//         }
//      }
//   }
//
//   public Item getItemDropped(int par1, Random par2Random, int par3) {
//      return Item.getItemById(0);
//   }
//
//   public int damageDropped(int par1) {
//      return par1;
//   }
//
//   public int getMobilityFlag() {
//      return 2;
//   }
//
//   public TileEntity createNewTileEntity(World var1, int md) {
//      return new TileWarded();
//   }
//
//   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
//      return false;
//   }
//
//   public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
//      return false;
//   }
//
//   public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
//      return false;
//   }
//
//   public Item getItem(World ba, int x, int y, int z) {
//      return this.getBlock(ba, x, y, z).getItem(ba, x, y, z);
//   }
//
//   public int getLightValue(IBlockAccess world, int x, int y, int z) {
//      TileEntity tile = world.getTileEntity(x, y, z);
//      return tile instanceof TileWarded ? ((TileWarded)tile).light : 0;
//   }
//
//   public boolean canHarvestBlock(Player player, int meta) {
//      return true;
//   }
//
//   public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
//   }
}
