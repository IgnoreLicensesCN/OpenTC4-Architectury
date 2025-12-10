package thaumcraft.common.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;

public class ItemWandRod extends Item {
   public IIcon[] iconWand = new IIcon[8];
   public IIcon[] iconStaff = new IIcon[8];
   public IIcon iconPrimalStaff;

   public ItemWandRod() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconWand[0] = ir.registerIcon("thaumcraft:wand_rod_greatwood");
      this.iconWand[1] = ir.registerIcon("thaumcraft:wand_rod_obsidian");
      this.iconWand[2] = ir.registerIcon("thaumcraft:wand_rod_silverwood");
      this.iconWand[3] = ir.registerIcon("thaumcraft:wand_rod_ice");
      this.iconWand[4] = ir.registerIcon("thaumcraft:wand_rod_quartz");
      this.iconWand[5] = ir.registerIcon("thaumcraft:wand_rod_reed");
      this.iconWand[6] = ir.registerIcon("thaumcraft:wand_rod_blaze");
      this.iconWand[7] = ir.registerIcon("thaumcraft:wand_rod_bone");
      this.iconStaff[0] = ir.registerIcon("thaumcraft:staff_rod_greatwood");
      this.iconStaff[1] = ir.registerIcon("thaumcraft:staff_rod_obsidian");
      this.iconStaff[2] = ir.registerIcon("thaumcraft:staff_rod_silverwood");
      this.iconStaff[3] = ir.registerIcon("thaumcraft:staff_rod_ice");
      this.iconStaff[4] = ir.registerIcon("thaumcraft:staff_rod_quartz");
      this.iconStaff[5] = ir.registerIcon("thaumcraft:staff_rod_reed");
      this.iconStaff[6] = ir.registerIcon("thaumcraft:staff_rod_blaze");
      this.iconStaff[7] = ir.registerIcon("thaumcraft:staff_rod_bone");
      this.iconPrimalStaff = ir.registerIcon("thaumcraft:staff_rod_primal");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return meta < 50 ? this.iconWand[meta] : (meta < 100 ? this.iconStaff[meta - 50] : this.iconPrimalStaff);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      List<ItemStack> list = (List<ItemStack>) par3List;
      list.add(new ItemStack(this, 1, 0));
      list.add(new ItemStack(this, 1, 1));
      list.add(new ItemStack(this, 1, 2));
      list.add(new ItemStack(this, 1, 3));
      list.add(new ItemStack(this, 1, 4));
      list.add(new ItemStack(this, 1, 5));
      list.add(new ItemStack(this, 1, 6));
      list.add(new ItemStack(this, 1, 7));
      list.add(new ItemStack(this, 1, 50));
      list.add(new ItemStack(this, 1, 51));
      list.add(new ItemStack(this, 1, 52));
      list.add(new ItemStack(this, 1, 53));
      list.add(new ItemStack(this, 1, 54));
      list.add(new ItemStack(this, 1, 55));
      list.add(new ItemStack(this, 1, 56));
      list.add(new ItemStack(this, 1, 57));
      list.add(new ItemStack(this, 1, 100));
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
