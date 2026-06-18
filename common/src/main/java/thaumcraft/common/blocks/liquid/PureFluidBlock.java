package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class PureFluidBlock extends LiquidBlock {
    public PureFluidBlock(FlowingFluid fluid, Properties props) {
        super(fluid, props);
    }
    public PureFluidBlock() {
        this(
                ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_SOURCE(),
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
        if (!level.isClientSide && blockState.getFluidState().isSource()) {
            if (entity instanceof Player player) {
                if (!player.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.WARP_WARD())){
                    var info = WarpInfo.getFromLivingEntity(player);
                    if (info != null) {
                        int warp = info.getPermWarp();
                        int div = 1;
                        if (warp > 0) {
                            div = (int) Math.sqrt(warp);
                            if (div < 1) {
                                div = 1;
                            }
                        }

                        player.addEffect(
                                new MobEffectInstance(
                                        ThaumcraftEffects.ThaumcraftEffectTypeInstances.WARP_WARD(),
                                        Math.min(32000, 200000 / div),
                                        0
                                )
                        );
                        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }
    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (blockState.getFluidState().isSource()) {
            levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(this.fluid.getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }
}
