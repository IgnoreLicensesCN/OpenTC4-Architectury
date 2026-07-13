package thaumcraft.api.listeners.aspects.entity.basic;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;


public abstract class EntityBasicAspectGetter implements Comparable<EntityBasicAspectGetter> {
    public final int weight;
    public EntityBasicAspectGetter(int weight) {
        this.weight = weight;
    }
    public abstract void onGetBasicAspect(Entity entityToGetAspect, @Modifiable /*this is the result*/ AspectList<Aspect> aspects);
    @Override
    public int compareTo(@NotNull EntityBasicAspectGetter o) {
        return Integer.compare(weight, o.weight);
    }
}
