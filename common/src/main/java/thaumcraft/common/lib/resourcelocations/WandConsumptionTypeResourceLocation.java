package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WandConsumptionTypeResourceLocation extends VariedResourceLocation<String, WandConsumptionTypeResourceLocation> {
    public static final WandConsumptionTypeResourceLocation EMPTY = new WandConsumptionTypeResourceLocation("","");
    public static final VariedResourceLocationBuilder<String, WandConsumptionTypeResourceLocation> BUILDER = WandConsumptionTypeResourceLocation::of;
    public static final VariedResourceLocationParser<String, WandConsumptionTypeResourceLocation> PARSER = WandConsumptionTypeResourceLocation::of;

    protected WandConsumptionTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected WandConsumptionTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected WandConsumptionTypeResourceLocation(String string) {
        super(string);
    }
    protected WandConsumptionTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, WandConsumptionTypeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, WandConsumptionTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static WandConsumptionTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, WandConsumptionTypeResourceLocation::new);
    }
    public static WandConsumptionTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new WandConsumptionTypeResourceLocation(namespace,path));
    }
    public static WandConsumptionTypeResourceLocation of(String namespaceAndPath){
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
    public static WandConsumptionTypeResourceLocation tryParse(String string) {
        try {
            return new WandConsumptionTypeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
