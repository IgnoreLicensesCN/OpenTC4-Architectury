package com.linearity.opentc4.mixin.accessors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityType.class)
public interface EntityTypeAccessor<T extends Entity> {

    @Accessor("factory")
    EntityType.EntityFactory<T> getFactory();
}
