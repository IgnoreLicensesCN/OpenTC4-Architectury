package thaumcraft.common.items.abstracts.wandabstraction.wand;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static thaumcraft.common.items.ThaumcraftItems.ItemTags.NODE_HARMFUL;

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
        return wandComponentsAnyMatch(componentOwnerStack,component -> component.is(NODE_HARMFUL));
    }

}
