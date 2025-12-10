package thaumcraft.api.expands.wandconsumption;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;

import static thaumcraft.api.expands.wandconsumption.consts.CalculateWandConsumptionListeners.*;

public class ConsumptionModifierCalculator {
    public static final ListenerManager<CalculateWandConsumptionListener> calculateWandConsumptionListenerManager = new ListenerManager<>();
    
    public static void init(){
        calculateWandConsumptionListenerManager.registerListener(CASTING_MODIFIER);
        calculateWandConsumptionListenerManager.registerListener(PLAYER_DISCOUNT);
        calculateWandConsumptionListenerManager.registerListener(FOCUS_DISCOUNT);
        calculateWandConsumptionListenerManager.registerListener(SCEPTRE);
        calculateWandConsumptionListenerManager.registerListener(ENSURE_LOWER_BOUND);
    }

    /**
     * {@link CalculateWandConsumptionListener#onCalculation(Item, ItemStack, LivingEntity, Aspect, boolean, float)}
     */
    public static float getConsumptionModifier(Item casting, ItemStack is, @Nullable LivingEntity user, Aspect aspect, boolean crafting) {
        float consumptionModifier = 1.0F;
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListenerManager.getListeners()) {
            consumptionModifier = listener.onCalculation(casting,is,user,aspect,crafting,consumptionModifier);
        }
        return consumptionModifier;
    }
}
