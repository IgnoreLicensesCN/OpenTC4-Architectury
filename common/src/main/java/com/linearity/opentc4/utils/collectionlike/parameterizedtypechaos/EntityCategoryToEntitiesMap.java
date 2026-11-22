package com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos;

import com.linearity.opentc4.utils.entity.category.EntityCategory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.Entity;

import java.util.*;

@SuppressWarnings("unchecked")
public class EntityCategoryToEntitiesMap /*extends Map<Class<? extends Entity>,Set<? extends Entity>>*/ {
    private final Map<EntityCategory<? extends Entity>, Set<? extends Entity>> innerMap = new HashMap<>();

    public <E extends Entity> Set<E> get(EntityCategory<E> clazz) {
        return (Set<E>) innerMap.get(clazz);
    }

    public <E extends Entity> void put(EntityCategory<E> clazz, Set<E> entities) {
        innerMap.put(clazz, entities);
    }

    public <E extends Entity> void add(EntityCategory<E> clazz, E entity) {
        getOrCreate(clazz).add(entity);
    }

    public <E extends Entity> Set<E> getOrCreate(EntityCategory<E> clazz) {
        return (Set<E>) innerMap.computeIfAbsent(clazz, k -> new HashSet<>());
    }

    public void clear() {
        innerMap.clear();
    }

    //dont hold this,DO use once you get this
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnifiedEntry<E extends Entity> implements Map.Entry<EntityCategory<E>, Set<E>> {
        private Map.Entry<EntityCategory<E>, Set<E>> innerEntry;

        @Override
        public EntityCategory<E> getKey() {
            return innerEntry.getKey();
        }

        @Override
        public Set<E> getValue() {
            return innerEntry.getValue();
        }

        @Override
        public Set<E> setValue(Set<E> value) {
            return innerEntry.setValue(value);
        }
    }

    public Iterator<UnifiedEntry<? extends Entity>> entrySetIterator() {
        return new Iterator<>() {
            private final UnifiedEntry<? extends Entity> iterationCache =
                    new UnifiedEntry<>();
            private final Iterator<Map.Entry<EntityCategory<? extends Entity>, Set<? extends Entity>>> innerIterator =
                    innerMap.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return innerIterator.hasNext();
            }

            @Override
            public UnifiedEntry<? extends Entity> next() {
                iterationCache.innerEntry = (Map.Entry) innerIterator.next();
                return iterationCache;
            }
        };
    }
}
