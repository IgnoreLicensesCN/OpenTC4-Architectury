package thaumcraft.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.common.Thaumcraft;

public class ItemGolemCore extends Item {
   public IIcon[] icon = new IIcon[12];
   public IIcon blankIcon;

   public ItemGolemCore() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:golem_core_fill");
      this.icon[1] = ir.registerIcon("thaumcraft:golem_core_empty");
      this.icon[2] = ir.registerIcon("thaumcraft:golem_core_gather");
      this.icon[3] = ir.registerIcon("thaumcraft:golem_core_harvest");
      this.icon[4] = ir.registerIcon("thaumcraft:golem_core_guard");
      this.icon[5] = ir.registerIcon("thaumcraft:golem_core_liquid");
      this.icon[6] = ir.registerIcon("thaumcraft:golem_core_essentia");
      this.icon[7] = ir.registerIcon("thaumcraft:golem_core_lumber");
      this.icon[8] = ir.registerIcon("thaumcraft:golem_core_use");
      this.icon[9] = ir.registerIcon("thaumcraft:golem_core_butcher");
      this.icon[10] = ir.registerIcon("thaumcraft:golem_core_sorting");
      this.icon[11] = ir.registerIcon("thaumcraft:golem_core_fish");
      this.blankIcon = ir.registerIcon("thaumcraft:golem_core_blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int d) {
      return d == 100 ? this.blankIcon : this.icon[d];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 100));

      for(int a = 0; a <= 11; ++a) {
         par3List.add(new ItemStack(this, 1, a));
      }

   }

   public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      list.add(StatCollector.translateToLocal("item.ItemGolemCore." + stack.getItemDamage() + ".name"));
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return itemstack.getItemDamage() == 100 ? EnumRarity.common : EnumRarity.uncommon;
   }

   public static boolean hasGUI(int core) {
      switch (core) {
         case 0:
         case 1:
         case 2:
         case 4:
         case 5:
         case 8:
         case 10:
            return true;
         case 3:
         case 6:
         case 7:
         case 9:
         default:
            return false;
      }
   }

   public static boolean canSort(int core) {
      switch (core) {
         case 0:
         case 1:
         case 2:
         case 8:
         case 10:
            return true;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 9:
         default:
            return false;
      }
   }

   public static boolean hasInventory(int core) {
      switch (core) {
         case 0:
         case 1:
         case 2:
         case 5:
         case 8:
            return true;
         case 3:
         case 4:
         case 6:
         case 7:
         default:
            return false;
      }
   }
}
