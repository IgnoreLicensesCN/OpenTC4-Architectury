package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import static thaumcraft.common.blocks.liquid.ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GAS_FLUID;

public class FluxGasBlock extends FiniteLiquidBlock {
   public FluxGasBlock() {
      super(
              FLUX_GAS_FLUID(),
              Properties.of()
              .strength(-1.0F, 3600000.0F)
                      .noOcclusion()
              .noCollission()
              .randomTicks()
              .liquid()
      );
   }


   public static BlockState fullOfGas(){
      var blockInstance = ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_GAS();
      return blockInstance.defaultBlockState().setValue(
              blockInstance.finiteFluid.liquidLevel,
              blockInstance.finiteFluid.maxLevel);
   };
   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      int lvl = state.getValue(LEVEL);
      if (!(entity instanceof LivingEntity living)) return;

      if (level.random.nextInt(10) != 0) return;

      boolean isTainted = entity instanceof ITaintedMob;
      if (isTainted) return;

      MobEffectInstance effect;
      if (level.random.nextBoolean()) {
         effect = new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.VIS_EXHAUST(), 1200, lvl / 3, true, false);
      } else {
         effect = new MobEffectInstance(MobEffects.CONFUSION, 80 + lvl * 20, 0, false, false,false);
      }
      living.addEffect(effect);

      finiteFluid.decreaseOrRemove(level,pos,state.getFluidState());
   }
}
