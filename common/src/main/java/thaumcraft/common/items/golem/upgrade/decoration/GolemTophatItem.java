package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;
import java.util.function.Supplier;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.HAT;

//ItemGolemDecoration:0
public class GolemTophatItem extends Item implements IGolemDecorationItem {
    public GolemTophatItem() {
        this(new Properties());
    }
    public GolemTophatItem(Properties properties) {
        super(properties);
    }

    public static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("ea78dac0-3fac-4644-bc14-a6385c779367");

    public static final Map<Attribute, Collection<Supplier<AttributeModifier>>> ATTRIBUTES = Collections.singletonMap(
                Attributes.MAX_HEALTH,
            Collections.singletonList(
                    () -> new AttributeModifier(
                            HEALTH_MODIFIER_UUID,
                            "Golem tophat health",
                            5,
                            AttributeModifier.Operation.ADDITION
                    )
            )
    );

    @Override
    public Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributes(ItemStack upgradeStack, Mob golem) {
        return ATTRIBUTES;
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(HAT);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }
}
