package thaumcraft.api.expands.wandconsumption.listeners;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ItemWandCasting;

import javax.annotation.Nonnull;

public abstract class CalculateWandConsumptionListener implements Comparable<CalculateWandConsumptionListener> {
    public final int priority;
    public CalculateWandConsumptionListener(int priority) {
        this.priority = priority;
    }

    /**
     * after all of these listeners,we have the percent of cost.
     * @param casting the casting using.
     * @param stack itemstack of the wand
     * @param player the player using wand.
     * @param aspect the aspect costing.each (primal aspect) will be calculated separately.
     * @param crafting if this operation is crafting item.
     * @param currentConsumption current consumption percent of this operation.
     * @return consumption percent after this calculation
     */
    public abstract float onCalculation(ItemWandCasting casting, ItemStack stack, Player player, Aspect aspect, boolean crafting,float currentConsumption);

    @Override
    public int compareTo(@Nonnull CalculateWandConsumptionListener o) {
        return Integer.compare(priority, o.priority);
    }
}
