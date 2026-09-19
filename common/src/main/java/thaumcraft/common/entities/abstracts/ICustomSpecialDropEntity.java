package thaumcraft.common.entities.abstracts;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface ICustomSpecialDropEntity {
    @Unmodifiable
    List<ItemStack> generateSpecialDrops();
}
