package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import static thaumcraft.common.blocks.liquid.ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GOO_FLUID;

public class FluxGooBlock extends FiniteLiquidBlock {
    public FluxGooBlock() {
        super(
                FLUX_GOO_FLUID(),
                Properties.of()
                        .mapColor(MapColor.COLOR_PURPLE)
                        .strength(-1.0F, 3600000.0F)
                        .noOcclusion()
                        .noCollission()
                        .randomTicks()
                        .liquid()
        );
    }
    public static BlockState fullOfGoo(){
        var blockInstance = ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_GOO();
        return blockInstance.defaultBlockState().setValue(
                blockInstance.finiteFluid.liquidLevel,
                blockInstance.finiteFluid.maxLevel
        );
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        var fluidState = state.getFluidState();
        int lvl = fluidState.getAmount();

        if (entity instanceof EntityThaumicSlime slime) {
            if (slime.getSlimeSize() < lvl && level.random.nextBoolean()) {
                slime.setSlimeSize(slime.getSlimeSize() + 1);
                finiteFluid.decreaseOrRemove(level, pos, fluidState);
            }
            return;
        }

        float pct = lvl / 8.0f;
        entity.setDeltaMovement(
                entity.getDeltaMovement().multiply(1.0 - pct, 1.0, 1.0 - pct)
        );

        if (entity instanceof LivingEntity living) {
            MobEffectInstance eff =
                    new MobEffectInstance(
                            ThaumcraftEffects.ThaumcraftEffectTypeInstances.VIS_EXHAUST(),
                            600,
                            lvl / 3,
                            true, false);
            living.addEffect(eff);
        }
    }

}
