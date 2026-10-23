package thaumcraft.common.items.golem.upgrade.abstracts.behavior.rangedattack;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemShootAccuracyModifierItem extends IGolemModifierItem {
    float modifyShootAccuracy(BaseGolemEntity golem, ItemStack stack, float divergence, float divergenceDefault);
}
