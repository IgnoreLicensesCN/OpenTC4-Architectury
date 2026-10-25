package thaumcraft.common.entities.championmod.abstracts.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

import static thaumcraft.common.entities.championmod.ChampionModifierManager.REGISTERED_MODIFIERS;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.CHAMPION_MOD;

public interface IChampionModifierOwnerLivingEntity {
    //use when added or removed modifier after entity triggered modifier
    <ModifierClass> void opentc4$refreshChampionModifierCheckedState(BiPredicate<LivingEntity, ModifierClass> checker,Class<ModifierClass> modifierClass);
    <ModifierClass> @Unmodifiable /*at least i dont want it modified*/ Set<ModifierClass> opentc4$getChampionModifiersForChecker(
            /*singleton*/BiPredicate<LivingEntity,ModifierClass> checker, Class<ModifierClass> championModifierClass
    );

    class ChampionModifierMultiMap {
        protected final Map<Class<?>,Map<Class<BiPredicate<LivingEntity, /*ModifierClass*/ Object>>, Set</*ModifierClass*/ ?>>> internalMap = new ConcurrentHashMap<>();

        @SuppressWarnings("unchecked")
        public <ModifierClass> Set<ModifierClass> get(BiPredicate<LivingEntity, ModifierClass> checker,
                                      Class<ModifierClass> championModifierClass,
                                                      LivingEntity target
        ){
            return (Set<ModifierClass>) internalMap.get(championModifierClass).computeIfAbsent(
                    (Class<BiPredicate<LivingEntity, Object>>) checker.getClass(),
                    k -> {
                        Set<ModifierClass> result = ConcurrentHashMap.newKeySet();
                        var attributeInstance = target.getAttribute(CHAMPION_MOD());
                        if (attributeInstance != null) {
                            for (var attributeModifier : attributeInstance.getModifiers()) {
                                var modifier = REGISTERED_MODIFIERS.get(attributeModifier.getId());
                                if (modifier != null) {
                                    if (championModifierClass.isInstance(modifier)) {
                                        var modifierCasted = (ModifierClass) modifier;
                                        if (checker.test(target, modifierCasted)) {
                                            result.add(modifierCasted);
                                        }
                                    }
                                }
                            }
                        }
                        return result;
                    }
                    );
        }
        public <ModifierClass> void remove(BiPredicate<LivingEntity, ModifierClass> checker,Class<ModifierClass> modifierClass){
            var mapOrNull = internalMap.get(modifierClass);
            if (mapOrNull != null) {
                mapOrNull.remove(checker.getClass());
            }
        }
    }
}
