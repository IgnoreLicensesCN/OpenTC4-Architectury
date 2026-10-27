package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemShootAccuracyModifierItem extends BaseGolemEntity.IGolemUpgradeItem {
    float modifyShootDivergence(BaseGolemEntity golem, ItemStack stack, float divergence, float divergenceDefault);
}
