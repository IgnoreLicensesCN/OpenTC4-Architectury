package thaumcraft.api.listeners.wandconsumption.listeners;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

public abstract class CalculateWandConsumptionListener implements Comparable<CalculateWandConsumptionListener> {
    public final int priority;
    public CalculateWandConsumptionListener(int priority) {
        this.priority = priority;
    }

    /**
     * after all of these listeners,we have the percent of cost.
     * @param casting the casting using.may not be WandCastingItem
     * @param wandStack itemstack of the wand
     * @param user the user using wand.
     * @param aspect the aspect costing.each (primal aspect) will be calculated separately.
     * @param crafting if this operation is crafting item.
     * @param currentConsumption current consumption percent of this operation.
     * @return consumption percent after this calculation
     */
    public abstract float onCalculation(Item casting, ItemStack wandStack, @Nullable LivingEntity user, Aspect aspect, boolean crafting, float currentConsumption);

    @Override
    public int compareTo(@NotNull CalculateWandConsumptionListener o) {
        return Integer.compare(priority, o.priority);
    }
}
