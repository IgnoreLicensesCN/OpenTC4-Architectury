package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.common.Thaumcraft;

public class ItemGoggles extends ItemArmor implements IRepairable, IVisDiscountGear, IRevealer, IGoggles, IRunicArmor {
   public IIcon icon;

   public ItemGoggles(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setMaxDamage(350);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.addInformation(stack, player, list, par4);
      list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, null) + "%");
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:gogglesrevealing");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return "thaumcraft:textures/models/goggles.png";
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(Items.gold_ingot)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
      return 5;
   }

   public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
