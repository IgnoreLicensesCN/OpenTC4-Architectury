package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//oh here we go again, another "any-string" resourceLocation
//you can mark your scanned type with this
//you may need some i-ma-gi-na-tion to use this.
public class ScannedTypeResourceLocation extends VariedResourceLocation<String, ScannedTypeResourceLocation> {
    public static final ScannedTypeResourceLocation EMPTY = new ScannedTypeResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, ScannedTypeResourceLocation> BUILDER = ScannedTypeResourceLocation::of;
    public static final VariedResourceLocationParser<String, ScannedTypeResourceLocation> PARSER = ScannedTypeResourceLocation::of;


    protected ScannedTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ScannedTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ScannedTypeResourceLocation(String string) {
        super(string);
    }
    protected ScannedTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ScannedTypeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ScannedTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ScannedTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, ScannedTypeResourceLocation::new);
    }
    public static ScannedTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> ScannedTypeResourceLocation.of(namespace,path));
    }
    public static ScannedTypeResourceLocation of(String namespaceAndPath){
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
    public static ScannedTypeResourceLocation tryParse(String string) {
        try {
            return new ScannedTypeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
