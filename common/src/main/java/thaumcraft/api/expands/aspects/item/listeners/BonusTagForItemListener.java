package thaumcraft.api.expands.aspects.item.listeners;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.expands.UnmodifiableAspectList;


public abstract class BonusTagForItemListener implements Comparable<BonusTagForItemListener> {

    public final int priority;
    public BonusTagForItemListener(int priority) {
        this.priority = priority;
    }

    public abstract void onItem(@NotNull Item item,@NotNull ItemStack itemstack,@NotNull  UnmodifiableAspectList sourceTags,@NotNull  AspectList currentAspects);

    @Override
    public int compareTo(@NotNull BonusTagForItemListener o) {
        return Integer.compare(priority, o.priority);
    }
}
