package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemSpeedMultiplierModifierItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.Set;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:2
public class GolemBowtieItem extends Item implements IGolemDecorationItem, IGolemSpeedMultiplierModifierItem {
    public GolemBowtieItem() {
        this(new Properties());
    }
    public GolemBowtieItem(Properties properties) {
        super(properties);
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(NECKLACE);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }

    @Override
    public float modifySpeedMultiplier(BaseGolemEntity golem, ItemStack upgradeStack, float speedMultiplier) {
        return speedMultiplier + 0.1F;
    }
}
