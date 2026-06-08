package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeIDResourceLocation extends VariedResourceLocation<ResourceLocation, NodeIDResourceLocation> {
    public static final NodeIDResourceLocation EMPTY = new NodeIDResourceLocation("","");
    public static final VariedResourceLocationBuilder<ResourceLocation, NodeIDResourceLocation> BUILDER = NodeIDResourceLocation::of;
    public static final VariedResourceLocationParser<ResourceLocation, NodeIDResourceLocation> PARSER = NodeIDResourceLocation::of;


    protected NodeIDResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected NodeIDResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected NodeIDResourceLocation(String string) {
        super(string);
    }
    protected NodeIDResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeIDResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, NodeIDResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static NodeIDResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, NodeIDResourceLocation::new);
    }
    public static NodeIDResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> NodeIDResourceLocation.of(namespace,path));
    }
    public static NodeIDResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
