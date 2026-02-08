package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FocusUpgradeTypeResourceLocation extends VariedResourceLocation<ResearchItem, FocusUpgradeTypeResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, FocusUpgradeTypeResourceLocation> BUILDER = FocusUpgradeTypeResourceLocation::of;
    public static final VariedResourceLocationParser<ResearchItem, FocusUpgradeTypeResourceLocation> PARSER = FocusUpgradeTypeResourceLocation::of;


    protected FocusUpgradeTypeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected FocusUpgradeTypeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected FocusUpgradeTypeResourceLocation(String string) {
        super(string);
    }
    protected FocusUpgradeTypeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, FocusUpgradeTypeResourceLocation> mapToResearchItemResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, FocusUpgradeTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    public static FocusUpgradeTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToResearchItemResourceLocation.computeIfAbsent(resourceLocation, FocusUpgradeTypeResourceLocation::new);
    }
    public static FocusUpgradeTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new FocusUpgradeTypeResourceLocation(namespace,path));
    }
    public static FocusUpgradeTypeResourceLocation of(String namespaceAndPath){
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
