package thaumcraft.api.aspects;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.api.aspects.Aspects.PRIMAL_ASPECTS;

public class PrimalAspect extends Aspect {

    public final @NotNull String chatcolor;//maybe should be removed?

    /**
     * Shortcut constructor I use for the primal aspects -
     * you shouldn't use this as making your own primal aspects will break all the things.
     * --anazor
     */
    public PrimalAspect(ResourceLocation tag, int color, @NotNull String chatcolor, int blend) {
        super(tag,color, blend);
        this.chatcolor = chatcolor;
        PRIMAL_ASPECTS.put(tag,this);
    }


}
