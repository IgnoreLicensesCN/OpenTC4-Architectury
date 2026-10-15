package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.nodes.INodeLockBlock;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeLockResourceLocation extends VariedResourceLocation<INodeLockBlock, NodeLockResourceLocation> {
    public static final NodeLockResourceLocation EMPTY = new NodeLockResourceLocation("", "");
    public static final VariedResourceLocationBuilder<INodeLockBlock, NodeLockResourceLocation> BUILDER = NodeLockResourceLocation::of;
    public static final VariedResourceLocationParser<INodeLockBlock, NodeLockResourceLocation> PARSER = NodeLockResourceLocation::of;

    protected NodeLockResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected NodeLockResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected NodeLockResourceLocation(String string) {
        super(string);
    }

    protected NodeLockResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeLockResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, NodeLockResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static NodeLockResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, NodeLockResourceLocation::new);
    }

    public static NodeLockResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new NodeLockResourceLocation(namespace, path));
    }

    public static NodeLockResourceLocation of(String namespaceAndPath) {
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
    public static NodeLockResourceLocation tryParse(String string) {
        try {
            return new NodeLockResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static @NotNull("null -> empty") NodeLockResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull("null -> empty") NodeLockResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
