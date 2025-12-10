package com.linearity.opentc4.simpleutils.bauble;

import java.util.Objects;

public record EquippedBaubleSlot(
        String namespace,   // "curios", "trinkets"
        String slotType,    // "ring", "necklace", "charm"
        int index           // 同类槽位下的第几个
) {
    public static EquippedBaubleSlot curios(String slotType, int index) {
        return new EquippedBaubleSlot("curios", slotType, index);
    }

    public static EquippedBaubleSlot trinkets(String slotType, int index) {
        return new EquippedBaubleSlot("trinkets", slotType, index);
    }

    @Override
    public String toString() {
        return namespace + ":" + slotType + "#" + index;
    }

    public static EquippedBaubleSlot parse(String s) {
        // curios:ring#0
        var parts = s.split("[:#]");
        return new EquippedBaubleSlot(parts[0], parts[1], Integer.parseInt(parts[2]));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EquippedBaubleSlot(String namespace1, String type, int index1))) return false;
        return index == index1 && Objects.equals(slotType, type) && Objects.equals(
                namespace, namespace1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, slotType, index);
    }


}

