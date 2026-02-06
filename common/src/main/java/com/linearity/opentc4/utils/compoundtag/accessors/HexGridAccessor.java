package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.HashMap;
import java.util.Map;

public class HexGridAccessor extends CompoundTagAccessor<Map<HexCoord, HexEntry>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final HexCoordAccessor hexCoordAccessorInternal;
    protected final HexEntryAccessor hexEntryAccessorInternal;

    public HexGridAccessor(String tagKey) {
        super(tagKey, (Class<Map<HexCoord, HexEntry>>) (Class<?>) Map.class);
        listTagAccessorInternal = new ListTagAccessor(tagKey + "_list");
        hexCoordAccessorInternal = new HexCoordAccessor(tagKey + "_hex_coord");
        hexEntryAccessorInternal = new HexEntryAccessor(tagKey + "_hex_type");
    }

    @Override
    public Map<HexCoord, HexEntry> readFromCompoundTag(CompoundTag tag) {
        Map<HexCoord, HexEntry> result = new HashMap<>();
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = hexCoordAccessorInternal.readFromCompoundTag(compoundTag);
            var hexType = hexEntryAccessorInternal.readFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
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
            hexCoordAccessorInternal.writeToCompoundTag(compound, coord);
            hexEntryAccessorInternal.writeToCompoundTag(compound, hexEntry);
            listTag.add(compound);
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}
