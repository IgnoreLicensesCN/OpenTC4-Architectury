package com.linearity.opentc4.utils.collectionlike.coordlookups;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static thaumcraft.api.aspects.essentiabe.IRemoteDrainableEssentiaSourceBlockEntity.COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE;
import static thaumcraft.api.visnet.VisNetNodeBlockEntity.RELAY;
import static thaumcraft.api.visnet.VisNetNodeBlockEntity.SOURCE;
import static thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity.COORD_LOOKUP_KEY_FOR_NODE;
import static thaumcraft.common.tiles.crafted.ArcaneEarBlockEntity.COORD_LOOKUP_KEY_FOR_ARCANE_EAR;
import static thaumcraft.common.tiles.crafted.infusion.ArcanePedestalBlockEntity.COORD_LOOKUP_KEY_FOR_INFUSION_COMPONENT_PROVIDER;

public interface CoordLookup<StoreItem> {
    void store(Vec3i pos, StoreItem value);
    void remove(Vec3i pos, StoreItem value);
    void forItemsNearPos(Vec3i pos, Consumer<StoreItem> action);
    void forItemsNearPosWithRange(Vec3i cubeCenterPos, Consumer<StoreItem> action, int halfLengthOfCube);
    @SuppressWarnings("UnusedReturnValue")
    boolean forItemsNearPosWithBreak(Vec3i cubeCenterPos, Predicate<StoreItem> action);
    boolean forItemsNearPosWithBreakWithRange(Vec3i cubeCenterPos, Predicate<StoreItem> action, int rangeManhattan);

    static <StoreItem> @NotNull CoordLookup<StoreItem> withSizeBits(byte bits){
        return new CubeChunkedWeakLookups<>(bits);//TODO:[maybe wont finished]:more impl for different size bits
    }
    class Default{
        public static <T> CoordLookup<T> getDefaultCoordLookupFor(CoordLookupOwnerLevel.CoordLookupTypeKey<T> key){
            return CoordLookup.withSizeBits((byte) 5);
        }
        public static final Map<Level,Map<VisNetNodeBlockEntity.VisNetNodeType,CoordLookup<VisNetNodeBlockEntity>>> DEFAULT_VIS_NET_LOOKUPS = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
        public static void init(){
            registerDefaultVisNetNodeLookupForType(SOURCE);
            registerDefaultVisNetNodeLookupForType(RELAY);
            CoordLookupOwnerLevel.LEVEL_COORD_LOOKUP_CONSUMERS.add(l -> l.opentc4$registerCoordLookupForLevel(COORD_LOOKUP_KEY_FOR_NODE,getDefaultCoordLookupFor(COORD_LOOKUP_KEY_FOR_NODE)));
            CoordLookupOwnerLevel.LEVEL_COORD_LOOKUP_CONSUMERS.add(l -> l.opentc4$registerCoordLookupForLevel(COORD_LOOKUP_KEY_FOR_INFUSION_COMPONENT_PROVIDER,getDefaultCoordLookupFor(COORD_LOOKUP_KEY_FOR_INFUSION_COMPONENT_PROVIDER)));
            CoordLookupOwnerLevel.LEVEL_COORD_LOOKUP_CONSUMERS.add(l -> l.opentc4$registerCoordLookupForLevel(COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE,getDefaultCoordLookupFor(COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE)));
            CoordLookupOwnerLevel.LEVEL_COORD_LOOKUP_CONSUMERS.add(l -> l.opentc4$registerCoordLookupForLevel(COORD_LOOKUP_KEY_FOR_ARCANE_EAR,getDefaultCoordLookupFor(COORD_LOOKUP_KEY_FOR_ARCANE_EAR)));
        }
        public static void registerDefaultVisNetNodeLookupForType(VisNetNodeBlockEntity.VisNetNodeType type){
            CoordLookupOwnerLevel.LEVEL_COORD_LOOKUP_CONSUMERS.add(l -> {
                var defaultLookup = DEFAULT_VIS_NET_LOOKUPS.computeIfAbsent((Level) l, _ignored -> new ConcurrentHashMap<>()).computeIfAbsent(
                        type,Default::getDefaultCoordLookupFor
                );
                l.opentc4$registerCoordLookupForLevel(type, defaultLookup);
            });
        }
    }
}
