package com.linearity.opentc4.utils.bauble;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record EquippedBaubleSlot(
        String slotType,
        int index
) {

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
    
}

