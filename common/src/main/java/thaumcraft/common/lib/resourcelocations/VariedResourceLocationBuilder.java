package thaumcraft.common.lib.resourcelocations;

import org.jetbrains.annotations.Nullable;

public interface VariedResourceLocationBuilder<C,R extends VariedResourceLocation<C,R>> {
    @Nullable
    R tryBuild(String string, String string2);

}
