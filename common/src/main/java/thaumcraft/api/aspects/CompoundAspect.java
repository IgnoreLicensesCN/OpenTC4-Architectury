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
    public static final Map<CompoundAspectComponent,CompoundAspect> COMPOUND_ASPECT_RECIPES = new ConcurrentHashMap<>();

    public final @NotNull CompoundAspectComponent components;
    private void verifyDuplicate(CompoundAspect registeringAspect){
        var duplicatedIfNotNull = COMPOUND_ASPECT_RECIPES.getOrDefault(components,null);
        if (duplicatedIfNotNull != null) {
            //i have to say
            // if you have two aspects with same recipe
            // you wont be happy to handle this recipe collision.
            // (that would be weird in game.)
            throw new RuntimeException("Duplicate compound aspect recipe:" + components + "|" + duplicatedIfNotNull + " and " + registeringAspect);
        }
        COMPOUND_ASPECT_RECIPES.put(components,this);
    }
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, @NotNull CompoundAspectComponent components, ResourceLocation image, int blend) {
        super(tag,color,image,blend);
        this.components = components;
        COMPOUND_ASPECTS.put(tag,this);
        verifyDuplicate(this);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, CompoundAspectComponent components) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),1);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(AspectResourceLocation tag, @RGBColor int color, CompoundAspectComponent components, int blend) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),blend);
    }

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return components.isCombinedFrom(a,b);
    }

    @Override
    public String toString() {
        return "CompoundAspect{" +
                "aspectKey=" + aspectKey +
                ", components=" + components +
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
