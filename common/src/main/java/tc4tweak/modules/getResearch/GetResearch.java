package tc4tweak.modules.getResearch;

import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.Map;
import java.util.stream.Stream;

/**
 * getResearch speed up
 */
@Deprecated(forRemoval = true)
public class GetResearch {
    private static final ResearchItemCache cache = new ResearchItemCache();

    private GetResearch() {
    }

    /**
     * Called from {@link ResearchItem#getResearch(ResearchItemResourceLocation)}
     */
    public static ResearchItem getResearch(ResearchItemResourceLocation key) {
        if (key == null) return null;
        final Map<ResearchItemResourceLocation, ResearchItem> map = cache.getCache();
        return map == null ? getResearchSlow(key) : map.get(key);
    }

    public static Stream<ResearchItem> stream() {
        return cache.getCache().values().stream();
    }

    /**
     * Fallback for when server starting
     */
    private static ResearchItem getResearchSlow(ResearchItemResourceLocation key) {
        return ResearchCategories.researchCategories.values().stream()
                .flatMap(l -> l.researches.values().stream())
                .filter(i -> key.equals(i.key))
                .findFirst().orElse(null);
    }
}
