package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShapedArcaneRecipeResourceLocation extends AbstractRecipeResourceLocation<ShapedArcaneRecipe, ShapedArcaneRecipeResourceLocation> {
    public static final ShapedArcaneRecipeResourceLocation EMPTY = new ShapedArcaneRecipeResourceLocation("","");
    public static final VariedResourceLocationBuilder<ShapedArcaneRecipe, ShapedArcaneRecipeResourceLocation> BUILDER = ShapedArcaneRecipeResourceLocation::of;
    public static final VariedResourceLocationParser<ShapedArcaneRecipe, ShapedArcaneRecipeResourceLocation> PARSER = ShapedArcaneRecipeResourceLocation::of;


    protected ShapedArcaneRecipeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ShapedArcaneRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ShapedArcaneRecipeResourceLocation(String string) {
        super(string);
    }
    protected ShapedArcaneRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ShapedArcaneRecipeResourceLocation> mapToRecipeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ShapedArcaneRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ShapedArcaneRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRecipeResourceLocation.computeIfAbsent(resourceLocation, ShapedArcaneRecipeResourceLocation::new);
    }
    public static ShapedArcaneRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> ShapedArcaneRecipeResourceLocation.of(namespace,path));
    }
    public static ShapedArcaneRecipeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return ShapedArcaneRecipeResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
