package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import static thaumcraft.common.blocks.liquid.ThaumcraftFluids.FLUX_GAS_FLOWING;

public class FluxGasBlock extends FiniteLiquidBlock {


   private static final VoxelShape FULL = Shapes.block();

   public FluxGasBlock() {
      super(FLUX_GAS_FLOWING,Properties.of()
              .strength(-1.0F, 3600000.0F)
              .noCollission()
              .randomTicks()
              .liquid(),Direction.UP
      );
   }
//
//   @Override
//   public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
//      return FULL;
//   }

   // =========================================
   // ENTITY INTERACTION
   // =========================================
   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      int lvl = state.getValue(LEVEL);
      if (!(entity instanceof LivingEntity living)) return;

      // 随机生效概率
      if (level.random.nextInt(10) != 0) return;

      // TODO: 替换成 ITaintedMob 判断
      boolean isTainted = entity instanceof ITaintedMob;
      if (isTainted) return;

      // TODO: 替换为实际 Vis Exhaust / Confusion MobEffect
      MobEffectInstance effect;
      if (level.random.nextBoolean()) {
         effect = new MobEffectInstance(ThaumcraftEffects.VIS_EXHAUST, 1200, lvl / 3, true, false);
      } else {
         effect = new MobEffectInstance(MobEffects.CONFUSION, 80 + lvl * 20, 0, false, false,false);
      }
      living.addEffect(effect);

      // LEVEL 减少 / 消失
      if (lvl > 0) {
         level.setBlock(pos, state.setValue(LEVEL, lvl - 1), 3);
      } else {
         level.removeBlock(pos, false);
      }
   }

   // =========================================
   // BLOCK PLACEMENT
   // =========================================
   @Override
   public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
      return false;
   }
//   // =========================================
//   // RANDOM TICK
//   // =========================================
//   @Override
//   public void tick(@NotNull BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
//      super.tick(state, level, pos, rand);
//   }
}
