package com.linearity.opentc4.utils;

import com.google.gson.*;
import com.linearity.opentc4.OpenTC4;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.nbt.Tag.*;
import static thaumcraft.api.aspects.Aspect.aspects;

public class CompoundTagHelper {
    public static abstract class CompoundTagAccessor<T>{
        public final String tagKey;
        public final Class<T> tagClass;

        protected CompoundTagAccessor(String tagKey, Class<T> tagClass) {
            this.tagKey = tagKey;
            this.tagClass = tagClass;
        }

        public abstract T readFromCompoundTag(CompoundTag tag);
        public abstract void writeToCompoundTag(CompoundTag tag, T value);
        public abstract boolean compoundTagHasKey(CompoundTag tag);
    }
    public static class JsonObjectTagAccessor extends CompoundTagAccessor<JsonObject> {

        private static final Gson GSON = new Gson();

        public JsonObjectTagAccessor(String tagKey) {
            super(tagKey, JsonObject.class);
        }

        @Override
        public JsonObject readFromCompoundTag(CompoundTag tag) {
            if (!tag.contains(tagKey)) return new JsonObject(); // 没有该tag返回空对象
            String jsonString = tag.getString(tagKey);
            try {
                return JsonParser.parseString(jsonString).getAsJsonObject();
            } catch (JsonSyntaxException | IllegalStateException e) {
                e.printStackTrace();
                return new JsonObject(); // 解析失败返回空对象
            }
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, JsonObject value) {
            if (value == null || value.entrySet().isEmpty()) return;
            tag.putString(tagKey, GSON.toJson(value));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey);
        }
    }
    public static class VisOwningTagAccessor extends CompoundTagAccessor<Map<Aspect,Integer>> {
        private static final Map<Aspect,Integer> classProvider = Collections.emptyMap();
        private final JsonObjectTagAccessor internalAccessor;

        public VisOwningTagAccessor(String tagKey) {
            super(tagKey, (Class<Map<Aspect, Integer>>) classProvider.getClass());
            internalAccessor = new JsonObjectTagAccessor(tagKey);
        }

        @Override
        public Map<Aspect,Integer> readFromCompoundTag(CompoundTag tag) {
            Map<Aspect,Integer> map = new HashMap<>();
            if (!tag.contains(tagKey)) return map; // 没有该tag返回空
            var json = internalAccessor.readFromCompoundTag(tag);
            if (json == null) return map;
            for (var entry : json.entrySet()) {
                String aspectName = entry.getKey();
                var aspect = aspects.get(aspectName);
                if (aspect == null) {
                    OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", aspectName, tagKey);
                    continue;
                }
                int value = 0;
                try {
                    value = entry.getValue().getAsInt();
                }catch (Exception e) {
                    OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", aspectName, tagKey,e);
                    continue;
                }
                map.put(aspect,value);
            }
            return map;
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Map<Aspect,Integer> value) {
            if (value.isEmpty()) return;
            JsonObject json = new JsonObject();
            for (var entry : value.entrySet()) {
                var aspect = entry.getKey().getTag();
                json.addProperty(aspect,entry.getValue());
            }
            internalAccessor.writeToCompoundTag(tag,json);
//            tag.put(tagKey, value.save(new CompoundTag()));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey);
        }

    }
    public static class ItemStackTagAccessor extends CompoundTagAccessor<ItemStack> {

        public ItemStackTagAccessor(String tagKey) {
            super(tagKey, ItemStack.class);
        }

        @Override
        public ItemStack readFromCompoundTag(CompoundTag tag) {
            if (!tag.contains(tagKey)) return ItemStack.EMPTY; // 没有该tag返回空
            return ItemStack.of(tag.getCompound(tagKey));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, ItemStack value) {
            if (value.isEmpty()) return;
            tag.put(tagKey, value.save(new CompoundTag()));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey);
        }

    }
    public static class StringTagAccessor extends CompoundTagAccessor<String> {

        public StringTagAccessor(String tagKey) {
            super(tagKey, String.class);
        }

        @Override
        public String readFromCompoundTag(CompoundTag tag) {
            return tag.getString(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, String value) {
            tag.putString(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_STRING);
        }
    }

    public static class IntTagAccessor extends CompoundTagAccessor<Integer> {

        public IntTagAccessor(String tagKey) {
            super(tagKey, Integer.class);
        }

        @Override
        public Integer readFromCompoundTag(CompoundTag tag) {
            return tag.getInt(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Integer value) {
            tag.putInt(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_INT);
        }
    }

    public static class BooleanTagAccessor extends CompoundTagAccessor<Boolean> {

        public BooleanTagAccessor(String tagKey) {
            super(tagKey, Boolean.class);
        }

        @Override
        public Boolean readFromCompoundTag(CompoundTag tag) {
            return tag.getBoolean(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Boolean value) {
            tag.putBoolean(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_BYTE); // boolean 实际存 byte
        }
    }

    public static class FloatTagAccessor extends CompoundTagAccessor<Float> {

        public FloatTagAccessor(String tagKey) {
            super(tagKey, Float.class);
        }

        @Override
        public Float readFromCompoundTag(CompoundTag tag) {
            return tag.getFloat(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Float value) {
            tag.putFloat(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_FLOAT);
        }
    }

    public static class LongTagAccessor extends CompoundTagAccessor<Long> {

        public LongTagAccessor(String tagKey) {
            super(tagKey, Long.class);
        }

        @Override
        public Long readFromCompoundTag(CompoundTag tag) {
            return tag.getLong(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Long value) {
            tag.putLong(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_LONG);
        }
    }

    public static class ShortTagAccessor extends CompoundTagAccessor<Short> {

        public ShortTagAccessor(String tagKey) {
            super(tagKey, Short.class);
        }

        @Override
        public Short readFromCompoundTag(CompoundTag tag) {
            return tag.getShort(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Short value) {
            tag.putLong(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_LONG);
        }
    }
    public static class ByteTagAccessor extends CompoundTagAccessor<Byte> {

        public ByteTagAccessor(String tagKey) {
            super(tagKey, Byte.class);
        }

        @Override
        public Byte readFromCompoundTag(CompoundTag tag) {
            return tag.getByte(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Byte value) {
            tag.putByte(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_BYTE);
        }
    }

    public static class CompoundTagAccessorImpl extends CompoundTagAccessor<CompoundTag> {

        public CompoundTagAccessorImpl(String tagKey) {
            super(tagKey, CompoundTag.class);
        }

        @Override
        public CompoundTag readFromCompoundTag(CompoundTag tag) {
            return tag.getCompound(tagKey);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, CompoundTag value) {
            tag.put(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_COMPOUND);
        }
    }

    public static abstract class GenericListTagAccessor<T extends Tag> extends CompoundTagAccessor<ListTag> {
        public final int elementType;

        public GenericListTagAccessor(String tagKey, int elementType) {
            super(tagKey, ListTag.class);
            this.elementType = elementType;
        }

        @Override
        public ListTag readFromCompoundTag(CompoundTag tag) {
            return tag.getList(tagKey, elementType);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, ListTag value) {
            tag.put(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_LIST);
        }

    }

//    public static class ByteListAccessor extends GenericListTagAccessor<ByteTag> {
//        public ByteListAccessor(String tagKey) {
//            super(tagKey, Tag.TAG_BYTE);
//        }
//    }
//
//    public static class IntListAccessor extends GenericListTagAccessor<IntTag> {
//        public IntListAccessor(String tagKey) {
//            super(tagKey, Tag.TAG_INT);
//        }
//    }
//
//    public static class LongListAccessor extends GenericListTagAccessor<LongTag> {
//        public LongListAccessor(String tagKey) {
//            super(tagKey, Tag.TAG_LONG);
//        }
//    }
//
//    public static class StringListAccessor extends GenericListTagAccessor<StringTag> {
//        public StringListAccessor(String tagKey) {
//            super(tagKey, Tag.TAG_STRING);
//        }
//    }

    public static class CompoundListAccessor extends GenericListTagAccessor<CompoundTag> {
        public CompoundListAccessor(String tagKey) {
            super(tagKey, Tag.TAG_COMPOUND);
        }
    }

    public static class ListTagAccessor extends CompoundTagAccessor<ListTag> {

        public ListTagAccessor(String tagKey) {
            super(tagKey, ListTag.class);
        }

        @Override
        public ListTag readFromCompoundTag(CompoundTag tag) {
            return tag.getList(tagKey, Tag.TAG_COMPOUND); // 默认 ListTag 存放 CompoundTag
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, ListTag value) {
            tag.put(tagKey, value);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_LIST);
        }
    }


}
