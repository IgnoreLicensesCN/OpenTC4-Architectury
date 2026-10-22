package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemRegenDelayModifierItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.HAT;

//ItemGolemDecoration:3
public class GolemFezItem extends Item implements IGolemDecorationItem, IGolemRegenDelayModifierItem {
    public GolemFezItem() {
        this(new Properties());
    }
    public GolemFezItem(Properties properties) {
        super(properties);
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(HAT);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }

    @Override
    public float getModifiedRegenDelay(BaseGolemEntity golem, ItemStack upgradeStack, float regenDelay) {
        return (regenDelay*0.66f);
    }
}
