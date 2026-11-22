package com.linearity.opentc4.utils.entity.category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityCategoryByType<E extends Entity> extends EntityCategory<E> {
    public final EntityType<E> entityType;

    @Override
    public boolean isInCategory(Entity entity) {
        return entity.getType() == entityType;
    }

    private static final Map<EntityType<?>,EntityCategoryByType<?>> instances = new HashMap<>();
    public static <E extends Entity> EntityCategoryByType<E> of(EntityType<E> type) {
        return (EntityCategoryByType<E>) instances.computeIfAbsent(type, EntityCategoryByType::new);
    }


}
