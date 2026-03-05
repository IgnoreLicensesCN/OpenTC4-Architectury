package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class VariedResourceLocation<
        LocationReferredType,
        ResourceLocationType extends VariedResourceLocation<LocationReferredType, ResourceLocationType>
        > extends ResourceLocation {

    protected VariedResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    public VariedResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public VariedResourceLocation(String string) {
        super(string);
    }

    public VariedResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

}
