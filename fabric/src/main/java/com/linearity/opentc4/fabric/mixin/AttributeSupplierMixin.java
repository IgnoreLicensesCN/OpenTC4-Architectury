package com.linearity.opentc4.fabric.mixin;

import com.linearity.opentc4.fabric.mixinaccessor.AttributeSupplierAccessor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(AttributeSupplier.class)
public class AttributeSupplierMixin implements AttributeSupplierAccessor {
    @Final
    @Shadow private Map<Attribute, AttributeInstance> instances;

    @Override
    public Map<Attribute, AttributeInstance> opentc4$getAttributesMap() {
        return instances;
    }
}

