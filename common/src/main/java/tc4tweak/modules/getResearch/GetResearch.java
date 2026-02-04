package tc4tweak.modules.getResearch;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import java.util.Map;
import java.util.stream.Stream;

/**
 * getResearch speed up
 */
public class GetResearch {
    private static final ResearchItemCache cache = new ResearchItemCache();

    private GetResearch() {
    }

    /**
     * Called from {@link ResearchCategories#getResearch(ResourceLocation)}
     */
    public static ResearchItem getResearch(ResourceLocation key) {
        if (key == null) return null;
        final Map<ResourceLocation, ResearchItem> map = cache.getCache();
        return map == null ? getResearchSlow(key) : map.get(key);
    }

    public static Stream<ResearchItem> stream() {
        return cache.getCache().values().stream();
    }

    /**
     * Fallback for when server starting
     */
    private static ResearchItem getResearchSlow(ResourceLocation key) {
        return ResearchCategories.researchCategories.values().stream()
                .flatMap(l -> l.research.values().stream())
                .filter(i -> key.equals(i.key))
                .findFirst().orElse(null);
    }
}
