package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;

//TODO:CrystalEssence drops
public class DeathFluidBlock extends FiniteLiquidBlock{
    public DeathFluidBlock(FiniteFlowingFluid fluid, Properties props) {
        super(fluid, props);
    }
    public DeathFluidBlock() {
        this(
                ThaumcraftFluids.DEATH_FLUID_FLOWING,
                Properties.of()                      // 方块属性
                        .mapColor(MapColor.COLOR_PURPLE) // 方块颜色
                        .strength(-1.0F, 3600000.0F)     // 不可破坏
                        .noOcclusion()
                        .noCollission()                   // 无碰撞
                        .randomTicks()                    // 支持随机 tick
                        .liquid()
                );
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!level.isClientSide) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.hurt(
                        new DamageSource(
                                ThaumcraftDamageSources.getHolder(
                                        level,
                                        ThaumcraftDamageSources.DISSOLVE
                                )
                        ),
                        blockState.getValue(finiteFluid.liquidLevel) + 1
                );
            }
        }
    }
}
