package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemBathSalts extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemBathSalts() {
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:bath_salts");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public int getEntityLifespan(ItemStack itemStack, World world) {
      return 200;
   }
}
