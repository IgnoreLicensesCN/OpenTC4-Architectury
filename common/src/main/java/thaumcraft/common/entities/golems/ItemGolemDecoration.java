package thaumcraft.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.common.Thaumcraft;

public class ItemGolemDecoration extends Item {
   public IIcon[] icon = new IIcon[8];

   public ItemGolemDecoration() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:golemdecotophat");
      this.icon[1] = ir.registerIcon("thaumcraft:golemdecoglasses");
      this.icon[2] = ir.registerIcon("thaumcraft:golemdecobowtie");
      this.icon[3] = ir.registerIcon("thaumcraft:golemdecofez");
      this.icon[4] = ir.registerIcon("thaumcraft:golemdecodart");
      this.icon[5] = ir.registerIcon("thaumcraft:golemdecovisor");
      this.icon[6] = ir.registerIcon("thaumcraft:golemdecoarmor");
      this.icon[7] = ir.registerIcon("thaumcraft:golemdecomace");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int d) {
      return this.icon[d];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int a = 0; a <= 7; ++a) {
         par3List.add(new ItemStack(this, 1, a));
      }

   }

   public static String getDecoChar(int md) {
      switch (md) {
         case 0:
            return "H";
         case 1:
            return "G";
         case 2:
            return "B";
         case 3:
            return "F";
         case 4:
            return "R";
         case 5:
            return "V";
         case 6:
            return "P";
         case 7:
            return "M";
         default:
            return "";
      }
   }

   public String getItemStackDisplayName(ItemStack stack) {
      return StatCollector.translateToLocal("item.ItemGolemDecoration.name") + ": " + super.getItemStackDisplayName(stack);
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
