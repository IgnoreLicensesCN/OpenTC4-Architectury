package thaumcraft.api.listeners.wandconsumption.listeners;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculationContext;

public abstract class CalculateWandConsumptionListener implements Comparable<CalculateWandConsumptionListener> {
    public final int priority;
    public CalculateWandConsumptionListener(int priority) {
        this.priority = priority;
    }

    public abstract void onCalculation(ConsumptionModifierCalculationContext context);

    @Override
    public int compareTo(@NotNull CalculateWandConsumptionListener o) {
        return Integer.compare(priority, o.priority);
    }
}
