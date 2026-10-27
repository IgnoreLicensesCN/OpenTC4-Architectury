package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemShootCooldownModifierItem extends BaseGolemEntity.IGolemUpgradeItem {
    float modifyShootCooldown(BaseGolemEntity golem, ItemStack stack, float cooldown, float defaultCooldown);
}
