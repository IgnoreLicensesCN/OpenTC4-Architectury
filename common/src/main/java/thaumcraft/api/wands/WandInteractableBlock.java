package thaumcraft.api.wands;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface WandInteractableBlock {

    @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext);
    void interact(Level level, LivingEntity livingEntity, ItemStack usingWand, int useCount);
}
