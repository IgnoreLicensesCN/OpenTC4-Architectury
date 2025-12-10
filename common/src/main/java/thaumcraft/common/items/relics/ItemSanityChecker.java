package thaumcraft.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;

public class ItemSanityChecker extends Item {
   private IIcon icon;

   public ItemSanityChecker() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(false);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon = par1IconRegister.registerIcon("thaumcraft:sanitychecker");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this));
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }
}
