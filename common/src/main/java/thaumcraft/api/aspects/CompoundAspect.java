package thaumcraft.api.aspects;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.api.aspects.Aspects.COMPOUND_ASPECTS;

public class CompoundAspect extends Aspect {

    public final @NotNull AspectComponent components;


    public CompoundAspect(ResourceLocation tag, int color, @NotNull AspectComponent components, ResourceLocation image, int blend) {
        super(tag,color,image,blend);
        this.components = components;
        COMPOUND_ASPECTS.put(tag,this);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(ResourceLocation tag, int color, AspectComponent components) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),1);
    }

    /**
     * Shortcut constructor I use for the default aspects - you shouldn't be using this.
     */
    public CompoundAspect(ResourceLocation tag, int color, AspectComponent components, int blend) {
        this(tag,color,components,new ResourceLocation(tag.getNamespace(),"textures/aspects/"+tag.getPath()+".png"),blend);
    }

    public boolean isCombinedFrom(Aspect a, Aspect b) {
        return components.isCombinedFrom(a,b);
    }

}
