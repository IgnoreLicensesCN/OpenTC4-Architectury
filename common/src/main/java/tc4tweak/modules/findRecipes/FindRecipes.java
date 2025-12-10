package tc4tweak.modules.findRecipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import tc4tweak.ConfigurationHandler;
import tc4tweak.network.NetworkedConfiguration;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;

import java.util.List;

public class FindRecipes {
    private static final ArcaneCraftingHistory cache = new ArcaneCraftingHistory();

    private FindRecipes() {
    }

    public static IArcaneRecipe findArcaneRecipe(IInventory inv, EntityPlayer player) {
        IArcaneRecipe r = cache.findInCache(inv, player);
        if (r != null)
            return r;
        r = ((List<?>) ThaumcraftApi.getCraftingRecipes()).parallelStream()
                .filter(o -> o instanceof IArcaneRecipe && ((IArcaneRecipe) o).matches(inv, player.worldObj, player))
                .map(o -> (IArcaneRecipe) o)
                .findFirst()
                .orElse(null);
        if (r != null)
            cache.addToCache(r);
        return r;
    }

    public static ItemStack getNormalCraftingRecipeOutput(CraftingManager inst, InventoryCrafting ic, World world) {
        // only check synced config if in remote world
        if (ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes()
                && (!world.isRemote || NetworkedConfiguration.isCheckWorkbenchRecipes())) {
            return inst.findMatchingRecipe(ic, world);
        } else {
            return null;
        }
    }
}
