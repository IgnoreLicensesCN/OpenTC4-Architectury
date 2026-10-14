package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FocusUpgradeTypeResourceLocation extends ResourceLocation {
    public static final FocusUpgradeTypeResourceLocation EMPTY = new FocusUpgradeTypeResourceLocation("", "");
    public static final VariedResourceLocationBuilder<FocusUpgradeType, FocusUpgradeTypeResourceLocation> BUILDER = FocusUpgradeTypeResourceLocation::of;
    public static final VariedResourceLocationParser<FocusUpgradeType, FocusUpgradeTypeResourceLocation> PARSER = FocusUpgradeTypeResourceLocation::of;


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
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, FocusUpgradeTypeResourceLocation> mapToFocusUpgradeTypeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, FocusUpgradeTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static FocusUpgradeTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToFocusUpgradeTypeResourceLocation.computeIfAbsent(resourceLocation, FocusUpgradeTypeResourceLocation::new);
    }

    public static FocusUpgradeTypeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new FocusUpgradeTypeResourceLocation(namespace, path));
    }

    public static FocusUpgradeTypeResourceLocation of(String namespaceAndPath) {
        if (namespaceAndPath.isEmpty()) {
            return of("", "");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0], split[1]);
    }

    public static @NotNull("empty if not exist") FocusUpgradeTypeResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull("empty if not exist") FocusUpgradeTypeResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
