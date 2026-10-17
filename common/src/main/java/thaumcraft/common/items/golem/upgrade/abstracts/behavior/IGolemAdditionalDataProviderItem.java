package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemAdditionalDataProviderItem extends IGolemModifierItem {
    void readAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag);
    void writeAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag);
}
