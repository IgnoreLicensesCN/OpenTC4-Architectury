package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

public class ResearchItemResourceLocation extends VariedResourceLocation<ResearchItem,ResearchItemResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem,ResearchItemResourceLocation> BUILDER = ResearchItemResourceLocation::new;
    public static final VariedResourceLocationParser<ResearchItem,ResearchItemResourceLocation> PARSER = ResearchItemResourceLocation::new;


    protected ResearchItemResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    public ResearchItemResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public ResearchItemResourceLocation(String string) {
        super(string);
    }
    public ResearchItemResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public ClueResourceLocation convertToResearchItemResLoc(){
        return new ClueResourceLocation(this.getNamespace(),this.getPath());
    }
}
