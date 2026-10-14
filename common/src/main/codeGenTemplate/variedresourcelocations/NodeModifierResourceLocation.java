package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.nodes.NodeModifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeModifierResourceLocation extends ResourceLocation {
    public static final NodeModifierResourceLocation EMPTY = new NodeModifierResourceLocation("", "");
    public static final VariedResourceLocationBuilder<NodeModifier, NodeModifierResourceLocation> BUILDER = NodeModifierResourceLocation::of;
    public static final VariedResourceLocationParser<NodeModifier, NodeModifierResourceLocation> PARSER = NodeModifierResourceLocation::of;


    protected NodeModifierResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected NodeModifierResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected NodeModifierResourceLocation(String string) {
        super(string);
    }

    protected NodeModifierResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeModifierResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, NodeModifierResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static NodeModifierResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, NodeModifierResourceLocation::new);
    }

    public static NodeModifierResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new NodeModifierResourceLocation(namespace, path));
    }

    public static NodeModifierResourceLocation of(String namespaceAndPath) {
        if (namespaceAndPath.isEmpty()) {
            return of("", "");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0], split[1]);
    }

    public static @NotNull("empty if not exist") NodeModifierResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull("empty if not exist") NodeModifierResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
