package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemHomeRangeModifierItem extends IGolemModifierItem {
    int modifyHomeRange(BaseGolemEntity baseGolemEntity, ItemStack b, int range);
}
