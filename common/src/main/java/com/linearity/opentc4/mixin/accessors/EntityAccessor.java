package com.linearity.opentc4.mixin.accessors;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("random")
    RandomSource opentc4$Entity$getRandom();
    @Invoker("reapplyPosition")
    void reapplyPosition();
}
