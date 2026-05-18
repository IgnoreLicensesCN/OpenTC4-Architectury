package thaumcraft.api.aspects;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

//which can be read when in inventory
//TODO:Another display frame below "item name and description(tooltip/hoverText,whatever)"
public interface IAspectDisplayItem<Asp extends Aspect> {
    //do not modify it
    @NotNull
    @UnmodifiableView AspectList<Asp> getAspectsToDisplay(ItemStack stack);
}
