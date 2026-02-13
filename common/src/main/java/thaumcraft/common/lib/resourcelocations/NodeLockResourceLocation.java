package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeLockResourceLocation extends VariedResourceLocation<ResearchItem, NodeLockResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, NodeLockResourceLocation> BUILDER = NodeLockResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, NodeLockResourceLocation> PARSER = NodeLockResourceLocation::of;


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
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeLockResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, NodeLockResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    public static NodeLockResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, NodeLockResourceLocation::new);
    }
    public static NodeLockResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> NodeLockResourceLocation.of(namespace,path));
    }
    public static NodeLockResourceLocation of(String namespaceAndPath){
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
