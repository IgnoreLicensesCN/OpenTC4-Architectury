package thaumcraft.api.wands;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IWandComponentsOwnerItem {
    List<ItemStack> getWandComponents(ItemStack componentOwnerStack);
    @UtilityLikeAbstraction(reason = "reducing code and 'forEachWithBreak' without stream")
    default boolean wandComponentsAnyMatch(ItemStack componentOwnerStack,Predicate<ItemStack>/*ToBooleanFunction*/ matcher){
        for (var wandComponent : getWandComponents(componentOwnerStack)) {
            if (matcher.test(wandComponent)) {
                return true;
            }
        }
        return false;
    }
    @UtilityLikeAbstraction
    default void wandComponentsForEach(ItemStack componentOwnerStack,Consumer<ItemStack> consumer){
        for (var wandComponent : getWandComponents(componentOwnerStack)) {
            consumer.accept(wandComponent);
        }
    }
    @UtilityLikeAbstraction
    default boolean isWandNodeHarmful(ItemStack componentOwnerStack){
        return wandComponentsAnyMatch(componentOwnerStack,component -> component.getItem() instanceof INodeHarmfulComponent);
    }

}
