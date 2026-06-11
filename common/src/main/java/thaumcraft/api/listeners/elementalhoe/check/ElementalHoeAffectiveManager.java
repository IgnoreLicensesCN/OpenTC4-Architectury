package thaumcraft.api.listeners.elementalhoe.check;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.elementalhoe.IElementalHoeAffectiveBlock;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_AFFECTIVE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_NOT_AFFECTIVE;

public class ElementalHoeAffectiveManager {
    public static final ListenerManager<ElementalHoeAffectiveChecker> affectiveConditions = new ListenerManager<>();

    static {
        affectiveConditions.registerListener(new ElementalHoeAffectiveChecker(0) {
            @Override
            public void test(ElementalHoeAffectiveContext context) {
                if (context.state.is(ELEMENTAL_HOE_NOT_AFFECTIVE)){
                    context.recommendAffective = false;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new ElementalHoeAffectiveChecker(100) {
            @Override
            public void test(ElementalHoeAffectiveContext context) {
                if (context.state.is(ELEMENTAL_HOE_AFFECTIVE)){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new ElementalHoeAffectiveChecker(200) {
            @Override
            public void test(ElementalHoeAffectiveContext context) {
                if (context.state instanceof BonemealableBlock){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new ElementalHoeAffectiveChecker(300) {
            @Override
            public void test(ElementalHoeAffectiveContext context) {
                if (context.state instanceof IElementalHoeAffectiveBlock){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
    }
    public static boolean isAffectivePlant(Level level, BlockState state, BlockPos pos) {
        ElementalHoeAffectiveContext context = new ElementalHoeAffectiveContext(level, pos, state);
        for (var checker : affectiveConditions.getListeners()){
            checker.test(context);
            if (context.endCheck){
                return context.recommendAffective;
            }
        }
        return context.recommendAffective;
    }
}
