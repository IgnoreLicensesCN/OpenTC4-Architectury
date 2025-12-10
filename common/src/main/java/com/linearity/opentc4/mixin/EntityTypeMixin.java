package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.EntityTypeAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> implements EntityTypeAccessor<T> {

    @Shadow @Final private EntityType.EntityFactory<T> factory;

    @Override
    public EntityType.EntityFactory<T> opentc4$getFactory() {
        return factory;
    }
}

