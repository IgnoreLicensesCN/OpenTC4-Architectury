package thaumcraft.common.lib.resourcelocations;

import org.jetbrains.annotations.Nullable;

public interface VariedResourceLocationParser<C,R extends VariedResourceLocation<C,R>> {
    @Nullable
    R tryParse(String string);
}
