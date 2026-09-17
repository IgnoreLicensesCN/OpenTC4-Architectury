package thaumcraft.common.lib.resourcelocations;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.championmod.ChampionModifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//only for research needs this.
//store some string and tell both side "Oh we have something."
//you may need some i-ma-gi-na-tion to use this.
public class ChampionModifierResourceLocation extends VariedResourceLocation<ChampionModifier, ChampionModifierResourceLocation> {
    public static final ChampionModifierResourceLocation EMPTY = new ChampionModifierResourceLocation("","");
    public static final VariedResourceLocationBuilder<ChampionModifier, ChampionModifierResourceLocation> BUILDER = ChampionModifierResourceLocation::of;
    public static final VariedResourceLocationParser<ChampionModifier, ChampionModifierResourceLocation> PARSER = ChampionModifierResourceLocation::of;


    protected ChampionModifierResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    protected ChampionModifierResourceLocation(String string, String string2) {
        super(string, string2);
    }

    protected ChampionModifierResourceLocation(String string) {
        super(string);
    }
    protected ChampionModifierResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public static final Map<ResourceLocation, ChampionModifierResourceLocation> mapToReferredResourceLocation = new ConcurrentHashMap<>();
    public static final Map<String,Map<String, ChampionModifierResourceLocation>> mapFromNamespaceAndPathToResourceLocation = new ConcurrentHashMap<>();
    static {
        mapFromNamespaceAndPathToResourceLocation.computeIfAbsent("",s -> new ConcurrentHashMap<>()).computeIfAbsent("", s -> EMPTY);
    }
    public static ChampionModifierResourceLocation of(ResourceLocation resourceLocation) {
        return mapToReferredResourceLocation.computeIfAbsent(resourceLocation, ChampionModifierResourceLocation::new);
    }
    public static ChampionModifierResourceLocation of(String namespace, String path) {
        return mapFromNamespaceAndPathToResourceLocation
                .computeIfAbsent(namespace,n -> new ConcurrentHashMap<>())
                .computeIfAbsent(path, p -> new ChampionModifierResourceLocation(namespace,path));
    }
    public static ChampionModifierResourceLocation of(String namespaceAndPath){
        if (namespaceAndPath.isEmpty()){
            return of("","");
        }
        var split = namespaceAndPath.split(":");
        if (split.length != 2){
            throw new IllegalArgumentException("Invalid namespace and path: " + namespaceAndPath);
        }
        return of(split[0],split[1]);
    }
    @Nullable
    public static ChampionModifierResourceLocation tryParse(String string) {
        try {
            return new ChampionModifierResourceLocation(string);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }
}
