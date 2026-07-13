package com.linearity.opentc4.utils.equip.bauble;

import com.linearity.opentc4.utils.equip.ILivingEntityEquippedSlotAccess;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public record EquippedBaubleSlot(
        String slotType,
        int index
)implements ILivingEntityEquippedSlotAccess {

    @Override
    public @NotNull String toString() {
        return slotType + "#" + index;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EquippedBaubleSlot(String type, int index1))) return false;
        return index == index1 && Objects.equals(slotType, type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotType, index);
    }

    @Override
    public ItemStack getEquippedStack(LivingEntity living) {
        var capability = AccessoriesCapability.get(living);
        if (capability != null) {
            var container = capability.getContainers().get(slotType);
            if (container == null){
                return ItemStack.EMPTY;
            }
            if (container.getSize() <= index){
                return ItemStack.EMPTY;
            }
            return container.getAccessories().getItem(index);
        }
        return ItemStack.EMPTY;
    }
}

