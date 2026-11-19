package thaumcraft.api.listeners.elementalhoe.check;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.elementalhoe.IElementalHoeAffectiveBlock;

public class ElementalHoeAffectiveManager {
    public static final ListenerManager<ElementalHoeAffectiveChecker> AFFECTIVE_CONDITIONS = new ListenerManager<>();

    public static boolean isAffectivePlant(Level level, BlockState state, BlockPos pos) {
        ElementalHoeAffectiveContext context = new ElementalHoeAffectiveContext(level, pos, state);
        for (var checker : AFFECTIVE_CONDITIONS.getListeners()){
            checker.test(context);
            if (context.endCheck){
                return context.recommendAffective;
            }
        }
        return context.recommendAffective;
    }
    public static void init(){
        for (var instanceEnum: ElementalHoeAffectiveChecker.DefaultImpl.values()){
            AFFECTIVE_CONDITIONS.registerListener(instanceEnum.instance);
        }
    }
}
