package com.linearity.opentc4.utils.entity.category;

import net.minecraft.world.entity.Entity;

public abstract class EntityCategory<E extends Entity> {
    public abstract boolean isInCategory(Entity entity);
    @SuppressWarnings("unchecked")
    public E getAsCategory(Entity entity){
        return (E) entity;
    }
}
