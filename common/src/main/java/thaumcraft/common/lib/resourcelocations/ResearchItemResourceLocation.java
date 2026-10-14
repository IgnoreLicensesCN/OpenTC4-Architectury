package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.research.ResearchItem;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class ResearchItemResourceLocation extends VariedResourceLocation<ResearchItem,ResearchItemResourceLocation> {
    public static final ResearchItemResourceLocation EMPTY = new ResearchItemResourceLocation("", "");
    public static final VariedResourceLocationBuilder<ResearchItem, ResearchItemResourceLocation> BUILDER = ResearchItemResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, ResearchItemResourceLocation> PARSER = ResearchItemResourceLocation::of;

    protected ResearchItemResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ResearchItemResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ResearchItemResourceLocation(String string) {
        super(string);
    }

    protected ResearchItemResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ResearchItemResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, ResearchItemResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static ResearchItemResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, ResearchItemResourceLocation::new);
    }

    public static ResearchItemResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new ResearchItemResourceLocation(namespace, path));
    }

    public static ResearchItemResourceLocation of(String namespaceAndPath) {
        if (namespaceAndPath.isEmpty()) {
            return of("", "");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0], split[1]);
    }

    @Nullable
    public static ResearchItemResourceLocation tryParse(String string) {
        try {
            return new ResearchItemResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static @NotNull ResearchItemResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull ResearchItemResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
    public ClueResourceLocation convertToClueResLoc() {
        return ClueResourceLocation.of(getNamespace(), getPath());
    }
}
