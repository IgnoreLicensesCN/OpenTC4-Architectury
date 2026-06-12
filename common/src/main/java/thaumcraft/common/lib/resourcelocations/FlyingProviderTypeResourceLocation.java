package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//oh here we go again, another "any-string" resourceLocation
//you can mark your scanned type with this
//you may need some i-ma-gi-na-tion to use this.
public class FlyingProviderTypeResourceLocation extends VariedResourceLocation<String, FlyingProviderTypeResourceLocation> {
    public static final FlyingProviderTypeResourceLocation EMPTY = new FlyingProviderTypeResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, FlyingProviderTypeResourceLocation> BUILDER = FlyingProviderTypeResourceLocation::of;
    public static final VariedResourceLocationParser<String, FlyingProviderTypeResourceLocation> PARSER = FlyingProviderTypeResourceLocation::of;


    protected FlyingProviderTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected FlyingProviderTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected FlyingProviderTypeResourceLocation(String string) {
        super(string);
    }
    protected FlyingProviderTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, FlyingProviderTypeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, FlyingProviderTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static FlyingProviderTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, FlyingProviderTypeResourceLocation::new);
    }
    public static FlyingProviderTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new FlyingProviderTypeResourceLocation(namespace,path));
    }
    public static FlyingProviderTypeResourceLocation of(String namespaceAndPath){
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
    public static FlyingProviderTypeResourceLocation tryParse(String string) {
        try {
            return new FlyingProviderTypeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
