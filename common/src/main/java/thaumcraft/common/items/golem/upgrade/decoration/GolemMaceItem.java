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

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:7
public class GolemMaceItem extends Item implements IGolemDecorationItem {
    public GolemMaceItem() {
        this(new Properties());
    }
    public GolemMaceItem(Properties properties) {
        super(properties);
    }

    public static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("56d852b7-b7da-4d3f-a195-6534d732d931");

    public static final Map<Attribute, Collection<Supplier<AttributeModifier>>> ATTRIBUTES = Collections.singletonMap(
                Attributes.ATTACK_DAMAGE,
            Collections.singletonList(
                    () -> new AttributeModifier(
                            DAMAGE_MODIFIER_UUID,
                            "Golem mace damage",
                            2,
                            AttributeModifier.Operation.ADDITION
                    )
            )
    );

    @Override
    public Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributes(ItemStack upgradeStack, Mob golem) {
        return ATTRIBUTES;
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(ARM_LEFT,HAND_LEFT);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }
}
