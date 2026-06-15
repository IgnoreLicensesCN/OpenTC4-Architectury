package thaumcraft.common.items.dispensebehavior;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.projectile.AlumentumEntity;

public class BehaviorDispenseAlumentum extends AbstractProjectileDispenseBehavior {


   @Override
   protected @NotNull Projectile getProjectile(Level level, Position pos, ItemStack itemStack) {
      return new AlumentumEntity(level, pos.x(), pos.y(), pos.z());
   }

   @Override
   protected void playSound(BlockSource source) {
      source.getLevel().playSound(
              null,
              source.x(),
              source.y(),
              source.z(),
              SoundEvents.DISPENSER_LAUNCH,
              SoundSource.BLOCKS,
              1.0F,
              1.0F
      );
   }
}
//
//public class BehaviorDispenseAlumetum extends BehaviorProjectileDispense {
//   public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
//      if (par2ItemStack.getItemDamage() != 0) {
//         BehaviorDefaultDispenseItem def = new BehaviorDefaultDispenseItem();
//         return def.dispense(par1IBlockSource, par2ItemStack);
//      } else {
//         World var3 = par1IBlockSource.getWorld();
//         IPosition var4 = BlockDispenser.func_149939_a(par1IBlockSource);
//         EnumFacing var5 = BlockDispenser.func_149937_b(par1IBlockSource.getBlockMetadata());
//         IProjectile var6 = this.getProjectileEntity(var3, var4);
//         var6.setThrowableHeading(var5.getFrontOffsetX(), (float)var5.getFrontOffsetY() + 0.1F, var5.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
//         var3.spawnEntityInWorld((Entity)var6);
//         par2ItemStack.splitStack(1);
//         return par2ItemStack;
//      }
//   }
//
//   protected IProjectile getProjectileEntity(Level par1World, IPosition par2IPosition) {
//      return new EntityAlumentum(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
//   }
//
//   protected void playDispenseSound(IBlockSource par1IBlockSource) {
//      par1IBlockSource.getWorld().playAuxSFX(1009, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
//   }
//}
