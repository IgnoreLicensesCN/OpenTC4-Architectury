package tc4tweak.modules.findRecipes;

/**
 * thread local to make integrated server happy
 */
@Deprecated(forRemoval = true)
class ArcaneCraftingHistory /*extends FlushableCache<ThreadLocal<LinkedList<AbstractArcaneRecipe>>>*/ {
//    @Override
//    protected ThreadLocal<LinkedList<AbstractArcaneRecipe>> createCache() {
//        return ThreadLocal.withInitial(LinkedList::new);
//    }
//
//    AbstractArcaneRecipe findInCache(Container inv, Player player) {
//        if (isEnabled()) {
//            LinkedList<AbstractArcaneRecipe> history = getCache().get();
//            for (Iterator<AbstractArcaneRecipe> iterator = history.iterator(); iterator.hasNext(); ) {
//                AbstractArcaneRecipe recipe = iterator.next();
//                if (recipe.matches(inv, player.level(), player)) {
//                    iterator.remove();
//                    history.addFirst(recipe);
//                    return recipe;
//                }
//            }
//        }
//        return null;
//    }
//
//    void addToCache(AbstractArcaneRecipe r) {
//        if (isEnabled()) {
//            LinkedList<AbstractArcaneRecipe> history = getCache().get();
//            history.addFirst(r);
//            if (history.size() > ConfigurationHandler.INSTANCE.getArcaneCraftingHistorySize())
//                history.removeLast();
//        }
//    }
}
