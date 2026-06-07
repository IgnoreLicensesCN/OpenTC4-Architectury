package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.arcane.AbstractArcaneRecipe;
import thaumcraft.api.crafting.arcane.ShapedArcaneRecipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractArcaneRecipeResourceLocation
        extends AbstractRecipeResourceLocation<
        AbstractArcaneRecipe,
        AbstractArcaneRecipeResourceLocation
        > {
    public static final AbstractArcaneRecipeResourceLocation EMPTY = new AbstractArcaneRecipeResourceLocation("","");
    public static final VariedResourceLocationBuilder<AbstractArcaneRecipe, AbstractArcaneRecipeResourceLocation> BUILDER = AbstractArcaneRecipeResourceLocation::of;
    public static final VariedResourceLocationParser<AbstractArcaneRecipe, AbstractArcaneRecipeResourceLocation> PARSER = AbstractArcaneRecipeResourceLocation::of;


    protected AbstractArcaneRecipeResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected AbstractArcaneRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected AbstractArcaneRecipeResourceLocation(String string) {
        super(string);
    }
    protected AbstractArcaneRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, AbstractArcaneRecipeResourceLocation> mapToRecipeResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, AbstractArcaneRecipeResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static AbstractArcaneRecipeResourceLocation of(ResourceLocation resourceLocation) {
        return mapToRecipeResourceLocation.computeIfAbsent(resourceLocation, AbstractArcaneRecipeResourceLocation::new);
    }
    public static AbstractArcaneRecipeResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> AbstractArcaneRecipeResourceLocation.of(namespace,path));
    }
    public static AbstractArcaneRecipeResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return AbstractArcaneRecipeResourceLocation.of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
}
