package thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.function.BiPredicate;

//interaction:pickup item or something
//maybe you want to ignore NBT or match tag or whatever here?
public interface IGolemItemStackMatcherModifierItem extends BaseGolemEntity.IGolemUpgradeItem{
    BiPredicate<ItemStack,ItemStack> modifyItemStackMatching(BaseGolemEntity golem, ItemStack upgradeStack/*, ItemStack interactingStack*/);
}
