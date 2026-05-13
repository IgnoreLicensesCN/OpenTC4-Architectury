package thaumcraft.common.lib.world;

import com.google.common.collect.MapMaker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HolderCache<RegistryClass> {

    private final Map<Level, Map<ResourceKey<RegistryClass>, Holder<RegistryClass>>> holderCacheForResKey = new MapMaker().weakKeys().makeMap();
    private final ResourceKey<Registry<RegistryClass>> registry;

    private static final Map<ResourceKey<Registry<?>>,HolderCache<?>> allInstancesMap = new MapMaker().weakKeys().makeMap();
    protected HolderCache(ResourceKey<Registry<RegistryClass>> registry) {
        this.registry = registry;
    }

    public Holder<RegistryClass> getHolder(Level level, ResourceKey<RegistryClass> key) {
        return holderCacheForResKey
                .computeIfAbsent(level, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(key,_key -> level.registryAccess().registryOrThrow(registry).getHolderOrThrow(_key));
    }
    public static void onDatapackReload() {
        allInstancesMap.values().forEach(cache -> cache.holderCacheForResKey.clear());
    }

    @SuppressWarnings("unchecked")
    public static <RegistryClass> HolderCache<RegistryClass> of(ResourceKey<Registry<RegistryClass>> registry){
        return (HolderCache<RegistryClass>) allInstancesMap.computeIfAbsent((ResourceKey<Registry<?>>)(Object)registry, reg -> new HolderCache<>(registry));
    }
}
