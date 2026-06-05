package tc4tweak.modules.findRecipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import tc4tweak.ConfigurationHandler;
import tc4tweak.modules.FlushableCache;
import thaumcraft.api.crafting.arcane.AbstractArcaneRecipe;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * thread local to make integrated server happy
 */
class ArcaneCraftingHistory extends FlushableCache<ThreadLocal<LinkedList<AbstractArcaneRecipe>>> {
    @Override
    protected ThreadLocal<LinkedList<AbstractArcaneRecipe>> createCache() {
        return ThreadLocal.withInitial(LinkedList::new);
    }

    AbstractArcaneRecipe findInCache(Container inv, Player player) {
        if (isEnabled()) {
            LinkedList<AbstractArcaneRecipe> history = getCache().get();
            for (Iterator<AbstractArcaneRecipe> iterator = history.iterator(); iterator.hasNext(); ) {
                AbstractArcaneRecipe recipe = iterator.next();
                if (recipe.matches(inv, player.level(), player)) {
                    iterator.remove();
                    history.addFirst(recipe);
                    return recipe;
                }
            }
        }
        return null;
    }

    void addToCache(AbstractArcaneRecipe r) {
        if (isEnabled()) {
            LinkedList<AbstractArcaneRecipe> history = getCache().get();
            history.addFirst(r);
            if (history.size() > ConfigurationHandler.INSTANCE.getArcaneCraftingHistorySize())
                history.removeLast();
        }
    }
}
