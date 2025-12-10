package thaumcraft.api.expands.aspects.item.listeners;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.expands.UnmodifiableAspectList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BonusTagForItemListener implements Comparable<BonusTagForItemListener> {

    public final int priority;
    public BonusTagForItemListener(int priority) {
        this.priority = priority;
    }

    public abstract void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects);

    @Override
    public int compareTo(@Nonnull BonusTagForItemListener o) {
        return Integer.compare(priority, o.priority);
    }
}
