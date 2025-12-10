package thaumcraft.api.wands;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

//i want
public interface InventoryTickableComponent {
    void tickAsComponent(ItemStack usingWand, Level level, Entity entity, int i, boolean bl);
}
