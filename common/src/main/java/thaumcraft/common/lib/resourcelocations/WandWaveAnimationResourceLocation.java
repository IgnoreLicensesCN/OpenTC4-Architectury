package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WandWaveAnimationResourceLocation extends VariedResourceLocation<String, WandWaveAnimationResourceLocation> {
    public static final WandWaveAnimationResourceLocation EMPTY = new WandWaveAnimationResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, WandWaveAnimationResourceLocation> BUILDER = WandWaveAnimationResourceLocation::of;
    public static final VariedResourceLocationParser<String, WandWaveAnimationResourceLocation> PARSER = WandWaveAnimationResourceLocation::of;


    protected WandWaveAnimationResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected WandWaveAnimationResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected WandWaveAnimationResourceLocation(String string) {
        super(string);
    }
    protected WandWaveAnimationResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, WandWaveAnimationResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, WandWaveAnimationResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static WandWaveAnimationResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, WandWaveAnimationResourceLocation::new);
    }
    public static WandWaveAnimationResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new WandWaveAnimationResourceLocation(namespace,path));
    }
    public static WandWaveAnimationResourceLocation of(String namespaceAndPath){
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
    public static WandWaveAnimationResourceLocation tryParse(String string) {
        try {
            return new WandWaveAnimationResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
