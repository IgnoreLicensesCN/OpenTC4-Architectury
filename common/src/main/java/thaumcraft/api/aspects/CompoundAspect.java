package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.api.aspects.Aspects.COMPOUND_ASPECTS;

public class CompoundAspect extends Aspect {
    public static final Map<AspectComponent,CompoundAspect> COMPOUND_ASPECT_RECIPES = new ConcurrentHashMap<>();

    public final @NotNull AspectComponent components;
    private void verifyDuplicate(){
        var duplicatedIfNotNull = COMPOUND_ASPECT_RECIPES.getOrDefault(components,null);
        if (duplicatedIfNotNull != null) {
            throw new RuntimeException("Duplicate aspect recipe:" + components + "|" + duplicatedIfNotNull);
        }
        COMPOUND_ASPECT_RECIPES.put(components,this);
    }
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, @NotNull AspectComponent components, ResourceLocation image, int blend) {
        super(tag,color,image,blend);
        this.components = components;
        COMPOUND_ASPECTS.put(tag,this);
        verifyDuplicate();
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, AspectComponent components) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),1);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, AspectComponent components, int blend) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),blend);
    }

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return components.isCombinedFrom(a,b);
    }

    @Override
    public String toString() {
        return "CompoundAspect{" +
                "components=" + components +
                ", aspectKey=" + aspectKey +
                ", color=" + color +
                ", image=" + image +
                ", blend=" + blend +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompoundAspect that)) return false;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(components);
    }
}
