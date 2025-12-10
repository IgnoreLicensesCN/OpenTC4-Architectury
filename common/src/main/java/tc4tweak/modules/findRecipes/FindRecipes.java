package tc4tweak.modules.findRecipes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.level.Level;
import tc4tweak.ConfigurationHandler;
import tc4tweak.network.NetworkedConfiguration;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;

import java.util.List;

public class FindRecipes {
    private static final ArcaneCraftingHistory cache = new ArcaneCraftingHistory();

    private FindRecipes() {
    }

    public static IArcaneRecipe findArcaneRecipe(Container inv, Player player) {
        IArcaneRecipe r = cache.findInCache(inv, player);
        if (r != null)
            return r;
        r = (ThaumcraftApi.getIArcaneRecipes()).parallelStream()
                .filter(o -> o.matches(inv, player.level(), player))
                .findFirst()
                .orElse(null);
        if (r != null)
            cache.addToCache(r);
        return r;
    }

    public static ItemStack getNormalCraftingRecipeOutput(CraftingManager inst, InventoryCrafting ic, Level world) {
        // only check synced config if in remote world
        if (ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes()
                && (Platform.getEnvironment() != Env.CLIENT || NetworkedConfiguration.isCheckWorkbenchRecipes())) {
            return inst.findMatchingRecipe(ic, world);
        } else {
            return null;
        }
    }
}
