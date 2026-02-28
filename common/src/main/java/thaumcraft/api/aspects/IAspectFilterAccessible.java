package thaumcraft.api.aspects;

import org.jetbrains.annotations.NotNull;

public interface IAspectFilterAccessible {

    @NotNull Aspect getAspectFilter();
    boolean setAspectFilter(@NotNull Aspect aspectFilter);
}
