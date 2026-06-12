package thaumcraft.common.items.equipment.armor;

@Deprecated(forRemoval = true)
public class ItemHoverHarness /*extends ArmorItem implements IRepairEnchantable, IVisDiscountGear, IRunicShieldProviderItem*/ {
//   ModelBiped model = null;
//   public IIcon icon;
//   public IIcon iconLightningRing;
//
//   public ItemHoverHarness(Tier enumarmormaterial, int j, int k) {
//      super(enumarmormaterial, j, k);
//      this.setMaxDamage(400);
//      this.setCreativeTab(Thaumcraft.tabTC);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
//      if (this.model == null) {
//         this.model = new ModelHoverHarness();
//      }
//
//      return this.model;
//   }
//
//   public int getRunicCharge(ItemStack itemstack) {
//      return 0;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public void registerIcons(IIconRegister ir) {
//      this.icon = ir.registerIcon("thaumcraft:hoverharness");
//      this.iconLightningRing = ir.registerIcon("thaumcraft:lightningring");
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IIcon getIconFromDamage(int par1) {
//      return this.icon;
//   }
//
//   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
//      return "thaumcraft:textures/models/hoverharness.png";
//   }

//   public EnumRarity getRarity(ItemStack itemstack) {
//      return EnumRarity.epic;
//   }
//
//   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
//      return par2ItemStack.isItemEqual(new ItemStack(Items.gold_ingot)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
//   }
//
//   public int getVisCostPercentDecrease(ItemStack stack, LivingEntity living, Aspect aspect) {
//      return aspect == Aspects.AIR ? 5 : 2;
//   }

//   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, Player par3Player) {
//      if (!(Platform.getEnvironment() == Env.CLIENT)) {
//         par3Player.openGui(Thaumcraft.instance, 17, par2World, MathHelper.floor_double(par3Player.posX), MathHelper.floor_double(par3Player.posY), MathHelper.floor_double(par3Player.posZ));
//      }
//
//      return par1ItemStack;
//   }

//   public void onArmorTick(World world, Player player, ItemStack itemStack) {
//      if (!player.capabilities.isCreativeMode) {
//         Hover.handleHoverArmor(player, player.inventory.armorItemInSlot(2));
//      }
//
//   }

//   public void addInformation(ItemStack is, Player player, List list, boolean par4) {
//      super.addInformation(is, player, list, par4);
//      if (is.hasTagCompound() && is.stackTagCompound.hasKey("jar")) {
//         ItemStack jar = ItemStack.loadItemStackFromNBT(is.stackTagCompound.getCompoundTag("jar"));
//
//         try {
//            AspectList<Aspect>aspects = ((ItemJarFilled)jar.getItem()).getAspects(jar);
//            addAspectDescriptionToList(aspects,player,list);
//         } catch (Exception ignored) {
//         }
//      }
//
//      list.add(EnumChatFormatting.DARK_PURPLE + Component.translatable("tc.visdiscount") + ": " + this.getVisCostPercentDecrease(is, player, null) + "%");
//      list.add(EnumChatFormatting.DARK_PURPLE + Component.translatable("tc.visdiscount") + " (Aer): " + this.getVisCostPercentDecrease(is, player, Aspects.AIR) + "%");
//   }
}
