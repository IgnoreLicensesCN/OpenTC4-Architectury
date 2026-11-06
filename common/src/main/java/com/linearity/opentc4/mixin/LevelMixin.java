package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(Level.class)
public class LevelMixin implements CoordLookupOwnerLevel {
    @Unique
    public Map<CoordLookupTypeKey<?>, Set<CoordLookup<?>>> opentc4$coordLookups = new ConcurrentHashMap<>();

    @Override
    public <T> void opentc4$registerCoordLookupForLevel(CoordLookupTypeKey<T> type, CoordLookup<T> coordLookup) {
        opentc4$getCoordLookupsForType(type).add(coordLookup);
    }

    @Override
    public <T> Collection<CoordLookup<T>> opentc4$getCoordLookupsForType(CoordLookupTypeKey<T> type) {
        return (Collection<CoordLookup<T>>)
                (Object)
                opentc4$coordLookups.computeIfAbsent(type,_ignored -> ConcurrentHashMap.newKeySet());
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void initLookupsOnInit(CallbackInfo ci) {
        for (var consumer:LEVEL_COORD_LOOKUP_CONSUMERS){
            consumer.accept(this);
        }
    }
}
