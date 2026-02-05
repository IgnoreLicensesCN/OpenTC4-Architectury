package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchItem;

public class ResearchCategoryResourceLocation extends VariedResourceLocation<ResearchCategory, ResearchCategoryResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchCategory, ResearchCategoryResourceLocation> BUILDER = ResearchCategoryResourceLocation::new;
    public static final VariedResourceLocationParser<ResearchCategory, ResearchCategoryResourceLocation> PARSER = ResearchCategoryResourceLocation::new;


    protected ResearchCategoryResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    public ResearchCategoryResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public ResearchCategoryResourceLocation(String string) {
        super(string);
    }
    public ResearchCategoryResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }
}
