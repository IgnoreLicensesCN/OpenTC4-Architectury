package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;

public interface IAspectFilterAccessible {

    @NotNull Aspect getAspectFilter();
    //return if applied filter
    boolean setAspectFilter(@NotNull Aspect aspectFilter);
}
