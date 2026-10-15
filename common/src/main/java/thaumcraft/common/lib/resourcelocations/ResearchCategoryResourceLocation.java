package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.research.client.ResearchCategory;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResearchCategoryResourceLocation extends VariedResourceLocation<ResearchCategory, ResearchCategoryResourceLocation> {
    public static final ResearchCategoryResourceLocation EMPTY = new ResearchCategoryResourceLocation("", "");
    public static final VariedResourceLocationBuilder<ResearchCategory, ResearchCategoryResourceLocation> BUILDER = ResearchCategoryResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchCategory, ResearchCategoryResourceLocation> PARSER = ResearchCategoryResourceLocation::of;

    protected ResearchCategoryResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ResearchCategoryResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ResearchCategoryResourceLocation(String string) {
        super(string);
    }

    protected ResearchCategoryResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ResearchCategoryResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, ResearchCategoryResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static ResearchCategoryResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, ResearchCategoryResourceLocation::new);
    }

    public static ResearchCategoryResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new ResearchCategoryResourceLocation(namespace, path));
    }

    public static ResearchCategoryResourceLocation of(String namespaceAndPath) {
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
    public static ResearchCategoryResourceLocation tryParse(String string) {
        try {
            return new ResearchCategoryResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static @NotNull("null -> empty") ResearchCategoryResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull("null -> empty") ResearchCategoryResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
