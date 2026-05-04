package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;

public interface IAspectFilterAccessibleBlockEntity {

    @NotNull Aspect getAspectFilter();
    //return if applied filter
    boolean setAspectFilter(@NotNull Aspect aspectFilter);
}
