package thaumcraft.common.items.wands;

import thaumcraft.api.wands.IWandTriggerManager;

import static thaumcraft.common.multiparts.constructmatch.MultipartMatcherImpls.INFERNAL_FURNACE_BEFORE_FORMING;

public class ThaumcraftWandTriggers {
    public static final IWandTriggerManager INFERNAL_FURNACE_WAND_TRIGGER = (level,usingWand,player,pos,side) ->
    {
        var matchInfo = INFERNAL_FURNACE_BEFORE_FORMING.match(level,pos);
        if (matchInfo != null) {
            //TODO:Place and return true if works

        }
        return false;
    };
}
