package thaumcraft.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemPrimalArrow extends Item {
   public IIcon[] icon = new IIcon[6];

   public ItemPrimalArrow() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:el_arrow_air");
      this.icon[1] = ir.registerIcon("thaumcraft:el_arrow_fire");
      this.icon[2] = ir.registerIcon("thaumcraft:el_arrow_water");
      this.icon[3] = ir.registerIcon("thaumcraft:el_arrow_earth");
      this.icon[4] = ir.registerIcon("thaumcraft:el_arrow_order");
      this.icon[5] = ir.registerIcon("thaumcraft:el_arrow_entropy");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon[par1];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int a = 0; a <= 5; ++a) {
         par3List.add(new ItemStack(this, 1, a));
      }

   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
