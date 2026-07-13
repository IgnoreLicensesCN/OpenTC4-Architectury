package thaumcraft.common.items.abstracts.armorcomponents;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public interface IArmorTickableComponentItem {
    void tickAsComponent(
            @Unmodifiable/*i dont promise this part*/ ItemStack selfStack,
            @Unmodifiable/*i dont promise this part*/ ItemStack parentStack,
            LivingEntity user
            );
}
