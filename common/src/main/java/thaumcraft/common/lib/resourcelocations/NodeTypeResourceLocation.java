package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeTypeResourceLocation extends VariedResourceLocation<ResearchItem, NodeTypeResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, NodeTypeResourceLocation> BUILDER = NodeTypeResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, NodeTypeResourceLocation> PARSER = NodeTypeResourceLocation::of;


    protected NodeTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected NodeTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected NodeTypeResourceLocation(String string) {
        super(string);
    }
    protected NodeTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeTypeResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, NodeTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    public static NodeTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, NodeTypeResourceLocation::new);
    }
    public static NodeTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> NodeTypeResourceLocation.of(namespace,path));
    }
    public static NodeTypeResourceLocation of(String namespaceAndPath){
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
