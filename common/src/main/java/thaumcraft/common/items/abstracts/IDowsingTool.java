package thaumcraft.common.items.abstracts;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

//TODO Elemental pickaxe use this
public interface IDowsingTool {
    default boolean canDowsing(ItemStack stack, @Nullable LivingEntity user){
        return true;
    }
}
