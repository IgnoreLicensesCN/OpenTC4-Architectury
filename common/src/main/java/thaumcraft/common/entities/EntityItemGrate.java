package thaumcraft.common.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;

public class EntityItemGrate extends EntityItem {
   public EntityItemGrate(World par1World) {
      super(par1World);
   }

   public EntityItemGrate(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      super(par1World, par2, par4, par6, par8ItemStack);
   }

   protected boolean func_145771_j(double x, double y, double z) {
      int i = MathHelper.floor_double(x);
      int j = MathHelper.floor_double(y);
      int k = MathHelper.floor_double(z);
      return this.worldObj.getBlock(i, j, k) == ConfigBlocks.blockMetalDevice && (this.worldObj.getBlockMetadata(i, j, k) == 5 || this.worldObj.getBlockMetadata(i, j, k) == 6) || super.func_145771_j(x, y, z);
   }
}
