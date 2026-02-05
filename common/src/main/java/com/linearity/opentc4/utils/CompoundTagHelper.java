package com.linearity.opentc4.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.linearity.opentc4.OpenTC4;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.research.HexType;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.*;

import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;

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
                OpenTC4.LOGGER.error("Error parsing JSON object", e);
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

    public static class StringSetTagAccessor extends CompoundTagAccessor<Set<String>> {
        private final ListTagAccessor LIST_TAG_ACCESSOR;
        public StringSetTagAccessor(String tagKey) {
            super(tagKey, (Class<Set<String>>) (Class<?>)Set.class);
            this.LIST_TAG_ACCESSOR = new ListTagAccessor(tagKey);
        }

        @Override
        public Set<String> readFromCompoundTag(CompoundTag tag) {
            ListTag listTag = LIST_TAG_ACCESSOR.readFromCompoundTag(tag);
            List<String> list = new ArrayList<>(listTag.size());
            for (int i=0;i<listTag.size();i++) {
                String element = listTag.getString(i);
                list.add(element);
            }
            return Set.of(list.toArray(new String[0]));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Set<String> value) {
            ListTag listTag = new ListTag();
            value.forEach(s -> listTag.add(StringTag.valueOf(s)));
            LIST_TAG_ACCESSOR.writeToCompoundTag(tag, listTag);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey);
        }
    }

    public static class VisOwningTagAccessor extends CompoundTagAccessor<Map<Aspect,Integer>> {
        private final JsonObjectTagAccessor internalAccessor;

        public VisOwningTagAccessor(String tagKey) {
            super(tagKey, (Class<Map<Aspect, Integer>>) (Class<?>)Map.class);
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
                var aspect = ALL_ASPECTS.get(new AspectResourceLocation(aspectName));
                if (aspect == null) {
                    OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", aspectName, tagKey);
                    continue;
                }
                int value;
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
                var aspectResourceLocation = entry.getKey().getAspectKey();
                json.addProperty(String.valueOf(aspectResourceLocation),entry.getValue());
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
    public static class ResourceLocationTagAccessor extends CompoundTagAccessor<ResourceLocation> {

        public ResourceLocationTagAccessor(String tagKey) {
            super(tagKey, ResourceLocation.class);
        }

        @Override
        public ResourceLocation readFromCompoundTag(CompoundTag tag) {
            return new ResourceLocation(tag.getString(tagKey));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag,@NotNull ResourceLocation value) {
            tag.putString(tagKey, String.valueOf(value));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_STRING);
        }
    }
    public static class AspectResourceLocationTagAccessor extends CompoundTagAccessor<AspectResourceLocation> {
        public AspectResourceLocationTagAccessor(String tagKey) {
            super(tagKey, AspectResourceLocation.class);
        }

        @Override
        public AspectResourceLocation readFromCompoundTag(CompoundTag tag) {
            return new AspectResourceLocation(tag.getString(tagKey));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag,@NotNull AspectResourceLocation value) {
            tag.putString(tagKey, String.valueOf(value));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_STRING);
        }
    }
    public static class ResearchItemResourceLocationTagAccessor extends CompoundTagAccessor<ResearchItemResourceLocation> {

        public ResearchItemResourceLocationTagAccessor(String tagKey) {
            super(tagKey, ResearchItemResourceLocation.class);
        }

        @Override
        public ResearchItemResourceLocation readFromCompoundTag(CompoundTag tag) {
            return new ResearchItemResourceLocation(tag.getString(tagKey));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag,@NotNull ResearchItemResourceLocation value) {
            tag.putString(tagKey, String.valueOf(value));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_STRING);
        }
    }
    public static class ClueResourceLocationTagAccessor extends CompoundTagAccessor<ClueResourceLocation> {

        public ClueResourceLocationTagAccessor(String tagKey) {
            super(tagKey, ClueResourceLocation.class);
        }

        @Override
        public ClueResourceLocation readFromCompoundTag(CompoundTag tag) {
            return new ClueResourceLocation(tag.getString(tagKey));
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag,@NotNull ClueResourceLocation value) {
            tag.putString(tagKey, String.valueOf(value));
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return tag.contains(tagKey, Tag.TAG_STRING);
        }
    }
    public static class AspectAccessor extends CompoundTagAccessor<Aspect> {
        protected final ResourceLocationTagAccessor resourceLocationAccessor;
        public AspectAccessor(String tagKey) {
            super(tagKey, Aspect.class);
            resourceLocationAccessor = new ResourceLocationTagAccessor(tagKey);
        }

        @Override
        @NotNull
        public Aspect readFromCompoundTag(CompoundTag tag) {
            var resLoc = resourceLocationAccessor.readFromCompoundTag(tag);
            if (resLoc.getPath().isEmpty()){
                return Aspects.EMPTY;
            }
            var result = Aspects.ALL_ASPECTS.get(resLoc);
            if (result == null) {
                OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", resLoc, tag,new Exception());
                return Aspects.EMPTY;
            }
            return result;
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag,@NotNull Aspect value) {
            resourceLocationAccessor.writeToCompoundTag(tag,value.getAspectKey());
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

    public static class HexCoordAccessor extends CompoundTagAccessor<HexCoord> {
        protected final IntTagAccessor qAccessorInternal;
        protected final IntTagAccessor rAccessorInternal;

        protected HexCoordAccessor(String tagKey) {
            super(tagKey, HexCoord.class);
            this.qAccessorInternal = new IntTagAccessor(tagKey + "_q");
            this.rAccessorInternal = new IntTagAccessor(tagKey + "_r");
        }

        @Override
        public HexCoord readFromCompoundTag(CompoundTag tag) {
            var q = qAccessorInternal.readFromCompoundTag(tag);
            var r = rAccessorInternal.readFromCompoundTag(tag);
            return new HexCoord(q,r);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, HexCoord value) {
            qAccessorInternal.writeToCompoundTag(tag, value.q());
            rAccessorInternal.writeToCompoundTag(tag, value.r());
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return qAccessorInternal.compoundTagHasKey(tag) && rAccessorInternal.compoundTagHasKey(tag);
        }
    }
    public static class HexTypeAccessor extends CompoundTagAccessor<HexType> {
        protected final IntTagAccessor intTypeAccessorInternal;
        public HexTypeAccessor(String tagKey) {
            super(tagKey, HexType.class);
            this.intTypeAccessorInternal = new IntTagAccessor(tagKey + "_type_int");
        }

        @Override
        public HexType readFromCompoundTag(CompoundTag tag) {
            return HexType.values()[intTypeAccessorInternal.readFromCompoundTag(tag)];
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, HexType value){
            intTypeAccessorInternal.writeToCompoundTag(tag,value.ordinal());
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return intTypeAccessorInternal.compoundTagHasKey(tag);
        }
    }
    public static class HexEntryAccessor extends CompoundTagAccessor<HexEntry> {
        protected final AspectResourceLocationTagAccessor aspectResLocAccessorInternal;
        protected final HexTypeAccessor hexTypeAccessorInternal;
        public HexEntryAccessor(String tagKey) {
            super(tagKey, HexEntry.class);
            this.aspectResLocAccessorInternal = new AspectResourceLocationTagAccessor(tagKey + "_aspect_res");
            this.hexTypeAccessorInternal = new HexTypeAccessor(tagKey + "_hex_type");
        }

        @Override
        public HexEntry readFromCompoundTag(CompoundTag tag) {
            var aspectLoc = aspectResLocAccessorInternal.readFromCompoundTag(tag);
            var hexType = hexTypeAccessorInternal.readFromCompoundTag(tag);
            return new HexEntry(Aspect.getAspect(aspectLoc),hexType);
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, HexEntry value) {
            aspectResLocAccessorInternal.writeToCompoundTag(tag,value.aspect().aspectKey);
            hexTypeAccessorInternal.writeToCompoundTag(tag,value.type());
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return aspectResLocAccessorInternal.compoundTagHasKey(tag)
                    && hexTypeAccessorInternal.compoundTagHasKey(tag);
        }
    }

    public static class HexGridAccessor extends CompoundTagAccessor<Map<HexCoord, HexEntry>> {
        protected final ListTagAccessor listTagAccessorInternal;
        protected final HexCoordAccessor hexCoordAccessorInternal;
        protected final HexEntryAccessor hexEntryAccessorInternal;
        public HexGridAccessor(String tagKey) {
            super(tagKey,(Class<Map<HexCoord, HexEntry>>) (Class<?>) Map.class);
            listTagAccessorInternal = new ListTagAccessor(tagKey+"_list");
            hexCoordAccessorInternal = new HexCoordAccessor(tagKey + "_hex_coord");
            hexEntryAccessorInternal = new HexEntryAccessor(tagKey + "_hex_type");
        }
        @Override
        public Map<HexCoord, HexEntry> readFromCompoundTag(CompoundTag tag) {
            Map<HexCoord, HexEntry> result = new HashMap<>();
            var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
            for (int i=0;i<listTag.size();i++) {
                var compoundTag = listTag.getCompound(i);
                var hexCoord = hexCoordAccessorInternal.readFromCompoundTag(compoundTag);
                var hexType = hexEntryAccessorInternal.readFromCompoundTag(compoundTag);
                result.put(hexCoord,hexType);
            }
            return result;
        }

        @Override
        public void writeToCompoundTag(CompoundTag tag, Map<HexCoord, HexEntry> value) {
            var listTag = new ListTag();
            for (var entry : value.entrySet()) {
                var coord = entry.getKey();
                var hexEntry = entry.getValue();
                var compound = new CompoundTag();
                hexCoordAccessorInternal.writeToCompoundTag(compound,coord);
                hexEntryAccessorInternal.writeToCompoundTag(compound,hexEntry);
                listTag.add(compound);
            }
            listTagAccessorInternal.writeToCompoundTag(tag,listTag);
        }

        @Override
        public boolean compoundTagHasKey(CompoundTag tag) {
            return listTagAccessorInternal.compoundTagHasKey(tag);
        }
    }


}
