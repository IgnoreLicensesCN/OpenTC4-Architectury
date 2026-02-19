package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClueResourceLocation extends VariedResourceLocation<ResearchItem, ClueResourceLocation> {
    public static final ClueResourceLocation EMPTY = new ClueResourceLocation("","");
    public static final VariedResourceLocationBuilder<ResearchItem, ClueResourceLocation> BUILDER = ClueResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, ClueResourceLocation> PARSER = ClueResourceLocation::of;


    protected ClueResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ClueResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ClueResourceLocation(String string) {
        super(string);
    }
    protected ClueResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ClueResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ClueResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ClueResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, ClueResourceLocation::new);
    }
    public static ClueResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> ClueResourceLocation.of(namespace,path));
    }
    public static ClueResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
    public ResearchItemResourceLocation convertToResearchItemResLoc(){
        return ResearchItemResourceLocation.of(this.getNamespace(),this.getPath());
    }
}
