package thaumcraft.common.items.junkbox;

@Deprecated(forRemoval = true)
public class ItemWispEssence /*extends Item implements IEssentiaContainerItem*/ {
//   public IIcon icon;
//   static Aspect[] displayAspects;
//
//   public ItemWispEssence() {
//      this.setMaxStackSize(64);
//      this.setHasSubtypes(true);
//      this.setMaxDamage(0);
//      this.setCreativeTab(Thaumcraft.tabTC);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:wispessence");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return this.icon;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
//      for(Aspect tag : Aspects.ALL_ASPECTS.values()) {
//         ItemStack i = new ItemStack(this, 1, 0);
//         this.setAspects(i, (new LinkedHashAspectList<>()).addAll(tag, 2));
//         par3List.add(i);
//      }
//
//   }
//
//   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
//      AspectList<Aspect>aspects = this.getAspects(stack);
//      addAspectDescriptionToList(aspects,player,list);
//
//      super.addInformation(stack, player, list, par4);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getColorFromItemStack(ItemStack stack, int par2) {
//      if (stack == null){return 0;}
//      if (this.getAspects(stack) != null) {
//         return this.getAspects(stack).getAspects()[0].getColor();
//      } else {
//         int idx = (int)(System.currentTimeMillis() / 500L % (long)displayAspects.length);
//         return displayAspects[idx].getColor();
//      }
//   }
//
//   public AspectList<Aspect>getAspects(ItemStack itemstack) {
//      if (itemstack == null) {
//         return null;
//      }
//      if (itemstack.hasTagCompound()) {
//         AspectList<Aspect>aspects = new LinkedHashAspectList<>();
//         aspects.readFromNBT(itemstack.getTagCompound());
//         return aspects.size() > 0 ? aspects : null;
//      } else {
//         return null;
//      }
//   }
//
//   public void setAspects(ItemStack itemstack, AspectList<Aspect>aspects) {
//      if (!itemstack.hasTagCompound()) {
//         itemstack.setTagCompound(new NBTTagCompound());
//      }
//
//      aspects.writeToNBT(itemstack.getTagCompound());
//   }
//
//   static {
//      displayAspects = Aspects.ALL_ASPECTS.values().toArray(new Aspect[0]);
//   }
}
