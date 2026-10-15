package thaumcraft.common.lib.resourcelocations;

import thaumcraft.api.crafting.crucible.CrucibleRecipe;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrucibleRecipeResourceLocation extends AbstractRecipeResourceLocation<CrucibleRecipe, CrucibleRecipeResourceLocation> {
    public static final CrucibleRecipeResourceLocation EMPTY = new CrucibleRecipeResourceLocation("", "");
    public static final VariedResourceLocationBuilder<CrucibleRecipe, CrucibleRecipeResourceLocation> BUILDER = CrucibleRecipeResourceLocation::of;
    public static final VariedResourceLocationParser<CrucibleRecipe, CrucibleRecipeResourceLocation> PARSER = CrucibleRecipeResourceLocation::of;

    protected CrucibleRecipeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected CrucibleRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected CrucibleRecipeResourceLocation(String string) {
        super(string);
    }

    protected CrucibleRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, CrucibleRecipeResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String, Map<String, CrucibleRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();

    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("", s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }

    public static CrucibleRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, CrucibleRecipeResourceLocation::new);
    }

    public static CrucibleRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace, n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new CrucibleRecipeResourceLocation(namespace, path));
    }

    public static CrucibleRecipeResourceLocation of(String namespaceAndPath) {
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
    public static CrucibleRecipeResourceLocation tryParse(String string) {
        try {
            return new CrucibleRecipeResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static @NotNull("null -> empty") CrucibleRecipeResourceLocation ofExisting(String namespace, String path) {
        var innerMap = mapFromNamespaceAndPathToResourceLocation.get(namespace);
        if (innerMap == null) {
            return EMPTY;
        }
        return innerMap.getOrDefault(path, EMPTY);
    }

    public static @NotNull("null -> empty") CrucibleRecipeResourceLocation ofExisting(ResourceLocation resLoc) {
        return ofExisting(resLoc.getNamespace(), resLoc.getPath());
    }
}
