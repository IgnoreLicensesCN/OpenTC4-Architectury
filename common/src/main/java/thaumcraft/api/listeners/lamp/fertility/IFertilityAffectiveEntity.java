package thaumcraft.api.listeners.lamp.fertility;

import com.linearity.opentc4.utils.entity.category.EntityCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.listeners.lamp.fertility.apply.FertilityLampAffectContext;

public interface IFertilityAffectiveEntity<C extends EntityCategory<E>,E extends Entity & IFertilityAffectiveEntity<C,E>> {
    boolean fertilityLampAffect(FertilityLampAffectContext<C,E> context);
    @Nullable("when not useable") C getFertilityLampCategory();

    abstract class IFertilityAffectiveCategory<
            C extends IFertilityAffectiveCategory<C,E>,
            E extends Entity & IFertilityAffectiveEntity<C,E>>
            extends EntityCategory<E>{
    }
}
