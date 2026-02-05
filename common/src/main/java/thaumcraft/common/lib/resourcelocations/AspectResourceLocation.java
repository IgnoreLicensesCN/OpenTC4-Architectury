package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchItem;

public class AspectResourceLocation extends VariedResourceLocation<Aspect, AspectResourceLocation> {
    public static final VariedResourceLocationBuilder<Aspect, AspectResourceLocation> BUILDER = AspectResourceLocation::new;
    public static final VariedResourceLocationParser<Aspect, AspectResourceLocation> PARSER = AspectResourceLocation::new;


    protected AspectResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    public AspectResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public AspectResourceLocation(String string) {
        super(string);
    }
    public AspectResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }
}
