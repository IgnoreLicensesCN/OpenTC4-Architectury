package thaumcraft.common.tiles.abstracts;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainer;

//TODO:for TileAlembic
public interface IAlembic extends IAspectContainer<Aspect> {
    @NotNull("null -> empty")
    Aspect getAspect();
    @NotNull("null -> empty")
    Aspect getAspectFilter();

    int getAmount();
    int getMaxAmount();

}
