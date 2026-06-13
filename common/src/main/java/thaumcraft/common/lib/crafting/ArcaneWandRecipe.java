package thaumcraft.common.lib.crafting;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.utils.collectionlike.SimplePair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.HashCentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.crafting.arcane.AbstractArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.resourcelocations.AbstractArcaneRecipeResourceLocation;
import thaumcraft.api.research.ThaumcraftResearches;
import thaumcraft.common.tiles.abstracts.IArcaneWorkbenchContainer;

import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.Consts.WandCastingCompoundTagAccessors.WAND_CAP_ACCESSOR;
import static com.linearity.opentc4.Consts.WandCastingCompoundTagAccessors.WAND_ROD_ACCESSOR;
import static com.linearity.opentc4.SomeRecipeItemMatchers.*;

//tried
public class ArcaneWandRecipe extends AbstractArcaneRecipe {

    private final RecipeItemMatcher[] inMatchers = new RecipeItemMatcher[]{
            EMPTY_MATCHER, EMPTY_MATCHER, WAND_CAP_MATCHER,
            EMPTY_MATCHER, WAND_ROD_MATCHER, EMPTY_MATCHER,
            WAND_CAP_MATCHER, EMPTY_MATCHER, EMPTY_MATCHER
    };

    private ArcaneWandRecipe() {
        super(AbstractArcaneRecipeResourceLocation.of(Thaumcraft.MOD_ID, "arcane_wand"));
    }

    @Override
    public ResearchItem getResearch() {
        return ThaumcraftResearches.BASIC_THAUMATURGY;
    }

    @Override
    public boolean matches(IArcaneWorkbenchContainer inv, Level world, Player player) {
        if (!getResearch().isPlayerCompletedResearch(player)){
            return false;
        }
        var stacks = inv.getInputItemStacks();
        var result = getResult(stacks);
        return result != null;
    }

    protected static final Map<SimplePair<ItemStack,ItemStack>,SimplePair<ItemStack,CentiVisList<Aspect>>> resultCaches = new MapMaker().weakKeys().makeMap();

    protected @Nullable SimplePair<ItemStack,ItemStack> getProbablyComponentPair(List<ItemStack> stacks){

        if (stacks.size() != 9) {
            return null;
        }
        ItemStack cap = ItemStack.EMPTY;
        ItemStack rod = ItemStack.EMPTY;
        for (int i = 0; i < 9; i++){
            var currentStack = stacks.get(i);
            var currentMatcher = inMatchers[i];
            if (!currentMatcher.matches(currentStack)){
                return null;
            }
            if (currentMatcher == WAND_CAP_MATCHER){
                if (cap.isEmpty()){
                    cap = stacks.get(i);
                }else if(!ItemStack.isSameItemSameTags(cap, currentStack)){
                    return null;
                }
            }else if (currentMatcher == WAND_ROD_MATCHER){
                rod = currentStack;//there's only one rod
            }
        }
        return new SimplePair<>(cap, rod);
    }
    protected @Nullable SimplePair<ItemStack, CentiVisList<Aspect>> getResult(List<ItemStack> stacks){
        var componentPair = getProbablyComponentPair(stacks);
        if (componentPair == null){
            return null;
        }
        var resultCache = resultCaches.get(componentPair);
        if (resultCache != null){
            return resultCache;
        }
        var cap = componentPair.a();
        var rod = componentPair.b();

        if (cap.isEmpty() || rod.isEmpty()){
            return null;
        }
        if (!(cap.getItem() instanceof ICraftingCostAspectOwnerComponent<? extends Aspect> capCostOwner)){
            return null;
        }
        if (!(rod.getItem() instanceof ICraftingCostAspectOwnerComponent<? extends Aspect> rodCostOwner)){
            return null;
        }
        var requiredCentiVis = new HashCentiVisList<>();
        capCostOwner.getCraftingCostCentiVis().forEach((aspect,amount) -> requiredCentiVis.merge(
                aspect,amount,(a,b) -> (a*b)/100
        ));
        rodCostOwner.getCraftingCostCentiVis().forEach((aspect,amount) -> requiredCentiVis.merge(
                aspect,amount,(a,b) -> (a*b)/100
        ));
        var wandResult = getItemOfResult().getDefaultInstance();
        var tag = wandResult.getOrCreateTag();
        WAND_CAP_ACCESSOR.writeToCompoundTag(tag,cap);
        WAND_ROD_ACCESSOR.writeToCompoundTag(tag,rod);
        var resultPair = new  SimplePair<ItemStack, CentiVisList<Aspect>>(cap, requiredCentiVis);
        resultCaches.put(componentPair,resultPair);
        return resultPair;
    }

    @Override
    public ItemStack getCraftingResult(IArcaneWorkbenchContainer var1) {
        var result = getResult(var1.getInputItemStacks());
        if (result == null){
            return ItemStack.EMPTY;
        }
        return result.a();
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public CentiVisList<Aspect> getCentiVisCost(IArcaneWorkbenchContainer var1) {
        var result = getResult(var1.getInputItemStacks());
        if (result == null){
            return UnmodifiableCentiVisList.of();
        }
        return result.b();
    }

    @Override
    public boolean matchViaOutput(ItemStack res) {
        return res.getItem() == getItemOfResult();
    }

    protected Item getItemOfResult(){
        return ThaumcraftItems.ThaumcraftItemInstances.WAND_CASTING();
    }
}
