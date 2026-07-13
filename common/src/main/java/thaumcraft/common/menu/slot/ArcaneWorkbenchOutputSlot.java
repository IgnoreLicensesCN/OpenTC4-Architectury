package thaumcraft.common.menu.slot;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.LinkedHashCentiVisList;
import thaumcraft.common.inventory.ArcaneWorkbenchResultContainer;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

import static thaumcraft.api.crafting.arcane.AbstractArcaneRecipe.ARCANE_RECIPES_VIEW;
import static thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator.getConsumptionModifier;
import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_CRAFTING;

public class ArcaneWorkbenchOutputSlot extends ResultSlot {

    protected final ArcaneWorkbenchBlockEntity workbench;
    protected final ArcaneWorkbenchResultContainer workbenchResultContainer;
    public ArcaneWorkbenchOutputSlot(Player player, ArcaneWorkbenchBlockEntity workbench, CraftingContainer craftingContainer, ArcaneWorkbenchResultContainer resultContainer, int i, int j, int k) {
        super(player, craftingContainer, resultContainer, i, j, k);
        this.workbench = workbench;
        this.workbenchResultContainer = resultContainer;
    }

    @Override
    public boolean mayPickup(Player player) {
        var centiVisCost = getFinalCentiVisCost(player);
        return canWandSatisfyCentiVisConsumption(centiVisCost);
    }

    protected CentiVisList<Aspect> getFinalCentiVisCost(Player player) {
        var centiVisCostOriginal = workbenchResultContainer.getCostsCentiVis();
        CentiVisList<Aspect> centiVisCostFinal = new LinkedHashCentiVisList<>();
        var wandStack = workbench.getStackInWandSlot();
        for (var aspect:centiVisCostOriginal.keySet()){
            float modifier = getConsumptionModifier(wandStack.getItem(),wandStack,player,aspect, CONSUMPTION_CRAFTING);
            centiVisCostFinal.addAll(aspect, (int) (centiVisCostOriginal.get(aspect) * modifier));
        }
        return centiVisCostFinal;
    }
    protected boolean canWandSatisfyCentiVisConsumption(CentiVisList<Aspect> centiVisConsumption) {
        return workbench.canWandSatisfyCentiVisConsumption(centiVisConsumption);
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        if (!workbench.consumeCentiVisNoThrow(player,getFinalCentiVisCost(player))){
            OpenTC4.LOGGER.warn("failed to consume CentiVis");
        }
        var recipe = ARCANE_RECIPES_VIEW.get(workbench.getRecipeResourceLocation());
        if (recipe != null) {
            recipe.afterCrafting(workbench,player.level(),player);
        }
//        else {
//            OpenTC4.LOGGER.warn("failed to find recipe for location {}", workbench.getRecipeResourceLocation());
//        }
        super.onTake(player, itemStack);
    }
}
