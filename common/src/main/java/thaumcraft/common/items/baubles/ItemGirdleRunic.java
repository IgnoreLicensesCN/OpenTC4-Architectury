package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.ItemRunic;
import thaumcraft.common.Thaumcraft;

public class ItemGirdleRunic extends ItemRunic implements IBauble {
   public IIcon[] icon = new IIcon[2];

   public ItemGirdleRunic() {
      super(10);
      this.maxStackSize = 1;
      this.canRepair = false;
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:runic_girdle");
      this.icon[1] = ir.registerIcon("thaumcraft:runic_girdle_kinetic");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon[par1];
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
   }

   public EnumRarity getRarity(ItemStack par1ItemStack) {
      return EnumRarity.rare;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return itemstack.getItemDamage() == 0 ? 10 : 9;
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.BELT;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
      Thaumcraft.instance.runicEventHandler.isDirty = true;
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
      Thaumcraft.instance.runicEventHandler.isDirty = true;
   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
