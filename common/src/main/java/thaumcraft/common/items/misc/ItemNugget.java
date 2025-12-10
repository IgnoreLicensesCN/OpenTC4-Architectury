package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

import java.util.List;

public class ItemNugget extends Item {
   public IIcon[] icon = new IIcon[64];

   public ItemNugget() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:nuggetiron");
      this.icon[16] = ir.registerIcon("thaumcraft:clusteriron");
      this.icon[5] = ir.registerIcon("thaumcraft:nuggetquicksilver");
      this.icon[21] = ir.registerIcon("thaumcraft:clustercinnabar");
      this.icon[6] = ir.registerIcon("thaumcraft:nuggetthaumium");
      this.icon[7] = ir.registerIcon("thaumcraft:nuggetvoid");
      this.icon[31] = ir.registerIcon("thaumcraft:clustergold");
      this.icon[1] = ir.registerIcon("thaumcraft:nuggetcopper");
      this.icon[17] = ir.registerIcon("thaumcraft:clustercopper");
      this.icon[2] = ir.registerIcon("thaumcraft:nuggettin");
      this.icon[18] = ir.registerIcon("thaumcraft:clustertin");
      this.icon[3] = ir.registerIcon("thaumcraft:nuggetsilver");
      this.icon[19] = ir.registerIcon("thaumcraft:clustersilver");
      this.icon[4] = ir.registerIcon("thaumcraft:nuggetlead");
      this.icon[20] = ir.registerIcon("thaumcraft:clusterlead");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icon[meta];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 5));
      par3List.add(new ItemStack(this, 1, 21));
      par3List.add(new ItemStack(this, 1, 6));
      par3List.add(new ItemStack(this, 1, 7));
      par3List.add(new ItemStack(this, 1, 16));
      par3List.add(new ItemStack(this, 1, 31));
      if (Config.foundCopperIngot) {
         par3List.add(new ItemStack(this, 1, 1));
         par3List.add(new ItemStack(this, 1, 17));
      }

      if (Config.foundTinIngot) {
         par3List.add(new ItemStack(this, 1, 2));
         par3List.add(new ItemStack(this, 1, 18));
      }

      if (Config.foundSilverIngot) {
         par3List.add(new ItemStack(this, 1, 3));
         par3List.add(new ItemStack(this, 1, 19));
      }

      if (Config.foundLeadIngot) {
         par3List.add(new ItemStack(this, 1, 4));
         par3List.add(new ItemStack(this, 1, 20));
      }

   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
