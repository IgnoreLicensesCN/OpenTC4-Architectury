package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WandSpellEventTypeResourceLocation extends VariedResourceLocation<String, WandSpellEventTypeResourceLocation> {
    public static final WandSpellEventTypeResourceLocation EMPTY = new WandSpellEventTypeResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, WandSpellEventTypeResourceLocation> BUILDER = WandSpellEventTypeResourceLocation::of;
    public static final VariedResourceLocationParser<String, WandSpellEventTypeResourceLocation> PARSER = WandSpellEventTypeResourceLocation::of;


    protected WandSpellEventTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected WandSpellEventTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected WandSpellEventTypeResourceLocation(String string) {
        super(string);
    }
    protected WandSpellEventTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, WandSpellEventTypeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, WandSpellEventTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static WandSpellEventTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, WandSpellEventTypeResourceLocation::new);
    }
    public static WandSpellEventTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new WandSpellEventTypeResourceLocation(namespace,path));
    }
    public static WandSpellEventTypeResourceLocation of(String namespaceAndPath){
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
    public static WandSpellEventTypeResourceLocation tryParse(String string) {
        try {
            return new WandSpellEventTypeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
