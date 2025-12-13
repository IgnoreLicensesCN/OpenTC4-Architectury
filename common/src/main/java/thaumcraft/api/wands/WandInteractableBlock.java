package thaumcraft.api.wands;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface WandInteractableBlock {

    //if InteractionResult.CONSUME,hold right click will lead to loooooong use(call #interact below every tick).
    @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext);

    //InteractionResult.CONSUME above to get here every using tick
    default void interact(Level level, LivingEntity livingEntity, ItemStack usingWand, int useCount){};
}
