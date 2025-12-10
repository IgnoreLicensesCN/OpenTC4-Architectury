package thaumcraft.common.items.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;

public class ItemTripleMeatTreat extends ItemFood {
   public IIcon icon;

   public ItemTripleMeatTreat() {
      super(6, 0.8F, true);
      this.setAlwaysEdible();
      this.setPotionEffect(Potion.regeneration.id, 5, 0, 0.66F);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:tripletreat");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icon;
   }
}
