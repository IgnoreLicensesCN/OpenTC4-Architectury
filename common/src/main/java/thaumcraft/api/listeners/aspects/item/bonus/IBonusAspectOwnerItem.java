package thaumcraft.api.listeners.aspects.item.bonus;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;

public interface IBonusAspectOwnerItem<Asp extends Aspect> {
    @Unmodifiable//usually
    @NotNull
    AspectList<Asp> getOwningBonusAspects(ItemStack stack);
}
