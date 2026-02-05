package thaumcraft.common.gui.slot;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.common.inventory.ArcaneWorkbenchResultContainer;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

import static thaumcraft.api.expands.listeners.wandconsumption.ConsumptionModifierCalculator.getConsumptionModifier;

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
        var centiVisCostFinal = CentiVisList.of();
        var wandStack = workbench.getWand();
        for (var aspect:centiVisCostOriginal.getAspects().keySet()){
            float modifier = getConsumptionModifier(wandStack.getItem(),wandStack,player,aspect,true);
            centiVisCostFinal.addAll(aspect, (int) (centiVisCostOriginal.getAmount(aspect) * modifier));
        }
        return centiVisCostFinal;
    }
    protected boolean canWandSatisfyCentiVisConsumption(CentiVisList<Aspect> centiVisConsumption) {
        return workbench.canWandSatisfyCentiVisConsumption(centiVisConsumption);
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        super.onTake(player, itemStack);
        if (!workbench.consumeCentiVisNoThrow(player,getFinalCentiVisCost(player))){
            OpenTC4.LOGGER.warn("failed to consume CentiVis");
        };
    }
}
