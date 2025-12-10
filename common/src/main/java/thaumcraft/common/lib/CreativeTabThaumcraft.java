package thaumcraft.common.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import thaumcraft.common.config.ConfigItems;

public final class CreativeTabThaumcraft extends CreativeTabs {
   public CreativeTabThaumcraft(int par1, String par2Str) {
      super(par1, par2Str);
   }

   @SideOnly(Side.CLIENT)
   public Item getTabIconItem() {
      return ConfigItems.itemWandCasting;
   }
}
