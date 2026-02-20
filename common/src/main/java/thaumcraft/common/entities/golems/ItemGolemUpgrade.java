package thaumcraft.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;

import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemGolemUpgrade extends Item {
   public IIcon[] icon = new IIcon[6];
   public IIcon iconEmpty;

   public ItemGolemUpgrade() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconEmpty = ir.registerIcon("thaumcraft:golem_upgrade_empty");
      this.icon[0] = ir.registerIcon("thaumcraft:golem_upgrade_air");
      this.icon[1] = ir.registerIcon("thaumcraft:golem_upgrade_earth");
      this.icon[2] = ir.registerIcon("thaumcraft:golem_upgrade_fire");
      this.icon[3] = ir.registerIcon("thaumcraft:golem_upgrade_water");
      this.icon[4] = ir.registerIcon("thaumcraft:golem_upgrade_order");
      this.icon[5] = ir.registerIcon("thaumcraft:golem_upgrade_entropy");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int d) {
      return d < 0 ? this.iconEmpty : this.icon[d];
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

   public void addInformation(ItemStack stack, Player par2Player, List list, boolean par4) {
      list.add(Component.translatable("item.ItemGolemUpgrade." + stack.getItemDamage() + ".desc"));
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }
}
