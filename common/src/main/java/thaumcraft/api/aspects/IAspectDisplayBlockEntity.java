package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

//which can be read with Goggles
//e.g. infusion matrix and jar and things
public interface IAspectDisplayBlockEntity<Asp extends Aspect> {
    //do not modify it
    @NotNull
    @UnmodifiableView AspectList<Asp> getAspectsToDisplay();
}
