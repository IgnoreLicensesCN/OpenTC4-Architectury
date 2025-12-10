package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface  EntityTypeAccessor<T extends Entity> {

    EntityType.EntityFactory<T> opentc4$getFactory();
}
