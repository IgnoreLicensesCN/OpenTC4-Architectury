package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IArmorComponentsOwnerItem {
    List<ItemStack> getArmorComponents(ItemStack componentOwnerStack);
    @UtilityLikeAbstraction(reason = "reducing code and 'forEachWithBreak' without stream")
    default boolean armorComponentsAnyMatch(ItemStack componentOwnerStack, Predicate<ItemStack>/*ToBooleanFunction*/ matcher){
        for (var armorComponent : getArmorComponents(componentOwnerStack)) {
            if (matcher.test(armorComponent)) {
                return true;
            }
        }
        return false;
    }
    @UtilityLikeAbstraction
    default void armorComponentsForEach(ItemStack componentOwnerStack, Consumer<ItemStack> consumer){
        for (var armorComponent : getArmorComponents(componentOwnerStack)) {
            consumer.accept(armorComponent);
        }
    }
}
