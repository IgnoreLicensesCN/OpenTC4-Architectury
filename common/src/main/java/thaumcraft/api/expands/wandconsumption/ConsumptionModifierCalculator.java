package thaumcraft.api.expands.wandconsumption;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import simpleutils.ListenerManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.common.items.wands.ItemWandCasting;

import static thaumcraft.api.expands.wandconsumption.consts.CalculateWandConsumptionListeners.*;

public class ConsumptionModifierCalculator {
    public static final ListenerManager<CalculateWandConsumptionListener> calculateWandConsumptionListenerManager = new ListenerManager<>();
    
    public static void init(){
        calculateWandConsumptionListenerManager.registerListener(CASTING_MODIFIER);
        calculateWandConsumptionListenerManager.registerListener(PLAYER_DISCOUNT);
        calculateWandConsumptionListenerManager.registerListener(FOCUS_DISCOUNT);
        calculateWandConsumptionListenerManager.registerListener(SCEPTRE);
        calculateWandConsumptionListenerManager.registerListener(SET_MIN);
    }

    /**
     * {@link CalculateWandConsumptionListener#onCalculation(ItemWandCasting, ItemStack, Player, Aspect, boolean, float)}
     */
    public static float getConsumptionModifier(ItemWandCasting casting, ItemStack is, Player player, Aspect aspect, boolean crafting) {
        float consumptionModifier = 1.0F;
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListenerManager.getListeners()) {
            consumptionModifier = listener.onCalculation(casting,is,player,aspect,crafting,consumptionModifier);
        }
        return consumptionModifier;
    }
}
