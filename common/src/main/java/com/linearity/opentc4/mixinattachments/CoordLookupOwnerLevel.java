package com.linearity.opentc4.mixinattachments;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@InterfaceShouldAttachTo(Level.class)
public interface CoordLookupOwnerLevel {
    <T> void opentc4$registerCoordLookupForLevel(CoordLookupTypeKey<T> type, CoordLookup<T> coordLookup);
    <T> Collection<CoordLookup<T>> opentc4$getCoordLookupsForType(CoordLookupTypeKey<T> type);

    Collection<Consumer<CoordLookupOwnerLevel>> LEVEL_COORD_LOOKUP_CONSUMERS = new CopyOnWriteArrayList<>();
    class CoordLookupTypeKey<T>{}
}
