package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VisNetNodeTypeResourceLocation extends VariedResourceLocation<ResearchItem, VisNetNodeTypeResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, VisNetNodeTypeResourceLocation> BUILDER = VisNetNodeTypeResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, VisNetNodeTypeResourceLocation> PARSER = VisNetNodeTypeResourceLocation::of;


    protected VisNetNodeTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected VisNetNodeTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected VisNetNodeTypeResourceLocation(String string) {
        super(string);
    }
    protected VisNetNodeTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, VisNetNodeTypeResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, VisNetNodeTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    public static VisNetNodeTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, VisNetNodeTypeResourceLocation::new);
    }
    public static VisNetNodeTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> VisNetNodeTypeResourceLocation.of(namespace,path));
    }
    public static VisNetNodeTypeResourceLocation of(String namespaceAndPath){
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
