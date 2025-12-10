package tc4tweak.modules.findRecipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import tc4tweak.ConfigurationHandler;
import tc4tweak.modules.FlushableCache;
import thaumcraft.api.crafting.IArcaneRecipe;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * thread local to make integrated server happy
 */
class ArcaneCraftingHistory extends FlushableCache<ThreadLocal<LinkedList<IArcaneRecipe>>> {
    @Override
    protected ThreadLocal<LinkedList<IArcaneRecipe>> createCache() {
        return ThreadLocal.withInitial(LinkedList::new);
    }

    IArcaneRecipe findInCache(Container inv, Player player) {
        if (isEnabled()) {
            LinkedList<IArcaneRecipe> history = getCache().get();
            for (Iterator<IArcaneRecipe> iterator = history.iterator(); iterator.hasNext(); ) {
                IArcaneRecipe recipe = iterator.next();
                if (recipe.matches(inv, player.level(), player)) {
                    iterator.remove();
                    history.addFirst(recipe);
                    return recipe;
                }
            }
        }
        return null;
    }

    void addToCache(IArcaneRecipe r) {
        if (isEnabled()) {
            LinkedList<IArcaneRecipe> history = getCache().get();
            history.addFirst(r);
            if (history.size() > ConfigurationHandler.INSTANCE.getArcaneCraftingHistorySize())
                history.removeLast();
        }
    }
}
