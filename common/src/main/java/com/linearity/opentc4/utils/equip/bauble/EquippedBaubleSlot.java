package com.linearity.opentc4.utils.equip.bauble;

import com.linearity.opentc4.utils.equip.IPlayerEquippedSlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public record EquippedBaubleSlot(
        String slotType,
        int index
)implements IPlayerEquippedSlotAccess {

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
    public ItemStack getEquippedStack(Player player) {
        return platformUtils.getEquippedItem(player,this).orElse(ItemStack.EMPTY);
    }
}

