package tc4tweak.modules.findCrucibleRecipe;
@Deprecated(forRemoval = true)
class CrucibleRecipeByHash /*extends FlushableCache<TIntObjectMap<CrucibleRecipe>>*/ {
//	@Override
//	protected TIntObjectMap<CrucibleRecipe> createCache() {
//		List<CrucibleRecipe> list = CrucibleRecipe.getCrucibleRecipes();
//		TIntObjectMap<CrucibleRecipe> result = new TIntObjectHashMap<>();
//		for (CrucibleRecipe o : list) {
//			CrucibleRecipe existing = result.putIfAbsent(o.hash, o);
//			if (existing != null && log.isWarnEnabled()) {
//				log.warn("Recipe {} ignored due to collision with {} for hash {}", CommonUtils.toString(o), CommonUtils.toString(existing), o.hash);
//			}
//		}
//		return result;
//	}
}
