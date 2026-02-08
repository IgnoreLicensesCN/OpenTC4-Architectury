package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

public class JsonObjectTagAccessor extends CompoundTagAccessor<JsonObject> {

    private static final Gson GSON = new Gson();

    public JsonObjectTagAccessor(String tagKey) {
        super(tagKey, JsonObject.class);
    }

    @Override
    public JsonObject readFromCompoundTag(CompoundTag tag) {
        if (!tag.contains(tagKey)) return new JsonObject(); // 没有该tag返回空对象
        String jsonString = tag.getString(tagKey);
        try {
            return JsonParser.parseString(jsonString)
                    .getAsJsonObject();
        } catch (JsonSyntaxException | IllegalStateException e) {
            OpenTC4.LOGGER.error("Error parsing JSON object", e);
            return new JsonObject(); // 解析失败返回空对象
        }
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, JsonObject value) {
        if (value == null || value.entrySet()
                .isEmpty()) return;
        tag.putString(tagKey, GSON.toJson(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }
}
