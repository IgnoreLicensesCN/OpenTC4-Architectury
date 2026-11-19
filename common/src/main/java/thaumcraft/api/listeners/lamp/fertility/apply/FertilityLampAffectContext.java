package thaumcraft.api.listeners.lamp.fertility.apply;

import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

@RequiredArgsConstructor
public class FertilityLampAffectContext<E extends Entity> {
    public final Class<E> entityClass;
    public final @UnmodifiableView Set<E> entities;
    public final int recommendedEntityLimit;
    public boolean endAffect = false;
    public boolean affected = false;

}
