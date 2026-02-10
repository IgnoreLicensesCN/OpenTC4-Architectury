package tc4tweak.modules.findCrucibleRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.List;

public class FindCrucibleRecipe {
    static final Logger log = LogManager.getLogger("FindCrucibleRecipe");
    private static final CrucibleRecipeByHash cache = new CrucibleRecipeByHash();

    public static CrucibleRecipe getCrucibleRecipeFromHash(int hash) {
        if (cache.isEnabled())
            return cache.getCache().get(hash);
        List<CrucibleRecipe> list = CrucibleRecipe.getCrucibleRecipes();
        return list.stream().filter(
                        r -> r.hash == hash)
                .findAny().orElse(null);
    }
}
