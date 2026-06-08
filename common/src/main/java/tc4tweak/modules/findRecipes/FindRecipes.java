package tc4tweak.modules.findRecipes;
@Deprecated(forRemoval = true)
public class FindRecipes {
//    private static final ArcaneCraftingHistory cache = new ArcaneCraftingHistory();
//
//    private FindRecipes() {
//    }
//
//    public static AbstractArcaneRecipe findArcaneRecipe(Container inv, Player player) {
//        AbstractArcaneRecipe r = cache.findInCache(inv, player);
//        if (r != null)
//            return r;
//        r = (AbstractArcaneRecipe.getAbstractArcaneRecipes()).parallelStream()
//                .filter(o -> o.matches(inv, player.level(), player))
//                .findFirst()
//                .orElse(null);
//        if (r != null)
//            cache.addToCache(r);
//        return r;
//    }
//
//    public static ItemStack getNormalCraftingRecipeOutput(CraftingManager inst, InventoryCrafting ic, Level world) {
//        // only check synced config if in remote world
//        if (ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes()
//                && (Platform.getEnvironment() != Env.CLIENT || NetworkedConfiguration.isCheckWorkbenchRecipes())) {
//            return inst.findMatchingRecipe(ic, world);
//        } else {
//            return null;
//        }
//    }
}
