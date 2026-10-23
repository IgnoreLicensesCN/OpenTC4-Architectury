package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemHomeRangeModifierItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.Set;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:1
public class GolemGlassesItem extends Item implements IGolemDecorationItem, IGolemHomeRangeModifierItem {
    public GolemGlassesItem() {
        this(new Properties());
    }
    public GolemGlassesItem(Properties properties) {
        super(properties);
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(EYE_LEFT,EYE_RIGHT);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }

    @Override
    public float modifyHomeRange(BaseGolemEntity baseGolemEntity, ItemStack b, float range, float defaultRange) {
        return range + Math.max(defaultRange * 0.1F, 1.0F);
    }
}
