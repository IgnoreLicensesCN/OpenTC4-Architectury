package thaumcraft.api.aspects;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.Objects;

import static thaumcraft.api.aspects.Aspects.PRIMAL_ASPECTS;

public class PrimalAspect extends Aspect {

    public final @NotNull String chatcolor;//maybe should be removed?

    /**
     * Shortcut constructor I use for the primal aspects -
     * you shouldn't use this as making your own primal aspects will break all the things.
     * --anazor
     * .
     * no,you can do that.
     * but tc4's own aspect in wand may not add your aspects
     * and many compatibility issue you have to fix.
     * TODO:[maybe wont finished]easier to add new primal aspect
     */
    public PrimalAspect(AspectResourceLocation tag, @RGBColor int color, @NotNull String chatcolor, int blend) {
        super(tag,color, blend);
        this.chatcolor = chatcolor;
        PRIMAL_ASPECTS.put(tag,this);
    }

    @Override
    public String toString() {
        return "PrimalAspect{" +
                "chatcolor='" + chatcolor + '\'' +
                ", aspectKey=" + aspectKey +
                ", color=" + color +
                ", image=" + image +
                ", blend=" + blend +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PrimalAspect that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(chatcolor, that.chatcolor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chatcolor);
    }
}
