package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeModifierResourceLocation extends VariedResourceLocation<ResearchItem, NodeModifierResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, NodeModifierResourceLocation> BUILDER = NodeModifierResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, NodeModifierResourceLocation> PARSER = NodeModifierResourceLocation::of;


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
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, NodeModifierResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, NodeModifierResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    public static NodeModifierResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, NodeModifierResourceLocation::new);
    }
    public static NodeModifierResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> NodeModifierResourceLocation.of(namespace,path));
    }
    public static NodeModifierResourceLocation of(String namespaceAndPath){
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
