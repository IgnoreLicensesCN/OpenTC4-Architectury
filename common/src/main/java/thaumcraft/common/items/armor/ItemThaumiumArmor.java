package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ItemThaumiumArmor extends ItemArmor implements IRepairable, IRunicArmor {
   public IIcon iconHelm;
   public IIcon iconChest;
   public IIcon iconLegs;
   public IIcon iconBoots;

   public ItemThaumiumArmor(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconHelm = ir.registerIcon("thaumcraft:thaumiumhelm");
      this.iconChest = ir.registerIcon("thaumcraft:thaumiumchest");
      this.iconLegs = ir.registerIcon("thaumcraft:thaumiumlegs");
      this.iconBoots = ir.registerIcon("thaumcraft:thaumiumboots");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.armorType == 0 ? this.iconHelm : (this.armorType == 1 ? this.iconChest : (this.armorType == 2 ? this.iconLegs : this.iconBoots));
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      if (stack.getItem() != ConfigItems.itemHelmetThaumium && stack.getItem() != ConfigItems.itemChestThaumium && stack.getItem() != ConfigItems.itemBootsThaumium) {
         return stack.getItem() == ConfigItems.itemLegsThaumium ? "thaumcraft:textures/models/thaumium_2.png" : "thaumcraft:textures/models/thaumium_1.png";
      } else {
         return "thaumcraft:textures/models/thaumium_1.png";
      }
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 2)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }
}
