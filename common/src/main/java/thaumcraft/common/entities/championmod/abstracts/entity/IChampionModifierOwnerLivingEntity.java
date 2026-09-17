package thaumcraft.common.entities.championmod.abstracts.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.function.BiPredicate;

public interface IChampionModifierOwnerLivingEntity {
    //use when added or removed modifier after entity triggered modifier
    void opentc4$refreshChampionModifierCheckedState(BiPredicate<LivingEntity, ?> checker,Class<?> modifierClass);
    <ModifierClass> @Unmodifiable /*at least i dont want it modified*/ Set<ModifierClass> opentc4$getChampionModifiersForChecker(
            /*singleton*/BiPredicate<LivingEntity,ModifierClass> checker, Class<ModifierClass> championModifierClass
    );
}
