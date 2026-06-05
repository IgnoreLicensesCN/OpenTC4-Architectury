package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.runicshield.RunicShieldType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunicShieldTypeResourceLocation extends VariedResourceLocation<RunicShieldType, RunicShieldTypeResourceLocation> {
    public static final RunicShieldTypeResourceLocation EMPTY = new RunicShieldTypeResourceLocation("","");
    public static final VariedResourceLocationBuilder<RunicShieldType, RunicShieldTypeResourceLocation> BUILDER = RunicShieldTypeResourceLocation::of;
    public static final VariedResourceLocationParser<RunicShieldType, RunicShieldTypeResourceLocation> PARSER = RunicShieldTypeResourceLocation::of;


    protected RunicShieldTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected RunicShieldTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected RunicShieldTypeResourceLocation(String string) {
        super(string);
    }
    protected RunicShieldTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, RunicShieldTypeResourceLocation> mapToRunicShieldTypeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, RunicShieldTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static RunicShieldTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRunicShieldTypeResourceLocation.computeIfAbsent(resourceLocation, RunicShieldTypeResourceLocation::new);
    }
    public static RunicShieldTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new RunicShieldTypeResourceLocation(namespace,path));
    }
    public static RunicShieldTypeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
