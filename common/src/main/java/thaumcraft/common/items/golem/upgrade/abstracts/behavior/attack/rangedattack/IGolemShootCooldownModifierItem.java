package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemShootCooldownModifierItem extends IGolemModifierItem {
    float modifyShootCooldown(BaseGolemEntity golem, ItemStack stack, float cooldown, float defaultCooldown);
}
