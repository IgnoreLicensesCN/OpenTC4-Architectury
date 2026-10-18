package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.golem;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ByteTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.mc.BlockPosAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.mc.DirectionTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.Marker;

public class MarkerTagAccessor extends CompoundTagAccessor<Marker> {
    private final CompoundTagAccessorImpl wrappedAccessor;
    private final BlockPosAccessor posAccessor;
    private final ResourceLocationTagAccessor dimAccessor;
    private final DirectionTagAccessor directionAccessor;
    private final ByteTagAccessor colorAccessor;
    public MarkerTagAccessor(String tagKey) {
        super(tagKey);
        wrappedAccessor = new CompoundTagAccessorImpl(tagKey+"_wrapped");
        posAccessor = new BlockPosAccessor(tagKey+"_pos");
        dimAccessor = new ResourceLocationTagAccessor(tagKey+"_dim");
        directionAccessor = new DirectionTagAccessor(tagKey+"_direction");
        colorAccessor = new ByteTagAccessor(tagKey+"_color");
    }

    @Override
    public Marker readFromCompoundTag(CompoundTag tag) {
        var innerTag = wrappedAccessor.readFromCompoundTag(tag);
        return new Marker(
                posAccessor.readFromCompoundTag(innerTag),
                dimAccessor.readFromCompoundTag(innerTag),
                directionAccessor.readFromCompoundTag(innerTag),
                colorAccessor.readByteFromCompoundTag(innerTag)
                );
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Marker marker) {
        var innerTag = new CompoundTag();
        {
            posAccessor.writeToCompoundTag(innerTag, marker.pos);
            dimAccessor.writeToCompoundTag(innerTag, marker.dim);
            directionAccessor.writeToCompoundTag(innerTag, marker.side);
            colorAccessor.writeByteToCompoundTag(innerTag, marker.color);
        }
        wrappedAccessor.writeToCompoundTag(tag,innerTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return wrappedAccessor.compoundTagHasKey(tag);
    }
}
