package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;

//which can be read with Goggles
//e.g. infusion matrix and jar and things
public interface IAspectDisplayBlockEntity<Asp extends Aspect> {
    //do not modify it
    @NotNull
    @UnmodifiableView
    AspectList<Asp> getAspectsToDisplay();
}
