package thaumcraft.api.wands;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//put on a Block or BlockEntity.
//if a block impl this and it's be impl this both will trigger
//a wand should check and activate this part
public interface IWandInteractableBlockOrBlockEntity {

    //if InteractionResult.CONSUME,hold right click will lead to loooooong use(call #interact below every tick).
    @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext);

    //InteractionResult.CONSUME above to get here every using tick
    default void interactOnWandInteractable(Level level, LivingEntity livingEntity, ItemStack usingWand, int useRemainingCount){};
}
