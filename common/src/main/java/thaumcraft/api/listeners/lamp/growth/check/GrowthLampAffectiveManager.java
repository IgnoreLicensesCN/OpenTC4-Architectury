package thaumcraft.api.listeners.lamp.growth.check;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.lamp.growth.GrowthArcaneLampAffectiveBlock;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.GROWTH_LAMP_AFFECTIVE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.GROWTH_LAMP_NOT_AFFECTIVE;

public class GrowthLampAffectiveManager {
    public static final ListenerManager<GrowthLampAffectiveChecker> affectiveConditions = new ListenerManager<>();

    static {
        affectiveConditions.registerListener(new GrowthLampAffectiveChecker(0) {
            @Override
            public void test(GrowthLampAffectiveContext context) {
                if (context.state.is(GROWTH_LAMP_NOT_AFFECTIVE)){
                    context.recommendAffective = false;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new GrowthLampAffectiveChecker(100) {
            @Override
            public void test(GrowthLampAffectiveContext context) {
                if (context.state.is(GROWTH_LAMP_AFFECTIVE)){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new GrowthLampAffectiveChecker(200) {
            @Override
            public void test(GrowthLampAffectiveContext context) {
                if (context.state instanceof BonemealableBlock){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new GrowthLampAffectiveChecker(300) {
            @Override
            public void test(GrowthLampAffectiveContext context) {
                if (context.state instanceof GrowthArcaneLampAffectiveBlock){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
    }
    public static boolean isAffectivePlant(Level level, BlockState state, BlockPos pos) {
        GrowthLampAffectiveContext context = new GrowthLampAffectiveContext(level, pos, state);
        for (var checker : affectiveConditions.getListeners()){
            checker.test(context);
            if (context.endCheck){
                return context.recommendAffective;
            }
        }
        return context.recommendAffective;
    }
}
