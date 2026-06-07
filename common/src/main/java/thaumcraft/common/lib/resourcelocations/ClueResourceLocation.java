package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//only for research needs this.
//store some string and tell both side "Oh we have something."
//you may need some i-ma-gi-na-tion to use this.
public class ClueResourceLocation extends VariedResourceLocation<String, ClueResourceLocation> {
    public static final ClueResourceLocation EMPTY = new ClueResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, ClueResourceLocation> BUILDER = ClueResourceLocation::of;
    public static final VariedResourceLocationParser<String, ClueResourceLocation> PARSER = ClueResourceLocation::of;


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

    public static final Map<ResourceLocation, ClueResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ClueResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ClueResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, ClueResourceLocation::new);
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
    @Nullable
    public static ClueResourceLocation tryParse(String string) {
        try {
            return new ClueResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
