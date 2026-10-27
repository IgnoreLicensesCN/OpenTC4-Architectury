package thaumcraft.common.items.golem.upgrade.primal;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.AbstractGolemUpgradeApplicableEntity;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemBasicUpgradeItem;

@UtilityLikeAbstraction
public abstract class GolemBasicUpgradeItem extends Item implements
        TravelingTrunkEntity.ITravelingTrunkUpgradeItem,
        IGolemBasicUpgradeItem {
    public GolemBasicUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean golemUpgrade$isApplicableTo(ItemStack stack, AbstractGolemUpgradeApplicableEntity<?> couldBeTravelingTrunk) {
        if (couldBeTravelingTrunk instanceof TravelingTrunkEntity){
            return TravelingTrunkEntity.ITravelingTrunkUpgradeItem.super.golemUpgrade$isApplicableTo(stack, couldBeTravelingTrunk);
        }
        return IGolemBasicUpgradeItem.super.golemUpgrade$isApplicableTo(stack, couldBeTravelingTrunk);
    }
}
