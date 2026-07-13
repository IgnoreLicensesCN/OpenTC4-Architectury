package thaumcraft.api.listeners.wandconsumption;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.listeners.wandconsumption.consts.CalculateWandConsumptionListenerEnum;
import thaumcraft.api.listeners.wandconsumption.listeners.CalculateWandConsumptionListener;

public class ConsumptionModifierCalculator {
    public static final ListenerManager<CalculateWandConsumptionListener> calculateWandConsumptionListenerManager = new ListenerManager<>();
    
    public static void init(){
        for (var listenerEnum: CalculateWandConsumptionListenerEnum.values()){
            calculateWandConsumptionListenerManager.registerListener(listenerEnum.listener);
        }
    }

    /**
     * {@link CalculateWandConsumptionListener#onCalculation}
     */
    public static float getConsumptionModifier(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, WandConsumptionType wandConsumptionType) {
        var context = new ConsumptionModifierCalculationContext(casting,wandStack,user,aspect,wandConsumptionType,null);
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListenerManager.getListeners()) {
            listener.onCalculation(context);
        }
        return context.currentConsumption;
    }
    //maybe ThaumicEnergistics will back and add their own modifier
    public static float getConsumptionModifier(Item casting, ItemStack wandStack, @Nullable BlockPos pos, Aspect aspect, WandConsumptionType wandConsumptionType) {
        var context = new ConsumptionModifierCalculationContext(casting,wandStack,null,aspect,wandConsumptionType,pos);
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListenerManager.getListeners()) {
            listener.onCalculation(context);
        }
        return context.currentConsumption;
    }
}
