
package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InfusionRecipeResourceLocation extends AbstractRecipeResourceLocation<InfusionRecipe, InfusionRecipeResourceLocation> {
    public static final InfusionRecipeResourceLocation EMPTY = new InfusionRecipeResourceLocation("","");
    public static final VariedResourceLocationBuilder<InfusionRecipe, InfusionRecipeResourceLocation> BUILDER = InfusionRecipeResourceLocation::of;
    public static final VariedResourceLocationParser<InfusionRecipe, InfusionRecipeResourceLocation> PARSER = InfusionRecipeResourceLocation::of;


    protected InfusionRecipeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected InfusionRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected InfusionRecipeResourceLocation(String string) {
        super(string);
    }
    protected InfusionRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, InfusionRecipeResourceLocation> mapToRecipeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, InfusionRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static InfusionRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRecipeResourceLocation.computeIfAbsent(resourceLocation, InfusionRecipeResourceLocation::new);
    }
    public static InfusionRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> InfusionRecipeResourceLocation.of(namespace,path));
    }
    public static InfusionRecipeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return InfusionRecipeResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
