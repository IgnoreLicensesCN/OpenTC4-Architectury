package thaumcraft.api.listeners;

import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectCalculator;
import thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator;
import thaumcraft.api.listeners.researchtable.WriteAspectManager;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator;
import thaumcraft.api.listeners.warp.WarpEventManager;
import thaumcraft.api.listeners.worldgen.node.NodeGenerationManager;

public class EventListeners {
    public static void init(){
        ItemBasicAspectCalculator.init();
        NodeGenerationManager.init();
        ItemBonusAspectCalculator.init();
        ConsumptionModifierCalculator.init();
        WarpEventManager.init();
        WriteAspectManager.init();
    }
}
