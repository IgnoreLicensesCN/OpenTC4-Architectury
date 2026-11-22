package thaumcraft.api.listeners.lamp.fertility.apply;

import com.linearity.opentc4.utils.entity.category.EntityCategory;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

@RequiredArgsConstructor
public class FertilityLampAffectContext<C extends EntityCategory<E>,E extends Entity> {
    public final C entityCategory;
    public final @UnmodifiableView Set<E> entities;
    public final int recommendedEntityLimit;
    public boolean endAffect = false;
    public boolean affected = false;
    public <C2 extends EntityCategory<E2>,E2 extends Entity> FertilityLampAffectContext<C2,E2> castWith(C2 affectiveCategory){
        return (FertilityLampAffectContext<C2,E2>)this;
    }
}
