package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShapelessArcaneRecipeResourceLocation extends AbstractRecipeResourceLocation<ShapelessArcaneRecipe, ShapelessArcaneRecipeResourceLocation> {
    public static final ShapelessArcaneRecipeResourceLocation EMPTY = new ShapelessArcaneRecipeResourceLocation("","");
    public static final VariedResourceLocationBuilder<ShapelessArcaneRecipe, ShapelessArcaneRecipeResourceLocation> BUILDER = ShapelessArcaneRecipeResourceLocation::of;
    public static final VariedResourceLocationParser<ShapelessArcaneRecipe, ShapelessArcaneRecipeResourceLocation> PARSER = ShapelessArcaneRecipeResourceLocation::of;


    protected ShapelessArcaneRecipeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ShapelessArcaneRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ShapelessArcaneRecipeResourceLocation(String string) {
        super(string);
    }
    protected ShapelessArcaneRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ShapelessArcaneRecipeResourceLocation> mapToRecipeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ShapelessArcaneRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ShapelessArcaneRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRecipeResourceLocation.computeIfAbsent(resourceLocation, ShapelessArcaneRecipeResourceLocation::new);
    }
    public static ShapelessArcaneRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> ShapelessArcaneRecipeResourceLocation.of(namespace,path));
    }
    public static ShapelessArcaneRecipeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return ShapelessArcaneRecipeResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
