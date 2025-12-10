package thaumcraft.common.items.misc.dispensebehavior;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.projectile.EntityAlumentum;

public class BehaviorDispenseAlumentum extends AbstractProjectileDispenseBehavior {

//   @Override
//   public ItemStack dispense(BlockSource source, ItemStack stack) {
//
//      // 如果 damage != 0，走默认行为
//      if (stack.getDamageValue() != 0) {
//         return DispenseItemBehavior.NOOP.dispense(source, stack);
//      }
//
//      Level level = source.getLevel();
//      Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
//
//      double x = source.x() + dir.getStepX() * 0.7D;
//      double y = source.y() + dir.getStepY() * 0.7D;
//      double z = source.z() + dir.getStepZ() * 0.7D;
//
//      Projectile proj = new EntityAlumentum(level, x, y, z);
//
//      // 与旧版 setThrowableHeading 对应
//      proj.shoot(
//              dir.getStepX(),
//              dir.getStepY() + 0.1F,
//              dir.getStepZ(),
//              1.1F,     // velocity（原来 func_82500_b）
//              6.0F      // inaccuracy（原来 func_82498_a）
//      );
//
//      level.addFreshEntity(proj);
//
//      // play sound
//      level.levelEvent(1009, source.getPos(), 0);
//
//      stack.shrink(1);
//      return stack;
//   }



   @Override
   protected Projectile getProjectile(Level level, Position pos, ItemStack itemStack) {
      return new EntityAlumentum(level, pos.x(), pos.y(), pos.z());
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
}
