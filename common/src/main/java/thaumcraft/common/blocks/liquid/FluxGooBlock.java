package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import static thaumcraft.common.ThaumcraftSounds.GORE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.FLUX_GAS;
import static thaumcraft.common.blocks.liquid.ThaumcraftFluids.FLUX_GOO_FLOWING;

public class FluxGooBlock extends FiniteLiquidBlock {
    public FluxGooBlock() {
        super(FLUX_GOO_FLOWING, Properties.of()                      // 方块属性
                        .mapColor(MapColor.COLOR_PURPLE) // 方块颜色
                        .strength(-1.0F, 3600000.0F)     // 不可破坏
                        .noCollission()                   // 无碰撞
                        .randomTicks()                    // 支持随机 tick
                        .liquid()
                , Direction.DOWN);
    }

    //    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(LEVEL);
//    }
//
//    @Override
//    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
//        return FULL;
//    }
//
//    // =========================================
//    // ENTITY INTERACTION
//    // =========================================
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        int lvl = state.getValue(LEVEL);

        // 示例：处理 Slime（你替换为自己类）
        if (entity instanceof EntityThaumicSlime slime) {
            if (slime.getSlimeSize() < lvl && level.random.nextBoolean()) {
                slime.setSlimeSize(slime.getSlimeSize() + 1);
                decreaseOrRemove(level, pos, state);
            }
            return;
        }

        // 摩擦力减速
        float pct = lvl / 8.0f;
        entity.setDeltaMovement(
                entity.getDeltaMovement().multiply(1.0 - pct, 1.0, 1.0 - pct)
        );

        // 生物效果
        if (entity instanceof LivingEntity living) {
            MobEffectInstance eff =
                    new MobEffectInstance(
                            ThaumcraftEffects.VIS_EXHAUST,
                            600,
                            lvl / 3,
                            true, false);
            living.addEffect(eff);
        }
    }

    // =========================================
    // TICK UPDATE
    // =========================================
    @Override
    public void tick(@NotNull BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        super.tick(state, level, pos, rand);
        int lvl = state.getValue(LEVEL);

        // 生成 Slime（还原 1.7.10）
        if (lvl >= 2 && lvl < 6 && level.isEmptyBlock(pos.above())) {
            if (rand.nextInt(25) == 0) {
                spawnSlime(level, pos, 1);
                level.removeBlock(pos, false);
                return;
            }
        }
        if (lvl >= 6 && level.isEmptyBlock(pos.above())) {
            if (rand.nextInt(25) == 0) {
                spawnSlime(level, pos, 2);
                level.removeBlock(pos, false);
                return;
            }
        }

        // 流体蒸发 / 减少
        if (rand.nextInt(30) == 0) {
            if (lvl == 0) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(LEVEL, lvl - 1), 3);

                // 生成 Flux Gas（还原 1.7.10）
                if (rand.nextBoolean() && level.isEmptyBlock(pos.above())) {
                    level.setBlock(pos.above(),
                            FLUX_GAS.defaultBlockState(),
                            3
                    );
                }
            }
        }

    }

    // =========================================
    // SLIME SPAWN
    // =========================================
    private void spawnSlime(ServerLevel level, BlockPos pos, int size) {
        EntityThaumicSlime slime =
                new EntityThaumicSlime(level);
        slime.setSlimeSize(size);
        slime.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(slime);
        level.playSound(null, pos, GORE,
                net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    // =========================================
    // UTILS
    // =========================================
    private void decreaseOrRemove(Level level, BlockPos pos, BlockState state) {
        int lvl = state.getValue(LEVEL);
        if (lvl <= 1)
            level.removeBlock(pos, false);
        else
            level.setBlock(pos, state.setValue(LEVEL, lvl - 1), 3);
    }
}
