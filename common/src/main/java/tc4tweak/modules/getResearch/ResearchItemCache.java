package tc4tweak.modules.getResearch;

import net.minecraft.resources.ResourceLocation;
import tc4tweak.modules.FlushableCache;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class ResearchItemCache extends FlushableCache<Map<ResearchItemResourceLocation, ResearchItem>> {
    @Override
    protected Map<ResearchItemResourceLocation, ResearchItem> createCache() {
        return ResearchCategories.researchCategories.values().stream()
                .flatMap(l -> l.researches.values().stream())
                .collect(Collectors.toMap(
                        i -> i.key,
                        Function.identity(),
                        (u, v) -> u,
                        LinkedHashMap::new
                ));
    }
}
