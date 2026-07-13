package thaumcraft.api.listeners.wandconsumption;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
public class ConsumptionModifierCalculationContext {
    /**
     * after all of these listeners,we have the percent of cost.
     * @param casting the casting using.may not be WandCastingItem
     * @param wandStack itemstack of the wand
     * @param user the user using wand.
     * @param aspect the aspect costing.each (primal aspect) will be calculated separately.
     * @param crafting if this operation is crafting item.
     * @param currentConsumption current consumption percent of this operation(will pass to after_.
     */
    public final Item casting;
    public final ItemStack wandStack;
    public final @Nullable LivingEntity user;
    public final Aspect aspect;
//    public final boolean crafting;
    public final WandConsumptionType wandConsumptionType;
    //if there's a blockEntity calculating put BE pos
    public final @Nullable BlockPos atPos;
    public float currentConsumption = 1;

    public ConsumptionModifierCalculationContext(
            Item casting,
            ItemStack wandStack,
            @Nullable LivingEntity user,
            Aspect aspect,
            WandConsumptionType wandConsumptionType,
            @Nullable BlockPos atPos) {
        this.casting = casting;
        this.wandStack = wandStack;
        this.user = user;
        this.aspect = aspect;
        this.wandConsumptionType = wandConsumptionType;
        this.atPos = atPos;
    }
}
