package thaumcraft.api.wands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface InventoryTickable {
    void tick(Player player, ItemStack stack,int tickCount,int slot,SlotType slotType);
    enum SlotType {
        MAIN_HAND,
        OFF_HAND,
        HEAD,
        CHEST,
        LEGS,
        FEET,
        INVENTORY
    }
}
