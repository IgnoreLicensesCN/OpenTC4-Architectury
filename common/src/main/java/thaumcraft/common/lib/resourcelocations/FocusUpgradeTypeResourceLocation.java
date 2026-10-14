package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FocusUpgradeTypeResourceLocation extends VariedResourceLocation<FocusUpgradeType, FocusUpgradeTypeResourceLocation> {
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

    public static final Map<ResourceLocation, FocusUpgradeTypeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, FocusUpgradeTypeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static FocusUpgradeTypeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, FocusUpgradeTypeResourceLocation::new);
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

    @Nullable
    public static FocusUpgradeTypeResourceLocation tryParse(String string) {
        try {
            return new FocusUpgradeTypeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static @NotNull FocusUpgradeTypeResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull FocusUpgradeTypeResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
