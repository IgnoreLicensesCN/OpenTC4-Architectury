package thaumcraft.common.items.abstracts.wandabstraction.wand;

import com.google.common.util.concurrent.AtomicDouble;
import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.common.items.abstracts.wandabstraction.component.ICraftingCostAspectOwnerComponentItem;

import java.util.HashSet;
import java.util.Set;

@UtilityLikeAbstraction(reason = "migrated from listener,this name for better reading")
public interface IWandBonusAspectOwner extends IBonusAspectOwnerItem<Aspect>, IWandComponentsOwnerItem {

    @Unmodifiable//usually
    @NotNull
    default AspectList<Aspect> getOwningBonusAspects(ItemStack stack){
        double totalCraftingCostCentiVisDividedByType = 0;
        for (var component:getWandComponents(stack)){
            AtomicDouble craftingCostCentiVis = new AtomicDouble( 0);
            Set<Aspect> totalCraftingAspectTypes = new HashSet<>(6);

            if (component.getItem() instanceof ICraftingCostAspectOwnerComponentItem<? extends Aspect> craftingCostOwner){
                craftingCostOwner.getCraftingCostCentiVis().forEach((costAspect,costAmount) -> {
                    totalCraftingAspectTypes.add(costAspect);
                    craftingCostCentiVis.updateAndGet(v ->  (v + costAmount));
                });
            }

            if (totalCraftingAspectTypes.isEmpty()){
                continue;
            }
            totalCraftingCostCentiVisDividedByType += craftingCostCentiVis.get() /totalCraftingAspectTypes.size();
        }
        if (totalCraftingCostCentiVisDividedByType < 0){return UnmodifiableAspectList.EMPTY;}
        return UnmodifiableAspectList.of(
                Aspects.MAGIC,(int)Math.floor(totalCraftingCostCentiVisDividedByType / (2*100)),
                Aspects.TOOL,(int)Math.floor(totalCraftingCostCentiVisDividedByType / (3*100))
        );
    };

}
