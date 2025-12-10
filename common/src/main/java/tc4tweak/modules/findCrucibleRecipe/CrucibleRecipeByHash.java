package tc4tweak.modules.findCrucibleRecipe;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import tc4tweak.CommonUtils;
import tc4tweak.modules.FlushableCache;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.List;

import static tc4tweak.modules.findCrucibleRecipe.FindCrucibleRecipe.log;

class CrucibleRecipeByHash extends FlushableCache<TIntObjectMap<CrucibleRecipe>> {
	@Override
	protected TIntObjectMap<CrucibleRecipe> createCache() {
		List<CrucibleRecipe> list = ThaumcraftApi.getCrucibleRecipes();
		TIntObjectMap<CrucibleRecipe> result = new TIntObjectHashMap<>();
		for (CrucibleRecipe o : list) {
			CrucibleRecipe existing = result.putIfAbsent(o.hash, o);
			if (existing != null && log.isWarnEnabled()) {
				log.warn("Recipe {} ignored due to collision with {} for hash {}", CommonUtils.toString(o), CommonUtils.toString(existing), o.hash);
			}
		}
		return result;
	}
}
