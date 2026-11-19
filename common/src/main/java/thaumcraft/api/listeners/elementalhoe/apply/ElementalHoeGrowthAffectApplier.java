package thaumcraft.api.listeners.elementalhoe.apply;

import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.elementalhoe.IElementalHoeAffectiveBlock;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_AFFECTIVE_RANDOM_TICK;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_AFFECTIVE_TICK;

public abstract class ElementalHoeGrowthAffectApplier implements Comparable<ElementalHoeGrowthAffectApplier> {
    public final int priority;

    public ElementalHoeGrowthAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull ElementalHoeGrowthAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(ElementalHoeAffectContext context);
    
    @AllArgsConstructor
    public enum DefaultImpl {
        INSTANCE_AFFECT(
                new ElementalHoeGrowthAffectApplier(-100) {
                    @Override
                    public void apply(ElementalHoeAffectContext context) {
                        instanceAffect(context);
                    }
                }
        ),
        RANDOM_TICK_AFFECT_BY_TAG(
                new ElementalHoeGrowthAffectApplier(0) {
                    @Override
                    public void apply(ElementalHoeAffectContext context) {
                        randomTickAffectByTag(context);
                    }
                }
        ),
        TICK_AFFECT_BY_TAG(
                new ElementalHoeGrowthAffectApplier(100) {
                    @Override
                    public void apply(ElementalHoeAffectContext context) {
                        tickAffectByTag(context);
                    }
                }
        ),
        BONEMEAL_AFFECTIVE_AFFECT(
                new ElementalHoeGrowthAffectApplier(200) {
                    @Override
                    public void apply(ElementalHoeAffectContext context) {
                        bonemealable(context);
                    }
                }
        );

        public static void bonemealable(ElementalHoeAffectContext context) {
            BlockState state = context.state;
            Level level = context.level;
            BlockPos pos = context.pos;
            if (state.getBlock() instanceof BonemealableBlock bonemealable) {
                if (bonemealable.isValidBonemealTarget(level, pos, state,level.isClientSide)) {
                    context.endAffect = true;
                    if (level instanceof ServerLevel serverLevel) {
                        bonemealable.performBonemeal(serverLevel, level.random, pos, state);
                    }
                }
            }
        }

        ;

        public static void tickAffectByTag(ElementalHoeAffectContext context) {
            if (context.state.is(ELEMENTAL_HOE_AFFECTIVE_TICK)){
                context.endAffect = true;
                if (context.level instanceof ServerLevel serverLevel) {
                    context.state.getBlock()
                            .tick(
                                    context.state,
                                    serverLevel,
                                    context.pos,
                                    serverLevel.random
                            );
                }
            }
        }

        public static void randomTickAffectByTag(ElementalHoeAffectContext context) {
            if (context.state.is(ELEMENTAL_HOE_AFFECTIVE_RANDOM_TICK)){
                context.endAffect = true;
                if (context.level instanceof ServerLevel serverLevel) {
                    context.state.getBlock()
                            .randomTick(
                                    context.state,
                                    serverLevel,
                                    context.pos,
                                    serverLevel.random
                            );
                }
            }
        }

        public static void instanceAffect(ElementalHoeAffectContext context) {
            if (context.state.getBlock() instanceof IElementalHoeAffectiveBlock affectBlock) {
                context.endAffect = true;
                affectBlock.growthLampAffect(context);
            }
        }

        public final ElementalHoeGrowthAffectApplier applier;
    }
}
