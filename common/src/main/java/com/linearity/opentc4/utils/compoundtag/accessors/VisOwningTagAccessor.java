package com.linearity.opentc4.utils.compoundtag.accessors;

import com.google.gson.JsonObject;
import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;

public class VisOwningTagAccessor extends CompoundTagAccessor<Map<Aspect, Integer>> {
    private final JsonObjectTagAccessor internalAccessor;

    public VisOwningTagAccessor(String tagKey) {
        super(tagKey, (Class<Map<Aspect, Integer>>) (Class<?>) Map.class);
        internalAccessor = new JsonObjectTagAccessor(tagKey);
    }

    @Override
    public Map<Aspect, Integer> readFromCompoundTag(CompoundTag tag) {
        Map<Aspect, Integer> map = new HashMap<>();
        if (!tag.contains(tagKey)) return map; // 没有该tag返回空
        var json = internalAccessor.readFromCompoundTag(tag);
        if (json == null) return map;
        for (var entry : json.entrySet()) {
            String aspectName = entry.getKey();
            var aspect = ALL_ASPECTS.get(AspectResourceLocation.of(aspectName));
            if (aspect == null) {
                OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", aspectName, tagKey);
                continue;
            }
            int value;
            try {
                value = entry.getValue()
                        .getAsInt();
            } catch (Exception e) {
                OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", aspectName, tagKey, e);
                continue;
            }
            map.put(aspect, value);
        }
        return map;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Map<Aspect, Integer> value) {
        if (value.isEmpty()) return;
        JsonObject json = new JsonObject();
        for (var entry : value.entrySet()) {
            var aspectResourceLocation = entry.getKey()
                    .getAspectKey();
            json.addProperty(String.valueOf(aspectResourceLocation), entry.getValue());
        }
        internalAccessor.writeToCompoundTag(tag, json);
//            tag.put(tagKey, value.save(new CompoundTag()));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }

}
