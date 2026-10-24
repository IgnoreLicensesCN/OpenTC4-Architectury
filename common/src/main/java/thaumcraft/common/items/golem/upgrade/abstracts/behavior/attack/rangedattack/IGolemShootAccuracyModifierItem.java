package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemShootAccuracyModifierItem extends IGolemModifierItem {
    float modifyShootDivergence(BaseGolemEntity golem, ItemStack stack, float divergence, float divergenceDefault);
}
