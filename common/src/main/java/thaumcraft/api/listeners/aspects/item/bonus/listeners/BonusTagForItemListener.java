package thaumcraft.api.listeners.aspects.item.bonus.listeners;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;


public abstract class BonusTagForItemListener implements Comparable<BonusTagForItemListener> {

    public final int priority;
    public BonusTagForItemListener(int priority) {
        this.priority = priority;
    }

    public abstract void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull  UnmodifiableAspectList<Aspect> sourceTags, @NotNull  AspectList<Aspect> currentAspects);

    @Override
    public int compareTo(@NotNull BonusTagForItemListener o) {
        return Integer.compare(priority, o.priority);
    }
}
