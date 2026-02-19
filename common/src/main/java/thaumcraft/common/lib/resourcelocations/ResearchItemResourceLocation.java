package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResearchItemResourceLocation extends VariedResourceLocation<ResearchItem,ResearchItemResourceLocation> {
    public static final ResearchItemResourceLocation EMPTY = new ResearchItemResourceLocation("","");
    public static final VariedResourceLocationBuilder<ResearchItem,ResearchItemResourceLocation> BUILDER = ResearchItemResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem,ResearchItemResourceLocation> PARSER = ResearchItemResourceLocation::of;


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
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation,ResearchItemResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String,ResearchItemResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ResearchItemResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, ResearchItemResourceLocation::new);
    }
    public static ResearchItemResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new ResearchItemResourceLocation(namespace,path));
    }
    public static ResearchItemResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }

    public ClueResourceLocation convertToClueResLoc(){
        return ClueResourceLocation.of(this.getNamespace(),this.getPath());
    }
}
