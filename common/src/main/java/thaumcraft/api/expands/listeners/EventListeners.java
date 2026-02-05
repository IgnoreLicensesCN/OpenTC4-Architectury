package thaumcraft.api.expands.listeners;

import thaumcraft.api.expands.listeners.aspects.item.ItemAspectBonusTagsCalculator;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectManager;
import thaumcraft.api.expands.listeners.wandconsumption.ConsumptionModifierCalculator;
import thaumcraft.api.expands.listeners.warp.WarpEventManager;
import thaumcraft.api.expands.listeners.worldgen.node.NodeGenerationManager;

public class EventListeners {
    public static void init(){
        NodeGenerationManager.init();
        ItemAspectBonusTagsCalculator.init();
        ConsumptionModifierCalculator.init();
        WarpEventManager.init();
        WriteAspectManager.init();
    }
}
