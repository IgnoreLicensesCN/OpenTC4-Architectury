package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrucibleRecipeResourceLocation extends AbstractRecipeResourceLocation<CrucibleRecipe, CrucibleRecipeResourceLocation> {
    public static final CrucibleRecipeResourceLocation EMPTY = new CrucibleRecipeResourceLocation("","");
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
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, CrucibleRecipeResourceLocation> mapToRecipeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, CrucibleRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static CrucibleRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRecipeResourceLocation.computeIfAbsent(resourceLocation, CrucibleRecipeResourceLocation::new);
    }
    public static CrucibleRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> CrucibleRecipeResourceLocation.of(namespace,path));
    }
    public static CrucibleRecipeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return CrucibleRecipeResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
