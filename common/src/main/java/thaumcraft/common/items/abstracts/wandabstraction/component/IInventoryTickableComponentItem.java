package thaumcraft.common.items.abstracts.wandabstraction.component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//i want
public interface IInventoryTickableComponentItem {

    void tickAsComponent(
            @NotNull("null->self") ItemStack finalParentStack,
            @NotNull("null->self") ItemStack directParentStack,
            @NotNull("null->self") ItemStack selfStack,
            Level level,
            Entity owner,
            int finalParentAtContainerIndex,
            boolean parentSelected
    );
}
