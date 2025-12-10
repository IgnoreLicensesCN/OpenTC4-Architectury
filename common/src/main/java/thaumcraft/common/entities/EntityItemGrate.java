package thaumcraft.common.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import thaumcraft.common.config.ConfigBlocks;

public class EntityItemGrate extends EntityItem {
   public EntityItemGrate(Level par1World) {
      super(par1World);
   }

   public EntityItemGrate(Level par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      super(par1World, par2, par4, par6, par8ItemStack);
   }

   protected boolean func_145771_j(double x, double y, double z) {
      int i = MathHelper.floor_double(x);
      int j = MathHelper.floor_double(y);
      int k = MathHelper.floor_double(z);
      return this.level().getBlock(i, j, k) == ConfigBlocks.blockMetalDevice && (this.level().getBlockMetadata(i, j, k) == 5 || this.level().getBlockMetadata(i, j, k) == 6) || super.func_145771_j(x, y, z);
   }
}
