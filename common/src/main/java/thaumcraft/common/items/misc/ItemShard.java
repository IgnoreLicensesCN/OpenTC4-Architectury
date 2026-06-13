package thaumcraft.common.items.misc;

@Deprecated(forRemoval = true)
public class ItemShard /*extends Item*/ {
//   public IIcon icon;
//   public IIcon iconBalanced;
//
//   public ItemShard() {
//      this.setMaxStackSize(64);
//      this.setHasSubtypes(true);
//      this.setMaxDamage(0);
//      this.setCreativeTab(Thaumcraft.tabTC);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:shard");
//      this.iconBalanced = ir.registerIcon("thaumcraft:shard_balanced");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return par1 == 6 ? this.iconBalanced : this.icon;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getColorFromItemStack(ItemStack stack, int par2) {
//      return stack.getItemDamage() == 6 ? super.getColorFromItemStack(stack, par2) : BlockCustomOreItem.colors[stack.getItemDamage() + 1];
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//      for(int a = 0; a <= 6; ++a) {
//         par3List.add(new ItemStack(this, 1, a));
//      }
//
//   }
//
//   public String getUnlocalizedName(ItemStack par1ItemStack) {
//      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
//   }
}
