package thaumcraft.api.expands.listeners.wandconsumption;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.wandconsumption.consts.CalculateWandConsumptionListenerEnum;
import thaumcraft.api.expands.listeners.wandconsumption.listeners.CalculateWandConsumptionListener;

public class ConsumptionModifierCalculator {
    public static final ListenerManager<CalculateWandConsumptionListener> calculateWandConsumptionListenerManager = new ListenerManager<>();
    
    public static void init(){
        for (var listenerEnum: CalculateWandConsumptionListenerEnum.values()){
            calculateWandConsumptionListenerManager.registerListener(listenerEnum.listener);
        }
    }

    /**
     * {@link CalculateWandConsumptionListener#onCalculation(Item, ItemStack, LivingEntity, Aspect, boolean, float)}
     */
    public static float getConsumptionModifier(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting) {
        float consumptionModifier = 1.0F;
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListenerManager.getListeners()) {
            consumptionModifier = listener.onCalculation(casting,wandStack,user,aspect,crafting,consumptionModifier);
        }
        return consumptionModifier;
    }
}
