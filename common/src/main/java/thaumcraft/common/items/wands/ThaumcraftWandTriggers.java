package thaumcraft.common.items.wands;

import thaumcraft.common.items.abstracts.wandabstraction.IWandTriggerManager;

import static thaumcraft.common.multiparts.constructmatch.MultipartMatcherImpls.INFERNAL_FURNACE_CONSTRUCT_MATCHER;
import static thaumcraft.common.multiparts.placers.MultipartPlacerImpls.INFERNAL_FURNACE_PLACER;

//TODO:ListenerManager(and one of them is Block(instance) map matchers(get another listenerManager))
public class ThaumcraftWandTriggers {
    public static final IWandTriggerManager INFERNAL_FURNACE_WAND_TRIGGER = (level,usingWand,player,pos,side) ->
    {
        var matchInfo = INFERNAL_FURNACE_CONSTRUCT_MATCHER.match(level,pos);
        if (matchInfo != null) {
            //TODO:Place and return true if works

            INFERNAL_FURNACE_PLACER.place(level,pos,matchInfo);
        }
        return false;
    };
}
