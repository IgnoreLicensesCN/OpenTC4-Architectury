package com.linearity.opentc4.fabric.mixinaccessor;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Map;

public interface AttributeSupplierAccessor {
    Map<Attribute, AttributeInstance> opentc4$getAttributesMap();

}
