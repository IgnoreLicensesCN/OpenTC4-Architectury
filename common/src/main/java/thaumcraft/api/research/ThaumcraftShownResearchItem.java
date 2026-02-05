package thaumcraft.api.research;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThaumcraftShownResearchItem extends ResearchItem
{
    protected final List<ShownInfoInResearchCategory> shownInfosInternal = new ArrayList<>();
    @UnmodifiableView
    public final List<ShownInfoInResearchCategory> shownInfos = Collections.unmodifiableList(shownInfosInternal);

    public ThaumcraftShownResearchItem(ResearchItemResourceLocation key, AspectList<Aspect> tags, int complex) {
        super(key, tags, complex);
    }

    public ThaumcraftShownResearchItem(ResearchItemResourceLocation key) {
        super(key);
    }

    public ThaumcraftShownResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category, AspectList<Aspect> tags, int complex) {
        super(key, category, tags, complex);
    }

    public ThaumcraftShownResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category) {
        super(key, category);
    }
    public ThaumcraftShownResearchItem addShownInfo(ShownInfoInResearchCategory... infos) {
        this.shownInfosInternal.addAll(Arrays.asList(infos));
        return this;
    }
}
