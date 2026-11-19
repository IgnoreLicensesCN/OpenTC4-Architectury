package thaumcraft.api.listeners.elementalhoe.apply;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalHoeAffectManager {

    public static final ListenerManager<ElementalHoeGrowthAffectApplier> AFFECT_ACTIONS = new ListenerManager<>();

    public static void affectPlant(Level level,BlockState state,BlockPos pos) {
        var context = new ElementalHoeAffectContext(level, pos, state);
        for (var checker : AFFECT_ACTIONS.getListeners()){
            checker.apply(context);
            if (context.endAffect){
                break;
            }
        }
    }
    public static void init(){
        for (var applierEnum:ElementalHoeGrowthAffectApplier.DefaultImpl.values()){
            AFFECT_ACTIONS.registerListener(applierEnum.applier);
        }
    }
}
