package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchItem;

public class ClueResourceLocation extends VariedResourceLocation<ResearchItem, ClueResourceLocation> {
    public static final VariedResourceLocationBuilder<ResearchItem, ClueResourceLocation> BUILDER = ClueResourceLocation::new;
    public static final VariedResourceLocationParser<ResearchItem, ClueResourceLocation> PARSER = ClueResourceLocation::new;


    protected ClueResourceLocation(String string, String string2, @Nullable ResourceLocation.Dummy dummy) {
        super(string, string2, dummy);
    }

    public ClueResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public ClueResourceLocation(String string) {
        super(string);
    }
    public ClueResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation.getNamespace(),resourceLocation.getPath());
    }

    public ResearchItemResourceLocation convertToResearchItemResLoc(){
        return new ResearchItemResourceLocation(this.getNamespace(),this.getPath());
    }
}
