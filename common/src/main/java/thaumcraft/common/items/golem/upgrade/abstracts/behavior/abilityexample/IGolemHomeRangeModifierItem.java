package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemHomeRangeModifierItem extends IGolemModifierItem {
    float modifyHomeRange(BaseGolemEntity baseGolemEntity, ItemStack b, float range, float defaultRange);
}
