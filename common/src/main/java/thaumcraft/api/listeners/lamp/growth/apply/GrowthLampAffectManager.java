package thaumcraft.api.listeners.lamp.growth.apply;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.lamp.growth.IGrowthArcaneLampAffectiveBlock;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.GROWTH_LAMP_AFFECTIVE_RANDOM_TICK;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.GROWTH_LAMP_AFFECTIVE_TICK;

public class GrowthLampAffectManager {


    public static final ListenerManager<GrowthAffectApplier> affectActions = new ListenerManager<>();

    static {
        affectActions.registerListener(new GrowthAffectApplier(-100) {
            @Override
            public void apply(GrowthLampAffectContext context) {
                if (context.state.getBlock() instanceof IGrowthArcaneLampAffectiveBlock affectBlock) {
                    context.endAffect = true;
                    affectBlock.growthLampAffect(context);
                }
            }
        });
        affectActions.registerListener(new GrowthAffectApplier(0) {
            @Override
            public void apply(GrowthLampAffectContext context) {
                if (context.state.is(GROWTH_LAMP_AFFECTIVE_RANDOM_TICK)){
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
        });
        affectActions.registerListener(new GrowthAffectApplier(100) {
            @Override
            public void apply(GrowthLampAffectContext context) {
                if (context.state.is(GROWTH_LAMP_AFFECTIVE_TICK)){
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
        });
        affectActions.registerListener(new GrowthAffectApplier(200) {
            @Override
            public void apply(GrowthLampAffectContext context) {
                if (context.state.getBlock() instanceof BonemealableBlock bonemealable) {
                    context.endAffect = true;
                    if (context.level instanceof ServerLevel serverLevel) {
                        bonemealable.performBonemeal(serverLevel,context.level.random,context.pos,context.state);
                    }
                }
            }
        });
    }

    public static void affectPlant(Level level,BlockState state,BlockPos pos) {
        var context = new GrowthLampAffectContext(level, pos, state);
        for (var checker : affectActions.getListeners()){
            checker.apply(context);
            if (context.endAffect){
                break;
            }
        }
    }
}
