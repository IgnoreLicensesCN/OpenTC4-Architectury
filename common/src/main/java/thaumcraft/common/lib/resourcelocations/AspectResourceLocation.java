package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AspectResourceLocation extends VariedResourceLocation<ResearchItem, AspectResourceLocation> {
    public static final AspectResourceLocation EMPTY = new AspectResourceLocation("","");
    public static final VariedResourceLocationBuilder<ResearchItem, AspectResourceLocation> BUILDER = AspectResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, AspectResourceLocation> PARSER = AspectResourceLocation::of;


    protected AspectResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected AspectResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected AspectResourceLocation(String string) {
        super(string);
    }
    protected AspectResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, AspectResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, AspectResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static AspectResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, AspectResourceLocation::new);
    }
    public static AspectResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> AspectResourceLocation.of(namespace,path));
    }
    public static AspectResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return AspectResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
