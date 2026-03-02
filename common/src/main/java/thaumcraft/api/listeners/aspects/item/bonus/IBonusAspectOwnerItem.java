package thaumcraft.api.listeners.aspects.item.bonus;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IBonusAspectOwnerItem<Asp extends Aspect> {
    AspectList<Asp> getOwningBonusAspects(ItemStack stack);
}
